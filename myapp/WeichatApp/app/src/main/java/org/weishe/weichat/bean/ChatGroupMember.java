package org.weishe.weichat.bean;

public class ChatGroupMember {
	private int id;

	private User user;

	private ChatGroup chatGroup;

	private String remarkName;

	private boolean shield;

	public boolean isShield() {
		return shield;
	}

	public void setShield(boolean shield) {
		this.shield = shield;
	}

	public User getUser() {
		return user;
	}

	public ChatGroup getChatGroup() {
		return chatGroup;
	}

	public String getRemarkName() {
		return remarkName;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setChatGroup(ChatGroup chatGroup) {
		this.chatGroup = chatGroup;
	}

	public void setRemarkName(String remarkName) {
		this.remarkName = remarkName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
