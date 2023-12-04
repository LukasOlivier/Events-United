# Cloning and Starting the Project

To clone and start this Gradle project using Visual Studio Code, follow these steps:

1. Open Visual Studio Code.
2. Click on the "File" menu and select "Open Folder" **OR** press `Ctrl+k, Ctrl+o`.
3. Navigate to the directory where you cloned the project and select it.
4. To open a new terminal window, you can either click on the "Terminal" menu and select "New Terminal", or you can use the keyboard shortcut `Ctrl+Shift+<backtick>` (Windows and Linux) or `Cmd+Shift+<backtick>` (macOS)

To start this Gradle project using vscode, follow these steps:

1. Change into the project directory by running the following command:

```bash
cd server
```

2. To build the project, run the following command:

```bash
./gradlew build
```

3. To start the project, run the following command:

```bash
./gradlew run
```

❓This will start the project using the default configuration. If you need to customize the configuration, you can edit the build.gradle file and specify the desired options.❓

To see a list of available tasks, you can run the following command:

```bash
./gradlew tasks
```
