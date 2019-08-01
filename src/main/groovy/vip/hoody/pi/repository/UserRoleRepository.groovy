package vip.hoody.pi.repository

import org.springframework.data.jpa.repository.JpaRepository
import vip.hoody.pi.domain.User
import vip.hoody.pi.domain.UserRole

/**
 * Created by Hoody on 2019/1/15.
 */

interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    public List<UserRole> findAllByUser(User user)
}