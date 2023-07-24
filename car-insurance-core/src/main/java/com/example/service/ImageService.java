package com.example.service;

import com.example.Image;
import com.example.exception.ImageNotFoundException;
import com.example.repository.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ImageService {

    private final ImageRepository repository;

    @Autowired
    public ImageService(ImageRepository repository) {
        this.repository = repository;
    }

    public List<byte[]> findImagesByAutomobileId(final long id) {
        log.info(String.format("Find images of automobile with id = %s", id));
        List<Image> imageList = repository.findImagesByAutomobileId(id);
        List<byte[]> imageBytes = new ArrayList<>();
        imageList.forEach(image -> imageBytes.add(Base64.getDecoder().decode(image.getBase64File())));
        return imageBytes;
    }

    public Image createImage(Image image) {
        log.info(String.format("Create %s", image));
        return repository.save(image);
    }

    public byte[] findImageById(final long id) {
        log.info(String.format("Find image by id = %s", id));
        Optional<Image> imageOptional = repository.findById(id);
        if (imageOptional.isEmpty()) {
            String exceptionMessage = String.format("Image not found by id = %s", id);
            log.debug(exceptionMessage);
            throw new ImageNotFoundException(exceptionMessage);
        }
        return Base64.getDecoder().decode(imageOptional.get().getBase64File());
    }

    public Image deleteImageById(final long id) {
        log.info(String.format("Delete image with id = %s", id));
        Optional<Image> imageOptional = repository.findById(id);
        if (imageOptional.isEmpty()) {
            log.debug(String.format("Image not found by id = %s", id));
            throw new ImageNotFoundException(String.format("Image not found by id = %s", id));
        }
        repository.deleteById(id);
        return imageOptional.get();
    }
}
