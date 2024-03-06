package com.HITwh.TVStudio.api;

import com.HITwh.TVStudio.api.support.UserSupport;
import com.HITwh.TVStudio.domain.JsonResponse;
import com.HITwh.TVStudio.domain.UserMoment;
import com.HITwh.TVStudio.domain.annotation.ApiLimitedRole;
import com.HITwh.TVStudio.domain.annotation.DataLimited;
import com.HITwh.TVStudio.domain.constant.AuthRoleConstant;
import com.HITwh.TVStudio.service.UserMomentsService;
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
