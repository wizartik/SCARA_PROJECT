# SCARA_PROJECT
calculates trajectory of the each joint of the SCARA robot arm due to the specified G-code that is received from a G-code file
<p>
Path consists of ordered locii of points in the space (either the joint space
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
