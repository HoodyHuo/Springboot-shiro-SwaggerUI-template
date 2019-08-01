package vip.hoody.pi.repository

import org.springframework.data.jpa.repository.JpaRepository
import vip.hoody.pi.domain.Role

/**
 * Created by Hoody on 2019/1/14.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    public List<Role> findAllByparentRole(Role role)
}
