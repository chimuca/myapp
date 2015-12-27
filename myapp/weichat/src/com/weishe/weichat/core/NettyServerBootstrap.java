package com.weishe.weichat.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.weishe.weichat.core.bean.Msg;
import com.weishe.weichat.core.nio.handler.ChatHandler;
import com.weishe.weichat.core.nio.handler.ClientLoginHandler;
import com.weishe.weichat.core.nio.handler.ClientRequesHandler;
import com.weishe.weichat.core.nio.handler.FileUploadHandler;
import com.weishe.weichat.core.nio.handler.MsgChatHandler;
import com.weishe.weichat.core.nio.handler.PingHandler;
import com.weishe.weichat.core.nio.handler.ServerLoginHandler;
import com.weishe.weichat.service.ChatServerService;
import com.weishe.weichat.util.PropertyUtils;

@Service
@Scope("singleton")
public class NettyServerBootstrap {
	@Autowired
	private ClientLoginHandler clientLoginHandler;
	@Autowired
	private ServerLoginHandler serverLoginHandler;
	@Autowired
	private PingHandler pingHandler;
	@Autowired
	private ChatHandler chatHandler;
	@Autowired
	private MsgChatHandler msgChatHandler;
	@Autowired
	private ClientRequesHandler clientRequesHandler;

	@Autowired
	private FileUploadHandler fileUploadHandler;

	/**
	 * 本机外网ip
	 */
	// @Value("#{propertyConfigurer['netty.server.ip']}")
	// @Value("${netty.server.ip}")
	private String ip;
	/**
	 * 本机服务端监听端口
	 */
	// @Value("#{configProperties['netty.server.port']}")
	private int port;

	/**
	 * 本机服务端监听端口
	 */
	// @Value("#{configProperties['netty.server.name']}")
	private String name;
	// private SocketChannel socketChannel;

	private ChatServerService chatServerService;

	@Autowired
	public NettyServerBootstrap(ChatServerService chatServerService)
			throws InterruptedException {
		this.chatServerService = chatServerService;
		this.ip = PropertyUtils.getValue("netty.server.ip");
		this.port = Integer.parseInt(PropertyUtils
				.getValue("netty.server.port"));
		this.name = PropertyUtils.getValue("netty.server.name");
		bind();
	}

	private void bind() throws InterruptedException {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(boss, worker);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.option(ChannelOption.SO_BACKLOG, 128);
		// 通过NoDelay禁用Nagle,使消息立即发出去，不用等待到一定的数据量才发出去
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		// 保持长连接状态
		bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel socketChannel)
					throws Exception {
				ChannelPipeline p = socketChannel.pipeline();

				socketChannel.pipeline().addLast(
						new IdleStateHandler(200, 100, 0));

				// decoded
				p.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
				p.addLast(new ProtobufDecoder(Msg.Message.getDefaultInstance()));
				// encoded
				p.addLast(new LengthFieldPrepender(4));
				p.addLast(new ProtobufEncoder());

				// p.addLast("frameDecoder", new
				// ProtobufVarint32FrameDecoder());
				// p.addLast("frameEncoder",
				// new ProtobufVarint32LengthFieldPrepender());
				// 添加服务器间的
				// p.addLast(chatServerHandler);
				p.addLast(msgChatHandler);
				p.addLast(clientLoginHandler);
				p.addLast(serverLoginHandler);
				p.addLast(pingHandler);
				p.addLast(chatHandler);
				p.addLast(clientRequesHandler);
				p.addLast(fileUploadHandler);
			}
		});
		ChannelFuture f = bootstrap.bind(port).sync();

		if (f.isSuccess()) {
			chatServerService.regist(ip, port, name);
			System.out.println("server start on " + name + "(" + ip + ":"
					+ port + ")");
		}
	}

}