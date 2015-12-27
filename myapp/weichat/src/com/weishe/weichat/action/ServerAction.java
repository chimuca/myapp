package com.weishe.weichat.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "server")
public class ServerAction {

	@RequestMapping(value = "getChatServer")
	public String web500(HttpServletRequest request, ModelMap modelMap) {

		return "10.1.11.33";
	}
}
