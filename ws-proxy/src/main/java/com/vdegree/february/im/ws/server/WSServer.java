package com.vdegree.february.im.ws.server;

import com.vdegree.february.im.ws.pipeline.WSServerInitialzer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

/**
 * TODO
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/15 18:44
 */
@Component
@Log4j2
public class WSServer {

    private EventLoopGroup boss = new NioEventLoopGroup();
    private EventLoopGroup work = new NioEventLoopGroup();

    @Value("${app.ws.port:8088}")
    private int wsServerPort;


    @Autowired
    private WSServerInitialzer wsServerInitialzer;

    /**
     * 启动 cim server
     *
     * @return
     * @throws InterruptedException
     */
    @PostConstruct
    public void start() throws InterruptedException {

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(boss, work)
                .localAddress(new InetSocketAddress(wsServerPort))
                //保持长连接
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                //指定NIO的模式
                .channel(NioServerSocketChannel.class)
                .childHandler(wsServerInitialzer);


        ChannelFuture future = bootstrap.bind().sync();
        if (future.isSuccess()) {
            log.info("Start im server success!!! port:{}",wsServerPort);
        }
    }


    /**
     * 销毁
     */
    @PreDestroy
    public void destroy() {
        boss.shutdownGracefully().syncUninterruptibly();
        work.shutdownGracefully().syncUninterruptibly();
        log.info("Close cim server success!!! port:{}",wsServerPort);
    }

}
