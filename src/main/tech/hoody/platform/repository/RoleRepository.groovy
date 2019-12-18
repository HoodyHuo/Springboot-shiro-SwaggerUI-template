package tech.hoody.platform.repository

import org.springframework.data.jpa.repository.JpaRepository
import tech.hoody.platform.domain.Role

/**
 * Created by Hoody on 2019/1/14.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    public List<Role> findAllByparentRole(Role role)
}
