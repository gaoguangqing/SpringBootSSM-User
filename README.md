# SpringBootSSM-User 使用MyBatis Plus
SpringBoot整合SSM-User例子

使用SpringBoot + Mybatis + MybatisPlus(MP) 全新注解方式，自动产生SQL语句，替代旧的通用Mapper。旧的通用Mapper是基于Mybatis拦截器的机制，而新的MybatisPlus是基于注解扫描机制，在启动服务时就进行加载，所以性能比旧的通用Mapper方式高很多。

使用user.sql建库建表
```
CREATE DATABASE school CHARSET utf8;
USE school;
REATE TABLE `user` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(30) DEFAULT NULL,
  `BIRTHDAY` DATETIME DEFAULT NULL,
  `ADDRESS` VARCHAR(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
INSERT  INTO `user`(`ID`,`NAME`,`BIRTHDAY`,`ADDRESS`) VALUES (
1,'张三','1573-01-01 00:00:00','桂州村'),
(2,'李四','1587-01-01 00:00:00','分宜县城介桥村'),
(3,'王五','1580-01-01 00:00:00','明松江府华亭县'),
(4,'赵六','1566-01-01 00:00:00','河南省新郑市高老庄村'),
(5,'钱七','1558-01-01 00:00:00','江陵');
```
新建maven,jar项目，修改pom.xml
```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.spoon</groupId>
  <artifactId>usercenter</artifactId>
  <version>0.0.1-SNAPSHOT</version>
<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>1.5.6.RELEASE</version>
		<relativePath />
	</parent>
	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
		<java.version>1.8</java.version>
		<spring-cloud.version>Dalston.SR1</spring-cloud.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-eureka</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		
		<!-- mybatisplus/mybatis/jdbc/druid -->
		<dependency>
			<groupId>org.mybatis.spring.boot</groupId>
			<artifactId>mybatis-spring-boot-starter</artifactId>
			<version>1.3.0</version>
		</dependency>
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
		</dependency>
		<!-- mybatisplus与springboot整合 -->
		<dependency>
			<groupId>com.baomidou</groupId>
			<artifactId>mybatisplus-spring-boot-starter</artifactId>
			<version>1.0.5</version>
		</dependency>
		<!-- MP 核心库 -->
		<dependency>
			<groupId>com.baomidou</groupId>
			<artifactId>mybatis-plus</artifactId>
			<version>2.1.8</version>
		</dependency>
		<dependency>
			<groupId>com.github.pagehelper</groupId>
			<artifactId>pagehelper-spring-boot-starter</artifactId>
			<version>1.2.2</version>
		</dependency>
		<!-- alibaba的druid数据库连接池 -->
		<dependency>
			<groupId>com.alibaba</groupId>
			<artifactId>druid-spring-boot-starter</artifactId>
			<version>1.1.0</version>
		</dependency>
	</dependencies>
	<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-dependencies</artifactId>
				<version>${spring-cloud.version}</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>
</project>
```
编写pojo
@TableName("user") @TableId(type=IdType.AUTO)为MyBatisPlus特有注解
```
package com.spoon.pojo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
@TableName("user")
public class User implements Serializable{
	/**
	 * 表和类映射
	 * select 字段* from @TableName
	 */
	private static final long serialVersionUID = 1L;
	//主键,自增主键策略
	@TableId(type=IdType.AUTO)
	private Integer id;
	//映射，全局配置驼峰规则，MyBatisPlus自动修改
	private String name;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date birthday;
	private String address;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", birthday=" + birthday + ", address=" + address + "]";
	}
	
}

```
编写Controller
```
package com.spoon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spoon.pojo.User;
import com.spoon.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	@RequestMapping("/findAll")
	public List<User> findAll() {
		return userService.findAll();
	}
	@RequestMapping("/find/{name}")
	public List<User> find(User user) {
		return userService.find(user);
	}
	@RequestMapping("/insert/{name}/{address}/{birthday}")
	public String insert(User user) {
		try {
			System.out.println(user);
			userService.insert(user);
			return "插入成功";
		} catch (Exception e) {
			e.printStackTrace();
			return "插入失败";
		}
	}
	@RequestMapping("/update/{name}/{address}/{birthday}/{id}")
	public String  update(User user) {
		try {
			userService.update(user);
			return "修改成功";
		} catch (Exception e) {
			e.printStackTrace();
			return "修改失败";
		}
	}
	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {
		try {
			userService.delete(id);
			return "删除成功";
		} catch (Exception e) {
			e.printStackTrace();
			return "删除失败";
		}
	}
}
```
编写service
```
package com.spoon.service;

import java.util.List;

import com.spoon.pojo.User;

public interface UserService {
	List<User> findAll();

	List<User> find(User user);
	
	void insert(User user);

	void update(User user);
	public void delete(Integer id);
}
```
编写serviceImpl,MyBatis Plus自带如下mapper
#### 删除的
>     userMapper.delete(wrapper);
>   	userMapper.deleteBatchIds(idList);
>   	userMapper.deleteById(id);
>     userMapper.deleteByMap(columnMap);
#### 插入的	
>     userMapper.insert(entity);
>  	  userMapper.insertAllColumn(entity);
#### 查询的
>  		userMapper.selectBatchIds(idList);
>  		userMapper.selectById(id);
>  		userMapper.selectByMap(columnMap);
>  		userMapper.selectCount(wrapper)l
>  		userMapper.selectList(wrapper);
>  		userMapper.selectMaps(wrapper);
>  		userMapper.selectMapsPage(rowBounds, wrapper);
>  		userMapper.selectObjs(wrapper);
>  		userMapper.selectOne(entity);
>  		userMapper.selectPage(rowBounds, wrapper);
#### 更新的				
>  		userMapper.update(entity, wrapper);
>  		userMapper.updateAllColumnById(entity);
>  		userMapper.updateById(entity);
```
package com.spoon.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.spoon.mapper.UserMapper;
import com.spoon.pojo.User;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserMapper userMapper;
	
	//查询所有
	public List<User> findAll()
	{
		return userMapper.selectList(null);
	}
	//带条件查询EntityWrapper
	public List<User> find(User user) {
		//封装where条件
		EntityWrapper<User> wrapper=new EntityWrapper<User>();
		//封装对象里面写的都是数据库的字段的名称
		//QBC面向对象
		wrapper.like("name", user.getName()); //name like '%tony%'
		return userMapper.selectList(wrapper);
	}
	//新增
	public void insert(User user){
		userMapper.insert(user);
	}
	//修改
	public void update(User user) {
		userMapper.updateById(user);
	}
	//删除
	public void delete(Integer id){
		List<Integer> idList=new ArrayList<Integer>();
		idList.add(id);
		//批量删除
		userMapper.deleteBatchIds(idList);
	}
}
```
编写启动类，注意添加包扫描注解@MapperScan(basePackages="com.spoon.mapper")
```
package com.spoon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages="com.spoon.mapper")
public class RunAppProviderUser {

	public static void main(String[] args) {
		SpringApplication.run(RunAppProviderUser.class, args);
	}
}

```
运行结果
http://localhost:7900/user/findAll
```
2018-11-12 22:42:39.421 DEBUG 10504 --- [nio-7900-exec-1] com.spoon.mapper.UserMapper.selectList   : ==>  Preparing: SELECT id AS id,`name`,birthday,address FROM user 
2018-11-12 22:42:39.468 DEBUG 10504 --- [nio-7900-exec-1] com.spoon.mapper.UserMapper.selectList   : ==> Parameters: 
2018-11-12 22:42:39.533 DEBUG 10504 --- [nio-7900-exec-1] com.spoon.mapper.UserMapper.selectList   : <==      Total: 5
```
```
[{"id":1,"name":"张三","birthday":-12527251200000,"address":"桂州村"},{"id":2,"name":"李四","birthday":-12086352000000,"address":"分宜县城介桥村"},{"id":3,"name":"王五","birthday":-12306412800000,"address":"明松江府华亭县"},{"id":4,"name":"赵六","birthday":-12748176000000,"address":"河南省新郑市高老庄村"},{"id":5,"name":"钱七","birthday":-13000636800000,"address":"江陵"}]
```


http://localhost:7900/user/find/李四
```
2018-11-12 22:44:52.622 DEBUG 10504 --- [nio-7900-exec-5] com.spoon.mapper.UserMapper.selectList   : ==>  Preparing: SELECT id AS id,`name`,birthday,address FROM user WHERE (name LIKE ?) 
2018-11-12 22:44:52.626 DEBUG 10504 --- [nio-7900-exec-5] com.spoon.mapper.UserMapper.selectList   : ==> Parameters: %李四%(String)
2018-11-12 22:44:52.635 DEBUG 10504 --- [nio-7900-exec-5] com.spoon.mapper.UserMapper.selectList   : <==      Total: 1
```
```
[{"id":2,"name":"李四","birthday":-12086352000000,"address":"分宜县城介桥村"}]
```

http://localhost:7900/user/insert/moyan/beijing/1983-08-12
```
2018-11-12 22:49:07.540 DEBUG 6884 --- [nio-7900-exec-1] com.spoon.mapper.UserMapper.insert       : ==>  Preparing: INSERT INTO user ( `name`, birthday, address ) VALUES ( ?, ?, ? ) 
2018-11-12 22:49:07.591 DEBUG 6884 --- [nio-7900-exec-1] com.spoon.mapper.UserMapper.insert       : ==> Parameters: moyan(String), 1983-08-12 00:00:00.0(Timestamp), beijing(String)
2018-11-12 22:49:07.602 DEBUG 6884 --- [nio-7900-exec-1] com.spoon.mapper.UserMapper.insert       : <==    Updates: 1
```
```
插入成功
```


http://localhost:7900/user/delete/6
```
2018-11-12 22:50:35.866 DEBUG 6884 --- [nio-7900-exec-3] c.s.mapper.UserMapper.deleteBatchIds     : ==>  Preparing: DELETE FROM user WHERE id IN ( ? ) 
2018-11-12 22:50:35.870 DEBUG 6884 --- [nio-7900-exec-3] c.s.mapper.UserMapper.deleteBatchIds     : ==> Parameters: 6(Integer)
2018-11-12 22:50:35.896 DEBUG 6884 --- [nio-7900-exec-3] c.s.mapper.UserMapper.deleteBatchIds     : <==    Updates: 1
```
```
删除成功
```

http://localhost:7900/user/update/zhangdaqian/nanjing/1995-04-07/1
```
2018-11-12 23:14:50.337 DEBUG 5072 --- [nio-7900-exec-1] com.spoon.mapper.UserMapper.updateById   : ==>  Preparing: UPDATE user SET `name`=?, birthday=?, address=? WHERE id=? 
2018-11-12 23:14:50.408 DEBUG 5072 --- [nio-7900-exec-1] com.spoon.mapper.UserMapper.updateById   : ==> Parameters: zhangdaqian(String), 1995-04-07 00:00:00.0(Timestamp), nanjing(String), 1(Integer)
2018-11-12 23:14:50.424 DEBUG 5072 --- [nio-7900-exec-1] com.spoon.mapper.UserMapper.updateById   : <==    Updates: 1
```
```
修改成功
```

