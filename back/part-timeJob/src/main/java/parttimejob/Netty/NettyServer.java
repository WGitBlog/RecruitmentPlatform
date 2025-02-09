package parttimejob.Netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import parttimejob.Netty.Handler.MyTextWebSocketFrameHandler;
import parttimejob.service.MessagesService;

import java.net.InetSocketAddress;

@Slf4j
@Component
@Configuration
public class NettyServer {
    @Autowired
    private MessagesService messagesService;
    public void start() {
        //创建   boos线程组 和 worker线程组
        NioEventLoopGroup BoosEventLoop = new NioEventLoopGroup();
        NioEventLoopGroup WorkerEventLoop = new NioEventLoopGroup();
        try {
            //1.创建Server 启动器
            ChannelFuture channelFuture = new ServerBootstrap()
                    //2.添加工作组
                    .group(BoosEventLoop, WorkerEventLoop)
                    //3.选择服务器的SocketChannel的实现
                    .channel(NioServerSocketChannel.class)
                    //4.责任链----boos负责处理链接-----worker负责处理读写---执行那些操作（handler）
                    .childHandler(
                            //5.代表数据读写的通道--对handler进行初始化---负责添加别的handler操作
                            new ChannelInitializer<NioSocketChannel>() {
                                @Override
                                protected void initChannel(NioSocketChannel ch) throws Exception {
                                    //因为基于HTTP协议所以使用HTTP的编解码器
                                    ch.pipeline().addLast(new HttpServerCodec());
                                    //是以块的形式写的，添加ChunkedWriteHandler处理器
                                    ch.pipeline().addLast(new ChunkedWriteHandler());
                                    /*说明
                                     * 1.http数据在传输时是分段的HttpObjectAggregator---可以聚合数据
                                     * 2.当浏览器发送大量数据时会发送多次http请求
                                     */
                                    ch.pipeline().addLast(new HttpObjectAggregator(8192));
                                    /*说明
                                     * 1.对于浏览器的websocket是以帧frame的形式传递的
                                     * 2.WebSocketFrame下面有六个子类来处理传来的数据
                                     * 3.浏览器请求时 ws://localhost:8090/?  ?--请求的uri
                                     * 4.WebSocketServerProtocolHandler核心功能将HTTP协议升级为ws（websocket）协议
                                     */
                                    ch.pipeline().addLast(new WebSocketServerProtocolHandler("/"));
                                    //添加自定义HTTP处理器
                                    ch.pipeline().addLast(new MyTextWebSocketFrameHandler(messagesService));





//                                    //6.添加具体的handler
////                                    ch.pipeline().addLast(new StringDecoder());//添加解码操作：因为都是二进制存储---将ByteBuf转为String
////                                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 0, 0, 0));//解决黏包半包现象----LTC解码器
//                                    ch.pipeline().addLast(new HttpServerCodec());//对HTTP请求数据的编解码
//                                    // 添加 WebSocket 处理器
//                                    ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws"));
//                                    // 添加 HTTP 请求处理器
//
//                                    ch.pipeline().addLast(new SimpleChannelInboundHandler<FullHttpRequest>() {
//                                        @Override
//                                        protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
//                                            // 处理 FullHttpRequest
//                                            System.out.println(fullHttpRequest);
//                                        }
//                                    });




//                                    ch.pipeline().addLast(new MyTextWebSocketFrameHandler());//自定义 进栈handler
////                                            (new ChannelInboundHandlerAdapter() {//自定义 进栈handler
////                                        @Override
////                                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
////                                            System.out.println(msg);
////                                        }
////                                    })
//
//                                    ch.pipeline().addLast(new SimpleChannelInboundHandler<HttpRequest>() {
//                                        @Override
//                                        protected void channelRead0(ChannelHandlerContext ctx, HttpRequest httpRequest) throws Exception {
//
//                                        }
//                                    });
                                }
                            })
                    .bind(new InetSocketAddress("localhost", 8090)).sync();//绑定端口

            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()){
                        System.out.println("监听端口8090成功");
                    }else{
                        System.out.println("监听端口8090失败");
                    }

                }
            });
            channelFuture.channel().closeFuture().sync();
            System.out.println("服务端已经启动");
        } catch (InterruptedException e) {
            log.error("server error", e);
        } finally {
            BoosEventLoop.shutdownGracefully();
            WorkerEventLoop.shutdownGracefully();
        }

    }

}


