package ru.skillbox.diplom.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.Mapper.Constant;
import ru.skillbox.diplom.config.StorageConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class ImageService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StorageConfig storageConfig;


    public Path addImage(MultipartFile multipartFile) throws IOException {
        Path path = null;
        byte[] bytes = multipartFile.getBytes();
        String[] uploadName = Objects.requireNonNull(multipartFile.getOriginalFilename()).split("\\.");
        String imgName = Constant.codeGenerator(10) + "." + uploadName[uploadName.length - 1];
        path = Paths.get(storageConfig.getLocation() + "/" + imgName);
        Files.write(path, bytes);

        return path;
    }
}
