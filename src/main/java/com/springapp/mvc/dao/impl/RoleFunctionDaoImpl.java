package com.springapp.mvc.dao.impl;

import com.springapp.common.dao.BaseDaoImpl;
import com.springapp.mvc.entity.RoleFunction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("roleFunctionDao")
public class RoleFunctionDaoImpl extends BaseDaoImpl<RoleFunction>  {
    public void deleteByRoleId(String roleId) {
        super.getSqlSession().delete(getStatement("deleteByRoleId"),roleId);

    }

    public void deleteByFunctionId(String functionId) {
        super.getSqlSession().delete(getStatement("deleteByFunctionId"),functionId);
    }

    public List<String> getFunctionList(String roleId) {
        List<String> result = super.getSqlSession().selectList(getStatement("getFunctionList"), roleId);
        return result;
    }
}
