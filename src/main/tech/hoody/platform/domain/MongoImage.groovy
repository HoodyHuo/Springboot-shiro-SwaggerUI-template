package tech.hoody.platform.domain

import org.bson.types.Binary
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * 存储于mongoDB的图片文件
 */
@Document
class MongoImage {

    @Id
    private String id
    private String name
    private Date createDate
    private Binary content
    private String contentType
    private Long size


    String getId() {
        return id
    }

    void setId(String id) {
        this.id = id
    }

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    Date getCreateDate() {
        return createDate
    }

    void setCreateDate(Date createDate) {
        this.createDate = createDate
    }

    Binary getContent() {
        return content
    }

    void setContent(Binary content) {
        this.content = content
    }

    String getContentType() {
        return contentType
    }

    void setContentType(String contentType) {
        this.contentType = contentType
    }

    Long getSize() {
        return size
    }

    void setSize(Long size) {
        this.size = size
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MongoImage.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("name='" + name + "'")
                .add("createDate=" + createDate)
                .add("contentType='" + contentType + "'")
                .add("size=" + size)
                .toString();
    }
}
