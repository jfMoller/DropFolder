Feature: File Upload
  As a user, I want to be able to upload a file

  @cleanupUploadData
  Scenario Outline: Successful file upload
    Given the user has an account with username "<username>" and password "<password>"
    Given the user has a folder with name "<foldername>"
    And the user is logged in with username "<username>" and password "<password>"
    When the user uploads a file with name "<filename>" into her folder with name "<foldername>"
    Then the file should be uploaded successfully in the users folder

    Examples:
      | username  | password   | filename  | foldername |
      | username1 | Password_1 | mock.txt  | my_folder  |
      | username2 | Password_2 | mock.docx | my_folder  |
      | username3 | Password_3 | mock.pdf  | my_folder  |
      | username4 | Password_4 | mock.jpg  | my_folder  |
      | username5 | Password_5 | mock.png  | my_folder  |
