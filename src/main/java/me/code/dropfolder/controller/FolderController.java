package me.code.dropfolder.controller;

import me.code.dropfolder.dto.CreateFolderDto;
import me.code.dropfolder.dto.Success;
import me.code.dropfolder.security.JwtTokenProvider;
import me.code.dropfolder.service.folder.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/folder")
public class FolderController {

    private final FolderService folderService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public FolderController(FolderService folderService, JwtTokenProvider jwtTokenProvider) {
        this.folderService = folderService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/create")
    public ResponseEntity<Success> create(@RequestHeader("Authorization") String token, @RequestBody CreateFolderDto dto) {
        long userId = jwtTokenProvider.getTokenUserId(token);

        Success result = folderService.createFolder(dto.name(), userId);
        return result.toResponseEntity();
    }

}