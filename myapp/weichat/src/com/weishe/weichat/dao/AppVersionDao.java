package com.weishe.weichat.dao;

import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.weishe.weichat.bean.AppVersion;

@Repository
public class AppVersionDao extends BaseDao {

	/**
	 * 按id获取版本
	 * 
	 * @param userId
	 * @return
	 */
	public AppVersion getAppVersionById(int id) {
		String hql = "from  AppVersion where id = ? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, id);
		return (AppVersion) query.uniqueResult();
	}

	/**
	 * 获取最新版本
	 * 
	 * @param id
	 * @return
	 */
	public AppVersion getNewestAppVersion() {
		String hql = "from  AppVersion where publishTime<? order by publishTime desc";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setDate(0, new Date());
		return (AppVersion) query.uniqueResult();
	}
}
