package com.imooc.TVStudio.service;

import com.alibaba.fastjson.JSONObject;
import com.imooc.TVStudio.dao.UserDao;
import com.imooc.TVStudio.domain.PageResult;
import com.imooc.TVStudio.domain.RefreshTokenDetail;
import com.imooc.TVStudio.domain.User;
import com.imooc.TVStudio.domain.UserInfo;
import com.imooc.TVStudio.domain.constant.UserConstant;
import com.imooc.TVStudio.service.exception.ConditionException;

import com.mysql.cj.util.StringUtils;
import com.imooc.TVStudio.service.util.TokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserAuthService userAuthService;
    public void addUser(User user){
        String phone = user.getPhone();
        if(StringUtils.isNullOrEmpty(phone)){
            throw new ConditionException("手机号不能为空");
        }
        User dbUser=getUserByPhone(phone);
        if(dbUser!=null){
            throw new ConditionException("该手机号已经注册");
        }
        Date now = new Date();
//        String salt = String.valueOf(now.getTime());
//        String password = user.getPassword();
//        String rawPassword;
//        try {
//            rawPassword = RSAUtil.decrypt(password);
//        } catch (Exception e) {
//            throw new ConditionException("密码解密失败");
//        }
//        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
//        user.setPassword(md5Password);
//        user.setSalt(salt);
        user.setCreateTime(now);
        userDao.addUser(user);
        //添加用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setNick(UserConstant.DEFAULT_NICK);
        userInfo.setBirth(UserConstant.DEFAULT_BIRTH);
        userInfo.setGender(UserConstant.GENDER_MALE);
        userInfo.setCreateTime(now);
        userDao.addUserInfo(userInfo);
        //添加用户默认权限角色
        userAuthService.addUserDefaultRole(user.getId());
    }
    public User getUserByPhone(String phone){
        return userDao.getUserByPhone(phone);
    }
    public User getUserById(Long id){return userDao.getUserById(id);}

    public String login(User user) throws Exception {
        String phone = user.getPhone();
        if(StringUtils.isNullOrEmpty(phone)){
            throw new ConditionException("手机号不能为空");
        }
        User dbUser=getUserByPhone(phone);
        if(dbUser==null){
            throw new ConditionException("当前用户不存在");
        }
        String password = user.getPassword();
//        String rawPassword;
//            try{
//                rawPassword = RSAUtil.decrypt(password);
//            }catch (Exception e){
//                throw new ConditionException("密码解密失败！");
//            }
//            String salt = dbUser.getSalt();
//            String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
//            if(!md5Password.equals(dbUser.getPassword())){
//                throw new ConditionException("密码错误！");
//            }
        if(!password.equals(dbUser.getPassword())){
            throw new ConditionException("密码错误！");
        }
            return TokenUtil.generateToken(dbUser.getId());
        }

    public User getUserInfo(Long userId) {
        User user = userDao.getUserById(userId);
        UserInfo userInfo = userDao.getUserInfoByUserId(userId);
        user.setUserInfo(userInfo);
        return user;
    }

    public void updateUserInfos(UserInfo userInfo) {
        userInfo.setUpdateTime(new Date());
        Integer integer = userDao.updateUserInfos(userInfo);

    }

    public List<UserInfo> getUserInfoByUserIds(Set<Long> followingIdSet) {
        return  userDao.getUserInfoByUserIds(followingIdSet);
    }

    public PageResult<UserInfo> pageListUserInfos(JSONObject params) {
        Integer no = params.getInteger("no");
        Integer size = params.getInteger("size");
        params.put("star",(no-1)*size);
        params.put("limit",size);
        Integer total = userDao.pageCountUserInfos(params);
        List<UserInfo> list = new ArrayList<>();
        if(total > 0){
            list = userDao.pageListUserInfos(params);
        }
        return new PageResult<>(total, list);
    }

    public Map<String, Object> loginForDts(User user) throws Exception {
        String phone = user.getPhone();
        if(StringUtils.isNullOrEmpty(phone)){
            throw new ConditionException("手机号不能为空");
        }
        User dbUser=getUserByPhone(phone);
        if(dbUser==null){
            throw new ConditionException("当前用户不存在");
        }
        String password = user.getPassword();
//        String rawPassword;
//            try{
//                rawPassword = RSAUtil.decrypt(password);
//            }catch (Exception e){
//                throw new ConditionException("密码解密失败！");
//            }
//            String salt = dbUser.getSalt();
//            String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
//            if(!md5Password.equals(dbUser.getPassword())){
//                throw new ConditionException("密码错误！");
//            }
        if(!password.equals(dbUser.getPassword())){
            throw new ConditionException("密码错误！");
        }
        Long userId = dbUser.getId();
        String accessToken= TokenUtil.generateToken(userId);
        String refreshToken = TokenUtil.generateRefreshToken(userId);
        //保存refresh token到数据库
        userDao.deleteRefreshToken(refreshToken,userId);
        userDao.addRefreshToken(refreshToken,userId,new Date());
        Map<String,Object>result=new HashMap<>();
        result.put("accessToken",accessToken);
        result.put("refreshToken",refreshToken);
        return result;
    }

    public void logout(String refreshToken, Long userId) {
        userDao.deleteRefreshToken(refreshToken, userId);
    }

    public String refreshAccessToken(String refreshToken) throws Exception {
        RefreshTokenDetail refreshTokenDetail = userDao.getRefreshTokenDetail(refreshToken);
        if(refreshTokenDetail==null){
            throw new ConditionException("555","token过期");
        }
        Long userId = refreshTokenDetail.getUserId();
        return TokenUtil.generateToken(userId);
    }

    public List<UserInfo> batchGetUserInfoByUserIds(Set<Long> userIdList) {
        return userDao.batchGetUserInfoByUserIds(userIdList);
    }
}
