Feature: File Deletion
  As a user, I want to be able to delete my uploaded files

  @setupDeletionData
    @cleanupDeletionData
  Scenario Outline: Successful file deletion
    Given the user owns a folder containing a file named "<filename>"
    When the user deletes the file with name "<filename>"
    Then the file should be successfully deleted from the user's folder

    Examples:
      | filename  |
      | mock.txt  |
      | mock.docx |
      | mock.pdf  |
      | mock.jpg  |
      | mock.png  |

  @setupDeletionData
    @cleanupDeletionData
  Scenario Outline: Unsuccessful file deletion (user does not own the file)
    Given the user does not own a folder named "<foldername>"
    Then the user does not own an uploaded file named "<filename>" in the folder
    Then the deletion should fail if the user tries to delete the file named "<filename>" in the folder that they do not own

    Examples:
      | filename | foldername |
      | mock.txt | my_folder1 |

  @setupDeletionData
    @cleanupDeletionData
  Scenario Outline: Unsuccessful file deletion (the file does not exist)
    Given the user owns a folder named "<foldername>"
    Then the user does not have a file with id "<invalid_file_id>" in the folder
    Then the deletion should fail if the user tries to delete the file with id "<invalid_file_id>" in the folder that they own

    Examples:
      | invalid_file_id | foldername |
      | -1              | my_folder1 |