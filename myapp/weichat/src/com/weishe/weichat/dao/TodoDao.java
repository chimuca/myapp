package com.weishe.weichat.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.weishe.weichat.bean.Todo;

@Repository
public class TodoDao extends BaseDao {

	/**
	 * 获取用户所有的代办
	 * 
	 * @param userId
	 * @return
	 */
	public List<Todo> getTodoByUserId(int userId) {
		String hql = "from  Todo where userId = ? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, userId);
		return query.list();
	}

	/**
	 * 获取用户未完成的代办
	 * 
	 * @param userId
	 * @return
	 */
	public List<Todo> getUnCompleteTodoByUserId(int userId) {
		String hql = "from  Todo where userId = ? and complete=? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, userId);
		query.setBoolean(1, false);
		return query.list();
	}

	public Todo getTodoById(int todoId) {
		String hql = "from  Todo where id = ?  ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, todoId);
		return (Todo) query.uniqueResult();
	}

	public List<Todo> getAllTodoByToId(int userId, int fid) {
		String hql = "from  Todo where userId = ? and complete=? and id >? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, userId);
		query.setBoolean(1, false);
		query.setInteger(2, fid);
		return query.list();
	}

}
