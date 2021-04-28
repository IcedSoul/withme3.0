package cn.icedsoul.websocketserverservice.init;

import cn.icedsoul.websocketserverservice.handler.WebSocketServerHandler;
import cn.icedsoul.websocketserverservice.util.ScriptReaderUtil;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

import static cn.icedsoul.websocketserverservice.constant.CONSTANT.*;
import static cn.icedsoul.websocketserverservice.constant.Global.gameMap;


/**
 * 接收/处理/响应Websocket请求的核心业务处理类
 * Created by 14437 on 2018/2/8.
 */
@Slf4j
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel e) throws Exception {
        //将请求和应答消息解码为http消息
        e.pipeline().addLast("http-codec", new HttpServerCodec());
        //HttpObjectAggregator:将Http消息的多个部分合成一条完整的消息
        e.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
        //ChunkedWriteHandler:向客户端发送HTML5文件
        e.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
        //在管道中添加我们自己实现的接收数据实现方法
        e.pipeline().addLast("handler", new WebSocketServerHandler());
        initGameMap();
    }

    /**
     * 解析四个游戏选择文件，放入内存。
     */
    private void initGameMap(){
        try {
            log.info("当前路径：{}", System.getProperty("user.dir"));
            gameMap.putAll(ScriptReaderUtil.parseScript(CHOICE_SCRIPT, ROLE_CHOICE));
            gameMap.putAll(ScriptReaderUtil.parseScript(CAT_TEST_SCRIPT, ROLE_CAT));
            gameMap.putAll(ScriptReaderUtil.parseScript(CEO_TEST_SCRIPT, ROLE_CEO));
            gameMap.putAll(ScriptReaderUtil.parseScript(NEW_GRAD_TEST_SCRIPT, ROLE_NEW_GRAD));
            gameMap.putAll(ScriptReaderUtil.parseScript(AUNT_TEST_SCRIPT, ROLE_AUNT));
        } catch (Exception e){
            log.info("解析文件出现出现异常", e);
        }

    }
}
