package cn.icedsoul.websocketserverservice.service.api;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author IcedSoul
 * @date 19-5-17 上午9:46
 */
public interface MessageService {
    /**
     * 建立WebSocket连接时回调方法
     * @param ctx
     * @param req
     */
    void onOpen(ChannelHandlerContext ctx, FullHttpRequest req);

    /**
     * 关闭WebSocket连接时回调方法
     */
    void onClose();

    /**
     * 收到消息时回调方法
     * @param ctx
     * @param jsonMessage
     */
    void onMessage(ChannelHandlerContext ctx, String jsonMessage);
}
