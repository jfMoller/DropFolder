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
      | mock.txt  | my_folder1  |
      | mock.docx | my_folder2  |
      | mock.pdf  | my_folder3  |
      | mock.jpg  | my_folder4  |
      | mock.png  | my_folder5  |
