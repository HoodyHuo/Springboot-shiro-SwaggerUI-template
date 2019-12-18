package tech.hoody.platform.exception.handler

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import tech.hoody.platform.exception.ProjectException
import tech.hoody.platform.util.ResponseData

import javax.servlet.http.HttpServletRequest
import javax.validation.ConstraintViolationException

@RestControllerAdvice
@Order(3)
class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class)
    /**
     * 未知异常处理
     * @param request
     * @param e
     * @param model
     * @return
     */
    @ExceptionHandler(value = ProjectException.class)
    public ResponseData handleProjectException(HttpServletRequest request, ProjectException e) {
        return new ResponseData(code: 20001, msg: e.getMessage())
    }

    /**
     * 处理参数映射异常
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseData handleProjectException(HttpServletRequest request, ConstraintViolationException e) {
        return new ResponseData(code: 20001, msg: e.getMessage())
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseData handleRuntimeException(HttpServletRequest request, RuntimeException e) {
        log.error("发生了未知运行时异常", e)
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        def msg = """URL:${request.getRequestURI()},\n
                     params:${request.getParameterMap().toString()},\n
                     error: ${e.getMessage()}"""
        return new ResponseData(code: 50001, msg: msg)
    }
    /**
     * 未知异常处理
     * @param request
     * @param e
     * @param model
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseData handleUnknownException(HttpServletRequest request, Exception e, Model model) {
        log.error("发生了未知异常", e);
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        def msg = """URL:${request.getRequestURI()},\n
                     params:${request.getParameterMap().toString()},\n
                     error: ${e.getMessage()}"""
        return new ResponseData(code: 50000, msg: msg)
    }

}
