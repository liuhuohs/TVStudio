package com.imooc.TVStudio.dao;

import com.imooc.TVStudio.domain.auth.AuthRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthRoleDao {

    AuthRole getRoleByCode(String code);
}
