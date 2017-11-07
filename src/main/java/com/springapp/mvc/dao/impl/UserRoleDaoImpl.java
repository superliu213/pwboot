package com.springapp.mvc.dao.impl;

import com.springapp.common.dao.BaseDaoImpl;
import com.springapp.mvc.entity.UserRole;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userRoleDao")
public class UserRoleDaoImpl extends BaseDaoImpl<UserRole>  {

    public void deleteByUserId(String userId) {
        super.getSqlSession().delete(getStatement("deleteByUserId"),userId);
    }

    public void deleteByRoleId(String roleId) {
        super.getSqlSession().delete(getStatement("deleteByRoleId"),roleId);
    }

    public List<String> getRoleList(String userId) {
        List<String> result = super.getSqlSession().selectList(getStatement("getRoleList"), userId);
        return result;
    }
}
