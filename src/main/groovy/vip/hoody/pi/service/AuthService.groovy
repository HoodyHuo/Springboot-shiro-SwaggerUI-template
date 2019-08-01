package vip.hoody.pi.service


import org.apache.shiro.crypto.SecureRandomNumberGenerator
import org.apache.shiro.crypto.hash.Md5Hash
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import vip.hoody.pi.domain.Permission
import vip.hoody.pi.domain.Role
import vip.hoody.pi.domain.User
import vip.hoody.pi.repository.RolePermissionRepository
import vip.hoody.pi.repository.RoleRepository
import vip.hoody.pi.repository.UserRoleRepository
import vip.hoody.pi.util.SaltAndencryptPwd

@Service
class AuthService {

    @Autowired
    UserRoleRepository userRoleRepository
    @Autowired
    RolePermissionRepository rolePermissionRepository
    @Autowired
    RoleRepository roleRepository


    /**
     * 用户注册时加密用户的密码
     * 输入密码明文 返回密文与盐值
     * @param password
     * @return SaltAndencryptPwd
     */
    static SaltAndencryptPwd encryptPassword(String password) {
        String salt = new SecureRandomNumberGenerator().nextBytes().toHex(); //生成盐值
        String encryptPwd = new Md5Hash(password, salt, 3).toString(); //生成的密文

        return new SaltAndencryptPwd(salt, encryptPwd)
    }

    static String getInputPasswordCiph(String password, String salt) {
        if (salt.isEmpty()) {
            password = "";
        }

        String ciphertext = new Md5Hash(password, salt, 3).toString(); //生成的密文

        return ciphertext;
    }

    /**
     * 获取指定用户的所有角色信息
     * @param user
     * @return
     */
    Set<Role> findRolesByUser(User user) {
        Set<Role> temp = userRoleRepository.findAllByUser(user).role
        Set<Role> result = new HashSet<>()
        result.addAll(temp)
        temp.each { Role it ->
            findAllChildrens(it, result)
        }
        return result
    }

    Set<Permission> findPermissionByRoles(Set<Role> roles) {
        Set<Permission> result = new HashSet<>()
        roles.each { Role it ->
            Set<Permission> temp = rolePermissionRepository.findAllByRole(it).permission
            result.addAll(temp)
        }
        return result
    }

    /**
     * 将指定Role的所有子角色加入到Set中
     * @param role
     * @param set
     * @return
     */
    private findAllChildrens(Role role, Set<Role> set) {
        if (role.children.size() > 0) {
            set.addAll(role.children)
            role.children.each {
                findAllChildrens(it, set)
            }
        }
    }
}
