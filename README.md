### About this project
A graded assignment focused on building a file storage API; the project was built using Java Spring and Postgres, and includes Spring Security as well as Cucumber integration testing.

### How to run locally
1. **Download the Project:**
    - Unzip and run it in your IDE.


2. **Configure Database Connection:**
    - Add the provided `spring.datasource.url` to the `application.properties` file.


3. **Run the Application:**
    - Execute the `Application.java` file to start the application.

### How to run Cucumber tests
1. **Database Setup:**
    - Ensure the database connection is properly configured.


2. **Run Cucumber Tests:**
    - Execute the `CucumberTestRunner.java` file for running integration tests.

### API Features

**User registration**
```
Method: POST
Endpoint: http://localhost:8080/api/products/all
Body (JSON): {"username": "example_username", "password": "example_password"}
```
**User login**
```
Method: POST
Endpoint: http://localhost:8080/api/login
Body (JSON): {"username": "example_username", "password": "example_password"}
```
**Create a folder**
```
Method: POST
Endpoint: http://localhost:8080/api/folder/create
Body (JSON): {"name": "folder"}
Headers: {key: Authorization, value: your_jwt_token}
```

**Upload a file to a specific folder**
```
Method: POST
Endpoint: http://localhost:8080/api/file/upload/{your_folder_id}
Body (form-data): {key: file, value: a file of your choice, e.g flower.png}
Headers: {key: Authorization, value: your_jwt_token}
```

**Download a file**
```
Method: GET
Endpoint: http://localhost:8080/api/file/download?folderId={your_folder_id}&fileId={your_file_id}
Params: [{key: folderId, value: your_folder_id}, {key: fileId, value: your_file_id}]
Headers: {key: Authorization, value: your_jwt_token}
```

**Delete a file**
```
Method: DELETE
Endpoint: http://localhost:8080/api/file/delete?folderId={your_folder_id}&fileId={your_file_id}
Params: [{key: folderId, value: your_folder_id}, {key: fileId, value: your_file_id}]
Headers: {key: Authorization, value: your_jwt_token}
```
    