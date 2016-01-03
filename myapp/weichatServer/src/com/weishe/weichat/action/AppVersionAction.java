package com.weishe.weichat.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.weishe.weichat.bean.AppVersion;
import com.weishe.weichat.service.AppVersionService;

@Controller
@RequestMapping(value = "appVersion")
public class AppVersionAction {

	@Autowired
	private AppVersionService appVersionService;

	/**
	 * 
	 * @param request
	 * @param condition
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getNewestVersion.json")
	public AppVersion getNewestVersion(HttpServletRequest request) {
		return appVersionService.getNewestVersion();
	}

}
