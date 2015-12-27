package com.messake.messake.service;

import io.netty.handler.timeout.IdleStateHandler;

/**
 *
 * Created by messake on 2015/12/26.
 */
public class MyIdleStateHandler extends IdleStateHandler {

    public MyIdleStateHandler(int readerIdleTimeSeconds,
                              int writerIdleTimeSeconds, int allIdleTimeSeconds) {
        super(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds);

    }

}
