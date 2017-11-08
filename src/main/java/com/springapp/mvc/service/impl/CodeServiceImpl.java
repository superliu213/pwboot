package com.springapp.mvc.service.impl;

import com.springapp.common.page.PageBean;
import com.springapp.common.page.PageParam;
import com.springapp.common.service.BaseServiceImpl;
import com.springapp.common.util.GenUtils;
import com.springapp.common.entity.Columns;
import com.springapp.common.entity.Tables;
import com.springapp.mvc.dao.impl.CodeDaoImpl;
import com.springapp.mvc.service.CodeService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

@Service("codeService")
public class CodeServiceImpl extends BaseServiceImpl implements CodeService{

    @Autowired
    CodeDaoImpl codeDao;

    public PageBean getTables(PageParam pageParam, Map<String, Object> paramMap) {
        PageBean pageBean = codeDao.listPage(pageParam, paramMap);
        return pageBean;
    }

    public byte[] generatorCode(String[] buttonsNames, String[] tableNames) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for(String tableName : tableNames){
            //查询表信息
            Tables table = codeDao.getTable(tableName);
            //查询列信息
            List<Columns> columns = codeDao.getColumns(tableName);
            //生成代码
            GenUtils.generatorCode(table, columns, zip, buttonsNames);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }
}
