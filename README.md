# pwboot

## 项目介绍
本项目是一个通过maven工具管理，基于 spring boot框架，前后台完全分离的SSM典型实现，特点如下：
- 实现了基本的权限管理、日志管理和smartbi报表结合
- 基于 spring mvc 搭建整个平台，通过json传输前后台数据
- 通过独立的接口设计，实现前后端完全分离
- 通过nodejs，提供前端server，结合mock工具实现ajax拦截和数据模拟，得以实现前端完全脱离后台，实现完整展示功能，以适应快速迭代的敏捷开发和频繁的前端需求变化
- 通过maven工具管理项目，实现开发自测发布一体化管理
- ORM使用mybatis实现，灵活的实现持久化处理
- 自动生成代码功能，便于快速实现功能
- 使用Druid连接池，进行运行监控
- 完备的文档说明和问题帮助说明

## 使用说明
> 本项目通过maven管理项目，提供必备的maven命令如下：
- 下载依赖
```
$ mvn install
```
- 打包
```
$ mvn package
```
- 清理target目录
```
$ mvn clean
```
> 本项目提供mysql版本数据库脚本，详见doc目录，运行项目前请配置数据库

> 运行项目，执行PwbootApplication.java的main方法，打开url地址 http://localhost:8080/login.html

## 目录结构说明

```
├─doc
│  └─pw.sql
├─src
│  ├─main
│  │   ├─java # java源码
│  │   └─resources # 配置文件目录
│  │          ├─mybatis
│  │          │    ├─mapper # mapper映射
│  │          │    └─mybatis-config.xml # mybatis配置
│  │          ├─static # 前端
│  │          │    ├─css # 样式
│  │          │    ├─fonts # 字体
│  │          │    ├─img # 图片
│  │          │    ├─js # 非npm管理的第三方js和部分公共js
│  │          │    ├─mock # 公共模块mock
│  │          │    ├─modules # 业务逻辑代码模块
│  │          │    ├─node_modules # 利用npm管理的所有包及其依赖
│  │          │    ├─app.js # server入口
│  │          │    ├─examples.js
│  │          │    ├─favicon.ico
│  │          │    ├─index.html # 主页面
│  │          │    ├─login.html # 登录页面
│  │          │    ├─package.json # 项目包
│  │          │    └─README.md # 文档说明
│  │          ├─templates.generater # 自动生成代码velocity模板目录
│  │          ├─application.yml # 配置文件入口
│  │          ├─application-dev.yml # 开发环境配置
│  │          ├─application-pro.yml # 生产环境配置
│  │          ├─generator.properties # 自动生成代码属性配置
│  │          └─public_system.properties # 公共系统属性配置
│  └─test
│      └─java # 自测源码
├─target # 输出目录
├─pom.xml # maven配置文件
└─README.md # 文档说明
```
- 其中modules 业务逻辑代码模块中每一个模块由可以分为mock、page.js文件和html展示页面

## 界面（图片同pwssm）
![登录](https://github.com/superliu213/resources/blob/master/images/pwssm/%E7%99%BB%E5%BD%95.png)

![主页](https://github.com/superliu213/resources/blob/master/images/pwssm/%E4%B8%BB%E9%A1%B5.png)

![用户管理](https://github.com/superliu213/resources/blob/master/images/pwssm/%E7%94%A8%E6%88%B7%E7%AE%A1%E7%90%86.png)

![运行监控](https://github.com/superliu213/resources/blob/master/images/pwssm/%E8%BF%90%E8%A1%8C%E7%9B%91%E6%8E%A7.png)

![生成代码](https://github.com/superliu213/resources/blob/master/images/pwssm/%E7%94%9F%E6%88%90%E4%BB%A3%E7%A0%81.png)

## 调整思路（pwssm to pwboot）
### 分析pwssm配置
```
1.mybatis配置
    1.1 druid连接池
    1.2 指定配置mapper配置文件和mapper映射文件
        1.2.1 基本配置  别名 boolean类型 数据库适配器
        1.2.2 映射文件（原样使用）
    1.3 sqlsession模板实例配置
    1.4 sqlsession模板实例配置
    1.5 事务配置
2.springmvc
    2.1 RequestMappingHandlerMapping
    2.2 适配器 json适配器 返回json放入response中
    2.3 异常 ExceptionHandlerExceptionResolver
    2.4 权限拦截器
    2.5 配置resources
3.web.xml中配置字符集
4.druid
5.generator模板（单独）
6.log4j（单独）
```
默认支持logback
### 调整配置
> 使用DBConfig.java
  - 配置druid连接池及其监控（同1.1和4）
  - 显性地配置sqlSessionFactory（同1.3）
  - 显性地配置 sqlSessionTemplate（同1.4）

> 使用@EnableTransactionManagement开启事务（同1.5）

> 启用application.yml
  - 配置tomcat（同3）
  - 区分开发环境和生成环境配置
  - 配置mybatis（同1.2，mybatis的config和mappers没有任何调整）

> 使用WebConfigurer.java
  - springboot默认支持RequestMappingHandlerMapping和webBindingInitializer(2.1）
  - 配置MessageConverters(同2.2)
  - 配置HandlerExceptionResolvers(同2.3)
  - 权限拦截(同2.4)

> static目录放前端代码(同2.5)

> 启用application-dev.yml
  - 配置logback日志（等同于6）
  - 配置datasource属性，为DBConfig提供参数（等同于1.1）

> 调整部分generator代码模板(同5)

> 去除了dao接口使用daoimpl

## 问答
- 为什么弃用log4j而使用logback
  因为springboot默认支持logback，starter包中也含有logback包，不用再单独配置日志包，同时logback性能更优越，功能更强大
- 为什么去除了dao接口使用daoimpl
  可以直接使用接口加上@Mapper来进一步简化mybatis，但是为了统一代码，更好的使用封装的公共代码使用daoimpl更合适
- @SpringBootConfiguration和@Configuration的区别
  前者是一个包含多个注解的复合注解，其中就包含后者，为了简单在满足功能的情况下建议使用后者

