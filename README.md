# SCARA_PROJECT
App for driving robotic arms, built using stepper motors.
<p>
 UI:
 
![alt text](https://github.com/wizartik/SCARA_PROJECT/blob/master/1.png "ui")
 
![alt text](https://github.com/wizartik/SCARA_PROJECT/blob/master/2.png "ui")
<p>
robot:
 
![alt text](https://github.com/wizartik/SCARA_PROJECT/blob/master/photo_2019-02-12_21-49-37.jpg "robot")

<p>
Generation, development and calculation of the trajectory of motion is performed by the backend server which is this app. Microcontroller serves client side of the software architecture which directly manages the motion of stepping motors and receives signals from the terminal switches, the already ready calculated data that is necessary for the realization of the given trajectory of the motion of the working body comes fully adjusted by this app. As a result, the microcontroller is "released" from the heavy and costly calculations of complex algorithms for constructing the trajectory of the motion of the manipulator and validating the input data, and all the load for the calculation takes the backend server. Communication between the server and the microcontroller occurs via the local WiFi network, through the TCP/IP.
<p>
Calculates trajectory of the each joint of the SCARA robot arm due to the specified G-code that is received from a G-code file.
<p>
Path consists of ordered containers of points in the space (either the joint space
or the operational space), which the robot should follow.
Trajectory is a path plus velocities and accelerations in its each point

Requirements:
Provide the capability to smoothly move the manipulator arm and its end effector robot from the initial posture to the final posture.
  
Motion laws are considered in order:
<p>
• not to violate saturation limits of joint drives;
  <p>
• not to excite the resonant modes of the actuator mechanical structure, being driven by electric drives.
    <p>
