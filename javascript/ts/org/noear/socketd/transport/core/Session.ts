/* Generated from Java with JSweet 3.1.0 - http://www.jsweet.org */
namespace org.noear.socketd.transport.core {
    /**
     * 会话
     * 
     * @author noear
     * @since 2.0
     * @class
     */
    export interface Session {
        /**
         * 是否有效
         * @return {boolean}
         */
        isValid(): boolean;

        /**
         * 获取远程地址
         * @return {java.net.InetSocketAddress}
         */
        remoteAddress(): java.net.InetSocketAddress;

        /**
         * 获取本地地址
         * @return {java.net.InetSocketAddress}
         */
        localAddress(): java.net.InetSocketAddress;

        /**
         * 获取握手信息
         * @return {*}
         */
        handshake(): org.noear.socketd.transport.core.Handshake;

        /**
         * 获取握手参数
         * 
         * @param {string} name 名字
         * @return {string}
         */
        param(name: string): string;

        /**
         * 获取握手参数或默认值
         * 
         * @param {string} name 名字
         * @param {string} def  默认值
         * @return {string}
         */
        paramOrDefault(name: string, def: string): string;

        /**
         * 获取握手路径
         * @return {string}
         */
        path(): string;

        /**
         * 设置握手新路径
         * @param {string} pathNew
         */
        pathNew(pathNew: string);

        /**
         * 获取所有属性
         * @return {*}
         */
        attrMap(): any;

        /**
         * 获取属性或默认值
         * 
         * @param {string} name 名字
         * @param {*} def  默认值
         * @return {*}
         */
        attrOrDefault<T>(name: string, def: T): T;

        /**
         * 设置属性
         * 
         * @param {string} name  名字
         * @param {*} value 值
         */
        attr<T0 = any>(name?: any, value?: any);

        /**
         * 获取会话Id
         * @return {string}
         */
        sessionId(): string;

        /**
         * 手动重连（一般是自动）
         */
        reconnect();

        /**
         * 手动发送 Ping（一般是自动）
         */
        sendPing();

        /**
         * 发送
         * 
         * @param {string} topic   主题
         * @param {*} content 内容
         */
        send(topic: string, content: org.noear.socketd.transport.core.Entity);

        /**
         * 发送并请求（限为一次答复；指定超时）
         * 
         * @param {string} topic   主题
         * @param {*} content 内容
         * @param {number} timeout 超时（毫秒）
         * @return {*}
         */
        sendAndRequest(topic?: any, content?: any, timeout?: any): org.noear.socketd.transport.core.Entity;

        /**
         * 发送并订阅（答复结束之前，不限答复次数）
         * 
         * @param {string} topic    主题
         * @param {*} content  内容
         * @param {*} consumer 回调消费者
         */
        sendAndSubscribe(topic: string, content: org.noear.socketd.transport.core.Entity, consumer: org.noear.socketd.utils.IoConsumer<org.noear.socketd.transport.core.Entity>);

        /**
         * 答复
         * 
         * @param {*} from    来源消息
         * @param {*} content 内容
         */
        reply(from: org.noear.socketd.transport.core.Message, content: org.noear.socketd.transport.core.Entity);

        /**
         * 答复并结束（即最后一次答复）
         * 
         * @param {*} from    来源消息
         * @param {*} content 内容
         */
        replyEnd(from: org.noear.socketd.transport.core.Message, content: org.noear.socketd.transport.core.Entity);
    }
}

