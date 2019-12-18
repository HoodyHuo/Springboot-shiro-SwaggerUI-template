package tech.hoody.platform.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tech.hoody.platform.domain.User
import tech.hoody.platform.domain.UserRole
import tech.hoody.platform.repository.UserRoleRepository

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
