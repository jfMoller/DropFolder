Feature: File Upload
  As a user, I want to be able to upload a file

  @cleanupUploadData
  Scenario Outline: Successful file upload
    Given the user has an account with username "<username>" and password "<password>"
    And the user is logged in with username "<username>" and password "<password>"
    When the user uploads a file with name "<filename>"
    Then the file should be uploaded successfully

    Examples:
      | username  | password   | filename |
      | username1 | Password_1 | .txt     |
      | username2 | Password_2 | .docx    |
      | username3 | Password_3 | .pdf     |
      | username4 | Password_4 | .jpg     |
      | username5 | Password_5 | .png     |
