package com.HITwh.TVStudio.dao;

import com.HITwh.TVStudio.domain.UserMoment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMomentsDao {

    Integer addUserMoments(UserMoment userMoment);
}
