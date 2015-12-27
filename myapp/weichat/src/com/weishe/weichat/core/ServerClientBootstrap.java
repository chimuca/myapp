package com.weishe.weichat.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weishe.weichat.bean.ChatServer;
import com.weishe.weichat.core.bean.Msg;
import com.weishe.weichat.core.bean.Msg.ServerLoginMessage;
import com.weishe.weichat.core.nio.handler.ServerClientHandler;
import com.weishe.weichat.service.ChatServerService;
import com.weishe.weichat.util.PropertyUtils;

@Service
public class ServerClientBootstrap {
	// 本地日志记录对象
	@Autowired
	private ServerClientHandler serverClientHandler;
	private static final Logger LOGGER = Logger
			.getLogger(ServerClientBootstrap.class);
	private SocketChannel socketChannel;
	private static final EventExecutorGroup group = new DefaultEventExecutorGroup(
			20);

	private ChatServerService chatServerService;

	private String ip;
	private int port;

	@Autowired
	public ServerClientBootstrap(ChatServerService chatServerService)
			throws InterruptedException {
		this.chatServerService = chatServerService;
		this.ip = PropertyUtils.getValue("netty.server.ip");
		this.port = Integer.parseInt(PropertyUtils
				.getValue("netty.server.port"));
		start();
	}

	private void start() throws InterruptedException {
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.group(eventLoopGroup);
		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel socketChannel)
					throws Exception {
				socketChannel.pipeline().addLast(
						new IdleStateHandler(50, 50, 0));
				// socketChannel.pipeline().addLast("frameDecoder",
				// new ProtobufVarint32FrameDecoder());
				// socketChannel.pipeline().addLast("frameEncoder",
				// new ProtobufVarint32LengthFieldPrepender());
				// decoded
				socketChannel.pipeline().addLast(
						new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
				socketChannel.pipeline().addLast(
						new ProtobufDecoder(Msg.Message.getDefaultInstance()));
				// encoded
				socketChannel.pipeline().addLast(new LengthFieldPrepender(4));
				socketChannel.pipeline().addLast(new ProtobufEncoder());

				socketChannel.pipeline().addLast(
						new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
				socketChannel.pipeline().addLast(serverClientHandler);
			}
		});

		List<ChatServer> servers = chatServerService.getOnlineServer();
		for (ChatServer server : servers) {
			if (this.ip.equals(server.getIp()) && (port == server.getPort())) {
				// 本机不用链接
				continue;
			}
			ChannelFuture future = bootstrap.connect(server.getIp(),
					server.getPort()).sync();
			if (future.isSuccess()) {
				socketChannel = (SocketChannel) future.channel();
				// MessageBack loginMsg = new MessageBack(MsgType.SERVER_LOGIN);
				Msg.ServerLoginMessage.Builder builder = Msg.ServerLoginMessage
						.newBuilder();
				ServerLoginMessage loginMsg = builder
						.setIp(SessionManager.getIp())
						.setPort(SessionManager.getPort()).build();

				socketChannel.writeAndFlush(loginMsg);
				LOGGER.info("链接服务器 " + server.getIp() + ":" + server.getPort()
						+ "  成功。");
				Session session = new Session(socketChannel);
				String clientId = server.getIp() + ":" + server.getPort();
				SessionManager.add(clientId, session);
			}
		}

	}
}
