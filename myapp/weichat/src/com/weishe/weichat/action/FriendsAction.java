package com.weishe.weichat.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.weishe.weichat.bean.Friends;
import com.weishe.weichat.bean.FriendsGroup;
import com.weishe.weichat.bean.Result;
import com.weishe.weichat.bean.User;
import com.weishe.weichat.bean.UserAuthToken;
import com.weishe.weichat.service.FriendsGroupService;
import com.weishe.weichat.service.FriendsService;
import com.weishe.weichat.service.UserAuthTokenService;
import com.weishe.weichat.service.UserService;

@Controller
@RequestMapping(value = "friends")
public class FriendsAction {

	@Autowired
	private FriendsGroupService friendsGroupService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserAuthTokenService userAuthTokenService;
	@Autowired
	private FriendsService friendsService;

	@ResponseBody
	@RequestMapping(value = "/moveFriendsGroup.json")
	public Result moveFriendsGroup(HttpServletRequest request,
			@RequestParam(value = "userId", defaultValue = "") int userId,
			@RequestParam(value = "token", defaultValue = "") String token,
			@RequestParam(value = "groupId", defaultValue = "") int groupId,
			@RequestParam(value = "friendsId", defaultValue = "") int friendsId) {
		String message = "";
		boolean success = true;

		FriendsGroup fg = friendsGroupService.getFriendsGroupById(groupId);
		if (groupId != 0 && (fg == null || fg.getId() < 1)) {
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
		Friends f = friendsService.getFriendsByUserIdAndFriendsId(userId,
				friendsId);
		if (f != null) {
			if (groupId != 0) {
				friendsService.moveFriendsGroup(f, fg);
			} else {
				friendsService.moveFriendsGroup(f, null);
			}
		} else {
			message = message + "好友不存在！";
			success = false;
		}
		return new Result(success, message);
	}
}
