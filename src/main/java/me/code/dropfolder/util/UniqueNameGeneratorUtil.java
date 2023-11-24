package me.code.dropfolder.util;

import me.code.dropfolder.model.File;
import me.code.dropfolder.model.Folder;
import me.code.dropfolder.model.User;
import org.springframework.stereotype.Component;

/**
 * Utility class for generating unique names for files and folders.
 * <p>
 * This class provides methods to set unique names for files and folders.
 */
@Component
public class UniqueNameGeneratorUtil {

    private final JpQueryUtil query;

    /**
     * Constructs a new UniqueNameGeneratorUtil with the given query utility.
     *
     * @param query The JpQueryUtil used for database queries related to file and folder names.
     */
    public UniqueNameGeneratorUtil(JpQueryUtil query) {
        this.query = query;
    }

    /**
     * Sets a unique name for the provided file based on its current name.
     *
     * @param file The file for which to set a unique name.
     */
    public void setUniqueFileName(File file) {
        String attachedFileName = file.getName();
        Folder folder = file.getFolder();
        String uniqueFileName = generateUniqueFileName(folder, attachedFileName);
        file.setName(uniqueFileName);
    }

    /**
     * Generates a unique file name within the specified folder based on the provided file name.
     * <p>
     * The method appends a numeric suffix to the file name until a unique name is found within the folder.
     *
     * @param folder   The folder in which the uniqueness of the file name is checked.
     * @param fileName The original file name to be made unique.
     * @return A unique file name within the folder.
     */
    private String generateUniqueFileName(Folder folder, String fileName) {
        String uniqueFileName = fileName;
        int count = 2;

        int dotIndex = fileName.indexOf(".");
        String fileNameWithoutFileExtension = fileName.substring(0, dotIndex);
        String fileExtension = fileName.substring(dotIndex);

        while (query.folderHasExistingFileByName(folder, uniqueFileName)) {
            uniqueFileName = fileNameWithoutFileExtension + "_" + count + fileExtension;
            count++;
        }
        return uniqueFileName;
    }

    /**
     * Sets a unique name for the provided folder based on its current name.
     *
     * @param user   The user who owns the folder.
     * @param folder The folder for which to set a unique name.
     */
    public void setUniqueFolderName(User user, Folder folder) {
        String folderName = folder.getName();
        String uniqueFolderName = generateUniqueFolderName(user, folderName);
        folder.setName(uniqueFolderName);
    }

    /**
     * Generates a unique folder name within the specified user's folders based on the provided folder name.
     * <p>
     * The method appends a numeric suffix to the folder name until a unique name is found within the user's folders.
     *
     * @param user       The user for whom the uniqueness of the folder name is checked.
     * @param folderName The original folder name to be made unique.
     * @return A unique folder name within the user's folders.
     */
    private String generateUniqueFolderName(User user, String folderName) {
        String uniqueFolderName = folderName;
        int count = 2;

        while (query.userHasExistingFolderByName(user, uniqueFolderName)) {
            uniqueFolderName = folderName + "_" + count;
            count++;
        }
        return uniqueFolderName;
    }

}
