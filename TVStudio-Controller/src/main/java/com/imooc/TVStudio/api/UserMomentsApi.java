package com.imooc.TVStudio.api;

import com.imooc.TVStudio.api.support.UserSupport;
import com.imooc.TVStudio.domain.JsonResponse;
import com.imooc.TVStudio.domain.UserMoment;
import com.imooc.TVStudio.domain.annotation.ApiLimitedRole;
import com.imooc.TVStudio.domain.annotation.DataLimited;
import com.imooc.TVStudio.domain.constant.AuthRoleConstant;
import com.imooc.TVStudio.service.UserMomentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserMomentsApi {
    @Autowired
    private UserMomentsService userMomentsService;
    @Autowired
    private UserSupport userSupport;
    @ApiLimitedRole(limitedRoleCodeList = {AuthRoleConstant.ROLE_LV0})
    @DataLimited
    @PostMapping("/user-moments")
    public JsonResponse<String> addUserMoments(@RequestBody UserMoment userMoment) throws Exception {
        Long userId = userSupport.getCurrentUserId();
        userMoment.setUserId(userId);
        userMomentsService.addUserMonments(userMoment);
        return JsonResponse.success();
    }
    @GetMapping("/user-subscribed-moments")
    public JsonResponse<List<UserMoment>> getUserSubscribedMoments(){
        Long userId = userSupport.getCurrentUserId();
        List<UserMoment>list=userMomentsService.getUserSubscribedMoments(userId);
        return new JsonResponse<>(list);

    }
}
