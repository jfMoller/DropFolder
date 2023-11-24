package me.code.dropfolder.service.file;

import me.code.dropfolder.dto.SuccessDto;
import me.code.dropfolder.exception.dto.detail.FileOperationErrorDetail;
import me.code.dropfolder.exception.dto.detail.FileUploadErrorDetail;
import me.code.dropfolder.exception.type.*;
import me.code.dropfolder.model.File;
import me.code.dropfolder.model.Folder;
import me.code.dropfolder.model.User;
import me.code.dropfolder.repository.FileRepository;
import me.code.dropfolder.util.JpQueryUtil;
import me.code.dropfolder.util.UniqueNameGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final JpQueryUtil query;
    private final UniqueNameGeneratorUtil nameGenerator;

    @Autowired
    public FileService(FileRepository fileRepository, JpQueryUtil jpQueryUtil, UniqueNameGeneratorUtil nameGenerator) {
        this.fileRepository = fileRepository;
        this.query = jpQueryUtil;
        this.nameGenerator = nameGenerator;
    }

    public SuccessDto upload(long userId, long folderId, MultipartFile attachedFile) {
        try {
            User requestingUser = query.loadUserById(userId);
            Folder targetFolder = query.loadFolderById(folderId);
            long targetFolderId = targetFolder.getId();

            if (query.isUserOwnerOfTargetFolder(requestingUser, targetFolderId)) {
                File file = new File(attachedFile, targetFolder);
                nameGenerator.setUniqueFileName(file);

                fileRepository.save(file);

                return new SuccessDto(
                        HttpStatus.CREATED,
                        "Successfully uploaded a new file with name: " + file.getName());
            } else throw new CouldNotFindFolderException(
                    "Could not find folder with id: {" + folderId + "} owned by user with id: {" + userId + "}");

        } catch (Exception exception) {
            throw new FileUploadFailureException("Failed to upload file",
                    new FileUploadErrorDetail(attachedFile, exception));
        }
    }

    public File fetchFileForDownload(long userId, long folderId, long fileId) {
        try {
            User requestingUser = query.loadUserById(userId);
            Folder targetFolder = query.loadFolderById(folderId);
            long targetFolderId = targetFolder.getId();

            if (query.isUserOwnerOfTargetFolder(requestingUser, targetFolderId) &&
                    query.isFilePartOfTargetFolder(fileId, targetFolder)) {
                return query.loadFileById(fileId);

            } else throw new CouldNotFindFileException(
                    "Could not find file with id: {" + fileId + "} in folder with id: {" + folderId + "}" +
                            " owned by user with id: {" + userId + "}");

        } catch (Exception exception) {
            throw new FileDownloadFailureException("Failed to download file",
                    new FileOperationErrorDetail(exception.getMessage()));
        }
    }

    public SuccessDto delete(long userId, long folderId, long fileId) {
        try {
            User requestingUser = query.loadUserById(userId);
            Folder targetFolder = query.loadFolderById(folderId);
            long targetFolderId = targetFolder.getId();

            if (query.isUserOwnerOfTargetFolder(requestingUser, targetFolderId) &&
                    query.isFilePartOfTargetFolder(fileId, targetFolder)) {
                fileRepository.deleteById(fileId);

                return new SuccessDto(
                        HttpStatus.OK,
                        "Successfully deleted a file with id: {" + fileId + "}");
            } else throw new CouldNotFindFileException(
                    "Could not find file with id: {" + fileId + "} in folder with id: {" + folderId + "}" +
                            " owned by user with id: {" + userId + "}");

        } catch (Exception exception) {
            throw new FileDownloadFailureException("Failed to delete file",
                    new FileOperationErrorDetail(exception.getMessage()));
        }
    }

}
