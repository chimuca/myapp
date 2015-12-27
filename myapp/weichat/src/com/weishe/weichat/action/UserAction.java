package com.weishe.weichat.action;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.weishe.weichat.bean.Attachment;
import com.weishe.weichat.bean.Friends;
import com.weishe.weichat.bean.FriendsGroup;
import com.weishe.weichat.bean.Result;
import com.weishe.weichat.bean.Todo;
import com.weishe.weichat.bean.User;
import com.weishe.weichat.bean.UserAuthToken;
import com.weishe.weichat.core.Session;
import com.weishe.weichat.core.SessionManager;
import com.weishe.weichat.core.bean.Msg.Message;
import com.weishe.weichat.core.bean.MsgHelper;
import com.weishe.weichat.service.AttachmentService;
import com.weishe.weichat.service.ChatGroupMemberService;
import com.weishe.weichat.service.FriendsGroupService;
import com.weishe.weichat.service.FriendsService;
import com.weishe.weichat.service.TodoService;
import com.weishe.weichat.service.UserAuthTokenService;
import com.weishe.weichat.service.UserOnlineServerService;
import com.weishe.weichat.service.UserService;
import com.weishe.weichat.util.AuthUtils;
import com.weishe.weichat.util.UUIDUtil;

@Controller
@RequestMapping(value = "user")
public class UserAction {
	@Autowired
	private UserService userService;
	@Autowired
	private UserAuthTokenService userAuthTokenService;
	@Autowired
	private TodoService todoService;
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private UserOnlineServerService userOnlineServerService;
	@Autowired
	private FriendsService friendsService;

	@Autowired
	private ChatGroupMemberService chatGroupMemberService;
	@Autowired
	private FriendsGroupService friendsGroupService;

	@Autowired
	private AttachmentService attachmentService;

	/**
	 * 用户注册
	 * 
	 * @param request
	 * @param name
	 * @param account
	 * @param password
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/register.json")
	public Result register(
			HttpServletRequest request,
			@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "account", defaultValue = "") String account,
			@RequestParam(value = "password", defaultValue = "") String password,
			@RequestParam(value = "birthday") long birthday,
			@RequestParam(value = "gender", defaultValue = "") int gender,
			@RequestParam(value = "signature", defaultValue = "") String signature,
			@RequestParam(value = "avatarId", defaultValue = "") int avatarId) {
		String message = "";
		boolean success = true;
		if (name == null || name.isEmpty()) {
			message = message + "昵称不能为空！";
			success = false;
		}

		if (password == null || password.isEmpty()) {
			message = message + "密码不能为空！";
			success = false;
		} else if (password.length() < 6) {
			message = message + "密码至少六位！";
			success = false;
		}

		if (account == null || account.isEmpty()) {
			message = message + "账户不能为空！";
			success = false;
		} else {
			User user = userService.getUserByAccount(account);
			if (user != null && user.getId() > 0) {
				message = message + "账户[" + account + "]已存在！";
				success = false;
			}
		}

		User u = null;
		Attachment a = null;
		if (success) {
			if (avatarId > 0) {
				a = attachmentService.getAttachmentById(avatarId);
			}
			u = userService.addUser(name, account, password,
					new Date(birthday), signature, gender, a);
		}
		Result result = new Result(success);
		result.setMessage(message);
		result.setObj(u);
		return result;
	}

	/**
	 * 用户登录
	 * 
	 * @param request
	 * @param name
	 * @param account
	 * @param password
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/login.json")
	public Result login(
			HttpServletRequest request,
			@RequestParam(value = "account", defaultValue = "") String account,
			@RequestParam(value = "password", defaultValue = "") String password,
			@RequestParam(value = "clientId", defaultValue = "") String clientId,
			@RequestParam(value = "clientType", defaultValue = "android") String clientType) {
		boolean success = false;
		String message = "";
		// 校验参数
		if (account == null || account.isEmpty()) {
			message = message + "账户不能为空！";
		}

		if (clientId == null || clientId.isEmpty()) {
			message = message + "客户端id不能为空！";
		}

		if (password == null || password.isEmpty()) {
			message = message + "用户密码不能为空！";
		}
		// 调用参数不合格
		if (!message.isEmpty()) {
			Result r0 = new Result(success, message);
			String str = JSON.toJSONString(r0);
			return r0;
		}

		User user = userService.getUserByAccount(account);
		if (user != null
				&& user.getPassword().equals(AuthUtils.getPassword(password))) {
			// 登录成功，产生token写入message字段，将user写入obj返回给客户端
			String token = UUIDUtil.uuid();
			message = token;
			userAuthTokenService.save(user, clientId, clientType, token);
			success = true;
		} else {
			message = "用户名或密码错误！";
		}
		Result r = new Result(success, message, user);
		// String str = JSON.toJSONString(r);
		return r;
	}

	@ResponseBody
	@RequestMapping(value = "/quickLogin.json")
	public Result quickLogin(
			HttpServletRequest request,
			@RequestParam(value = "account", defaultValue = "") String account,
			@RequestParam(value = "clientId", defaultValue = "") String clientId,
			@RequestParam(value = "token", defaultValue = "") String token) {
		boolean success = false;
		String message = "";
		User user = userService.getUserByAccount(account);
		if (user != null) {
			UserAuthToken ut = userAuthTokenService
					.getUserAuthTokenByUserIdAndToken(user.getId(), token);
			if (ut != null) {
				if (ut.getToken().equals(token)
						&& ut.getClientId().equals(ut.getClientId())) {
					success = true;
				}
			}
		}
		return new Result(success, message, user);
	}

	@ResponseBody
	@RequestMapping(value = "/checkAccount.json")
	public Result checkAccount(HttpServletRequest request,
			@RequestParam(value = "account", defaultValue = "") String account) {
		boolean success = false;
		String message = "";
		User user = userService.getUserByAccount(account);
		if (user != null && user.getId() > 0) {
			message = "用户存在！";
			success = true;
		} else {
			message = "用户不存在！";
		}
		return new Result(success, message);
	}

	@ResponseBody
	@RequestMapping(value = "/search.json")
	public List<User> search(
			HttpServletRequest request,
			@RequestParam(value = "condition", defaultValue = "") String condition) {
		List<User> list = userService.search(condition);
		if (list != null) {
			for (User u : list) {
				u.setPassword("");
			}
		}
		return list;
	}

	@ResponseBody
	@RequestMapping(value = "/getUser.json")
	public User getUser(HttpServletRequest request,
			@RequestParam(value = "userId", defaultValue = "") int userId) {
		User user = userService.getUserById(userId);
		return user;
	}

	@ResponseBody
	@RequestMapping(value = "/addFriends.json")
	public Result addFriends(
			HttpServletRequest request,
			@RequestParam(value = "userId", defaultValue = "") int userId,
			@RequestParam(value = "token", defaultValue = "") String token,
			@RequestParam(value = "friendsId", defaultValue = "") int friendsId,
			@RequestParam(value = "requestMsg", defaultValue = "") String requestMsg) {
		boolean success = false;
		String message = "";
		User user = userService.getUserById(userId);
		User friends = userService.getUserById(friendsId);
		UserAuthToken ut = userAuthTokenService
				.getUserAuthTokenByUserIdAndToken(userId, token);
		if (ut != null && ut.getId() > 0 && user != null && user.getId() > 0
				&& friends != null && friends.getId() > 0) {
			Todo todo = new Todo();
			todo.setUser(friends);
			todo.setComplete(false);
			todo.setCreateDate(new Date());
			todo.setFrom(user);
			todo.setRequestMsg(requestMsg);
			todo.setType(Todo.TODO_TYPE_ADD_FRIENDS);

			todo = todoService.saveTodo(todo);
			// 将消息发送出去
			List<UserAuthToken> uts = userAuthTokenService
					.getUserAuthTokenByUserId(friendsId);
			Session session = null;
			if (uts != null) {
				for (UserAuthToken u : uts) {
					session = SessionManager.get(friendsId + u.getToken());
					if (session != null) {
						Message msg = MsgHelper.newTodoMessage(todo.getId(),
								"用户[" + user.getName() + "]请求添加您为好友！",
								Todo.TODO_TYPE_ADD_FRIENDS, requestMsg);
						session.send(msg);
						// 成功发送即为完成
						todo.setComplete(true);
						todoService.updateTodo(todo);
					}
				}
			}

			success = true;
		} else {
			message = "非法请求";
		}

		return new Result(success, message);
	}

	@ResponseBody
	@RequestMapping(value = "/dealWithTodo.json")
	public Result dealWithTodo(
			HttpServletRequest request,
			@RequestParam(value = "userId", defaultValue = "") int userId,
			@RequestParam(value = "token", defaultValue = "") String token,
			@RequestParam(value = "todoId", defaultValue = "") int todoId,
			@RequestParam(value = "argeeOrNot", defaultValue = "") boolean argeeOrNot) {
		boolean success = false;
		String message = "";
		if (userAuth(userId, token)) {
			if (argeeOrNot) {
				Todo todo = todoService.getTodoById(todoId);
				if (todo != null ||todo.getHandleDate()!=null) {
					switch (todo.getType()) {
					// 加好友
					case Todo.TODO_TYPE_ADD_FRIENDS:
						// 添加好友
						friendsService.makeFriends(todo.getFrom(),
								todo.getUser());
						// 推送消息
						this.sendFriends(todo.getFrom());
						this.sendFriends(todo.getUser());
						// 更新代办状态
						todoService.finishTodo(todoId, "成功添加成好友。", argeeOrNot);
						message = "成功添加成好友。";
						break;
					// 加群
					case Todo.TODO_TYPE_JOIN_GROUP:
						chatGroupMemberService.addMember(todo.getGroup()
								.getId(), todo.getFrom());
						todoService.finishTodo(todoId, "已添加为群成员。", argeeOrNot);
						message = "已添加为群成员。";
						break;

					default:
						break;
					}
				}
				success = true;
			} else {
				todoService.finishTodo(todoId, "手机处理，已拒绝。", argeeOrNot);
				message = "手机处理，已拒绝。";
			}
		} else {
			success = false;
			message = "非法请求";
		}
		Todo todo = todoService.getTodoById(todoId);
		return new Result(success, message, todo);
	}

	/**
	 * 用户权限认证
	 * 
	 * @param userId
	 * @param token
	 * @return
	 */
	private boolean userAuth(int userId, String token) {
		User user = userService.getUserById(userId);
		UserAuthToken ut = userAuthTokenService
				.getUserAuthTokenByUserIdAndToken(userId, token);
		if (ut != null && ut.getId() > 0 && user != null && user.getId() > 0) {
			return true;
		} else {
			return false;
		}
	}

	private void sendFriends(User user) {
		// 获取用户的好友列表
		List<Friends> friends = friendsService.getFriendsByUserId(user.getId());
		// 设置好友的在线状态
		for (Friends u : friends) {
			u.setOnlineStatus(friendsService.getFriendsOnlineStatus(
					user.getId(), u.getFriend().getId()));
		}
		Message msg = MsgHelper.newFriendsListMessage(friends);
		// 获取好友分组列表
		List<FriendsGroup> fg = friendsGroupService
				.getFriendsGroupByUserId(user.getId());

		Message mfg = MsgHelper.newFriendsGroupListMessage(fg);

		Set<UserAuthToken> tokenSet = userOnlineServerService
				.getOnlineToken(user.getId());
		if (tokenSet != null) {
			for (UserAuthToken token : tokenSet) {
				String sessionKey = user.getId() + token.getToken();
				sessionManager.sendMessage(msg, sessionKey);
				sessionManager.sendMessage(mfg, sessionKey);
			}
		}

	}
}
