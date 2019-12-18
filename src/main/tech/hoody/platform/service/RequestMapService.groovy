package tech.hoody.platform.service


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tech.hoody.platform.domain.RequestMap
import tech.hoody.platform.repository.RequestMapRepository

@Service
class RequestMapService {

    @Autowired
    RequestMapRepository requestMapRepository

    List<RequestMap> findAllRequestMap() {
        return requestMapRepository.findAll()
    }
}
