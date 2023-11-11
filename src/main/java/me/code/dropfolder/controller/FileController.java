package me.code.dropfolder.controller;

import me.code.dropfolder.dto.Success;
import me.code.dropfolder.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/file")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Success> upload(@RequestParam("file") MultipartFile file) {
        Success result = fileService.upload(file);
        return result.toResponseEntity();
    }

}