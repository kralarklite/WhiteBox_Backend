package org.ltboys.aop;

import org.ltboys.context.WebEnv;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器切片
 */
@Slf4j
@Aspect
@Component
public class ControllerAspect {

    @Pointcut("execution(public * org.ltboys.controller*.*(..)) && @annotation(org.ltboys.annotation.OauthLogin)")
    public void OauthLoginCut() {}

    @Around("OauthLoginCut()")
    public Object aroundOauthLoginCut(ProceedingJoinPoint joinPoint) throws Throwable{
        log.info(String.format("调用接口类[%s]方法[%s]", joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getSignature().getName()));
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) ra;
        HttpServletRequest request = attributes.getRequest();
        String auth = "";
        try {
            auth = (String) request.getHeader("Authorization");
            if (StringUtils.isEmpty(auth)) {
                throw new RuntimeException("未授权登录");
            }

        } catch (Exception e) {
            throw new RuntimeException("未授权登录");
        }

        WebEnv.setOuthToken(auth);
        return joinPoint.proceed();
    }
}
