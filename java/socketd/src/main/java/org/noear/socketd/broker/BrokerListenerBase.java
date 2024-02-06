package org.noear.socketd.broker;

import org.noear.socketd.transport.core.Listener;
import org.noear.socketd.transport.core.Session;
import org.noear.socketd.utils.StrUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 经纪人监听器基类（实现玩家封闭管理）
 *
 * @author noear
 * @since 2.1
 */
public abstract class BrokerListenerBase implements Listener {
    private Map<String, Session> sessionAll = new ConcurrentHashMap<>();
    //玩家会话
    private Map<String, Set<Session>> playerSessions = new ConcurrentHashMap<>();

    /**
     * 获取所有会话（包括没有名字的）
     */
    public Collection<Session> getSessionAll() {
        return sessionAll.values();
    }

    /**
     * 获取所有玩家的名字
     */
    public Collection<String> getNameAll() {
        return playerSessions.keySet();
    }

    /**
     * 获取所有玩家数量
     *
     * @param name 名字
     */
    public int getPlayerNum(String name) {
        Collection<Session> tmp = getPlayerAll(name);

        if (tmp == null) {
            return 0;
        } else {
            return tmp.size();
        }
    }

    /**
     * 获取所有玩家会话
     *
     * @param name 名字
     */
    public Collection<Session> getPlayerAll(String name) {
        return playerSessions.get(name);
    }

    /**
     * 获取第一个玩家会话
     *
     * @param name 名字
     * @since 2.3
     */
    public Session getPlayerFirst(String name) {
        if (StrUtils.isEmpty(name)) {
            return null;
        }

        return BrokerPolicy.getFirst(getPlayerAll(name));
    }

    /**
     * 获取任意一个玩家会话
     *
     * @param name 名字
     * @since 2.3
     */
    public Session getPlayerAny(String name) {
        if (StrUtils.isEmpty(name)) {
            return null;
        }

        return BrokerPolicy.getAnyByPoll(getPlayerAll(name));
    }

    /**
     * 根据 ip_hash 获取任意一个玩家会话
     *
     * @param name 名字
     * @since 2.3
     */
    public Session getPlayerAnyByIpHash(String name, Session requester) throws IOException {
        if (StrUtils.isEmpty(name)) {
            return null;
        }

        return BrokerPolicy.getAnyByIpHash(getPlayerAll(name), requester);
    }


    /**
     * 获取任意一个玩家会话
     *
     * @param name 名字
     * @deprecated 2.3
     */
    @Deprecated
    public Session getPlayerOne(String name) {
        return getPlayerAny(name);
    }

    /**
     * 添加玩家会话
     *
     * @param name    名字
     * @param session 玩家会话
     */
    public void addPlayer(String name, Session session) {
        //注册玩家会话
        if (StrUtils.isNotEmpty(name)) {
            Set<Session> sessions = playerSessions.computeIfAbsent(name, n -> Collections.newSetFromMap(new ConcurrentHashMap<>()));
            sessions.add(session);
        }

        sessionAll.put(session.sessionId(), session);
    }

    /**
     * 移除玩家会话
     *
     * @param name    名字
     * @param session 玩家会话
     */
    public void removePlayer(String name, Session session) {
        //注销玩家会话
        if (StrUtils.isNotEmpty(name)) {
            Collection<Session> sessions = getPlayerAll(name);
            if (sessions != null) {
                sessions.remove(session);
            }
        }

        sessionAll.remove(session.sessionId());
    }
}