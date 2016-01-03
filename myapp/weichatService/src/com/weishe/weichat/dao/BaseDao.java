package com.weishe.weichat.dao;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.HibernateTemplate;

public class BaseDao extends HibernateTemplate {
	@Override
	@Resource
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	public void insert(Object entity) {
		this.save(entity);
	}

	public void update(Object entity) {
		super.update(entity);
	}

}
