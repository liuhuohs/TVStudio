package com.HITwh.TVStudio.dao;

import com.HITwh.TVStudio.domain.auth.AuthRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthRoleDao {

    AuthRole getRoleByCode(String code);
}
