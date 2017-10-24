## **1. Introduction**

### **1.1 Purpose**

This SRS describes all specifications for &quot;SheeshApp&quot;. Our project is an android application with a customized graphical user interface. &quot;SheeshApp&quot; allows users to solve the problems, which are coming up, when you smoke shisha together with your friends. In this document the usage of the &quot;SheeshApp&quot;-Android-App will be explained. Furthermore reliability, the shisha tracker and other important characteristics of this project will be specified. This includes design and architectural decisions.

### **1.2 Scope**

This software specification applies to the whole &quot;SheeshApp&quot; application. The application consists of two parts: The first part is an Android-App, which allows users to log-In with their own account, add friends/create groups, use the shisha rookie guide, take a look at their own shisha behavior history and use the main part of the app which is represented by the shisha timer. The second part is a company sided couponing, where they can insert specific benefits for the customers and also add their advertisement.

### **1.3 Definitions, Acronyms and Abbreviations**

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

**MVC** Model-View-Controlle

### **1.4 Overview**

The following chapters are about our vision and perspective, the software requirements, the demands we have, licensing and the technical realization of this project.

## **2. Overall Description**

### **2.1 Vision**

Out there are many shisha-smokers and almost everyone have had problems to schedule the order or also the time which everyone has left for smoking. Our app will be the solution for this problem and also for even more. Problems like how to start smoking shisha for beginners, tips &amp; tricks if you smoke in the private, how u can save some money and analyze your shisha behavior to be reasonable with shisha dealings.

### **2.2 OUCD**

The following picture shows the overall use case diagram of our software:
<img src="/documentation/SheeshAppOUCD.svg" alt="OUCD" width="500" />

## **3. Specific Requirements**

###  **3.1 Functionality - Android App**

####  **3.1.1 Create an account**

An account includes all the personal information of the user and gives him the possibility to use for example the social functions and the coupon feature.
Users without an account have only the possibility to use a lite version of our App.

#### **3.1.2 Add friends/groups**

On this tab the user is able to add friends and create groups. He can also define which groups and friend will get a notification, when the user starts a shisha tracker.

#### **3.1.3 Coupon**

Under the coupon tab the user is able to use coupons. The coupons themselves and their benefits are incumbent upon to the shisha bar owner. With the coupons, the user can save some money if he meets all the requirements.

#### **3.1.4 Shisha tracker**

The user is able to use a timer which schedules the amount of time which every smoker has left to smoke. There is also a function which selects the person, who has to turn/refresh the coal.

#### **3.1.5 Notifications**

The user has the possibility to use notifications, which alerts his chosen friends where and when he will be smoking, so that they can join him. Notifications are triggered by starting the shisha tracker.

#### **3.1.6 Tutorials**

In the tutorial section new users will fell themselves in good hands, because there will be all the information for beginners and also tips for smoking your own shisha in private.

#### **3.1.7 Achievement**

The achievements are for smokers for whom just smoking is to boring. They get the possibility to pose in front of their friends and show who the greatest smoker is.

### **3.2 Usability**

#### **3.2.1 Smartphone user**

Today almost every person has a smartphone and knows how to use an android application. Our target is to designing the interface for the user as simple and intuitive as possible. Our blog serves as a guide.

#### **3.3.2 Using a browser**

We will not provide an online website and the possibility to use our android application in a browser.

#### **3.3.3 Honest person**

We expect the user to be an honest person, who just upload profile images which won´t offend other people. Our users should obey the law.

### **3.3 Reliability**

#### **3.4.1 MTTR**

Due to this being a student&#39;s project the time from failure to fix might strongly vary. Downtimes of more than a day will be possible.

#### **3.4.2 Server availability**

**(No Commento)**

### **3.4 Performance**

Every data traffic which goes to the server must not guarantee real-time data transfer because the user doesn&#39;t get any notice from this traffic. The client saves the date for transmission and sends them to the server if an internet connection is available.

### **3.5 Supportability**

#### **3.5.1 Language support**

We will use the following languages, which will also be well supported in the future:

- Java 7/8 for Android
- Android 5.0 (Lollipop) until 7.1 (Nougat) (API-Level 21-25) and future release versions

### **3.7 Design Constraints**

All information about the architectural design of our application stack can be found in our software architecture document. In the following subchapters you can read about some general important decisions.

#### **3.7.1 Backend in Java**

#### **3.7.2 MVC architecture**

Our Android application should implement the MVC pattern.

### **3.8 On-line User Documentation and Help System Requirements**

The whole application will be built with an intuitive design, so there shouldn&#39;t be a need for the user to ask us or the program for help. However, we will write our own blog, on which users can find information and ask us questions.

### **3.9 Purchased Components**

Is their enough time left, we will maybe implement some in-app purchases

### 3.10 ** Interfaces**

Below you can see the interfaces which will be implemented in the final stage of the app. Currently, there is only the interface shisha tracker und Shisha behavior history

Currently:

- shisha tracker

<img src="/screenshots/tracker_start.jpeg" alt="tracker started" height="500" />
<img src="/screenshots/tracker_running.jpeg" alt="tracker running" height="500" />

- shisha behavior history

<img src="/screenshots/shisha_history.jpeg" alt="shisha history" height="500" />

Coming soon:

- Log in
- Coupon tab
- Add friends

###  **3.11 Licensing Requirement**

**(n/a)**

### 3.11 **Legal, Copyright and other Notices**

**(n/a)**

### **3.13 Applicable Standards**

**(n/a)**

**4. Supporting Information**

## **4.1 Appendices**

You can find any internal linked sources in the chapter References (go to the top of this document). If you would like to know what the current status of this project is please visit the SheeshApp Blog.
