package com.imooc.TVStudio.service;

import com.imooc.TVStudio.dao.AuthRoleElementOperationDao;
import com.imooc.TVStudio.domain.auth.AuthRoleElementOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AuthElementOperationService {
    @Autowired
    private AuthRoleElementOperationDao authRoleElementOperationDao;
    public List<AuthRoleElementOperation> getRoleElementOperationsByRoleIds(Set<Long> roleIdSet) {
        return authRoleElementOperationDao.getRoleElementOperationsByRoleIds(roleIdSet);
    }
}
