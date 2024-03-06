package com.imooc.TVStudio.service;

import com.imooc.TVStudio.dao.AuthRoleDao;
import com.imooc.TVStudio.domain.auth.AuthRole;
import com.imooc.TVStudio.domain.auth.AuthRoleElementOperation;
import com.imooc.TVStudio.domain.auth.AuthRoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AuthRoleService {
    @Autowired
    private AuthElementOperationService authElementOperationService;
    @Autowired
    private AuthRoleMenuService authRoleMenuService;
    @Autowired
    private AuthRoleDao authRoleDao;
    public List<AuthRoleElementOperation> getRoleElementOperationsByRoleIds(Set<Long> roleIdSet) {
        return authElementOperationService.getRoleElementOperationsByRoleIds(roleIdSet);
    }

    public List<AuthRoleMenu> getAuthRoleMenusByRoleIds(Set<Long> roleIdSet) {
        return authRoleMenuService.getAuthRoleMenusByRoleIds(roleIdSet);
    }

    public AuthRole getRoleByCode(String code) {
        return authRoleDao.getRoleByCode(code);
    }
}
