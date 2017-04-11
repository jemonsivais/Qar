# Qar

Qar is a small project AI-based system in which a car in a grid learns how to drive avoiding walls. It uses Q-Learning algorithm to obtain the optimal policy via trial and error. The car has the option to choose one action before every tick and the car will move one square into the facing direction after the tick.

## Action Space
Turn Left
Turn Right
Do Nothing

## State Space
Distance from wall in front
Distance from wall to the left
Distance from wall to the right

## Rewards
-100 for crashing
0 for crashing

To run it is required to have javatuples in your directory.
