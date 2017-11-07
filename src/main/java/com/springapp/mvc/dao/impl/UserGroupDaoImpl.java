package com.springapp.mvc.dao.impl;

import com.springapp.common.dao.BaseDaoImpl;
import com.springapp.mvc.entity.UserGroup;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userGroupDao")
public class UserGroupDaoImpl extends BaseDaoImpl<UserGroup>  {

    public void deleteByUserId(String userId) {
        super.getSqlSession().delete(getStatement("deleteByUserId"),userId);
    }

    public void deleteByGroupId(String groupId) {
        super.getSqlSession().delete(getStatement("deleteByGroupId"),groupId);

    }

    public List<String> getUserGroupByUserId(String userId) {
        List<String> reuslt = super.getSqlSession().selectList(getStatement("getUserGroupByUserId"),userId);
        return reuslt;
    }

}
