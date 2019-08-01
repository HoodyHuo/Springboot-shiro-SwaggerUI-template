package vip.hoody.pi.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.authz.annotation.RequiresUser
import org.apache.shiro.subject.Subject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import vip.hoody.pi.domain.User
import vip.hoody.pi.service.AuthService
import vip.hoody.pi.service.UserService
import vip.hoody.pi.util.ResponseData
import vip.hoody.pi.util.SaltAndencryptPwd

import javax.validation.Valid

@Api("权限接口")
@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    AuthService authService

    @Autowired
    UserService userService

    @ApiImplicitParams([
            @ApiImplicitParam(name = "username", dataType = "String", value = "111", required = true),
            @ApiImplicitParam(name = "password", dataType = "String", value = "111", required = true)
    ])
    @ApiOperation(value = "登录", notes = "根据账号密码返回用户")
    @PostMapping("login")
    ResponseData login(
            UsernamePasswordToken token, Model model) {
        Subject currentUser = SecurityUtils.getSubject()
        //登录
//        currentUser.login(new UsernamePasswordToken(username, password));
        currentUser.login(token)
        //从session取出用户信息
        User user = (User) currentUser.getPrincipal()
        if (user == null) throw new AuthenticationException()
        //返回登录用户的信息给前台，含用户的所有角色和权限
        def session = currentUser.getSession()
        return new ResponseData(data: session.id)
    }

    @ApiImplicitParam(name = "token", dataType = "String", required = true)
    @PostMapping("info")
    @RequiresUser
    ResponseData getAuthInfo() {
        Subject currentUser = SecurityUtils.getSubject()
        User user = (User) currentUser.getPrincipal()
        return new ResponseData(data: [
                username: user.username, roles: user.roles, perms: user.perms
        ])
    }

    @PostMapping("/userRegister")
    public ResponseData userRegister(@Valid User user,
                                     BindingResult bindingResult,
                                     RedirectAttributes attributes) {

        if (bindingResult.hasErrors()) {
            return new ResponseData(code: 20001, msg: bindingResult.getAllErrors().toString());
        }
        SaltAndencryptPwd saltAndencryptPwd = authService.encryptPassword(user.password)
        user.password = saltAndencryptPwd.encryptPassword
        user.salt = saltAndencryptPwd.salt
        userService.userRegister(user)
        return new ResponseData(data: user)
    }

    @GetMapping("logout")
    ResponseData logout() {
        Subject currentUser = SecurityUtils.getSubject()
        currentUser.logout()
        return new ResponseData()
    }
}
