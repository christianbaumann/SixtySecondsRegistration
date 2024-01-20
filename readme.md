# SixtySecondsRegistration

[![Java CI with Maven](https://github.com/christianbaumann/SixtySecondsRegistration/actions/workflows/maven.yml/badge.svg)](https://github.com/christianbaumann/SixtySecondsRegistration/actions/workflows/maven.yml)

## Project Overview

`SixtySecondsRegistration` is a Java-based automation script for registering to the "Sixty Seconds Spezial" competition
hosted by Rockland Radio. Utilizing the Playwright framework, it automates the process of filling out and submitting the
registration form, driven by user data and configuration settings defined in JSON files.

## Prerequisites

To run this project, the following are required:

- Java JDK 21 or higher
- Maven for managing dependencies
- Playwright's Java package for web automation

## Installation

1. Clone the repository:
   ``` bash
   git clone https://github.com/christianbaumann/SixtySecondsRegistration
   ``` 
2. Navigate to the project directory and run Maven install to set up the project dependencies:
   ``` bash
   cd SixtySecondsRegistration
   mvn install
   ```

## Configuration

- The `data.json` file in `src/main/resources` contains user data for the registration form. It currently holds
  placeholder data and should be updated with actual information.
- The `config.json` file in `src/main/resources` holds configuration settings for the script, such as the URL and
  browser options. Adjust as needed.

## Usage

Run the script using the following command in the project's root directory:

``` bash
java -classpath target/classes dev.christianbaumann.SixtySecondsRegistration
```

Ensure that the JSON files are properly configured before running the script.
