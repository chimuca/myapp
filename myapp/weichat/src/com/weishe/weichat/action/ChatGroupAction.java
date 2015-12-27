package com.weishe.weichat.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.weishe.weichat.bean.ChatGroup;
import com.weishe.weichat.bean.ChatGroupMember;
import com.weishe.weichat.bean.Result;
import com.weishe.weichat.bean.Todo;
import com.weishe.weichat.bean.User;
import com.weishe.weichat.bean.UserAuthToken;
import com.weishe.weichat.core.Session;
import com.weishe.weichat.core.SessionManager;
import com.weishe.weichat.core.bean.Msg;
import com.weishe.weichat.core.bean.MsgHelper;
import com.weishe.weichat.core.bean.Msg.Message;
import com.weishe.weichat.service.ChatGroupMemberService;
import com.weishe.weichat.service.ChatGroupService;
import com.weishe.weichat.service.TodoService;
import com.weishe.weichat.service.UserAuthTokenService;
import com.weishe.weichat.service.UserService;
import com.weishe.weichat.util.StringUtils;

@Controller
@RequestMapping(value = "chatGroup")
public class ChatGroupAction {

	@Autowired
	private ChatGroupService chatGroupService;
	@Autowired
	private UserService userService;

	@Autowired
	private UserAuthTokenService userAuthTokenService;
	@Autowired
	private TodoService todoService;
	@Autowired
	private ChatGroupMemberService chatGroupMemberService;
	@Autowired
	private SessionManager sessionManager;

	/**
	 * 
	 * @param request
	 * @param condition
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/search.json")
	public List<ChatGroup> search(
			HttpServletRequest request,
			@RequestParam(value = "condition", defaultValue = "") String condition) {

		return chatGroupService.search(condition);
	}

	@ResponseBody
	@RequestMapping(value = "/createChatGroup.json")
	public Result createChatGroup(HttpServletRequest request,
			@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "userId", defaultValue = "") int userId,
			@RequestParam(value = "token", defaultValue = "") String token,
			@RequestParam(value = "users", defaultValue = "") String users) {
		String message = "";
		boolean success = true;
		if (name == null || name.isEmpty()) {
			message = message + "群组名不能为空！";
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
		String[] userIds = users.split("#");
		if (userIds != null && userIds.length > 0) {
			for (String id : userIds) {
				int uid = StringUtils.toInt(id);
				if (uid > 0) {
					// 发出添加群通知
					User friends = userService.getUserById(uid);
					if (friends != null) {
						Todo todo = new Todo();
						todo.setUser(friends);
						todo.setComplete(false);
						todo.setCreateDate(new Date());
						todo.setFrom(user);
						todo.setRequestMsg(user.getName() + " 邀请你加入群 " + name
								+ "!");
						todo.setType(Todo.TODO_TYPE_ADD_CHAT_GROUP);

						todo = todoService.saveTodo(todo);
						// 将消息发送出去
						List<UserAuthToken> uts = userAuthTokenService
								.getUserAuthTokenByUserId(uid);
						Session session = null;
						if (uts != null) {
							for (UserAuthToken u : uts) {
								session = SessionManager
										.get(uid + u.getToken());
								if (session != null) {
									Message msg = MsgHelper.newTodoMessage(
											todo.getId(), user.getName()
													+ " 邀请你加入群 " + name + "!",
											Todo.TODO_TYPE_ADD_CHAT_GROUP, "");
									session.send(msg);
									// 成功发送即为完成
									todo.setComplete(true);
									todoService.updateTodo(todo);
								}
							}
						}
					}

				}
			}
		}

		ChatGroup gp = chatGroupService.addChatGroup(name, user);
		chatGroupMemberService.addMember(gp.getId(), user);

		if (gp == null || gp.getId() < 1) {
			message = message + "未知错误！";
			success = false;
		}
		return new Result(success, message);
	}

	@ResponseBody
	@RequestMapping(value = "/getChatGroup.json")
	public ChatGroup getChatGroup(
			HttpServletRequest request,
			@RequestParam(value = "chatGroupId", defaultValue = "") int chatGroupId) {
		ChatGroup cg = chatGroupService.getChatGroupById(chatGroupId);
		return cg;
	}

	@ResponseBody
	@RequestMapping(value = "/leaveChatGroup.json")
	public Result leaveChatGroup(
			HttpServletRequest request,
			@RequestParam(value = "chatGroupId", defaultValue = "") int chatGroupId,
			@RequestParam(value = "userId", defaultValue = "") int userId,
			@RequestParam(value = "token", defaultValue = "") String token) {
		String message = "";
		boolean success = true;
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

		if (success) {
			chatGroupMemberService.deleteMemberByUserId(chatGroupId,
					user.getId());
		}
		return new Result(success, message);
	}

	@ResponseBody
	@RequestMapping(value = "/getChatGroupMember.json")
	public List<ChatGroupMember> getChatGroupMember(
			HttpServletRequest request,
			@RequestParam(value = "chatGroupId", defaultValue = "") int chatGroupId,
			@RequestParam(value = "userId", defaultValue = "") int userId,
			@RequestParam(value = "token", defaultValue = "") String token) {
		String message = "";
		boolean success = true;
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
		ChatGroup cg = chatGroupService.getChatGroupById(chatGroupId);
		if (cg == null || cg.getId() < 1) {
			message = message + "群不存在！";
			success = false;
		}

		List<ChatGroupMember> list = chatGroupMemberService
				.getMemberByChatGroupId(chatGroupId);
		return list;
	}

	@ResponseBody
	@RequestMapping(value = "/joinChatGroup.json")
	public Result joinChatGroup(
			HttpServletRequest request,
			@RequestParam(value = "chatGroupId", defaultValue = "") int chatGroupId,
			@RequestParam(value = "userId", defaultValue = "") int userId,
			@RequestParam(value = "token", defaultValue = "") String token) {
		String message = "";
		boolean success = true;
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
		ChatGroup cg = chatGroupService.getChatGroupById(chatGroupId);
		if (cg == null || cg.getId() < 1) {
			message = message + "群不存在！";
			success = false;
		}

		String requestMsg = "用户 " + user.getName() + " 请求加入群 " + cg.getName();
		List<User> m = new ArrayList<User>();
		if (cg.getCreateBy() != null) {
			m.add(cg.getCreateBy());
		}
		if (cg.getManager1() != null) {
			m.add(cg.getManager1());
		}
		if (cg.getManager2() != null) {
			m.add(cg.getManager2());
		}

		for (User u : m) {

			Todo todo = new Todo();
			todo.setUser(u);
			todo.setComplete(false);
			todo.setCreateDate(new Date());
			todo.setFrom(user);
			todo.setGroup(cg);
			todo.setRequestMsg(requestMsg);
			todo.setType(Todo.TODO_TYPE_JOIN_GROUP);

			todo = todoService.saveTodo(todo);
			Msg.Message msg = MsgHelper.newTodoMessage(todo);
			boolean b = sessionManager.sendMessage(userId, msg);

			if (b) {
				// 成功发送即为完成
				todo.setComplete(true);
				todoService.updateTodo(todo);
			}
		}
		return new Result(success, message);
	}
}
