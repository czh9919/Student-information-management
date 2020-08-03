package com.lhl.springboot.springbootdemo.aspect;

import cn.hutool.core.util.URLUtil;

import cn.hutool.json.JSONUtil;
import com.lhl.springboot.springbootdemo.entity.WebLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
/**
 * @author czh99
 */
@Aspect
@Component
public class WebLogAspect{
    public static final Logger LOGGER= LoggerFactory.getLogger(WebLogAspect.class);

    @Pointcut("execution(public * com.lhl.springboot.springbootdemo.controller.*.*(..)))")
    public void log(){}

    @Around("log()")
    public Object logAround(ProceedingJoinPoint proceedingJoinPoint)throws Throwable{
        long startTime=System.currentTimeMillis();

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request=attributes.getRequest();

        WebLog webLog=new WebLog();
        Object result = proceedingJoinPoint.proceed();
        Signature signature=proceedingJoinPoint.getSignature();

        long endTime=System.currentTimeMillis();
        String urlString =request.getRequestURL().toString();

        webLog.setBasePath(URLUtil.url(urlString ).getPath());
        webLog.setIp(request.getRemoteUser());
        webLog.setMethod(request.getMethod());
        //lost parameter
        webLog.setResult(result);
        webLog.setStartTime(startTime);
        webLog.setSpendTime((int)(endTime-startTime));
        webLog.setUri(request.getRequestURI());
        webLog.setUrl(request.getRequestURL().toString());
        LOGGER.info("{}", JSONUtil.toJsonStr(webLog));


        return result;
    }
}
