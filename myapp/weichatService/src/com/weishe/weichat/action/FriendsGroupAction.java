package com.weishe.weichat.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.weishe.weichat.bean.FriendsGroup;
import com.weishe.weichat.bean.Result;
import com.weishe.weichat.bean.User;
import com.weishe.weichat.bean.UserAuthToken;
import com.weishe.weichat.service.FriendsGroupService;
import com.weishe.weichat.service.UserAuthTokenService;
import com.weishe.weichat.service.UserService;

@Controller
@RequestMapping(value = "friendsGroup")
public class FriendsGroupAction {

	@Autowired
	private FriendsGroupService friendsGroupService;
	@Autowired
	private UserService userService;

	@Autowired
	private UserAuthTokenService userAuthTokenService;

	@ResponseBody
	@RequestMapping(value = "/addFriendsGroup.json")
	public Result createChatGroup(
			HttpServletRequest request,
			@RequestParam(value = "userId", defaultValue = "") int userId,
			@RequestParam(value = "token", defaultValue = "") String token,
			@RequestParam(value = "groupName", defaultValue = "") String groupName) {
		String message = "";
		boolean success = true;
		if (groupName == null || groupName.isEmpty()) {
			message = message + "分组名不能为空！";
			success = false;
		}
		User user = userService.getUserById(userId);
		if (user == null || user.getId() < 1) {
			message = message + "用户不存在！";
			success = false;
		}
		UserAuthToken ut = userAuthTokenService
				.getUserAuthTokenByUserIdAndToken(userId, token);
		if (ut == null || ut.getId() < 1) {
			message = message + "非法用户！";
			success = false;
		}
		int position = friendsGroupService
				.getUserNextFriendsGroupPosition(userId);
		FriendsGroup fg = friendsGroupService.addFriendsGroup(user, groupName,
				position);

		if (fg == null || fg.getId() < 1) {
			message = message + "未知错误！";
			success = false;
		}
		List list = friendsGroupService.getFriendsGroupByUserId(userId);

		return new Result(success, message, list);
	}

	@ResponseBody
	@RequestMapping(value = "/adjustPosition.json")
	public Result adjustPosition(
			@RequestParam(value = "userId", defaultValue = "") int userId,
			@RequestParam(value = "token", defaultValue = "") String token,
			@RequestParam(value = "groupId", defaultValue = "") int groupId,
			@RequestParam(value = "toPosition", defaultValue = "") int toPosition) {

		String message = "";
		boolean success = true;
		if (toPosition < 1) {
			message = message + "位置参数有误！";
			success = false;
		}

		FriendsGroup fg = friendsGroupService.getFriendsGroupById(groupId);
		if (fg == null || fg.getId() < 1) {
			message = message + "分组不存在！";
			success = false;
		}
		User user = userService.getUserById(userId);
		if (user == null || user.getId() < 1) {
			message = message + "用户不存在！";
			success = false;
		}
		UserAuthToken ut = userAuthTokenService
				.getUserAuthTokenByUserIdAndToken(userId, token);
		if (ut == null || ut.getId() < 1) {
			message = message + "非法用户！";
			success = false;
		}

		friendsGroupService.adjustPosition(groupId, toPosition);

		List list = friendsGroupService.getFriendsGroupByUserId(userId);

		return new Result(success, message, list);
	}

	@ResponseBody
	@RequestMapping(value = "/removeFriendsGroup.json")
	public Result removeFriendsGroup(
			@RequestParam(value = "userId", defaultValue = "") int userId,
			@RequestParam(value = "token", defaultValue = "") String token,
			@RequestParam(value = "groupId", defaultValue = "") int groupId) {
		String message = "";
		boolean success = true;
		FriendsGroup fg = friendsGroupService.getFriendsGroupById(groupId);
		if (fg == null || fg.getId() < 1) {
			message = message + "分组不存在！";
			success = false;
		}
		User user = userService.getUserById(userId);
		if (user == null || user.getId() < 1) {
			message = message + "用户不存在！";
			success = false;
		}
		UserAuthToken ut = userAuthTokenService
				.getUserAuthTokenByUserIdAndToken(userId, token);
		if (ut == null || ut.getId() < 1) {
			message = message + "非法用户！";
			success = false;
		}
		friendsGroupService.removeFriendsGroup(groupId);
		List list = friendsGroupService.getFriendsGroupByUserId(userId);
		return new Result(success, message, list);
	}
}
