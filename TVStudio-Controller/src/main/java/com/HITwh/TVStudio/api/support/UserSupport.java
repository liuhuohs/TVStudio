package com.HITwh.TVStudio.api.support;

import com.HITwh.TVStudio.service.exception.ConditionException;
import com.HITwh.TVStudio.service.util.TokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
@Component
public class UserSupport {
    public Long getCurrentUserId(){
        ServletRequestAttributes requestAttributes =(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = requestAttributes.getRequest().getHeader("token");
        Long userId= TokenUtil.verifyToken(token);
        if(userId<0){
            throw new ConditionException("非法用户");
        }
        return userId;
    }
}
