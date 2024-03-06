package com.imooc.TVStudio.api;

import com.imooc.TVStudio.api.support.UserSupport;
import com.imooc.TVStudio.domain.JsonResponse;
import com.imooc.TVStudio.domain.auth.UserAuthorities;
import com.imooc.TVStudio.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAuthApi {
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private UserAuthService userAuthService;
    @GetMapping("/user-authorities")
    public JsonResponse<UserAuthorities> getUserAuthorities(){
        Long userId = userSupport.getCurrentUserId();
        UserAuthorities userAuthorities = userAuthService.getUserAuthorities(userId);
        return new JsonResponse<>(userAuthorities);
    }
}
