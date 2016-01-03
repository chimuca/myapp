# myapp
本项目来源oschina，http://git.oschina.net/735859399/weichat
包含server和android client

##迁移说明
由原来的eclipse环境分别迁移到了myeclipse和android studio
并合并了原来分开的开源项目swip和mobeta

##服务端
CentOS7+Wildfly-9.0.2.Final+Memcached+FastDFS+MariaDB
把项目中的weichatServer导出为war，放入wildfly（jboss）中的standalone/deployments就可以直接部署。

##优点
###利用Netty实现长连接。
     Netty 是一个基于NIO的客户，服务器端编程框架，使用Netty 可以确保你快速和简单的开发出一个网络  应     用，例如实现了某种协议的客户，服务端应用。Netty相当简化和流线化了网络应用的编程开发过程，例如，TCP和UDP的socket服务开发。
###利用protobuf实现数据传输
   一条消息数据，用protobuf序列化后的大小是json的10分之一，xml格式的20分之一，是二进制序列化的10分之一，总体看来ProtoBuf的优势还是很明显的。
   1.灵活（方便接口更新）、高效（效率经过google的优化，传输效率比普通的XML等高很多）
   2.易于使用；开发人员通过按照一定的语法定义结构化的消息格式，然后送给命令行工具，工具将自动生   成相关的类，可以支持java、c++、python等语言环境。通过将这些类包含在项目中，可以很轻松的调用相关方法来完成业务消息的序列化与反序列化工作。   3.语言支持；原生支持c++,java,python


