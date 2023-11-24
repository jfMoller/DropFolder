package me.code.dropfolder.util;

import me.code.dropfolder.model.File;
import me.code.dropfolder.model.Folder;
import me.code.dropfolder.model.User;
import org.springframework.stereotype.Component;

@Component
public class UniqueNameGeneratorUtil {

    private final JpQueryUtil query;

    public UniqueNameGeneratorUtil(JpQueryUtil query) {
        this.query = query;
    }

    public void setUniqueFileName(File file) {
        String attachedFileName = file.getName();
        Folder folder = file.getFolder();
        String uniqueFileName = getUniqueFileName(folder, attachedFileName);
        file.setName(uniqueFileName);
    }

    private String getUniqueFileName(Folder folder, String fileName) {
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

    public String getUniqueFolderName(User user, String name) {
        String uniqueName = name;
        int count = 2;

        while (query.userHasExistingFolderByName(user, uniqueName)) {
            uniqueName = name + "_" + count;
            count++;
        }
        return uniqueName;
    }

}
