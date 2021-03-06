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
