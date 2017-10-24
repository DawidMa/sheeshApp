## 0.1 **1. Introduction**

### 0.1.1 **1.1 Purpose**

This SRS describes all specifications for &quot;SheeshApp&quot;. Our project is an android application with a customized graphical user interface. &quot;SheeshApp&quot; allows users to solve the problems, which are coming up, when you smoke shisha together with your friends. In this document the usage of the &quot;SheeshApp&quot;-Android-App will be explained. Furthermore reliability, the shisha tracker and other important characteristics of this project will be specified. This includes design and architectural decisions.

### 0.1.2 **1.2 Scope**

This software specification applies to the whole &quot;SheeshApp&quot; application. The application consists of two parts: The first part is an Android-App, which allows users to log-In with their own account, add friends/create groups, use the shisha rookie guide, take a look at their own shisha behavior history and use the main part of the app which is represented by the shisha timer. The second part is a company sided couponing, where they can insert specific benefits for the customers and also add their advertisement.

### 0.1.3 **1.3 Definitions, Acronyms and Abbreviations**

In this section definitions and explanations of acronyms and abbreviations are listed to help the reader to understand these.

**Android**  This is a mobile operating system developed by Google for primarily use on smartphones and tablets.

**UC**  Use Case

**MTTR** Mean time to repair

**App** Application

**UCD**  Use Case Diagram

**OUCD**  Overall Use Case Diagram

**SAD**  Software Architecture Document

**RFC**  Request for Comments

**POPS** People respect other people seriously

**MVC** Model-View-Controller





### 0.1.4 **1.4 Overview**

The following chapters are about our vision and perspective, the software requirements, the demands we have, licensing and the technical realization of this project.

## 0.2 **2. Overall Description**

### 0.2.1 **2.1 Vision**

Out there are many shisha-smokers and almost everyone have had problems to schedule the order or also the time which everyone has left for smoking. Our app will be the solution for this problem and also for even more. Problems like how to start smoking shisha for beginners, tips &amp; tricks if you smoke in the private, how u can save some money and analyze your shisha behavior to be reasonable with shisha dealings.

### 0.2.2 **2.2 OUCD**

The following picture shows the overall use case diagram of our software:

## 0.3 **3. Specific Requirements**

### 0.3.1 **3.1 Functionality - Android App**

#### 0.3.1.1 **3.1.1 Create an account**

An account includes all the personal information of the user and gives him the possibility to use for example the social functions and the coupon feature.
Users without an account have only the possibility to use a lite version of our App.

#### 0.3.1.2 **3.1.2 Add friends/groups**

On this tab the user is able to add friends and create groups. He can also define which groups and friend will get a notification, when the user starts a shisha tracker.

#### 0.3.1.3 **3.1.3 Coupon**

Under the coupon tab the user is able to use coupons. The coupons themselves and their benefits are incumbent upon to the shisha bar owner. With the coupons, the user can save some money if he meets all the requirements.

#### 0.3.1.4 **3.1.4 Shisha tracker**

The user is able to use a timer which schedules the amount of time which every smoker has left to smoke. There is also a function which selects the person, who has to turn/refresh the coal.

#### 0.3.1.5 **3.1.5 Notifications**

The user has the possibility to use notifications, which alerts his chosen friends where and when he will be smoking, so that they can join him. Notifications are triggered by starting the shisha tracker.

#### 0.3.1.6 **3.1.6 Tutorials**

#### 0.3.1.7In the tutorial section new users will fell themselves in good hands, because there will be all the information for beginners and also tips for smoking your own shisha in private.

#### 0.3.1.8 **3.1.6 Achievement**

The achievements are for smokers for whom just smoking is to boring. They get the possibility to pose in front of their friends and show who the greatest smoker is.

### 0.3.2 **3.2 Usability**

#### 0.3.2.1 **3.3.1 Smartphone user**

#### 0.3.2.2Today almost every person has a smartphone and know how to use an android application. Our target is to designing the interface for the user as simple and intuitive as possible. Our blog serve as a guide.

#### 0.3.2.3 **3.3.2 Using a browser**

#### 0.3.2.4We will not provide an online website and the possibility to use our android application in a browser.

#### 0.3.2.5 **3.3.3 Honest person**

#### 0.3.2.6We expect the user to be an honest person, who just upload profile images which won´t offend other people. Our users should obey the law.

### 0.3.3 **3.4 Reliability**

#### 0.3.3.1 **3.4.1 MTTR**

#### 0.3.3.2Due to this being a student&#39;s project the time from failure to fix might strongly vary. Downtimes of more than a day will be possible.

#### 0.3.3.3 **3.4.2 Server availability**

#### 0.3.3.4**(No Commento)**

### 0.3.4 **3.5 Performance**

### 0.3.5 **3.6 Supportability**

### 0.3.6 **3.7 Design Constraints**

### 0.3.7 **3.8 On-line User Documentation and Help System Requirements**

### 0.3.8 **3.9 Purchased Components**

### 0.3.9 **3.10 Interfaces**

### 0.3.10 **3.11 Licensing Requirement**

### 0.3.11 **3.12 Legal, Copyright and other Notices**

### 0.3.12 **3.13 Applicable Standards**

**4. Supporting Information**

## 0.4 **4.1 Appendices**

You can find any internal linked sources in the chapter References (go to the top of this document). If you would like to know what the current status of this project is please visit the SheeshApp Blog.