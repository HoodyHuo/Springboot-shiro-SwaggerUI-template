package tech.hoody.platform.repository

import org.springframework.data.jpa.repository.JpaRepository
import tech.hoody.platform.domain.RequestMap

/**
 * Created by Hoody on 2019/1/17.
 */
interface RequestMapRepository extends JpaRepository<RequestMap, Long> {

    List<RequestMap> findAllByConfigAttribute(String configAttribute)

    RequestMap findByUrl(String url)

}
