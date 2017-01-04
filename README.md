##Continuations on Android

This is a continuation (heh) to the Bluetooth Comp. Offloader project I started.

Currently: Ay it actually works. Now I want to make a generation scheme

Goal: implement continuations and try to provide a robust generation scheme for necessary code conversions/generation

Heavily referenced a C# example: http://www.ccs.neu.edu/racket/pubs/stackhack4.html

- Used lambdas (java 1.8, see build.gradle for app) instead of delegates (C# only)

- I hate the whole enforcing each class into a seperate file by java