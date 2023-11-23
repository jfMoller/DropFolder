package me.code.dropfolder.controller;

import me.code.dropfolder.dto.SuccessDto;
import me.code.dropfolder.model.File;
import me.code.dropfolder.security.JwtTokenUtil;
import me.code.dropfolder.service.file.FileDownloadBuilder;
import me.code.dropfolder.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller class for handling file-related operations such as uploading, downloading, and deletion of files.
 * Provides endpoints under the "/api/file" path.
 */
@RestController
@RequestMapping("/api/file")
public class FileController {

    private final FileService fileService;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * Constructor for FileController.
     *
     * @param fileService  The file service to handle file-related business logic.
     * @param jwtTokenUtil The utility for handling JSON Web Tokens; used for user authentication.
     */
    @Autowired
    public FileController(FileService fileService, JwtTokenUtil jwtTokenUtil) {
        this.fileService = fileService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * Handles file upload to a specified folder.
     *
     * @param token    The authorization token containing the user's credentials.
     * @param folderId The ID of the target folder for the file upload.
     * @param file     The multipart file to be uploaded.
     * @return ResponseEntity containing the SuccessDto.
     */
    @PostMapping("/upload/{folderId}")
    public ResponseEntity<SuccessDto> upload(
            @RequestHeader("Authorization") String token,
            @PathVariable long folderId, @RequestParam("file") MultipartFile file) {
        long userId = jwtTokenUtil.getTokenUserId(token);

        SuccessDto result = fileService.upload(userId, folderId, file);
        return result.toResponseEntity();
    }

    /**
     * Handles file download from a specified folder.
     *
     * @param token    The authorization token containing the user's credentials.
     * @param folderId The ID of the target folder for the file download.
     * @param fileId   The ID of the file to be downloaded.
     * @return ResponseEntity containing the file content as a ByteArrayResource,
     * including the headers and content type.
     */
    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> download(
            @RequestHeader("Authorization") String token,
            @RequestParam long folderId,
            @RequestParam long fileId) {
        long userId = jwtTokenUtil.getTokenUserId(token);

        File requestedFile = fileService.fetchFileForDownload(userId, folderId, fileId);
        FileDownloadBuilder builder = new FileDownloadBuilder(requestedFile);

        return builder.buildResponseEntity();
    }

    /**
     * Handles file deletion from a specified folder.
     *
     * @param token    The authorization token containing the user's credentials.
     * @param folderId The ID of the target folder for the file deletion.
     * @param fileId   The ID of the file to be deleted.
     * @return ResponseEntity containing the SuccessDto.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<SuccessDto> delete(
            @RequestHeader("Authorization") String token,
            @RequestParam long folderId,
            @RequestParam long fileId) {
        long userId = jwtTokenUtil.getTokenUserId(token);

        SuccessDto result = fileService.delete(userId, folderId, fileId);
        return result.toResponseEntity();
    }

}