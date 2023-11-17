package me.code.dropfolder.controller;

import me.code.dropfolder.dto.CreateFolderDto;
import me.code.dropfolder.dto.Success;
import me.code.dropfolder.service.folder.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/folder")
public class FolderController {

    private final FolderService folderService;

    @Autowired
    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @PostMapping("/create")
    public ResponseEntity<Success> create(@RequestBody CreateFolderDto dto) {
        Success result = folderService.createFolder(dto.name(), dto.userId());
        return result.toResponseEntity();
    }

}