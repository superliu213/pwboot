package com.springapp.mvc.service.impl;

import com.springapp.common.application.ApplicationGlobalNames;
import com.springapp.common.page.PageBean;
import com.springapp.common.page.PageParam;
import com.springapp.common.service.BaseServiceImpl;
import com.springapp.common.util.MD5Util;
import com.springapp.mvc.dao.impl.UserDaoImpl;
import com.springapp.mvc.dao.impl.UserGroupDaoImpl;
import com.springapp.mvc.dao.impl.UserRoleDaoImpl;
import com.springapp.mvc.entity.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userService")
@Transactional
public class UserServiceImpl extends BaseServiceImpl {

    @Autowired
	UserDaoImpl userDao;

	@Autowired
	UserGroupDaoImpl userGroupDao;

	@Autowired
	UserRoleDaoImpl userRoleDao;

	Log logger = LogFactory.getLog(UserServiceImpl.class);

	public void initData() {
		Date date = new Date();
		User user1 = new User();
		Date pwValidityPeriod = date;
		String remark = "1";
		Date userBirthday = date;
		String userEmail = "liuliu@163.com";
		String userIdCard = "111111";
		String userTelephone = "123456789";
		Date userValidityPeriod = date;

		user1.setUserId("admin");
		user1.setUserPassWord("21232f297a57a5a743894a0e4a801fc3");//admin
		user1.setUserName("超级管理员");
		user1.setIfValid((short) 1);

		user1.setPwValidityPeriod(pwValidityPeriod);
		user1.setRemark(remark);
		user1.setUserBirthday(userBirthday);
		user1.setUserEmail(userEmail);
		user1.setUserIdCard(userIdCard);
		user1.setUserTelephone(userTelephone);
		user1.setUserValidityPeriod(userValidityPeriod);

		User user2 = new User();
		Date pwValidityPeriod2 = date;
		String remark2 = "1";
		Date userBirthday2 = date;
		String userEmail2 = "liuliu@163.com";
		String userIdCard2 = "111111";
		String userTelephone2 = "123456789";

		user2.setUserId("1");
		user2.setUserPassWord("c4ca4238a0b923820dcc509a6f75849b");//1
		user2.setUserName("2");
		user2.setIfValid((short) 1);

		user2.setPwValidityPeriod(pwValidityPeriod2);
		user2.setRemark(remark2);
		user2.setUserBirthday(userBirthday2);
		user2.setUserEmail(userEmail2);
		user2.setUserIdCard(userIdCard2);
		user2.setUserTelephone(userTelephone2);

		List<User> users = userDao.listBy(null);
		for (User user : users) {
			userDao.deleteById(user.getId());
		}

		userDao.insert(user1);
		userDao.insert(user2);

	}

	public PageBean getUsers(PageParam pageParam, Map<String, Object> paramMap) {
		PageBean pageBean = userDao.listPage(pageParam, paramMap);
		return pageBean;
	}

	public List<User> getAllUsers() {
		List<User> result = userDao.listBy(null);
		return result;
	}

	public void removeUserByKey(Long id) {
		User user = userDao.getById(id);
		String userId = user.getUserId();
		userGroupDao.deleteByUserId(userId);
		userRoleDao.deleteByRoleId(userId);
		userDao.deleteById(id);
	}

	public void addUser(User user) {
		userDao.insert(user);
	}

	public void updateUser(User user) {
		userDao.update(user);
	}

	public Boolean checkLoginUser(Map<String, Object> paramMap) {
		Boolean result = true;

        List<User> resultList = userDao.getUserByUserIdAndPassword(paramMap);

        if(resultList == null || resultList.size() == 0){
            result = false;
        }

		return result;
	}

	public void passwordreset(Long id,Integer version) {
		String newPasswordMd5 = MD5Util.MD5(ApplicationGlobalNames.RESET_PASSWD);
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id",id);
		paramMap.put("userPassWord", newPasswordMd5);
		paramMap.put("version",version);

		userDao.updatePassword(paramMap);
	}

	public void updatepassword(Long id, String oldPassword, String newPassword, Integer version) {
		User user = userDao.getById(id);
		String oldPasswordMd5 = MD5Util.MD5(oldPassword);
		String newPasswordMd5 = null;

		if (user.getUserPassWord().equals(oldPasswordMd5)) {
			newPasswordMd5 = MD5Util.MD5(newPassword);
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("id",id);
			paramMap.put("userPassWord",newPasswordMd5);
			paramMap.put("version",version);
			userDao.updatePassword(paramMap);
		}
//		else {
//			result = "旧密码同数据库数据不一致，请检查";
//		}
	}

	public User getUser(Long id) {
		User user = userDao.getById(id);
		return user;
	}
}
