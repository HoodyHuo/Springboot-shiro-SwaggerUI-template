package tech.hoody.platform.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Hoody
 * CachingSessionDAO impl
 * deal the session DAO with Redis
 */
public class RedisSessionDAO extends CachingSessionDAO {

    private static final String PREFIX = "SHIRO_SESSION_ID";

    private static final int EXPRIE = 10000;

    private static final Logger log = LoggerFactory.getLogger(RedisSessionDAO.class);

    private RedisTemplate<Serializable, Session> redisTemplate;

    public RedisSessionDAO(RedisTemplate<Serializable, Session> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable serializable = this.generateSessionId(session);
        assignSessionId(session, serializable);
        redisTemplate.opsForValue().set(serializable, session);
        return serializable;
    }

    @Override
    protected void doUpdate(Session session) {
        session.setTimeout(EXPRIE * 1000);
        redisTemplate.opsForValue().set(session.getId(), session, EXPRIE, TimeUnit.SECONDS);
    }

    @Override
    protected void doDelete(Session session) {
        if (session == null) {
            return;
        }
        redisTemplate.delete(session.getId());
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            return null;
        }
        Session session = redisTemplate.opsForValue().get(sessionId);
        return session;
    }
}
