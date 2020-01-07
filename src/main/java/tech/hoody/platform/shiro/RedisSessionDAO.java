package tech.hoody.platform.shiro;

import org.apache.shiro.cache.CacheManagerAware;
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
public class RedisSessionDAO extends CachingSessionDAO implements CacheManagerAware {

    private static final String PREFIX = "SHIRO_SESSION_ID";

    private static final String USERNAME_PREFIX = "USERNAME_SESSION_ID";

    private static final int EXPRIE = 10000;

    private static final Logger log = LoggerFactory.getLogger(RedisSessionDAO.class);

    private RedisTemplate<Serializable, Object> redisTemplate;

    public RedisSessionDAO(RedisTemplate<Serializable, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected Serializable doCreate(Session session) {
        CustomSession customSession = (CustomSession) session;
        Serializable serializable = this.generateSessionId(session);
        assignSessionId(session, serializable);
        redisTemplate.opsForValue().set(PREFIX + serializable, session);
        redisTemplate.opsForValue().set(USERNAME_PREFIX + customSession.getUsernmae(), serializable);
        return serializable;
    }

    @Override
    protected void doUpdate(Session session) {
        session.setTimeout(EXPRIE * 1000);
        CustomSession customSession = (CustomSession) session;
        redisTemplate.opsForValue().set(PREFIX + session.getId(), session, EXPRIE, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(USERNAME_PREFIX + customSession.getUsernmae(), session.getId(), EXPRIE, TimeUnit.SECONDS);
    }

    @Override
    protected void doDelete(Session session) {
        if (session == null) {
            return;
        }
        CustomSession customSession = (CustomSession) session;
        redisTemplate.delete(PREFIX + session.getId());
        redisTemplate.delete(USERNAME_PREFIX + customSession.getUsernmae());
    }

    /**
     * 从Redis读取Session，如果未读取到，则抛出异常
     *
     * @param sessionId
     * @return
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            return null;
        }
        Session session = (Session) redisTemplate.opsForValue().get(PREFIX + sessionId);
        if (session == null) {
            throw new SignOutException("Account Sign in offsite");
        }
        return session;
    }

    public Session getSessionByUsername(String username) {
        String sessionId = this.getSessionIdByUsername(username);
        return doReadSession(sessionId);
    }

    public String getSessionIdByUsername(String username) {
        return (String) redisTemplate.opsForValue().get(USERNAME_PREFIX + username);
    }
}
