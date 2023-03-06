# HNS

## What is an HNS?
An HNS is a _**H**ybrid **N**avigation **S**ystem_. 
It is a system that combines multiple sensors present on a robot to provide a more accurate and robust navigation system.
An HNS is better than a single sensor system because it can account for the error of each sensor.

For example, odometers are very accurate, but can be interrupted by a collision and are effectively ruined when the robot runs over a bump.
However, an IMU can be used to estimate the position when the odometry is providing bad data.

## What Sensors can Be Used?
The following sensors can be used in an HNS:
- Odometers
- IMUs
- Cameras
- Distance Sensors
- Motor encoders