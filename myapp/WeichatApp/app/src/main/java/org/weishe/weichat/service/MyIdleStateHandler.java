package org.weishe.weichat.service;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.timeout.IdleStateHandler;

@Sharable
public class MyIdleStateHandler extends IdleStateHandler {

	public MyIdleStateHandler(int readerIdleTimeSeconds,
			int writerIdleTimeSeconds, int allIdleTimeSeconds) {
		super(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds);

	}

}
