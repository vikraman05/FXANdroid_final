# SE750-PortableCloud-Project

## Prerequisites

1. Android Studio
2. Java 1.7+
3. Android phone or emulator 

## Installation

1. Open the project in Android Studio
2. Run the app module on an emulator, or a physical device whilst it is connected to your computer
3. Play around, find bugs, share your thoughts


## Project

The goal of the project was to create an Android client for the PortableCloud service. This would be done by using Fejoa, a codebase written by Clemens.
This service allows users to have accounts and a place to store their files, They would be able to message detailses and also migrate between servers.
Our original scope involved implementing account registration, logging in, migrating between servers and messaging between detailses using Fejoa.
However, due to the issues with certain aspects of Fejoa's incompatibility with Android and the time constraints we weren't able to achieve this.
We met up with Clemens and discussed with him the problem, and he said that implementing sign up and account creation would suffice for our scope.
The rest of our application's features are click dummies.

No server features are currently implemented, so don't worry about running the server.

## How to use

### Registration

* Uses Fejoa to create a local account on your device (phone or emulator) and proceeds to the home screen if successful. 
* Note this process can take upto 5 minutes depending on the speed of your device. This is a limitation of the logic in Fejoa. 

### Login

* Attempt to login using the same credentials made during registration
* Note this process often fails as we were unable to fully implement the login feature

### HomeScreen features

* All features past the login/registration screen are mockups/click dummies.
* Migration, messaging, adding detailses and account tabs all do not persist data on your device for later use. This means any changes you make when running the application will not show in subsequent runs of the application.

## Issues

* Integration issues with Fejoa and Anroid
    * Changing from Maven to Gradle
* The cryptography class of Fejoa used SHA512 encryption, something that wasn't supported by Android
    * We had to change every instance of "SHA512" to "SHA1"
* Apache Commons Code has encode() and decode() methods used for Base64 encryption
    * Android will always use the in-built version of these methods, even though we added an external library with updated methods
    * These Fejoa libraries conflicted with the in-built version and we had to change every instance to use the library we wanted
* Client.create() seemed to work but no files were created upon completion of this method
    