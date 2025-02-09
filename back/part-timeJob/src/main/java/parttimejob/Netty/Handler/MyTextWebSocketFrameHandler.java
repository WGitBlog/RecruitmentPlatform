package parttimejob.Netty.Handler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import parttimejob.Entity.Messages;
import parttimejob.service.MessagesService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ChannelHandler.Sharable
public class MyTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    //TODO:messagesService只能用构造方法注入
    private MessagesService messagesService;
    @Autowired
    public MyTextWebSocketFrameHandler(MessagesService messagesService) {
        this.messagesService = messagesService;
    }


    //记录客户端和channel的绑定
    private static Map<Long, Channel> channelMap = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext chc, TextWebSocketFrame wsf) throws Exception {
        System.out.println("正在走自己的channel逻辑");

        // 将发过来的内容进行解析成 自定义的Message
        Messages message = JSON.parseObject(wsf.text(), Messages.class);

        // 处理注册消息
        if (message.getMsgType().equals(5)) {
            channelMap.put(message.getSenderId(), chc.channel());
            System.out.println("channelMap:"+channelMap);
        } else {
            // 处理正常用户消息----向对应的接收者channel发送信息
            if (!channelMap.containsKey(message.getRecipientId())) {
                // 该用户暂未在线，先将消息存进数据库
                System.out.println("该用户暂未在线，先将消息存进数据库");
                System.out.println(message);
                boolean b = messagesService.save(message);
                System.out.println(b);
            } else {
                // 该用户暂在线，将消息发送
                Channel channel = channelMap.get(message.getRecipientId());
                try {
                    channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
                    System.out.println("该用户在线，将消息发送");
                    messagesService.save(message);
                } catch (Exception e) {
                    System.err.println("Error sending message: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // 出来管理员接收到的消息---举报和工作消息接收
//            if (channelMap.containsKey(60)) {
//                // 该管理员暂未在线，不做任何操作
//                System.out.println("管理员暂未在线，不做任何操作");
//            } else {
//                // 该管理员在线，将消息发送
//                Channel channel = channelMap.get(60);
//                try {
//                    channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
//                    System.out.println("该用户在线，将消息发送");
//                } catch (Exception e) {
//                    System.err.println("Error sending message: " + e.getMessage());
//                    e.printStackTrace();
//                }
//            }





        }









        // 计数-1（计数法来控制回收内存）
        chc.fireChannelRead(wsf.retain());
    }
    //当NettyClient断开连接清除channelMap通道中相应的数据
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //获取关闭的channel通道
        Channel closedChannel = ctx.channel();
        //根据Channel查询出对应的key
        Long longByChannel = getLongByChannel(closedChannel);
        System.out.println("longByChannel"+longByChannel);
        //将对应的key从channelMap删除
        try {
            Channel channel = channelMap.remove(longByChannel);
            System.out.println(longByChannel+":"+channel+"已经被删除");
            System.out.println("channelMap:"+channelMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //根据Channel查询出对应的key
    public Long getLongByChannel(Channel closedChannel){
        //遍历map中的数据
        for (Map.Entry<Long, Channel> entry : channelMap.entrySet()) {
            //判断value是否相等
            if (entry.getValue().equals(closedChannel)){
                //相等返回key
                System.out.println("查到数据key："+entry.getKey());
                return entry.getKey();
            }
            //不相等跳过
        }
    //没有找到返回null
    return null;
    }
}