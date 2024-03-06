package com.imooc.TVStudio.api;

import com.imooc.TVStudio.api.support.UserSupport;
import com.imooc.TVStudio.domain.FollowingGroup;
import com.imooc.TVStudio.domain.JsonResponse;
import com.imooc.TVStudio.domain.UserFollowing;
import com.imooc.TVStudio.service.UserFollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserFollowingApi {
    @Autowired
    private UserFollowingService userFollowingService;
    @Autowired
    private UserSupport userSupport;
    @PostMapping("/user-followings")
    private JsonResponse<String> addUserFollowings(@RequestBody UserFollowing userFollowing){
        Long userId = userSupport.getCurrentUserId();
        userFollowing.setUserId(userId);
        userFollowing.setUserId(userId);
        userFollowingService.addUserFollowings(userFollowing);
        return JsonResponse.success();
    }
    @GetMapping("/user-followings")
    public JsonResponse<List<FollowingGroup>> getUserFollowings(){
        Long userId = userSupport.getCurrentUserId();
        List<FollowingGroup> result = userFollowingService.getUserFollowings(userId);
        return new JsonResponse<>(result);
    }
    @GetMapping("/user-fans")
    public JsonResponse<List<UserFollowing>> getUserFans(){
        Long userId = userSupport.getCurrentUserId();
        List<UserFollowing> result = userFollowingService.getUserFans(userId);
        return new JsonResponse<>(result);
    }
    @PostMapping("/user-following-groups")
    public JsonResponse<Long> addUserFollowingGroups(@RequestBody FollowingGroup followingGroup){
        Long userId = userSupport.getCurrentUserId();
        followingGroup.setUserId(userId);
        Long groupId = userFollowingService.addUserFollowingGroups(followingGroup);
        return new JsonResponse<>(groupId);
    }
    @GetMapping("/user-following-groups")
    public JsonResponse<List<FollowingGroup>> getUserFollowingGroups(){
        Long userId = userSupport.getCurrentUserId();
        List<FollowingGroup> list = userFollowingService.getUserFollowingGroups(userId);
        return new JsonResponse<>(list);
    }
}
