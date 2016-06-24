package cn.zhuo.common.zk;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.util.List;

/**
 * ZooKeeper配置服务实现
 *
 * Created by ZhangLiang on 2015/11/26.
 */
public class Configurator {

    private Logger logger = LoggerFactory.getLogger(getClass());

    // 建立ZooKeeper连接的默认参数
    private static final int DEFAULT_MAX_RECONNECT_TIMES = 3;
    private static final int DEFAULT_RECONNECT_INTERVAL = 1000;
    private static final String ENV_ZK_SERVER = "ZK_CLUSTER";
    private static final String ENV_ZK_ROOT = "ZK_ROOT";

    /**
     * ZooKeeper cluster connect string
     */
    private String cluster;
    private String root;

    private int maxReconnectTimes = DEFAULT_MAX_RECONNECT_TIMES;
    private int reconnectInterval = DEFAULT_RECONNECT_INTERVAL;

    private CuratorFramework client = null;

    public Configurator(String cluster) {
//        Assert.hasText(cluster, "读取配置中心地址失败！");
        this.cluster = cluster;
    }

    public void init() {

        String envRoot = System.getProperty(ENV_ZK_ROOT);
        if (Strings.isNullOrEmpty(envRoot)) {
            logger.info("Can not get ZooKeeper root dir string from environment variable, use the value by default /gq/dev.");
            this.root = "/gq/dev";
        } else {
            this.root = envRoot;
        }

        String envCluster = System.getProperty(ENV_ZK_SERVER);
        if (Strings.isNullOrEmpty(envCluster)) {
            logger.warn("Can not get ZooKeeper cluster connect string from environment variable, use the value by setter.");
        } else {
            this.cluster = envCluster;
        }
//        Assert.hasText(cluster, "ZooKeeper cluster connect string must be set");
        logger.info("Using ZooKeeper cluster connect string: {}", cluster);

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(reconnectInterval, maxReconnectTimes);
        client = CuratorFrameworkFactory.newClient(cluster, retryPolicy);
        client.start();
    }

    public void destroy() {

        logger.info("Closing connection to ZooKeeper cluster...");
        client.close();
    }

    public String readConfig(String key) {

        String path = "";
        try {
            key = URLDecoder.decode(key , "UTF-8");
            path = makePath(key);
            byte[] data = client.getData().forPath(path);
            return new String(data);
        } catch (Exception e) {
            throw makeException("reading config", path, e);
        }
    }

    public String readConfig(String namespace, String key) {

        String path = makePath(namespace, key);
        try {
            byte[] data = client.getData().forPath(path);
            return new String(data);
        } catch (Exception e) {
            throw makeException("reading config", path, e);
        }
    }

    public void deleteConfig(String namespace, String key) {

        String path;
        if (StringUtils.isNotBlank(key)) {
            path = makePath(namespace, key);
        } else {
            path = makePath(namespace);
        }
        try {
            client.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e) {
            throw makeException("deleting config", path, e);
        }
    }

    public void addConfig(String namespace, String key, String config) {

        String path;
        if (StringUtils.isNotBlank(key)) {
            path = makePath(namespace, key);
        } else {
            path = makePath(namespace);
        }
        try {
            if (Strings.isNullOrEmpty(config)) {
                client.create().creatingParentsIfNeeded().forPath(path , "".getBytes());
            } else {
                client.create().creatingParentsIfNeeded().forPath(path, config.getBytes());
            }
        } catch (Exception e) {
            throw makeException("adding config", path, e);
        }
    }

    public void updateConfig(String namespace, String key, String config) {

        String path = makePath(namespace, key);
        try {
            if (Strings.isNullOrEmpty(config)) {
                client.setData().forPath(path);
            } else {
                client.setData().forPath(path, config.getBytes());
            }
        } catch (Exception e) {
            throw makeException("updating config", path, e);
        }
    }

    public List<String> listNamespaceChildren(String namespace) {

        String path = makePath(namespace);
        try {
            return client.getChildren().forPath(path);
        } catch (Exception e) {
            throw makeException("listing namespace children", path, e);
        }
    }

    public List<String> listNamespaces() {

        String path = makeRootPath();
        try {
            return client.getChildren().forPath(path);
        } catch (Exception e) {
            throw makeException("listing namespaces", path, e);
        }
    }

    private RuntimeException makeException(String operation, String path, Exception e) {

        logger.error("Error while {} from ZooKeeper by {}.", operation, path, e);
        if (e instanceof KeeperException) {
            Code code = ((KeeperException) e).code();
            switch (code) {
                case NONODE:
                    return new RuntimeException("No node exists.");
                case NODEEXISTS:
                    return new RuntimeException("Node already existed.");
                default:
                    return new RuntimeException(e);
            }
        } else if (e instanceof InterruptedException) {
            return new RuntimeException("ZK_INTERRUPTED_ERROR", e);
        } else {
            return new RuntimeException(e);
        }
    }

    private String makeRootPath() {
        return root;
    }

    private String makePath(String namespace) {
        return root + "/" + namespace;
    }

    private String makePath(String namespace, String key) {
        return root + "/" + namespace + "/" + key;
    }

    //region get and set
    /**
     * @return 返回 cluster
     */
    public String getCluster() {
        return cluster;
    }

    /**
     * @param cluster 对cluster进行赋值
     */
    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    /**
     * @return 返回 maxReconnectTimes
     */
    public int getMaxReconnectTimes() {
        return maxReconnectTimes;
    }

    /**
     * @param maxReconnectTimes 对maxReconnectTimes进行赋值
     */
    public void setMaxReconnectTimes(int maxReconnectTimes) {
        this.maxReconnectTimes = maxReconnectTimes;
    }

    /**
     * @return 返回 reconnectInterval
     */
    public int getReconnectInterval() {
        return reconnectInterval;
    }

    /**
     * @param reconnectInterval 对reconnectInterval进行赋值
     */
    public void setReconnectInterval(int reconnectInterval) {
        this.reconnectInterval = reconnectInterval;
    }
    //endregion
}
