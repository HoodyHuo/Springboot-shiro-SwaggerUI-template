package vip.hoody.pi.repository

import org.springframework.data.jpa.repository.JpaRepository
import vip.hoody.pi.domain.User

/**
 * Created by Hoody on 2019/1/14.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
