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