package com.gq.marketing.common.zk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gq.common.entity.config.DatabaseInstance;
import com.gq.common.entity.config.TomcatPoolConfig;
import com.gq.common.zk.Configurator;
import com.gq.marketing.config.TestConfig;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * 配置中心测试
 * Created by ZhangLiang on 2015/12/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ConfiguratorTest {

    @Resource
    private Configurator configurator;

    @Test
    public void m2(){
        System.out.println(System.getenv());
    }

    @Test
    public void m1(){
//        System.out.println(configurator.readConfig("test"));
//        PoolProperties p = new PoolProperties();
//        p.setUrl("jdbc:mysql://localhost:3306/mysql");
//        p.setDriverClassName("com.mysql.jdbc.Driver");
//        p.setUsername("root");
//        p.setPassword("password");
//        p.setJmxEnabled(true);
//        p.setTestWhileIdle(false);
//        p.setTestOnBorrow(true);
//        p.setValidationQuery("SELECT 1");
//        p.setTestOnReturn(false);
//        p.setValidationInterval(30000);
//        p.setTimeBetweenEvictionRunsMillis(30000);
//        p.setMaxActive(100);
//        p.setInitialSize(10);
//        p.setMaxWait(10000);
//        p.setRemoveAbandonedTimeout(60);
//        p.setMinEvictableIdleTimeMillis(30000);
//        p.setMinIdle(10);
//        p.setLogAbandoned(true);
//        p.setRemoveAbandoned(true);
//        p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
//                        "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");


        ObjectMapper mapper = new ObjectMapper();
        DatabaseInstance bi = new DatabaseInstance();
//        TomcatPoolConfig tpc = new TomcatPoolConfig();
        bi.setDriverClassName("com.mysql.jdbc.Driver");
        bi.setUrl("jdbc:mysql://10.100.200.104:3308/gq_p2pget_fy");
        bi.setPassword("112233");
//        bi.setUrl("jdbc:mysql://10.100.200.107:3306/gq_p2pget_fy");
//        bi.setPassword("123456");
        bi.setUsername("gq_p2pget_fy");
        bi.setConnectionProperties("useUnicode=true;characterEncoding=UTF-8;useOldAliasMetadataBehavior=true;autoReconnect=true");
        bi.setReadOnly(false);


        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bi);
            System.out.println(json);
//            configurator.updateConfig("db", "marketing", json);
            System.out.println(configurator.readConfig("db", "marketing"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
