package me.code.dropfolder.controller;

import me.code.dropfolder.dto.Success;
import me.code.dropfolder.security.JwtTokenProvider;
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
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public FileController(FileService fileService, JwtTokenProvider jwtTokenProvider) {
        this.fileService = fileService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/upload/{folderId}")
    public ResponseEntity<Success> upload(
            @RequestHeader("Authorization") String token,
            @PathVariable long folderId, @RequestParam("file") MultipartFile file) {
        long userId = jwtTokenProvider.getTokenUserId(token);

        Success result = fileService.upload(userId, folderId, file);
        return result.toResponseEntity();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Success> delete(
            @RequestHeader("Authorization") String token,
            @RequestParam long folderId,
            @RequestParam long fileId) {
        long userId = jwtTokenProvider.getTokenUserId(token);

        Success result = fileService.delete(userId, folderId, fileId);
        return result.toResponseEntity();
    }

}