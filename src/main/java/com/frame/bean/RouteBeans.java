package com.frame.bean;

import com.frame.annotation.RequestMapping;
import com.frame.annotation.RequestMappingInfo;
import com.frame.annotation.RequestMethod;
import com.frame.utils.SpringUtils;
import com.frame.utils.StringUtils;
import io.netty.handler.codec.http.HttpHeaderNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class RouteBeans {
    private Logger logger = LoggerFactory.getLogger(RouteBeans.class);
    @Autowired
    SpringUtils springUtils;
    @Autowired
    StringUtils stringUtils;
    @Bean(value = "routes")
    public final Map<String,RequestMappingInfo> getRoutes(){
        Map<String,RequestMappingInfo> rmis = new HashMap<String,RequestMappingInfo>();
        //根据注解获取Bean集合
        Map<String,Object> controllers = springUtils.getBeanWithAccotation(Controller.class);
        controllers.forEach((beanName,obj) ->{
            Arrays.asList(obj.getClass().getDeclaredMethods()).stream().filter(method -> method.getAnnotation(RequestMapping.class)!=null).forEach(method ->{
                RequestMapping rm = method.getAnnotation(RequestMapping.class);
                RequestMappingInfo rmi = new RequestMappingInfo();
                rmi.setBeanName(beanName);
                rmi.setName(rm.name());
                rmi.setConsumes(rm.consumes());
                rmi.setHeaders(rm.headers());
                rmi.setMethod(rm.method());
                rmi.setParams(rm.params());
                rmi.setPath(rm.path());
                rmi.setProduces(rm.produces());
                rmi.setValue(rm.value());
                rmi.setExecMethod(method);
                if(rmis.containsKey(rm.value())){
                    logger.error("存在重复的请求路径：[{}]",rm.value());
                    System.exit(1);
                }
                rmis.put(rm.value(),rmi);
                logger.info("请求路径：[{}],请求方法：[{}],头信息：[{}]",rm.value(),rm.method(),rm.headers());
            });
        });
        logger.info("请求总数为：{} ",rmis.size());
        return rmis;
    }
    public RequestMappingInfo getRoute(String uri, RequestMethod method, String contentType){
        Map<String,RequestMappingInfo> routes = springUtils.getBean("routes",Map.class);
        if (routes.containsKey(uri)&& Objects.isNull(method)&&stringUtils.isEmpty(contentType)){
            return routes.get(uri);
        }else if(routes.containsKey(uri)&&!Objects.isNull(method)&&stringUtils.isEmpty(contentType)){
            RequestMappingInfo rmi = routes.get(uri);
            return Arrays.asList(rmi.getMethod()).contains(method) ? rmi : null;
        }else{
            RequestMappingInfo rmi = routes.get(uri);
            if(Objects.isNull(rmi)){
                return null;
            }
            boolean isContentType = false;
            boolean isMethod = Arrays.asList(rmi.getMethod()).contains(method);
            if(isMethod) {
                for (String header : rmi.getHeaders()) {
                    String[] hkv = header.split(":");
                    if (hkv[0].toLowerCase().equals(HttpHeaderNames.CONTENT_TYPE.toString()) && hkv[1].toLowerCase().startsWith(contentType)) {
                        isContentType = true;
                        break;
                    }
                }
                return isContentType?rmi:null;
            }
            return null;
        }
    }
}
