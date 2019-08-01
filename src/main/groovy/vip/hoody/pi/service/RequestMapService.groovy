package vip.hoody.pi.service


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import vip.hoody.pi.domain.RequestMap
import vip.hoody.pi.repository.RequestMapRepository

@Service
class RequestMapService {

    @Autowired
    RequestMapRepository requestMapRepository

    List<RequestMap> findAllRequestMap() {
        return requestMapRepository.findAll()
    }
}
