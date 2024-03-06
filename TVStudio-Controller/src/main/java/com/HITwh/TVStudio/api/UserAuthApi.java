package com.HITwh.TVStudio.api;

import com.HITwh.TVStudio.api.support.UserSupport;
import com.HITwh.TVStudio.domain.JsonResponse;
import com.HITwh.TVStudio.domain.auth.UserAuthorities;
import com.HITwh.TVStudio.service.UserAuthService;
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
