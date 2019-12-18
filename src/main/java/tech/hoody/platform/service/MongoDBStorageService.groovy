package tech.hoody.platform.service

import org.bson.types.Binary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import tech.hoody.platform.domain.MongoImage
import tech.hoody.platform.repository.ImageRepository
import tech.hoody.platform.util.MimeTypeUtil

@Service
class MongoDBStorageService {

    @Autowired
    ImageRepository repository

    public String store(MultipartFile file) {
        MongoImage image = new MongoImage()
        image.setName(file.getName())
        image.setContentType(file.getContentType())
        image.setContent(new Binary(file.getBytes()))
        image.setSize(file.getSize())
        image.setCreateDate(new Date())
        repository.save(image)
        return image.id
    }

    public String store(File file) {
        MongoImage image = new MongoImage()
        image.setName(file.getName())
        image.setContent(new Binary(file.getBytes()))
        image.setSize(file.size())
        image.setCreateDate(new Date())
        repository.save(image)
        return image.id
    }

    public Optional<MongoImage> read(String id) {
        repository.findById(id)
    }

}
