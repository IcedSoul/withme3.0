package cn.icedsoul.websocketserverservice;

import cn.icedsoul.websocketserverservice.init.WebSocketServerInitializer;
import cn.icedsoul.websocketserverservice.constant.CONSTANT;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

/**
 * Created by 14437 on 2018/2/8.
 */
@Log
public class WebSocketServer {

    public static void main(String[] args) throws CertificateException, SSLException {
        new WebSocketServer().run();
    }

//    public void initNetty() {
//        new Thread() {
//            public void run() {
//                try {
//                    new WebSocketServer().run();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }

    public void run() {

        log.info("---------------Netty Server Start------------------");
        //在服务端每个监听的Socket都有一个Boss线程来处理，在客户端只有一个Boss线程来处理所有的Socket。
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //Worker线程：Worker线程执行所有的异步I/O
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //ServerBootstrap启动NIO服务的辅助启动类，负责初始化netty服务器，并且开始监听端口的socket请求
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(bossGroup, workerGroup);
            //设置非阻塞，用它来建立新的accept的连接，用于构造ServerSocketChannel的工厂类
            sb.channel(NioServerSocketChannel.class);
            //对出入的数据进行的业务操作，其继承ChannelInitializer
            sb.childHandler(new WebSocketServerInitializer());
            log.info("-----------------Netty Server is waiting for connect------------");
            log.info("Netty port: " + CONSTANT.NETTY_PORT);
            Channel channel = sb.bind(CONSTANT.NETTY_PORT).sync().channel();
            channel.closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
