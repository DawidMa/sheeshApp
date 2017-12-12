# SheeshApp
## Use-Case Specification: Setup Tracker



## 1. Use-Case: Setup Tracker

### 1.1 Brief Description

Users will be able to see an shisha tracker tap. In this tap the user is able to adjust the shisha tracker and specify such things like:
subcsriber or duratation and later add optional the smoking location. After that the user can simple start the tracker or cancle the setup. 


## 2. Flow of Events

<img src="/documentation/UC_setupTracker.png" alt="sendNotification" width="700" />

### 2.1 Basic flow

In the best case, the user inserts only valid data. He sets the time for the timer and enters the subscribers (if he´s not going to smoke alone). Finally he press the submit button and the 
tracker is going to start. 

<img src="/screenshots/setTracker.jpeg" alt="friendlist" width="400" />


### 2.2 No subcsriber chosen

If the user chooses no subcsriber he will get a message, if he really wants to smoke alone. In this case the timer will be deactivated, but nevertheless his activities will be saved in his history.

### 2.3 Timer settings invalid

The user can't continue if the time for the tracker isn´t valid.

### 2.4 All data valid

If all inserted data are valid, the workflow can go on.

### 2.5 Finish the tracker

If the user ends the tracker all his shisha activities and data will be safed in his hirtory

## 3. Special Requirements

### 3.1 Existing friends 

In order to enter somebody as subscriber, the user has to have friends in his friendslist.

<img src="/screenshots/chooseFriends.jpeg" alt="friendlist" width="400" />

## 4. Preconditions

The user has to be logged in.    

## 5. Postconditions

The user is able to follow his activities in his history.

## 6. Extension Points

**n / a**
