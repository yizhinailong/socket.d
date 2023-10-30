package org.noear.socketd.broker.websocket;

import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.noear.socketd.client.ClientChannel;
import org.noear.socketd.client.ClientConnectorBase;
import org.noear.socketd.exception.SocktedConnectionException;
import org.noear.socketd.protocol.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author noear
 * @since 2.0
 */
public class WsClientConnector extends ClientConnectorBase<WsClient> {
    private static final Logger log = LoggerFactory.getLogger(WsClientConnector.class);

    private SocketClientImpl real;

    public WsClientConnector(WsClient client) {
        super(client);
    }

    @Override
    public Channel connect() throws IOException {
        real = new SocketClientImpl(client.uri(), client);

        try {
            if (real.connectBlocking(client.config().getConnectTimeout(), TimeUnit.MILLISECONDS)) {
                return real.getChannel();
            } else {
                throw new SocktedConnectionException("Client:Connection fail");
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void close() throws IOException {
        if (real == null) {
            return;
        }

        try {
            real.close();
        } catch (Throwable e) {
            log.debug("{}", e);
        }
    }
}
