package com.springapp.common.util;


import com.springapp.common.entity.Columns;
import com.springapp.common.entity.Tables;
import com.springapp.common.exceptions.ApplicationException;
import com.springapp.common.vo.ColumnDO;
import com.springapp.common.vo.TableDO;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器   工具类
 *
 */
public class GenUtils {


	public static List<String> getTemplates(){
		List<String> templates = new ArrayList<String>();
		templates.add("templates/generator/Controller.java.vm");
		templates.add("templates/generator/DaoImpl.java.vm");
		templates.add("templates/generator/Entity.java.vm");
		templates.add("templates/generator/Function.sql.vm");
		templates.add("templates/generator/Mapper.xml.vm");
		templates.add("templates/generator/Page.html.vm");
		templates.add("templates/generator/Page.js.vm");
		templates.add("templates/generator/URL.js.vm");
		templates.add("templates/generator/Service.java.vm");
		templates.add("templates/generator/ServiceImpl.java.vm");
		return templates;
	}

	/**
	 * 生成代码
	 */
	public static void generatorCode(Tables table, List<Columns> columns, ZipOutputStream zip, String[] buttonsNames){
		//配置信息
		Configuration config = getConfig();
		//表信息
		TableDO TableDO = new TableDO();
		TableDO.setTableName(table.getTableName());
		TableDO.setComments(table.getTableComment());
		//表名转换成Java类名
		String className = tableToJava(TableDO.getTableName(), config.getString("tablePrefix"),config.getString("autoRemovePre"));
		TableDO.setClassName(className);
		TableDO.setClassname(StringUtils.uncapitalize(className));

		//列信息
		List<ColumnDO> columsList = new ArrayList<>();
		for(Columns column : columns){
			ColumnDO ColumnDO = new ColumnDO();
			ColumnDO.setColumnName(column.getColumnName());
			ColumnDO.setDataType(column.getDataType());
			ColumnDO.setComments(column.getColumnComment());
			ColumnDO.setExtra(column.getExtra());

			//列名转换成Java属性名
			String attrName = columnToJava(ColumnDO.getColumnName());
			ColumnDO.setAttrName(attrName);
			ColumnDO.setAttrname(StringUtils.uncapitalize(attrName));

			//列的数据类型，转换成Java类型
			String attrType = config.getString(ColumnDO.getDataType(), "unknowType");
			ColumnDO.setAttrType(attrType);

			//是否主键
			if("PRI".equalsIgnoreCase(column.getColumnKey()) && TableDO.getPk() == null){
				TableDO.setPk(ColumnDO);
			}

			columsList.add(ColumnDO);
		}
		TableDO.setColumns(columsList);

		//没主键，则第一个字段为主键
		if(TableDO.getPk() == null){
			TableDO.setPk(TableDO.getColumns().get(0));
		}

		//设置velocity资源加载器
		Properties prop = new Properties();
		prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		Velocity.init(prop);

		//封装模板数据
		Map<String, Object> map = new HashMap<>();
		map.put("tableName", TableDO.getTableName());
		map.put("comments", TableDO.getComments());
		map.put("pk", TableDO.getPk());
		map.put("className", TableDO.getClassName());
		map.put("classname", TableDO.getClassname());
		map.put("pathName", config.getString("package").substring(config.getString("package").lastIndexOf(".") + 1));
		map.put("columns", TableDO.getColumns());
		map.put("package", config.getString("package"));
		map.put("author", config.getString("author"));
		map.put("email", config.getString("email"));
		map.put("datetime", DateTimeUtil.dateToString(new Date(), DateTimeUtil.DATE_TIME_PATTERN));
		for (String buttonsName : buttonsNames) {
			map.put(buttonsName,buttonsName);
		}

        VelocityContext context = new VelocityContext(map);

        //获取模板列表
		List<String> templates = getTemplates();
		for(String template : templates){
			//渲染模板
			StringWriter sw = new StringWriter();
			Template tpl = Velocity.getTemplate(template, "UTF-8");
			tpl.merge(context, sw);

			try {
				//添加到zip
				zip.putNextEntry(new ZipEntry(getFileName(template, TableDO.getClassname(),TableDO.getClassName(), config.getString("package").substring(config.getString("package").lastIndexOf(".")+1))));
				IOUtils.write(sw.toString(), zip, "UTF-8");
				IOUtils.closeQuietly(sw);
				zip.closeEntry();
			} catch (IOException e) {
				throw new ApplicationException("渲染模板失败，表名：" + TableDO.getTableName(), e);
			}
		}
	}


	/**
	 * 列名转换成Java属性名
	 */
	public static String columnToJava(String columnName) {
		return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
	}

	/**
	 * 表名转换成Java类名
	 */
	public static String tableToJava(String tableName, String tablePrefix,String autoRemovePre) {
		if ("true".equals(autoRemovePre)) {
			tableName = tableName.substring(tableName.indexOf("_")+1);
		}
		if(StringUtils.isNotBlank(tablePrefix)){
			tableName = tableName.replace(tablePrefix, "");
		}

		return columnToJava(tableName);
	}

	/**
	 * 获取配置信息
	 */
	public static Configuration getConfig(){
		try {
			return new PropertiesConfiguration("generator.properties");
		} catch (ConfigurationException e) {
			throw new ApplicationException("获取配置文件失败，", e);
		}
	}

	/**
	 * 获取文件名
	 */
	public static String getFileName(String template, String classname,String className, String packageName){
		String packagePath = "main" + File.separator + "java" + File.separator;//main/java/
		if(StringUtils.isNotBlank(packageName)){
			packagePath += packageName.replace(".", File.separator) + File.separator;//+=com/spring/mvc
		}

		if(template.contains("Controller.java.vm")){
			return packagePath + "controllers" + File.separator + className + "Controller.java";
		}

		if(template.contains("DaoImpl.java.vm")){
			return packagePath + "dao" + File.separator + "impl" + File.separator + className + "DaoImpl.java";
		}

		if(template.contains("Entity.java.vm")){
			return packagePath + "entity" + File.separator + className + ".java";
		}

		if(template.contains("Function.sql.vm")){
			return packagePath + "function" + File.separator + className + ".sql";
		}
		if(template.contains("Mapper.xml.vm")){
			return "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + className + File.separator + className + ".Mapper.xml";
		}

		if(template.contains("Page.html.vm")){
			return  "frontframe" + File.separator + classname + File.separator + classname + ".html";
		}

		if(template.contains("Page.js.vm")){
			return  "frontframe" + File.separator + classname + File.separator + "page.js";
		}

		if(template.contains("URL.js.vm")){
			return  "frontframe" + File.separator + classname + File.separator + "URL.js";
		}

		if(template.contains("Service.java.vm")){
			return packagePath + "service" + File.separator + className + "Service.java";
		}

		if(template.contains("ServiceImpl.java.vm")){
			return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
		}

		return null;
	}
}
