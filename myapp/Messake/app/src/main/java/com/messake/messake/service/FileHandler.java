package com.messake.messake.service;

import com.messake.messake.bean.Attachment;
import com.messake.messake.utils.DBHelper;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

/**
 *
 * Created by messake on 2015/12/26.
 */
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