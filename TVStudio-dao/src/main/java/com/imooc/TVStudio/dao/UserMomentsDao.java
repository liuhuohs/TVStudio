package com.imooc.TVStudio.dao;

import com.imooc.TVStudio.domain.UserMoment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMomentsDao {

    Integer addUserMoments(UserMoment userMoment);
}
