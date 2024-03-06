package com.HITwh.TVStudio.api.aspect;

import com.HITwh.TVStudio.api.support.UserSupport;
import com.HITwh.TVStudio.domain.UserMoment;
import com.HITwh.TVStudio.domain.auth.UserRole;
import com.HITwh.TVStudio.domain.constant.AuthRoleConstant;
import com.HITwh.TVStudio.service.UserRoleService;
import com.HITwh.TVStudio.service.exception.ConditionException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Order(1)
@Component
@Aspect
public class DataLimitedAspect {
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private UserRoleService userRoleService;
    @Pointcut("@annotation(com.HITwh.TVStudio.domain.annotation.DataLimited)")
    public void check(){

    }
    @Before("check()")
    public void doBefore(JoinPoint joinPoint){
        Long userId = userSupport.getCurrentUserId();
        List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);
        Set<String> roleCodeSet = userRoleList.stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
        Object[] args = joinPoint.getArgs();
        for(Object arg:args){
            if(arg instanceof UserMoment){
                UserMoment userMoment=(UserMoment)arg;
                String type = userMoment.getType();
                if(roleCodeSet.contains(AuthRoleConstant.ROLE_LV1)&&!"0".equals(type)){
                    throw new ConditionException("参数异常");
                }
            }
        }
    }
}
