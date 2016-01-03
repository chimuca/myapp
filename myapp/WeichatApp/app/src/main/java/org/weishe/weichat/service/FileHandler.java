package org.weishe.weichat.service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.ReferenceCountUtil;

import org.weishe.weichat.bean.Attachment;
import org.weishe.weichat.util.DBHelper;

@Sharable
public class FileHandler extends SimpleChannelInboundHandler<Attachment> {
	private Session session;

	public FileHandler(Session session) {
		this.session = session;
	}

	@Override
	protected void messageReceived(ChannelHandlerContext channelHandlerContext,
			Attachment file) throws Exception {
		// 将数据保存到本地
		Attachment a = DBHelper.getgetInstance(session).addAttachment(file);
		if (a != null) {
			DBHelper.getgetInstance(session).updateChatMessageAttachment(a);

		}

		ReferenceCountUtil.release(file);
	}
}