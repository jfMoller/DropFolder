Feature: File Deletion
  As a user, I want to be able to delete my uploaded files

  @cleanupDeletionData
  Scenario Outline: Successful file deletion
    Given the user already has an account with username "<username>" and password "<password>"
    Given the user already has a folder with name "<foldername>"
    Given the user already has a file with name "<filename>" in their folder with name "<foldername>"
    And the user is already logged in with username "<username>" and password "<password>"
    When the user deletes the file with name "<filename>"
    Then the file should be successfully deleted from the users folder

    Examples:
      | username  | password   | filename  | foldername |
      | username1 | Password_1 | mock.txt  | my_folder  |
      | username2 | Password_2 | mock.docx | my_folder  |
      | username3 | Password_3 | mock.pdf  | my_folder  |
      | username4 | Password_4 | mock.jpg  | my_folder  |
      | username5 | Password_5 | mock.png  | my_folder  |