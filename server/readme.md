<!-- # Analysis & Development Project - Mars 2052 - server project

This is the **server side start-project** for Project II. 

This start project provides the basic scaffolding for an openapi webserver and an example bridge class for websockets.

There is already a fully working minimal example api with all the necessary classes.

Example classes (except WebServer.java) are allowed to be modified or deleted.

## Before you start:
- Choose Zulu jdk version 11 or opendjk 11 (Configure through this through intelij)
- Make sure to clone **all** the repositories **client**, **server** & **documentation**
    - **Use the following folder structure**
        - root_folder_with_name_of_choice
            - client
            - documentation
            - server

## Local testing and quality checks
You can **run** the Sonar validator and code coverage **locally!**

There is no need to push to the server to check if you are compliant with our rules.
In the interest of sparing the server, please result to local testing as often as possible.

**If everyone pushes to test, the remote will not last.**

Use the sonarlint plugin to see any code smells.
  - In the sonarlint plugin.
  - Open the report tab
  - Click on the Analyze all project files button. (left side)

## Configuring properties
All properties for a local setup are located in **conf/config.json**.

The remote properties are located on the remote server.

Add properties to conf/config.json are not automatically pushed to the remote server.

Adding new properties to the local config file is perfectly fine.

However, to apply new properties or property modifications on the server please contact mr. Blomme on MS Teams. With the following data:

  - valid config file in json format with filename config-group-XX.

Please, test the config file thoroughly on your local machine as mistakes will not be fixed every day.

## What's included
  - A very basic openapi specification
    - localhost:8080/api/quotes
  - H2 database web console
  - The setup of a vert.x and openapi (WebServer.java)
  - Minimal H2 repository class
  - A starter class for the RTC topic (MarsRtcBridge.java)
  - Database generation scripts

## How to run the start project locally
In Intelij choose gradle task **run**.

## Location OpenApi Specification
The location of the openapi specification is defined in the file **config**.

The property is called **api.url**.

By default, the local setup will pick the openapi specification located at https://project-ii.ti.howest.be/monitor/apis/group-02.

If for some reason, the api isn't available or you want to use the specification in your **local** documentation folder.
```json
"api": {
"url": "../documentation/api-spec/openapi-mars.yaml"
}
```
 - For the to work, the folder structure must be organised as describe above.

## Local endpoints
 - H2 web client
   - localhost:9000
   - url: jdbc:h2:./db-02
   - no credentials
 - Web api
   - localhost:8080/api/quotes
 - Web client
   - launch through webstorm/phpstorm (see client-side readme)
  
## Production endpoints
 - H2 web client
   - https://project-ii.ti.howest.be/db-02
   - url: jdbc:h2:./db-02
   - username:group-02
   - password:see Leho for details.
 - Useful information
   - Server logs
     - https://project-ii.ti.howest.be/monitor/logs/group-02
   - Swagger Interface
     - https://project-ii.ti.howest.be/monitor/swagger/group-02
     - Through this GUI remote & local API testing is possible!
   - Overview of all Mars API's
     - https://project-ii.ti.howest.be/monitor/overview/
     - Please complete the openapi.yaml file to contribute useful information to the overview page.
 - Web client project
   - https://project-ii.ti.howest.be/mars-02
 - Sonar
   - https://sonar.ti.howest.be/dashboard?id=2022.project-ii%3Amars-server-02
   - https://sonar.ti.howest.be/dashboard?id=2022.project-ii%3Amars-client-02
   - Sonarlint login token: 86dd00e5e50846f9284825fd0bf95cf6bcb28a15

## Keep the database up to date
There is no need to manually add entries into the database.

Please use the scripts: **db-create** and **db-populate** in the resource folder.

Everytime you run the api, the database will be rebuilt to the state described in db-create and db-populate scripts.

The **db-create** script is responsible for the database structure (tables, primary keys, ...)

The **db-populate** script is responsible for populating the database with useful data.

## Adding/updating an openapi endpoint.
   1. Update the openapi specification in the documentation repo.
      2. Commit and push the results.
   2. Update the function **buildRouter** in the class **MarsOpenApiBridge**
      1. Map the operationId (openapi id) to a new function in the class **MarsOpenApiBridge**
      1. Create this new function in the **MarsOpenApiBridge**
   2. (Optional) Use the Request class to get the data from the ctx parameter. 
   3. Add the wanted functionality to the controller layer and the layers below.
   4. Add a new response function in the **Response** class if needed.
   6. Write unit tests -->

# Events United Server

![Logo Events United](images/Events_United_logo.png "Logo Events United")

This is the server-side start project for the Analysis and Development project.

![badge](https://sonar.ti.howest.be/badges/project_badges/measure?project=2022.project-ii:mars-server-02&metric=bugs)
![badge](https://sonar.ti.howest.be/badges/project_badges/measure?project=2022.project-ii:mars-server-02&metric=code_smells)
![badge](https://sonar.ti.howest.be/badges/project_badges/measure?project=2022.project-ii:mars-server-02&metric=coverage)
![badge](https://sonar.ti.howest.be/badges/project_badges/measure?project=2022.project-ii:mars-server-02&metric=vulnerabilities)
![badge](https://sonar.ti.howest.be/badges/project_badges/measure?project=2022.project-ii:mars-server-02&metric=duplicated_lines_density)

# Table Of Contents

- [Events United Server](#events-united-server)
- [Table Of Contents](#table-of-contents)
  - [💡 Getting started](#-getting-started)
    - [Clone the repository](#clone-the-repository)
    - [CLI](#cli)
    - [GUI](#gui)
    - [Start the application](#start-the-application)
  - [🌐 Technologies used](#-technologies-used)
  - [📂 Folder structure](#-folder-structure)
  - [🔗 Links](#-links)
  - [🎉 Contributors](#-contributors)
  - [📨 Contact](#-contact)

## 💡 Getting started

To get started with the back-end, you'll need to clone this repository and install the necessary dependencies. Here's how to do that:

### Clone the repository

```bash
git clone git@git.ti.howest.be:TI/2022-2023/s3/analysis-and-development-project/server/group-02/client.git
```

  ***❓Cloning using HTTPS will not work because our repository is private and only accessible using ssh keys authorized by gitlab of Howest.❓***

*Now you can work with CLI or GUI*

### CLI

- navigate to the cloned repository using `cd server`.
  - If you work with **Visual Studio Code** you can type `code .`
  - If you work with **IntelliJ IDEA** you can type `idea.cmd .`
    - **❓ The `.` at the end of the command references to the current folder which is `server`. ❓**
  - If you don't want to use any IDE, you can also run a Gradle project in your terminal.

### GUI

- If you work with **Visual Studio Code**
    1. Open VSCode
    2. Press `Ctrl+k, Ctrl+o` to open a folder **OR** click `File` on the top left corner and select `Open Folder`
    3. Navigate to the cloned repository
    4. Open it
- If you work with **IntelliJ IDEA**
    1. Open IntelliJ IDEA Ultimate/Community
    2. Click on `File` on the top left corner and select `Open Folder`
    3. Navigate to the cloned repository
    4. Open it

### Start the application

- If you work with **Visual Studio Code**
  - [Run Gradle project using vscode](markdown/starting-vscode.md)
- If you work with **IntelliJ IDEA**
  - [Run Gradle project using IntelliJ IDEA](markdown/starting-intellij.md)
- If you don't use any IDE, do it using the **terminal**
  - [Run Gradle project using the terminal](markdown/starting-terminal.md)

## 🌐 Technologies used

This project was built using the following technologies:

- [![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/en/)
- [![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)](https://www.gradle.org)
- [![Eclipse Vert.x](https://a11ybadges.com/badge?logo=eclipsevertdotx)](https://vertx.io)
- [![h2](https://img.shields.io/badge/maven--repo-h2-blue)](https://mvnrepository.com/artifact/com.h2database/h2)

## 📂 Folder structure

```md
├── .gradle/
├── .idea/
├── bin/
├── build/
├── conf/
├── gen/
├── gradle/
├── images/
├── markdown/
├── src/
│   ├── main/
|   |     ├── java/
|   |     |     └─ be
|   |     |         └─ howest
|   |     |               └─ ti
|   |     |                   └─ mars
|   |     |                        ├── logic
|   |     |                        |     ├── controller
|   |     |                        |     ├── data
|   |     |                        |     ├── domain
|   |     |                        |     └── exceptions
|   |     |                        └── web
|   |     |                              ├── bridge
|   |     |                              ├── exceptions
|   |     |                              └── Webserver.java
|   |     └── resources/
|   |             ├── db-create.sql
|   |             ├── dp-populate.sql
|   |             └── vertx-default-jul-logging.properties
│   └── test/
|         ├── java/
|         |     └─ be
|         |         └─ howest
|         |               └─ ti
|         |                   └─ mars
|         |                        ├── logic
|         |                        |     ├── controller
|         |                        |     └── data
|         |                        └── web
|         |                             └── OpenAPITest.java
|         └── resources/
|                 ├── db-create.sql
|                 └── dp-populate.sql
├── .gitignore
├── .gitlab-ci.yml
├── **build.gradle**
├── db-02.mv.db
├── db-02.trace.db
├── gradle.properties
├── gradlew
├── gradle.bat
├── package-lock.json
├── package.json
├── README.md
└── setting.gradle
```

The images folder contains all the images used in the **README.md** file. So it is not the images for the application.

The src folder contains the source code for the project, including the Java classes, controller, repositories, etc...

## 🔗 Links

💻 [Click here to go to the app!](https://project-ii.ti.howest.be/mars-02/)

📃 [client README](https://git.ti.howest.be/TI/2022-2023/s3/analysis-and-development-project/projects/group-02/client/-/blob/main/readme.md)

- Sonar reports: <https://sonar.ti.howest.be/dashboard?id=2022.project-ii%3Amars-server-02>

## 🎉 Contributors

The following individuals are the owners and maintainers of this project:

- [**Ali Mola**](https://git.ti.howest.be/ali.mola)
- [**Jara Puype**](https://git.ti.howest.be/jara.puype)
- [**Lukas Olivier**](https://git.ti.howest.be/lukas.olivier)
- [**Niels Soete**](https://git.ti.howest.be/niels.soete)
- [**Sam Roovers**](https://git.ti.howest.be/sam.roovers)

## 📨 Contact

If you have any questions or would like to get in touch with the team, please feel free to contact us at info.eventsunited@gmail.com or through our contact form on the following link: [contact form](https://tihowest.wixsite.com/project/contact)
