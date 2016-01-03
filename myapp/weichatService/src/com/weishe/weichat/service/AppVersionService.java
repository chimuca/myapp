package com.weishe.weichat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weishe.weichat.bean.AppVersion;
import com.weishe.weichat.dao.AppVersionDao;

@Service
public class AppVersionService {

	@Autowired
	private AppVersionDao appVersionDao;

	public AppVersion getNewestVersion() {
		return appVersionDao.getNewestAppVersion();
	}
}
