# SixtySecondsRegistration

## Project Overview
This Java-based automation script, "SixtySecondsRegistration," automates the registration process for the "Sixty Seconds Spezial" competition hosted by the German Radio Station "Rockland Radio." The script, built with Playwright, interacts with web elements on the competition's webpage and fills the registration form using data from a JSON file.

## Prerequisites
To run this project, you need:
- Java JDK 21
- Maven
- Playwright's Java package

Ensure that all dependencies are installed and properly configured in your development environment.

## Installation
1. Clone the repository:
   ``` bash
   git clone [repository-url]
   ```
2. Navigate to the project directory and run Maven install to set up the project dependencies:
   ``` bash
   cd SixtySecondsRegistration
   mvn install
   ```

## Configuration
- Update the `src/main/resources/data.json` file with your details. The current file contains fake data and needs to be replaced with actual information.

## Usage
To execute the script, run the following command in the project's root directory:
``` bash
java -classpath target/classes dev.christianbaumann.SixtySecondsRegistration
```

## Acknowledgments
- Rockland Radio for hosting the "Sixty Seconds Spezial" competition
- Playwright community for providing the automation framework
