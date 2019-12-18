package tech.hoody.platform.repository

import org.springframework.data.jpa.repository.JpaRepository
import tech.hoody.platform.domain.User
import tech.hoody.platform.domain.UserRole

/**
 * Created by Hoody on 2019/1/15.
 */

interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    public List<UserRole> findAllByUser(User user)
}
