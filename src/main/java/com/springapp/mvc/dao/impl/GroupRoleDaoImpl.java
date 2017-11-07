package com.springapp.mvc.dao.impl;

import com.springapp.common.dao.BaseDaoImpl;
import com.springapp.mvc.entity.GroupRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("groupRoleDao")
public class GroupRoleDaoImpl extends BaseDaoImpl<GroupRole>  {

    public void deleteByGroupId(String groupId) {
        super.getSqlSession().delete(getStatement("deleteByGroupId"),groupId);
    }

    public void deleteByRoleId(String roleId) {
        super.getSqlSession().delete(getStatement("deleteByRoleId"),roleId);
    }

    public List<String> getRoleList(String groupId) {
        List<String> reuslt = super.getSqlSession().selectList(getStatement("getRoleList"),groupId);
        return reuslt;
    }
}
