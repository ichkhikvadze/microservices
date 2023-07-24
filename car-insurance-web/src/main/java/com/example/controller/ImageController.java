package com.example.controller;

import com.example.Image;
import com.example.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/images")
@Slf4j
public class ImageController {

    private final ImageService service;

    @Autowired
    public ImageController(ImageService service) {
        this.service = service;
    }

    @Operation(summary = "Get all images by automobile id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all images"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all/{id}")
    public List<byte[]> findImagesByAutomobileId(
            @Parameter(description = "Image id") @PathVariable(name = "id") final long id
    ) {
        log.info(String.format("Find images of automobile with id = %s", id));
        return service.findImagesByAutomobileId(id);
    }

    @Operation(summary = "Create image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Image created"),
            @ApiResponse(responseCode = "400", description = "Invalid format"),
            @ApiResponse(responseCode = "409", description = "Image already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    public Image createImage(Image image) {
        log.info(String.format("Create %s", image));
        return service.createImage(image);
    }

    @Operation(summary = "Find image by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image found"),
            @ApiResponse(responseCode = "404", description = "Image not found"),
    })
    @GetMapping("/{id}")
    public byte[] findImageById(@Parameter(description = "Image id") @PathVariable(name = "id") final long id) {
        log.info(String.format("Find image by id = %s", id));
        return service.findImageById(id);
    }

    @Operation(summary = "Delete image by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Image deleted"),
            @ApiResponse(responseCode = "404", description = "Image not found")
    })
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping
    public Image deleteImageById(final long id) {
        log.info(String.format("Delete image with id = %s", id));
        return service.deleteImageById(id);
    }
}
