package me.code.dropfolder.controller;

import me.code.dropfolder.dto.CreateFolderDto;
import me.code.dropfolder.dto.SuccessDto;
import me.code.dropfolder.security.JwtTokenUtil;
import me.code.dropfolder.service.folder.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling folder-related operations such as folder creation.
 * Provides endpoints under the "/api/folder" path.
 */
@RestController
@RequestMapping("/api/folder")
public class FolderController {

    private final FolderService folderService;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * Constructor for FolderController.
     *
     * @param folderService The folder service to handle folder-related business logic.
     * @param jwtTokenUtil  The utility for handling JSON Web Tokens; used for user authentication.
     */
    @Autowired
    public FolderController(FolderService folderService, JwtTokenUtil jwtTokenUtil) {
        this.folderService = folderService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * Handles folder creation.
     *
     * @param token The authorization token containing the user's credentials.
     * @param dto   The DTO containing information for creating a folder.
     * @return ResponseEntity containing the success DTO.
     */
    @PostMapping("/create")
    public ResponseEntity<SuccessDto> create(@RequestHeader("Authorization") String token, @RequestBody CreateFolderDto dto) {
        long userId = jwtTokenUtil.getTokenUserId(token);

        SuccessDto result = folderService.createFolder(userId, dto.name());
        return result.toResponseEntity();
    }

}