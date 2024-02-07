package org.noear.socketd.transport.client;

import org.noear.socketd.exception.SocketDException;
import org.noear.socketd.transport.core.*;
import org.noear.socketd.transport.core.impl.ProcessorDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 客户端基类
 *
 * @author noear
 * @since 2.0
 */
public abstract class ClientBase<T extends ChannelAssistant> implements ClientInternal {
    private static final Logger log = LoggerFactory.getLogger(ClientBase.class);

    //协议处理器
    protected Processor processor = new ProcessorDefault();
    //心跳处理
    protected HeartbeatHandler heartbeatHandler;

    //配置
    private final ClientConfig config;
    //助理
    private final T assistant;

    public ClientBase(ClientConfig clientConfig, T assistant) {
        this.config = clientConfig;
        this.assistant = assistant;
    }

    /**
     * 获取通道助理
     */
    public T getAssistant() {
        return assistant;
    }

    /**
     * 获取心跳处理
     */
    @Override
    public HeartbeatHandler getHeartbeatHandler() {
        return heartbeatHandler;
    }

    /**
     * 获取心跳间隔（毫秒）
     */
    @Override
    public long getHeartbeatInterval() {
        return config.getHeartbeatInterval();
    }


    /**
     * 获取配置
     */
    @Override
    public ClientConfig getConfig() {
        return config;
    }

    /**
     * 获取处理器
     */
    @Override
    public Processor getProcessor() {
        return processor;
    }

    /**
     * 设置心跳
     */
    @Override
    public Client heartbeatHandler(HeartbeatHandler handler) {
        if (handler != null) {
            this.heartbeatHandler = handler;
        }

        return this;
    }

    /**
     * 配置
     */
    @Override
    public Client config(ClientConfigHandler configHandler) {
        if (configHandler != null) {
            configHandler.clientConfig(config);
        }
        return this;
    }

    /**
     * 设置监听器
     */
    @Override
    public Client listen(Listener listener) {
        if (listener != null) {
            processor.setListener(listener);
        }
        return this;
    }


    /**
     * 打开会话
     */
    @Override
    public ClientSession open() throws IOException {
        return openDo(false);
    }

    /**
     * 打开会话或出异常
     */
    @Override
    public ClientSession openOrThow() throws IOException {
        return openDo(true);
    }

    private Session openDo(boolean isThow) throws IOException {
        ClientConnector connector = createConnector();
        ClientChannel clientChannel = new ClientChannel(connector);

        try {
            clientChannel.connect();

            log.info("Socket.D client successfully connected: {link={}}", getConfig().getLinkUrl());
        } catch (Throwable e) {
            if (isThow) {

                clientChannel.close(Constants.CLOSE2008_OPEN_FAIL);

                if (e instanceof RuntimeException || e instanceof IOException) {
                    throw e;
                } else {
                    throw new SocketDException("Socket.D client Connection failed", e);
                }
            } else {
                log.info("Socket.D client Connection failed: {link={}}", getConfig().getLinkUrl());
            }
        }

        return clientChannel.getSession();
    }

    /**
     * 创建连接器
     */
    protected abstract ClientConnector createConnector();
}