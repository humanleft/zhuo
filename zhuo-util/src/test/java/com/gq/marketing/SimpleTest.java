package com.gq.marketing;

import com.gq.core.utils.CoreConstants;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * TODO
 *
 * @author ZhangLiang 2015年12月26日
 */
public class SimpleTest {

    @Test
    public void m1(){
        System.out.println(56 & 8);
        System.out.println(56 & 16);
        System.out.println(56 & 32);
    }

    @Test
    public void m2(){

        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.set("productId", "6");
        String uuid = UUID.randomUUID().toString().replace("-", "");
        HttpHeaders headers = new HttpHeaders();
        headers.set("clientType", "2");
        headers.add("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        restTemplate.exchange("http://10.100.160.121:9010/event/16/0/"+ uuid, HttpMethod.POST,
                              new HttpEntity<>(params, headers), String.class);
    }
}
