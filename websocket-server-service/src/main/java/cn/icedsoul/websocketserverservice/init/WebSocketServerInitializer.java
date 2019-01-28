package cn.icedsoul.websocketserverservice.init;

import cn.icedsoul.websocketserverservice.handler.WebSocketServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;


/**
 * 接收/处理/响应Websocket请求的核心业务处理类
 * Created by 14437 on 2018/2/8.
 */
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel e) throws Exception {

        //protobuf处理器
//        e.pipeline().addLast("frameDecoder", new ProtobufVarint32FrameDecoder());//用于Decode前解决半包和粘包问题
        //此处需要传入自定义的protobuf的defaultInstance
        // e.pipeline().addLast("protobufDecoder",new ProtobufDecoder();
        //将请求和应答消息解码为http消息
        e.pipeline().addLast("http-codec", new HttpServerCodec());
        //HttpObjectAggregator:将Http消息的多个部分合成一条完整的消息
        e.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
        //ChunkedWriteHandler:向客户端发送HTML5文件
        e.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
        //在管道中添加我们自己实现的接收数据实现方法
        e.pipeline().addLast("handler", new WebSocketServerHandler());

    }
}
