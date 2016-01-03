package org.weishe.weichat.aidl;

interface SessionService {
 /**
  *发送消息
  */
 void sendMessage(String uuid,int contentType,String message,int toId,int msgType,String fileGroupName,String filePath); 
  /**
  *发送文件消息
  */
 void sendAttachment(long id);
 /**
  *获取朋友列表
  */
 void getFriendList();
  /**
  *获取朋友分组列表
  */
 void getFriendGroupsList();
  /**
  *获取朋友群组列表
  */
 void getChatGroupList();
   /**
  *获取群组成员列表
  */
 void getChatGroupMemberList(int groupId);
   /**
  *获取所在讨论组列表
  */
 void getDiscussionGroupList();
   /**
  *获取讨论组成员列表
  */
 void getDiscussionGroupMemberList(int dGroupId);
 /**
  *获取用户聊天消息 
  */
 void getMessageList(int fromMessageId);
  /**
  *获取用户待办消息
  */
 void getTodoList(int fromMessageId);
 
 int getUserId();
 
 String getUserName();
 
 String getToken();
 
 /**
  *获取与我相关的用户
  **/
 void getRelateUser();
 
 
}
