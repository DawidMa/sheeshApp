# SheeshApp
## Use-Case Specification: Popup Notifications



## 1. Use-Case: 

### 1.1 Brief Description

Users will be able see the most important information about the shisha tracker, as an popup notification in the scrolldown Menu at their smartphones and also at the lookscreen. 
Information like:
* time left
* who´s turn 
* ...


## 2. Flow of Events

<img src="/documentation/UC_popupNotification.png" alt="PopUp Notification" width="700" />

### 2.1 Basic flow

In the best case, the shisha tracker runs without problems. If the user looks his smartphone this will trigger the notification thread.
This thread collects all his needed information and displays it as an popup at the lookscreen. Same works if the user starts a other app. After the user returns to the shisha tracker, the notification popup will be disabeld and the thread stops. 

Lockscreen:
<img src="/screenshots/UC_popupNotificationLock.jpeg" alt="Pop Up Notification Lockscreen" width="400" />

Scroll down menu:
<img src="/screenshots/UC_popUpNotificationScrolldown.jpeg" alt="Pop Up Notification scrolldown menu" width="400" />


### 2.1 Open an other app

If the user start´s an other app also the basic flow will occur. 

### 2.3 Close tracker

If the user closes the shisha tracker, the notification pop up wont be triggered. 

## 4. Preconditions

The user must have started the tracker, to trigger the notification pop up.

## 5. Extension Points
