package com.gq.marketing.config;

import com.gq.common.ds.GqDataSource;
import com.gq.common.service.RestServiceContext;
import com.gq.common.zk.Configurator;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试用配置
 * Created by ZhangLiang on 2015/12/1.
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = false)
@EnableTransactionManagement(proxyTargetClass = false)
@ComponentScan(value = {"com.gq.marketing.common.*"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class),
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class)})
@PropertySource("classpath:/service.properties")
public class TestConfig {

    @Bean(initMethod = "init", destroyMethod = "destroy")
    @Lazy(false)
    public Configurator createConfigurator(Environment env) {
        return new Configurator(env.getProperty("config.service"));
    }

//    @Bean
//    @Lazy(false)
//    @Order(20)
//    public RestServiceContext createRestServiceContext(){
//        return new RestServiceContext();
//    }

//    @Bean
//    public DataSource createDataSource(Configurator configurator) {
//        return new GqDataSource(configurator, "marketing");
//    }
//
//    @Bean
//    public SqlSessionFactoryBean createSqlSessionFactoryBean(DataSource dataSource, ApplicationContext ctx)
//            throws IOException {
//
//        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
//        sessionFactoryBean.setDataSource(dataSource);
//        sessionFactoryBean.setTypeAliasesPackage("com.gq.marketing.entity");
//        sessionFactoryBean.setTypeHandlersPackage("com.gq.marketing.dao.typehandler");
//        sessionFactoryBean.setMapperLocations(ctx.getResources("classpath:sqlmap/*.xml"));
//
//        return sessionFactoryBean;
//    }
//
//    @Bean
//    public DataSourceTransactionManager createDataSourceTransactionManager(DataSource dataSource) {
//
//        DataSourceTransactionManager manager = new DataSourceTransactionManager();
//        manager.setDataSource(dataSource);
//
//        return manager;
//    }
//
//    @Bean
//    public MapperScannerConfigurer createMapperScannerConfigurer() {
//
//        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
//        configurer.setAnnotationClass(Repository.class);
//        configurer.setBasePackage("com.gq.marketing.dao");
//
//        return configurer;
//    }
//
//    @Bean
//    public RestTemplate createRestTemplate(){
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        // 我们默认只用json,String,byte三种格式
//        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
//        messageConverters.add(new MappingJackson2HttpMessageConverter());
//        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
//        messageConverters.add(new ByteArrayHttpMessageConverter());
//        messageConverters.add(new FormHttpMessageConverter());
//
//        restTemplate.setMessageConverters(messageConverters);
//
//        return restTemplate;
//    }
}