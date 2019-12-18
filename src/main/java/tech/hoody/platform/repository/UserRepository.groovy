package tech.hoody.platform.repository

import org.springframework.data.jpa.repository.JpaRepository
import tech.hoody.platform.domain.User

/**
 * Created by Hoody on 2019/1/14.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
