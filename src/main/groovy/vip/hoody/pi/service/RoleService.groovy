package vip.hoody.pi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import vip.hoody.pi.domain.User
import vip.hoody.pi.domain.UserRole
import vip.hoody.pi.repository.UserRoleRepository

@Service
class RoleService {

    @Autowired
    UserRoleRepository userRoleRepository

    Set<String> getRolesByUserId(User user) {
        List<UserRole> userRoleList = userRoleRepository.findAllByUser(user)
        //获取角色信息
        Set<String> roles = userRoleList.role.toSet()
        return roles
    }
}
