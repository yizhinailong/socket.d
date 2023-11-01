package labs.tcp.bio;

import org.noear.socketd.SocketD;
import org.noear.socketd.core.Entity;
import org.noear.socketd.core.Session;
import org.noear.socketd.core.SimpleListener;
import org.noear.socketd.core.entity.StringEntity;

/**
 * @author noear
 * @since 2.0
 */
public class ClientTest {
    public static void main(String[] args) throws Exception {
        //client
        Session session = SocketD
                .createClient("tcp://127.0.0.1:6329/path?u=a&p=2")
                .config(c -> c.autoReconnect(true)) //配置
                .listen(new ClientListener()) //如果要监听，加一下
                .heartbeatHandler(null) //如果要替代 ping,pong 心跳，加一下
                .open();
        session.send("/user/created", new StringEntity("hi"));

        Entity response = session.sendAndRequest("/user/get", new StringEntity("hi"));
        System.out.println("sendAndRequest====" + response);

        session.sendAndSubscribe("/user/sub", new StringEntity("hi"), message -> {
            System.out.println("sendAndSubscribe====" + message);
        });

        while (true) {
            try {
                Thread.sleep(5000);
                session.send("/user/updated", new StringEntity("hi"));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        //Entity response = session.sendAndRequest(null);
        //session.sendAndSubscribe(null, null);
    }

    public static class ClientListener extends SimpleListener {

    }
}
