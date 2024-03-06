package com.imooc.TVStudio.api.support;

import com.imooc.TVStudio.service.exception.ConditionException;
import com.imooc.TVStudio.service.util.TokenUtil;
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
