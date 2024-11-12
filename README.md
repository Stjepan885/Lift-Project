<h1>Overview</h1>
Lift Project is an Android application designed to monitor the movement of an elevator by tracking acceleration and timing between floors.

<h2>Installation</h2>
Fork the repository and clone it using Android Studio.
Connect your mobile device to your computer, and deploy the application directly via Android Studio.

<h2>Application Features</h2>
Upon launching, the main interface presents the following options:

- Start Lift Movement Tracker
- Lift Preference
- Calibrate
- Exit

Each option opens a specific activity within the app.

![image](https://github.com/user-attachments/assets/9d4d95ed-b25c-4f7c-8181-43ca5c884b0e)

<h2>Setup</h2> 
During initial setup, navigate to Lift Preference to enter the elevator’s floor count and starting floor. This data is saved using the SharedPreference interface.

![image](https://github.com/user-attachments/assets/d0ed3e40-9c0e-41ec-8c99-b9830f4303eb)

<h2>Calibration</h2>
In Calibrate, measure the acceleration and timing between floors. This section is divided into:

- Acceleration Measurement
- Time Interval Measurement

You can save, reset, and manage these values here. For accurate tracking, calibration is required. However, the app can operate without pre-measured timing if preferred, allowing you to toggle this mode.

![image](https://github.com/user-attachments/assets/d30f75d4-922e-4c6b-9c17-5e0f50bc6fa5)
![image](https://github.com/user-attachments/assets/32bddfe4-cdbb-42e4-b25e-df237ca17619)

<h2>Tracking Elevator Movement</h2>
The elevator movement tracker only functions if calibration data has been saved. Within this activity, you can start, stop, or reset tracking. Displayed data includes:

- Current acceleration
- Current floor
- Elevator movement status

After tracking, a graphical representation of the elevator’s movement is available, showing acceleration, speed, and cumulative acceleration over time.

![image](https://github.com/user-attachments/assets/cf7f585f-8ef7-48b6-a948-274f3449585f)
![image](https://github.com/user-attachments/assets/cd8196a8-86fd-4209-9794-7db8a1aa253a)

<h2>Elevator Tracking</h2>
The app tracks elevator movement using the accelerometer, which provides a three-dimensional vector representing acceleration along each axis. For best accuracy, place the mobile device on the elevator floor, with the Z-axis aligned with the elevator’s movement.

<h2>Signal Preprocessing</h2>
The accelerometer signal includes:

- Gravitational force (offset)
- Drift (value changes at rest)
- True acceleration (signal)
- Noise
- A bandpass filter (combining low-pass and high-pass filters) removes noise and the effect of gravity, isolating the true acceleration.

<h2>Movement Detection</h2>
Elevator movement consists of four stages:

- Acceleration
- Constant speed
- Deceleration
- Resting
Using the Z-axis values, the app detects the movement direction. 

<h2>Floor Change Detection</h2>
The app offers two methods for floor detection:

Pre-measured intervals: Measures the time interval between floors during calibration, storing these intervals in a table. By comparing the recorded time during tracking, it determines floor changes.

Adaptive intervals: For elevators with variable speeds, the app creates and refines the interval table during each trip.
