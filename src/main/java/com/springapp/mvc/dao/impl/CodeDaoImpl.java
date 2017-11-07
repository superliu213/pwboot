package com.springapp.mvc.dao.impl;

import com.springapp.common.dao.BaseDaoImpl;
import com.springapp.common.entity.Columns;
import com.springapp.common.entity.Tables;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("codeDao")
public class CodeDaoImpl extends BaseDaoImpl  {

    public Tables getTable(String tableName) {
        Tables tables = super.getSqlSession().selectOne(getStatement("getTable"), tableName);
        return tables;
    }

    public List<Columns> getColumns(String tableName) {
        List<Columns> columns = super.getSqlSession().selectList(getStatement("getColumns"), tableName);
        return columns;
    }
}
