package tech.hoody.platform.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import tech.hoody.platform.domain.MongoImage
import tech.hoody.platform.exception.ProjectException
import tech.hoody.platform.service.MongoDBStorageService
import tech.hoody.platform.util.ResponseData

@RestController
class MongoController {

    @Autowired
    MongoDBStorageService service

    @PostMapping("/images")
    public ResponseData upload(@RequestParam(value = "image") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseData(msg: "请选择图片")
        }
        String id = service.store(file)
        return new ResponseData(data: id)
    }

    @GetMapping(path = "/images/{id}", produces = [MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE])
    public byte[] load(@PathVariable String id) {
        Optional<MongoImage> result = service.read(id)
        if (!result.isPresent()) {
            throw new ProjectException("没有${id}的图片")
        }
        return result.get().content.getData()
    }

}
