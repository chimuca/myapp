package com.messake.messake.service;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.List;

import com.messake.messake.bean.Attachment;
import com.messake.messake.bean.Constants;
import com.messake.messake.bean.Todo;
import com.messake.messake.cache.CacheManager;
import com.messake.messake.core.ChatMessage;
import com.messake.messake.core.JsonMessage;
import com.messake.messake.core.Msg;
import com.messake.messake.core.Msg.ChatGroup;
import com.messake.messake.core.Msg.DiscussionGroup;
import com.messake.messake.core.Msg.Friends;
import com.messake.messake.core.Msg.FriendsGroup;
import com.messake.messake.core.Msg.Message;
import com.messake.messake.core.Msg.TodoMessage;
import com.messake.messake.core.Msg.User;
import com.messake.messake.core.MsgHelper;
import com.messake.messake.utils.AttachmentManager;
import com.messake.messake.utils.BroadcastHelper;
import com.messake.messake.utils.DBHelper;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

/**
 *
 * Created by messake on 2015/12/26.
 */
public class NettyClientHandler  extends SimpleChannelInboundHandler<Message> {
    private int pingTimes = 0;

    public NettyClientHandler(Session session) {
        this.session = session;
    }

    private Session session;

    /**
     * 当出现WRITER_IDLE、ALL_IDLE时则需要向客户端发出ping消息，试探远端是否还在
     */
    private void sendPing(ChannelHandlerContext ctx) {
        // Session session = SessionManager.get(ctx.channel());

        Message ping = MsgHelper.newPingMessage(Msg.MessageType.CLIENT_PING,
                session.getUser().getId() + session.getToken());
        ctx.channel().writeAndFlush(ping);
        Log.v("org.weiche.weichat", "pingTimes：" + pingTimes);
        Log.v("org.weiche.weichat", "session" + session);
        Log.v("org.weiche.weichat", "this" + this);

        if (pingTimes > 5 && !session.isReStarting()) {
            boolean r = session.reConnect();
            if (r) {
                pingTimes = 0;
            }
            Log.v("org.weiche.weichat", "session" + session);
            Log.v("org.weiche.weichat", "this" + this);
            Log.v("org.weiche.weichat", "重连结果：" + r);
        }

        pingTimes++;

    }

    // 利用写空闲发送心跳检测消息
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case WRITER_IDLE:
                    Log.v("com.messake.messake", "服务器客户端：WRITER_IDLE");
                    sendPing(ctx);
                    Log.v("com.messake.messake", "服务器客户端 send ping to server。");
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext,
                                   Message baseMsg) throws Exception {
        InetSocketAddress insocket = (InetSocketAddress) channelHandlerContext
                .channel().remoteAddress();
        String ip = insocket.getAddress().getHostAddress();
        int port = insocket.getPort();
        Log.v("com.messake.messake.msg", "receive msg from server port:" + port);
        Log.v("com.messake.messake.msg", "msgType:" + baseMsg.getMessageType());
        switch (baseMsg.getMessageType()) {
            case CHAT_MESSAGE:
                Log.v("com.messake.messake", "-------------聊天信息---------------");
                Log.v("com.messake.messake", "|from:"
                        + baseMsg.getChatMessage().getFromId());
                Log.v("com.messake.messake", "|to:"
                        + baseMsg.getChatMessage().getToId());
                Log.v("com.messake.messake", "|token:"
                        + baseMsg.getChatMessage().getToken());
                Log.v("com.messake.messake", "|content:"
                        + baseMsg.getChatMessage().getContent());
                Log.v("com.messake.messake", "|from:"
                        + baseMsg.getChatMessage().getFromId());
                Log.v("com.messake.messake", "---------------------------------");
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setContent(baseMsg.getChatMessage().getContent());

                chatMessage.setDate(new Date());
                chatMessage.setFromId(baseMsg.getChatMessage().getFromId());
                chatMessage.setToId(baseMsg.getChatMessage().getToId());
                chatMessage.setType(ChatMessage.TYPE_RECEIVE);
                chatMessage.setWhoId(baseMsg.getChatMessage().getToId());
                chatMessage.setChecked(false);
                chatMessage.setMsgType(baseMsg.getChatMessage().getMsgType());
                chatMessage.setChatGroupId(baseMsg.getChatMessage()
                        .getChatGroupId());
                chatMessage.setDiscussionGroupId(baseMsg.getChatMessage()
                        .getDiscussionGroupId());
                chatMessage.setContentType(baseMsg.getChatMessage()
                        .getContentType());
                chatMessage.setFileGroupName(baseMsg.getChatMessage()
                        .getFileGroupName());
                chatMessage.setFilePath(baseMsg.getChatMessage().getFilePath());
                chatMessage.setChatMessageId(baseMsg.getChatMessage()
                        .getChatMessageId());
                chatMessage.setUuid(baseMsg.getChatMessage().getUuid());
                chatMessage.setStatus(baseMsg.getChatMessage().getStatus());
                // 如果是带附件的消息
                if (baseMsg.getChatMessage().getContentType() == ChatMessage.CONTENT_TYPE_ATTACHMENT) {
                    String groupName = baseMsg.getChatMessage().getFileGroupName();
                    String path = baseMsg.getChatMessage().getFilePath();
                    Attachment a = DBHelper.getgetInstance(session).getAttachment(
                            groupName, path);
                    if (a != null) {
                        chatMessage.setAttachmentId(a.getId());
                    }

                }
                // 保存数据
                DBHelper.getgetInstance(session).addChatMessage(chatMessage,
                        session.getUser().getId());
                session.ring();
                // 广播事件
                Intent intent0 = new Intent();
                intent0.setAction(Constants.INTENT_ACTION_RECEIVE_CHAT_MESSAGE);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.INTENT_EXTRA_CHAT_MESSAGE,
                        chatMessage);
                intent0.putExtras(bundle);
                session.sendBroadcast(intent0);
                break;
            case FRIENDS_LIST:
                List<Friends> fl = baseMsg.getFriendsListMessageList();
                // 转发给FriendsListHandler处理
                if (fl == null || fl.size() < 1) {
                    CacheManager.saveObject(session, null,
                            com.messake.messake.bean.Friends.getCacheKey(session
                                    .getUser().getId()));
                    // 广播事件
                    Intent intent1 = new Intent();
                    intent1.setAction(Constants.INTENT_ACTION_RECEIVE_FRIEND_GROUP_LIST);
                    session.sendBroadcast(intent1);
                } else {
                    channelHandlerContext.fireChannelRead(fl);
                }
                break;
            case FRIENDS_GROUP_LIST:
                List<FriendsGroup> fg = baseMsg.getFriendsGroupListMessageList();
                // 转发给FriendsGroupListHandler处理
                if (fg == null || fg.size() < 1) {
                    CacheManager.saveObject(session, null,
                            com.messake.messake.bean.FriendsGroup
                                    .getCacheKey(session.getUser().getId()));
                    // 广播事件
                    Intent intent2 = new Intent();
                    intent2.setAction(Constants.INTENT_ACTION_RECEIVE_FRIEND_GROUP_LIST);
                    session.sendBroadcast(intent2);
                } else {
                    channelHandlerContext.fireChannelRead(fg);
                }
                break;
            case CHAT_MESSAGE_LIST:
                // 历史消息
                List<Msg.ChatMessage> cmg = baseMsg.getChatMessageListMessageList();
                if (cmg.size() < 1) {
                    BroadcastHelper.onReceiveChatMessageList(session);
                } else {
                    // 转发给JsonDataHandler处理
                    channelHandlerContext.fireChannelRead(cmg);
                }
                break;
            case JSON_DATA:
                JsonMessage jm = new JsonMessage();
                jm.setJsonMessageType(baseMsg.getJsonMessage().getJsonMessageType());
                jm.setJsonStr(baseMsg.getJsonMessage().getJsonStr());
                jm.setUserId(baseMsg.getJsonMessage().getUserId());

                // 转发给JsonDataHandler处理
                channelHandlerContext.fireChannelRead(jm);
                break;
            case TODO:
                Todo todo = new Todo();
                Msg.TodoMessage tm = baseMsg.getTodoMessage();

                todo.setChecked(false);
                todo.setComplete(false);
                todo.setCreateDate(new Date());
                todo.setRequestMsg(tm.getRequestMsg());
                todo.setTodoId(tm.getTodoId());
                todo.setType(tm.getType());
                todo.setTodoSubject(tm.getTodoSubject());
                todo.setWhoId(session.getUser().getId());
                Todo old = DBHelper.getgetInstance(session.getApplicationContext())
                        .getTodoByTodoId(todo.getTodoId(),
                                session.getUser().getId());
                if (old == null || old.getId() <= 0) {
                    DBHelper.getgetInstance(session.getApplicationContext())
                            .addTodo(todo);
                    // list.add(todo);
                    // 发出通知
                    NotificationHelper.getInstance(session).showTodoNotify(todo,
                            session.getUser().getId(), session.getToken());
                    session.ring();
                }
                break;
            case TODO_MESSAGE_LIST:
                List<TodoMessage> tds = baseMsg.getTodoListMessageList();
                // 转发给FriendsGroupListHandler处理
                channelHandlerContext.fireChannelRead(tds);
                break;
            case CHAT_GROUP_LIST:
                List<ChatGroup> tcs = baseMsg.getChatGroupListMessageList();
                // 转发给FriendsGroupListHandler处理
                channelHandlerContext.fireChannelRead(tcs);
                break;
            case DISCUSSION_GROUP_LIST:
                List<DiscussionGroup> tdl = baseMsg
                        .getDiscussionGroupListMessageList();
                // 转发给FriendsGroupListHandler处理
                channelHandlerContext.fireChannelRead(tdl);
                break;
            case SERVER_PONG:
                pingTimes = 0;
                break;
            case CLIENT_PONG:
                pingTimes = 0;
                break;
            case AUTH_ERROR:
                session.auth();
                break;
            case FIEL:
                //
                Attachment a = null;
                Msg.FileDownload fd = baseMsg.getFileDownload();
                if (fd != null) {
                    a = new Attachment();
                    a.setAttachmentId(fd.getFileId());
                    a.setCreateDate(new Date());
                    a.setGroupName(fd.getGroupName());
                    a.setPath(fd.getPath());
                    a.setName(fd.getName());
                    a.setSize(fd.getSize());
                    a.setType(fd.getType());
                }

                // 如果是语音消息直接 下载附件
                if (a.getType() == Attachment.TYPE_VOICE) {
                    String path0 = AttachmentManager.getFilePath(a);
                    if (path0 == null || path0.isEmpty()) {
                        new AsyncTask<Attachment, Object, String>() {

                            @Override
                            protected void onPostExecute(String result) {
                                BroadcastHelper.onVoiceMsgDownload(session);
                            }

                            @Override
                            protected String doInBackground(Attachment... params) {
                                Attachment b = params[0];
                                String path = "";
                                if (b != null) {
                                    // 防止异常
                                    b.setType(Attachment.TYPE_VOICE);
                                    path = AttachmentManager.getFile(b);
                                }
                                return path;
                            }
                        }.execute(a);
                    }
                }
                channelHandlerContext.fireChannelRead(a);
                break;
            case USER_LIST:
                List<User> ul = baseMsg.getUserListMessageList();
                // 转发给FriendsGroupListHandler处理
                channelHandlerContext.fireChannelRead(ul);
                break;
            case RECEIPT:
                // 消息回执
                Msg.ReceiptMessage rm = baseMsg.getReceiptMessage();
                // 更新本地消息状态
                DBHelper.getgetInstance(session).updateChatMessageStatus(
                        rm.getUuid(), rm.getStatus());
                // 通知UI组件更新界面
                BroadcastHelper.onReceiveReceiptMessage(session);
                break;
            default:
                break;
        }

        ReferenceCountUtil.release(baseMsg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        Log.v("com.messake.messake", "发生异常了");

        if (!session.isReStarting()) {
            boolean r = session.reConnect();
            Log.v("org.weiche.weichat", "2.异常重连结果：" + r);
        }

        // super.exceptionCaught(ctx, cause);
    }
}