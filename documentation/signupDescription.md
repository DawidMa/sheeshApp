# SheeshApp
## Use-Case Specification: Sign Up



## 1. Use-Case: Sign Up

### 1.1 Brief Description

Users will be able to create an account to save their profile online. Therefore, they have to sing up and enter some details.

## 2. Flow of Events

<img src="/documentation/signupDiagram.svg" alt="signup" width="700" />

### 2.1 Basic flow

Like in a typical Sign-Up process, the user has to enter his name, email and a password. This data will be validated and if okey, the user will get an email, which he has to read. By clicking on the activation-link, the user will be later able to log in.

### 2.2 Email address invalid

The value entered into email has to have a correct syntax, otherwise a message will tell him, that he has to reenter his address.

### 2.3 Email already in use.

If an account already exists with the given email address, a message will tell him and he has to enter another one.

### 2.4 The passwords do not match

If the entered passwords do not match, the user has to enter them again.

### 2.5 Missing activation

The user didn't activate his account with the link from the mail. He has to activate it first.

## 3. Special Requirements

### 3.1 Email address

In order to create an account, the user has to have a valid email address which has not been used before.

## 4. Preconditions

**n / a** 

## 5. Postconditions

### 5.1 Log in

After the successful sign up, the user will be able to log in with his credentials, as his data is added to the database.

## 6. Extension Points

**n / a**
