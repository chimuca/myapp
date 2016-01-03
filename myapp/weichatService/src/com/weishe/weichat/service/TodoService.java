package com.weishe.weichat.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weishe.weichat.bean.Todo;
import com.weishe.weichat.dao.TodoDao;

@Service
public class TodoService {

	@Autowired
	private TodoDao todoDao;

	public List<Todo> getUnCompleteTodoByUserId(int userId) {
		return todoDao.getUnCompleteTodoByUserId(userId);
	}

	public Todo saveTodo(Todo todo) {
		Integer id = (Integer) todoDao.save(todo);
		todo.setId(id);
		return todo;
	}

	public void updateTodo(Todo todo) {
		todoDao.update(todo);
	}

	public Todo getTodoById(int todoId) {
		return todoDao.getTodoById(todoId);
	}

	/**
	 * 处理代办
	 * 
	 * @param todoId
	 * @param handleMsg
	 * @param agree
	 */
	public void finishTodo(int todoId, String handleMsg, boolean agree) {
		Todo t = getTodoById(todoId);
		if (t != null) {
			t.setComplete(true);
			t.setHandleDate(new Date());
			t.setHandleMsg(handleMsg);
			t.setAgree(agree);
			todoDao.update(t);
		}
	}

	public List<Todo> getAllTodoByToId(int userId, int fid) {
		return todoDao.getAllTodoByToId(userId, fid);
	}

}
