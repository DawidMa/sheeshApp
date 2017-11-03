# SheeshApp
## Use-Case Specification: Send Notifications



## 1. Use-Case: Send Notifications

### 1.1 Brief Description

Users will be able to adjust the shisha tracker and specify such things like: subcsriber, invitations and add optional the smoking location. If a user starts subsequently the tracker all subcsriber and choosen friends will get an notifications. *Subscriber get a participation confirmation *Invited get an invitation.


## 2. Flow of Events

<img src="/documentation/sendNotificationDiagram.png" alt="sendNotification" width="700" />

### 2.1 Basic flow

In the best case, the user inserts only valid data. He sets the time for the timer, enters the subscribers, if he´s not going to smoke alone, enters his friends for invitations and adds his smoking location.

### 2.2 No subcsriber choosen

If the user chooses no subcsriber he will get a message if he really wants to smoke alone, in this case the timer would be deactivated, but nevertheless his activities will be saved in his history.

### 2.3 No invitations

If the user chooses no invitations he will get a message if he really wants to invite nobody, in this case the notifications would be deactivated and no message will be sent, but nevertheless his activities will be saved in his history.

### 2.4 No location added 

If the user enter no location he get´s a message if he want to leave this empty or not.

### 2.5 Timer settings invalid

The user cant continue if the time for the tracker isn´t valid.

### 2.6 All data invalid

If all inserted data are valid the workflow can go on and the notification for the subcsribers and the invitation will be sent.

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
