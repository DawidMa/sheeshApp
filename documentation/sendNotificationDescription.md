# SheeshApp
## Use-Case Specification: Send Notifications



## 1. Use-Case: Send Notifications

### 1.1 Brief Description

Users will be able to adjust the shisha tracker and specify such things like: subcsriber, invitations and add optional the smoking location. After that, if the user starts the tracker, all subcsribers and choosen friends will get a notification. 
*Subscriber gets a participation confirmation 
*Invited gets an invitation


## 2. Flow of Events

<img src="/documentation/sendNotificationDiagram.png" alt="sendNotification" width="700" />

### 2.1 Basic flow

In the best case, the user inserts only valid data. He sets the time for the timer, enters the subscribers (if he´s not going to smoke alone), enters his friends for invitations and adds his smoking location.
<img src="/screenshots/setTracker.jpeg" alt="friendlist" width="400" />


### 2.2 No subcsriber choosen

If the user chooses no subcsriber he will get a message, if he really wants to smoke alone. In this case the timer will be deactivated, but nevertheless his activities will be saved in his history.

### 2.3 No invitations

If the user chooses no invitations he will get a message if he really wants to invite nobody. In this case the notifications will be deactivated and no message will be sent, but nevertheless his activities will be saved in his history.

### 2.4 No location added 

If the user enters no location he get´s a message if he wants to leave this empty or not.

### 2.5 Timer settings invalid

The user can't continue if the time for the tracker isn´t valid.

### 2.6 All data invalid

If all inserted data are valid, the workflow can go on and the notifications for the subcsribers and the invitations will be sent and saved in the history.

## 3. Special Requirements

### 3.1 Existing friends 

In order to invite somebody, the user has to have friends in his friendslist.
<img src="/screenshots/chooseFriends.jpeg" alt="friendlist" width="400" />

## 4. Preconditions

The user has to be logged in.    

## 5. Postconditions

The user is able to follow his activities in his history.

## 6. Extension Points

**n / a**
