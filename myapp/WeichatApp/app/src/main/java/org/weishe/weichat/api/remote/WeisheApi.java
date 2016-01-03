package org.weishe.weichat.api.remote;

import java.util.List;

import org.weishe.weichat.api.ApiHttpClient;
import org.weishe.weichat.bean.Attachment;
import org.weishe.weichat.bean.Friends;
import org.weishe.weichat.bean.User;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class WeisheApi {

	/**
	 * 用户注册
	 * 
	 * @param handler
	 * @param user
	 */
	public static void register(AsyncHttpResponseHandler handler, User user) {
		RequestParams params = new RequestParams();

		params.put("name", user.getName());
		params.put("account", user.getAccount());
		params.put("password", user.getPassword());
		params.put("birthday", user.getBirthday().getTime());
		params.put("gender", user.getGender());
		params.put("signature", user.getSignature());
		params.put("avatarId", user.getAvatar());
		ApiHttpClient.post("user/register.json", params, handler);
	}

	/**
	 * 用户登录
	 * 
	 * @param handler
	 * @param user
	 */
	public static void login(AsyncHttpResponseHandler handler, User user,
			String clientId, String clientType) {
		RequestParams params = new RequestParams();

		params.put("account", user.getAccount());
		params.put("password", user.getPassword());
		params.put("clientId", clientId);
		params.put("clientType", clientType);
		ApiHttpClient.get("user/login.json", params, handler);
	}

	/**
	 * 用户快速登录
	 * 
	 * @param handler
	 * @param user
	 */
	public static void quickLogin(AsyncHttpResponseHandler handler, User user,
			String clientId, String token) {
		RequestParams params = new RequestParams();

		params.put("account", user.getAccount());
		params.put("clientId", clientId);
		params.put("token", token);
		ApiHttpClient.get("user/quickLogin.json", params, handler);
	}

	/**
	 * 检查用户是否存在
	 * 
	 * @param handler
	 * @param user
	 */
	public static void isAccountExist(AsyncHttpResponseHandler handler,
			String account) {
		RequestParams params = new RequestParams();
		params.put("account", account);
		ApiHttpClient.get("user/checkAccount.json", params, handler);
	}

	public static void searchFriends(AsyncHttpResponseHandler handler,
			String condition) {
		RequestParams params = new RequestParams();
		params.put("condition", condition);
		ApiHttpClient.get("user/search.json", params, handler);
	}

	public static void getUser(AsyncHttpResponseHandler handler, int userId) {
		RequestParams params = new RequestParams();
		params.put("userId", userId + "");
		ApiHttpClient.get("user/getUser.json", params, handler);
	}

	public static void addFriends(AsyncHttpResponseHandler handler, int userId,
			String token, int friendsId) {
		RequestParams params = new RequestParams();
		params.put("userId", userId + "");
		params.put("token", token + "");
		params.put("friendsId", friendsId + "");
		ApiHttpClient.get("user/addFriends.json", params, handler);
	}

	public static void dealWithTodo(AsyncHttpResponseHandler handler,
			int userId, String token, int todoId, boolean argeeOrNot) {
		RequestParams params = new RequestParams();
		params.put("userId", userId + "");
		params.put("token", token + "");
		params.put("todoId", todoId + "");
		params.put("argeeOrNot", argeeOrNot + "");
		ApiHttpClient.get("user/dealWithTodo.json", params, handler);
	}

	/**
	 * 添加好友分组
	 * 
	 * @param handler
	 * @param userId
	 * @param token
	 * @param groupName
	 */
	public static void addFriendsGroup(AsyncHttpResponseHandler handler,
			int userId, String token, String groupName) {
		RequestParams params = new RequestParams();
		params.put("userId", userId + "");
		params.put("token", token + "");
		params.put("groupName", groupName + "");
		ApiHttpClient.get("friendsGroup/addFriendsGroup.json", params, handler);
	}

	/**
	 * 调整好友分组
	 * 
	 * @param handler
	 * @param userId
	 * @param token
	 * @param groupId
	 * @param toPosition
	 */
	public static void adjustPosition(AsyncHttpResponseHandler handler,
			int userId, String token, int groupId, int toPosition) {
		RequestParams params = new RequestParams();
		params.put("userId", userId + "");
		params.put("token", token + "");
		params.put("groupId", groupId + "");
		params.put("toPosition", toPosition + "");
		ApiHttpClient.get("friendsGroup/adjustPosition.json", params, handler);
	}

	/**
	 * 调整好友分组
	 * 
	 * @param handler
	 * @param userId
	 * @param token
	 * @param groupId
	 * @param toPosition
	 */
	public static void removeFriendsGroup(AsyncHttpResponseHandler handler,
			int userId, String token, int groupId) {
		RequestParams params = new RequestParams();
		params.put("userId", userId + "");
		params.put("token", token + "");
		params.put("groupId", groupId + "");
		ApiHttpClient.get("friendsGroup/removeFriendsGroup.json", params,
				handler);
	}

	public static void moveFriendsGroup(AsyncHttpResponseHandler handler,
			int userId, String token, int friendsId, int groupId) {
		RequestParams params = new RequestParams();
		params.put("userId", userId + "");
		params.put("token", token + "");
		params.put("friendsId", friendsId + "");
		params.put("groupId", groupId + "");
		ApiHttpClient.get("friends/moveFriendsGroup.json", params, handler);
	}

	/*
	 * public static void getChatGroup(AsyncHttpResponseHandler handler, int
	 * userId, String token) { RequestParams params = new RequestParams();
	 * params.put("userId", userId + ""); params.put("token", token + "");
	 * ApiHttpClient.get("chatGroup/getChatGroup.json", params, handler); }
	 */

	public static void createChatGroup(AsyncHttpResponseHandler handler,
			int userId, String token, String groupName, List<Friends> friends) {

		String users = "";

		if (friends != null && friends.size() > 0) {
			for (int i = 0; i < friends.size(); i++) {
				if (i == 0) {
					users = friends.get(i).getUserId() + "";
				} else {
					users = users + "#" + friends.get(i).getUserId();
				}
			}
		}
		RequestParams params = new RequestParams();
		params.put("userId", userId + "");
		params.put("token", token + "");
		params.put("name", groupName + "");
		params.put("users", users);
		ApiHttpClient.post("chatGroup/createChatGroup.json", params, handler);
	}

	public static void addAttachment(AsyncHttpResponseHandler handler,
			Attachment attachment) {
		RequestParams params = new RequestParams();
		String s = JSON.toJSONString(attachment);
		params.put("attachment", s + "");
		ApiHttpClient.get("attachment/addAttachment.json", params, handler);
	}

	public static void searchChatGroup(AsyncHttpResponseHandler handler,
			String condition) {
		RequestParams params = new RequestParams();
		params.put("condition", condition);
		ApiHttpClient.get("chatGroup/search.json", params, handler);
	}

	public static void getChatGroup(AsyncHttpResponseHandler handler,
			int groupId) {
		RequestParams params = new RequestParams();
		params.put("chatGroupId", groupId);
		ApiHttpClient.get("chatGroup/getChatGroup.json", params, handler);
	}

	public static void leaveChatGroup(AsyncHttpResponseHandler handler,
			int chatGroupId, String token, int userId) {
		RequestParams params = new RequestParams();
		params.put("chatGroupId", chatGroupId);
		params.put("token", token);
		params.put("userId", userId);
		ApiHttpClient.get("chatGroup/leaveChatGroup.json", params, handler);
	}

	public static void joinChatGroup(AsyncHttpResponseHandler handler,
			int chatGroupId, String token, int userId) {
		RequestParams params = new RequestParams();
		params.put("chatGroupId", chatGroupId);
		params.put("token", token);
		params.put("userId", userId);
		ApiHttpClient.get("chatGroup/joinChatGroup.json", params, handler);
	}

	public static void getChatGroupMember(AsyncHttpResponseHandler handler,
			int chatGroupId, String token, int userId) {
		RequestParams params = new RequestParams();
		params.put("chatGroupId", chatGroupId);
		params.put("token", token);
		params.put("userId", userId);
		ApiHttpClient.get("chatGroup/getChatGroupMember.json", params, handler);
	}

	public static void getNewestVersion(AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		ApiHttpClient.get("appVersion/getNewestVersion.json", params, handler);
	}

}
