package tech.hoody.platform.shiro

import org.apache.shiro.web.servlet.ShiroHttpServletRequest
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager
import org.apache.shiro.web.util.WebUtils
import org.springframework.util.StringUtils

import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

/**
 *  @author Hoody
 * 自定义sessionId获取方式
 * 从前端发送的header中获取SessionId,如果没有再从cookie中读取
 */
class CustomSessionManager extends DefaultWebSessionManager {

    /** 存放 sessionID 的header key */
    private static final String AUTHORIZATION = "X-Token"
    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request"

    /**
     * 重写getSessionId方法, 从前端发送的header中获取SessionId,如果没有再从cookie中读取
     * @param request
     * @param response
     * @return
     */


    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String id = WebUtils.toHttp(request).getHeader(AUTHORIZATION)
        //如果请求头中有 Authorization 则其值为sessionId
        if (!StringUtils.isEmpty(id)) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE)
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id)
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE)
            return id
        } else {
            //否则按默认规则从cookie取sessionId
            return super.getSessionId(request, response)
        }
    }
}
