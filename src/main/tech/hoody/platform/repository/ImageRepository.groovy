package tech.hoody.platform.repository

import org.springframework.data.mongodb.repository.MongoRepository
import tech.hoody.platform.domain.MongoImage

interface ImageRepository extends MongoRepository<MongoImage,String> {

}
