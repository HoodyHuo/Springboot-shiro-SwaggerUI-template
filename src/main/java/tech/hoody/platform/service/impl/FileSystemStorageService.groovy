package tech.hoody.platform.service.impl


import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile
import tech.hoody.platform.exception.StorageException
import tech.hoody.platform.exception.StorageFileNotFoundException
import tech.hoody.platform.service.StorageService

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream

@Profile(["dev", "prod", "ecs"])
@Service
class FileSystemStorageService implements StorageService {


    @Value('${platform.uploadDir}')
    private String uploadDir


    @Override
    void init() {
        try {
            Files.createDirectory(Paths.get(uploadDir))
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    String store(MultipartFile file, String path, String suffix) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            Path targetDir = Paths.get(uploadDir + path)
            if (!targetDir.toFile().exists()) {
                Files.createDirectory(targetDir)
            }
            String fileName = UUID.randomUUID().toString() + file.getOriginalFilename() + (suffix == null ? "" : suffix)
            Path physicalPath = Paths.get(uploadDir + path).resolve(fileName)
            Files.copy(file.getInputStream(), physicalPath)
            return "/storage${path}/${fileName}"
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter { Path path -> !path.equals(this.rootLocation) }
                    .map { path -> this.rootLocation.relativize(path) }
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}
