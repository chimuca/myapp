package com.weishe.weichat.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.weishe.weichat.bean.Attachment;
import com.weishe.weichat.bean.User;

@Repository
public class UserDao extends BaseDao {

	/**
	 * 通过账户查找用户
	 * 
	 * @param account
	 * @return
	 */
	public User getUserByAccount(String account) {
		String hql = "from  User where account = ? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setString(0, account);
		return (User) query.uniqueResult();
	}

	public User addUser(String name, String account, String password,
			Date birthday, String signature, int gender, Attachment avatar) {
		User user = new User();
		user.setAccount(account);
		user.setName(name);
		user.setPassword(password);
		user.setBirthday(birthday);
		user.setSignature(signature);
		user.setGender(gender);
		if (avatar != null) {
			user.setAvatar(avatar);
		}
		Integer id = (Integer) this.save(user);
		user.setId(id);
		return user;
	}

	public List<User> search(String condition) {
		if (condition == null || condition.isEmpty()) {
			return null;
		} else {
			if (!condition.startsWith("%")) {
				condition = "%" + condition;
			}
			if (!condition.endsWith("%")) {
				condition = condition + "%";
			}
		}
		String hql = "from  User where account like ? or name like ? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setString(0, condition);
		query.setString(1, condition);
		return query.list();
	}

	public User getUserById(int userId) {
		String hql = "from  User where id = ? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, userId);
		return (User) query.uniqueResult();
	}

}
