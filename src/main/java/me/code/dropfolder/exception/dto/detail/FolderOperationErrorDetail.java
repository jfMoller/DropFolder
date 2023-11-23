package me.code.dropfolder.exception.dto.detail;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@Getter
@Data
@EqualsAndHashCode(callSuper = false)
public class FolderOperationErrorDetail extends ErrorDetail {

    private String rejectedFolderName;

    private String message;

    public FolderOperationErrorDetail(String rejectedFolderName, String message) {
        this.rejectedFolderName = rejectedFolderName;
        this.message = message;
    }
}
