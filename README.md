##Continuations on Android

On halt as of 1/20/17. I plan to resume soon though!

This is a continuation (heh) to the Bluetooth Comp. Offloader project I started.

Currently: Ay it actually works. Now I want to make a generation scheme

Goal: implement first class continuations and try to provide a robust generation scheme for necessary code conversions/generation

Heavily referenced a C# example: http://www.ccs.neu.edu/racket/pubs/stackhack4.html

- Used lambdas (java 1.8, see build.gradle for app) instead of delegates (C# only)

- I hate the whole enforcing each class into a seperate file by java

<<<<<<< HEAD
#Testing

- I did some small scale testing by implementing a non-continuation version of the same Fibonacci algorithm. I ran fib(37) on both, and it turned out the continuation version takes ~0.9seconds while the non-continuation version takes ~1.3 seconds. So I guess there must be some weird optimization that Dalvik can do as a result of the way I write the continuation version? Maybe ANF is the key here... Very interesting
=======
##Continuations

Continuations are representations of the program's runtime state. An example of a continuation may be a simple dump of the stack and the heap. First class continuations in java mean that the program's runtime state is a class that inherits object. Unfortunately, Java is structurally inefficient with first class continuations, but there is a way! We use exception handling to force java to capture the stack frame and manually keep track of the "heap" by creating a frame class for every function that stores the variable values at given states within a function.

##How good is it?

No idea... I really should perform a full test on run time speed and overhead. SoonTM. Another idea is to use Scala for Android, which you know... literally supports first class continuations. The good part about the implementation is that it supports partial first class continuations, so we do not have to make everything continuation-able!

##Future

Assuming an eventual runtime test shows that the overheads are not too bad, I plan to automate the following (which are at the moment required code changes by the programmer)

- Converting code to ANF
- Generate frame classes for functions with first class continuation capability
>>>>>>> 7d1c94bd862b5cfda67db0884ad992b23457bece
