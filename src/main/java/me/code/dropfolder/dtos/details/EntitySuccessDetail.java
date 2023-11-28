package me.code.dropfolder.dtos.details;

import com.fasterxml.jackson.annotation.JsonProperty;
import me.code.dropfolder.models.File;
import me.code.dropfolder.models.Folder;
import me.code.dropfolder.models.User;

import java.util.*;

/**
 * Represents the detailed information of a successful operation involving different entity types.
 * Extends the SuccessDetail class.
 */
public class EntitySuccessDetail extends SuccessDetail {

    @JsonProperty("entityDetail")
    private Map<String, Object> entityDetail;

    /**
     * Constructs an EntitySuccessDetail with the specified entity and description.
     *
     * @param entity            The entity for which details are being generated.
     * @param entityDescription A custom description for the entity.
     * @throws IllegalArgumentException if the provided entity is null.
     */
    public EntitySuccessDetail(Object entity, String entityDescription) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        this.entityDetail = generateEntityDetail(entity, entityDescription);
    }

    /**
     * Generates detailed information about the entity based on its type.
     *
     * @param entity            The entity for which details are being generated.
     * @param entityDescription A custom description for the entity.
     * @return A Map containing detailed information about the entity.
     * @throws IllegalArgumentException if the entity type is not supported.
     */
    private Map<String, Object> generateEntityDetail(Object entity, String entityDescription) {
        Map<String, Object> entityInfo = new LinkedHashMap<>();
        entityInfo.put("description", entityDescription);

        if (entity instanceof User user) {
            putUserInfo(user, entityInfo);
        } else if (entity instanceof Folder folder) {
            putFolderInfo(folder, entityInfo);
        } else if (entity instanceof File file) {
            putFileInfo(file, entityInfo);
        } else {
            throw new IllegalArgumentException("Unsupported entity type: " + entity.getClass());
        }
        return entityInfo;
    }

    /**
     * Puts detailed information about a user into the provided Map.
     *
     * @param user The user for which details are being generated.
     * @param info The Map to which user details are added.
     */
    private void putUserInfo(User user, Map<String, Object> info) {
        info.put("id", user.getId());
        info.put("username", user.getUsername());
    }

    /**
     * Retrieves detailed information about a user and returns it as a Map.
     *
     * @param user The user for which details are being generated.
     * @return A Map containing detailed information about the user.
     */
    private Map<String, Object> getUserInfo(User user) {
        Map<String, Object> userInfo = new LinkedHashMap<>();
        putUserInfo(user, userInfo);
        return userInfo;
    }

    /**
     * Puts detailed information about a folder into the provided Map.
     *
     * @param folder The folder for which details are being generated.
     * @param info   The Map to which folder details are added.
     */
    private void putFolderInfo(Folder folder, Map<String, Object> info) {
        info.put("id", folder.getId());
        info.put("folderName", folder.getName());
        info.put("ownedByUser", getUserInfo(folder.getUser()));
    }

    /**
     * Retrieves detailed information about a folder and returns it as a Map.
     *
     * @param folder The folder for which details are being generated.
     * @return A Map containing detailed information about the folder.
     */
    private Map<String, Object> getFolderInfo(Folder folder) {
        Map<String, Object> folderInfo = new LinkedHashMap<>();
        putFolderInfo(folder, folderInfo);
        return folderInfo;
    }

    /**
     * Puts detailed information about a file into the provided Map.
     *
     * @param file The file for which details are being generated.
     * @param info The Map to which file details are added.
     */
    private void putFileInfo(File file, Map<String, Object> info) {
        info.put("fileId", file.getId());
        info.put("fileName", file.getName());
        info.put("belongsToFolder", getFolderInfo(file.getFolder()));
    }
}
