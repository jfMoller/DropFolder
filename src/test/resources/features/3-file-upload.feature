Feature: File Upload
  As a user, I want to be able to upload a file

  @setupUploadData
    @cleanupUploadData
  Scenario Outline: Successful file upload
    Given the user has a folder with name "<foldername>"
    When the user uploads a file with name "<filename>" into their folder
    Then the file should be uploaded successfully in the users folder

    Examples:
      | filename  | foldername |
      | mock.txt  | my_folder1 |
      | mock.docx | my_folder2 |
      | mock.pdf  | my_folder3 |
      | mock.jpg  | my_folder4 |
      | mock.png  | my_folder5 |

  @setupUploadData
    @cleanupUploadData
  Scenario Outline: Unsuccessful file upload (user does not own folder)
    Given the user does not own a folder with name "<foldername>"
    Then the upload should fail if a user tries to upload a file with name "<filename>" into a folder that they do not own

    Examples:
      | filename  | foldername |
      | mock.txt  | my_folder1 |
      | mock.docx | my_folder2 |
      | mock.pdf  | my_folder3 |
      | mock.jpg  | my_folder4 |
      | mock.png  | my_folder5 |

  @setupUploadData
    @cleanupUploadData
  Scenario Outline: Unsuccessful file upload (non-existing folder)
    Given there is no existing folder with id "<invalid_folder_id>"
    Then the upload should fail if a user tries to upload a file with name "<filename>" into a non-existing folder with id "<invalid_folder_id>"

    Examples:
      | filename  | invalid_folder_id |
      | mock.txt  | -1                |
      | mock.docx | -2                |
      | mock.pdf  | -3                |
      | mock.jpg  | -4                |
      | mock.png  | -5                |
