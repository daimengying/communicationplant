package com.ahyx.wechat.communicationplant.utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.util.Map;
import java.util.Set;

/**
 * @Author: daimengying
 * @Date: 2018/6/5 15:42
 * @Description:远程调用工具类
 */
@Component
public class RestUtils {

    private  Logger _logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 使用getForEntity
     * @param url
     * @param reqMap
     * @return
     */
    public  String restGetForEntity(String url, Map reqMap){
        try {
            String reqJson = JSON.toJSONString(reqMap);
            StringBuffer paramStr=new StringBuffer();
            paramStr.append("?");
            Set keySet = reqMap.keySet();       //获取key集合对象
            for (Object keyName : keySet) {
                paramStr.append(keyName).append("="+ reqMap.get(keyName)+"&");
            }
            paramStr=paramStr.deleteCharAt(paramStr.length() - 1);
            _logger.info("【请求】调用远程服务请求报文：" + url+paramStr.toString());
            ResponseEntity<String> responseEntity = this.restTemplate.getForEntity( url+paramStr.toString(), String.class);
            String result = responseEntity.getBody();
            _logger.info("【响应】调用远程服务【" + url + "】返回报文：" + result);
            return result;
        }catch (Exception e){
            _logger.error(e.getMessage());
            return null;
        }

    }



    /**
     * 同步方法调用,使用postForObject
     * @param url
     * @param reqMap
     * @return string 直接返回json字符串
     */
    public String restCall(String url, Map reqMap){
        String reqJson = JSON.toJSONString(reqMap);
        _logger.info("【请求】调用远程服务【"  + url + "】请求报文：" + reqJson);
        String result = this.restTemplate.postForObject( url, reqJson, String.class);
        _logger.info("【响应】WEB调用远程服务【"  + url + "】返回报文：" + result);
        return result;
    }

    /**
     * exchange方法  || 统一接收json字符串
     * @param url
     * @param reqMap
     * @return
     */
    public String restCallExchange(String url ,Map reqMap){
        String reqJson = JSON.toJSONString(reqMap);
        _logger.info("【请求】调用远程服务【" +  url + "】请求报文：" + reqJson);
        //如果不在这里指定heads，则需要在配置bean的时候配置转换器
        HttpHeaders heads = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json ; charset=UTF-8");
        heads.setContentType(type);
        heads.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> entity = new HttpEntity<>(reqJson , heads);
        ResponseEntity<String> responseEntity = this.restTemplate.exchange( url , HttpMethod.POST , entity, String.class);
        String result = responseEntity.getBody();
        _logger.info("【响应】调用远程服务【" + url + "】返回报文：" + result);
        return result;
    }

    /**
     * 表单提交
     * POST方式
     * @param url
     * @param reqMap
     * @return
     */
    public String formPostExchange(String url ,MultiValueMap reqMap){
        String reqJson = JSON.toJSONString(reqMap);
        _logger.info("【请求】调用远程服务【" +  url + "】请求报文：" + reqJson);
        HttpHeaders heads = new HttpHeaders();
        heads.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String,Object>> entity = new HttpEntity<>(reqMap , heads);
        ResponseEntity<String> responseEntity = this.restTemplate.exchange( url , HttpMethod.POST , entity, String.class);
        String result = responseEntity.getBody();
        _logger.info("【响应】调用远程服务【" + url + "】返回报文：" + result);
        return result;
    }
}


