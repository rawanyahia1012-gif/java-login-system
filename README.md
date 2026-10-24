# SJava AWT Authentication & Login System

A lightweight, robust Java Graphical User Interface (GUI) application built using Java AWT (Abstract Window Toolkit). This application features a dual-panel interface supporting user authentication (Login and Sign-up), role-based access control, input validation, and a security lockout mechanism with a countdown timer.

## 🚀 Features

* **User Authentication:**

  * Login system supporting predefined and dynamically created accounts.

  * Multi-role support (`Student`, `TA`, `Professor`).

* **Input Validation & Error Handling:**

  * Prevents empty field submissions.

  * Validates username existence and matching role selection.

  * Validates password length and match verification during registration.

* **Security Lockout Mechanism (Bonus Feature):**

  * Tracks failed login attempts.

  * Automatically disables login and registration controls after **3 consecutive failed attempts**.

  * Initiates a **30-second lockout timer** with real-time feedback.

* **Dynamic Account Creation (Sign Up):**

  * In-memory storage for registering new users dynamically during execution.

* **CardLayout Navigation:**

  * Seamless switching between Login and Sign Up views within a single window frame.

## 📷 Screenshots & Interface Workflow

### 1. Initial Login Screen

The default view when launching the application.

### 2. Successful Login Scenarios

#### Student Login (`ahmed`)

Input credentials for a student role and navigate to the welcome screen.

| Login Input | Welcome View | 
| ----- | ----- | 
|  |  | 

#### Teaching Assistant Login (`sara`)

Input credentials for a TA role.

| Login Input | Welcome View | 
| ----- | ----- | 
|  |  | 

#### Professor Login (`drmohamed`)

Input credentials for a Professor role.

| Login Input | Welcome View | 
| ----- | ----- | 
|  |  | 

### 3. Error Handling & Security Lockout

#### Wrong Role / Invalid Input Handling

Attempting to log in with mismatched role selection or incorrect password triggers error notices and tracks remaining attempts.

| Mismatched Role Attempt | Error Notice | 
| ----- | ----- | 
|  |  | 

## 🔑 Pre-seeded Accounts (`loadUsers`)

The application comes pre-configured with the following test credentials:

| Username | Password | Role | 
| ----- | ----- | ----- | 
| `ahmed` | `1234` | Student | 
| `sara` | `abcd` | TA | 
| `drmohamed` | `pass123` | Professor | 

## 🛠️ Requirements & How to Run

### Prerequisites

* **Java Development Kit (JDK):** Version 8 or higher.

### Compilation & Execution

1. **Clone the Repository:**

   ```
   git clone https://github.com/your-username/your-repository-name.git
   cd your-repository-name
   
   ```

2. **Compile the Source Code:**

   ```
   javac code.java
   
   ```

3. **Run the Application:**

   ```
   java code
   
   ```

## 📂 Project Structure

```
.
├── code.java          # Main source code file containing Frame logic and UI components
├── README.md          # Project documentation
└── screenshots/       # Application screenshots

```

## 📝 Code Overview

* `code` *(extends `java.awt.Frame`)*: Main window component managing `CardLayout` switches between Login and Sign-up screens.

* `WelcomePage`: Secondary frame rendered upon successful authentication showing user greetings and roles.

* `registerFailure()` & `startLock()`: Handles failed attempt tracking and invokes the `javax.swing.Timer` for lockout countdown management.