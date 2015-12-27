
整个项目使用到的技术如下

Spring， hibernate，memcache，Netty，protobuf，FastDFS，GreenDao。

核心通信部分是netty 和protobuf

未来将加入webrtc 以实现在线通话、视频聊天

项目分为服务端和客户端，代码都在这里。代码量有点儿大，相当一个在线应用的70%。对于一般的程序员有很好的启发作用。

如想运行需要将服务端程序运行起来。数据库建表语句都是自动的，建好数据库就行了。

memcache， fastDFS服务器也需要自己搭建。（如果这也觉得难那么代码运行起来你也看不出他的价值）

核心通信部分是netty 和protobuf

未来将加入webrtc 以实现在线通话、视频聊天

项目分为服务端和客户端，代码都在这里。代码量有点儿大，相当一个在线应用的70%。对于一般的程序员有很好的启发作用。

如想运行需要将服务端程序运行起来。数据库建表语句都是自动的，建好数据库就行了。

memcache， fastDFS服务器也需要自己搭建。（如果这也觉得难那么代码运行起来你也看不出他的价值）

阅读入口：weichatApp/org.weishe.weichat.service.Session

weichat/com.weishe.weichat.core.NettyServerBootstrap
1.请了解整个聊天系统的设计思路，请阅读 doc/云推送介绍和架构分享.ppt 在这个之中我有一个地方没说清楚的就是服务端的的消息转发，有时间我会补上的。

2.整个项目用Eclipse开发，将源码下载下来之后导入即可

3.导入项目之后请修改 weichat/config db-config.properties文件中的数据库配置 
/#connection.url=jdbc:mysql://XXXXXXXXXX:3306/WeiChat?autoReconnect=true&autoReconnectForPools=true&useUnicode=true&characterEncoding=utf8 
/#connection.username= 
/#connection.password=

4.修改weichatApp/org.weishe.weichat.api.ApiHttpClient中的服务端连接地址 
public final static String HOST = ""; 
private static String API_URL = "http:// /weichat/%s";

5.修改weichatApp/org.weishe.weichat.service.Session 129行 服务端对应地址

6.修改FastDFS系统地址
 weichatapp/org.weishe.weichat.util.FastDFSUtil 第60行
 weichat/com.weishe.weichat.util.FastDFSUtil 第69行


7.修改memcache 地址
weichat/config/applicationContext.xml 240行


作者：
http://git.oschina.net/735859399/weichat
735859399@qq.com



