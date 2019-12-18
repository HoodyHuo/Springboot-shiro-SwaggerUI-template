package tech.hoody.platform.exception.handler

import org.apache.tomcat.util.http.fileupload.FileUploadBase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.multipart.MultipartException
import tech.hoody.platform.exception.StorageFileNotFoundException
import tech.hoody.platform.util.ResponseData

import java.text.DecimalFormat

@RestControllerAdvice
@Order(1)
class StorageExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(StorageExceptionHandler.class);

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseData handleFileSizeLimitExceededException(MultipartException exc) {
        String msg = null
        DecimalFormat format = new DecimalFormat(".00")
        Exception caseE = exc.getCause().getCause()
        if (caseE instanceof FileUploadBase.FileSizeLimitExceededException) {
            FileUploadBase.FileSizeLimitExceededException e = (FileUploadBase.FileSizeLimitExceededException) caseE
            msg = "上传文件过大,单个文件不得超过${format.format(e.permittedSize / 1048576L)}M"
        } else if (caseE instanceof FileUploadBase.SizeLimitExceededException) {
            FileUploadBase.SizeLimitExceededException e = (FileUploadBase.SizeLimitExceededException) caseE
            msg = "上传文件过大，总文件大小不得超过${format.format(e.permittedSize / 1048576L)}M"
        } else {
            msg = "文件上传失败：${exc.getMessage()}"
        }
        return new ResponseData(code: 200001, msg: msg)
    }
}
