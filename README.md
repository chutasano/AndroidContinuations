## Branches

- master contains AndroidStudio 3.0 (still not the stable release) only things. Mainly, I migrated away from jack (which was how I got lambdas working before)
- jack contains the previous (now unmaintained) code. This will work on pre-Android 3.0

# Continuations on Android


This is a continuation (heh) to the Bluetooth Comp. Offloader project I started.

Currently: The small demo works

Goal: implement first class continuations and try to provide a robust generation scheme for necessary code conversions/generation

Heavily referenced a C# example: http://www.ccs.neu.edu/racket/pubs/stackhack4.html

- Used lambdas (java 1.8, see build.gradle for app) instead of delegates (C# only)

- I hate the whole enforcing each class into a seperate file by java, but oh well


## Testing

- I did some small scale testing by implementing a non-continuation version of the same Fibonacci algorithm. I ran fib(37) on both, and it turned out the continuation version takes ~0.9seconds while the non-continuation version takes ~1.3 seconds. So I guess there must be some weird optimization that Dalvik can do as a result of the way I write the continuation version? Maybe ANF is the key here... Very interesting


## Continuations

Continuations are representations of the program's runtime state. An example of a continuation may be a simple dump of the stack and the heap. First class continuations in java mean that the program's runtime state is a class that inherits object. Unfortunately, Java is structurally inefficient with first class continuations, but there is a way! We use exception handling to force java to capture the stack frame and manually keep track of the "heap" by creating a frame class for every function that stores the variable values at given states within a function.

## How good is it?

From http://www.ccs.neu.edu/racket/pubs/stackhack4.html:
```
Advantages and Drawbacks

This method provides a mechanism by which first-class continuations may be used without incurring much overhead in the more common situation where they are not used very much. Code that has been converted as above does not typically exhibit a large decrease in performance.

ANF conversion makes little impact on the performance because the compiler must perform a similar transformation anyway. The virtual machine model may keep explicit local variables segregated from compiler temporaries, but the actual implementation is likely to store them both in the stack. (And were there a significant difference, an optimizing compiler could change locals to temporaries or vice versa.)

Procedure fragmentation is likely to introduce overhead, but as noted above, the fragments are only used when continuations are reinvoked. Inlining the fragments in the main body of the code will essentially eliminate the performance overhead at the cost of a fixed factor of space overhead. (Inlining every fragment would be proportional to the square of the number of fragments, but inlining the main path would only be double.)

Closure conversion will introduce a number of extra classes and methods, but these methods will not be frequently used. The runtime overhead should be small, but there will be an increase in code size proportional to the number of code fragments.

Code annotation introduces a number of try/catch blocks. This, too, will increase code size proportional to the number of code fragments. Inlined fragments will duplicate the try/catch blocks and have additional size overhead. The runtime overhead of a try/catch block depends greatly on whether an exception is thrown. The overhead when no exception occurs is quite small, and may be zero if the virtual machine optimizes this case. On the other hand, throwing an exception may be very expensive — perhaps several decimal orders of magnitude more than a normal return. Code that infrequently captures first-class continuations should therefore be comparable to un-converted code in performance, but it may be larger by some amount. Code that frequently captures first-class continuations may perform quite poorly if the cost of exception handling cannot be amortized over a sufficient number of normal call/return cycles.
```

However, as stated, it depends on the implementation of the JVM. I'm mainly concerned with Dalvik, so I plan to test a lot.

## Future

Assuming an eventual runtime test shows that the overheads are not too bad, I plan to automate the following (which are at the moment required code changes by the programmer)

- Converting code to ANF
- Generate frame classes for functions with first class continuation capability



## Notes

- 2000 stackframes ~ 0.01MB of RAM
- Definitely a HUGE upgrade over sending APKs, in the megabytes
- Class names and package names matter. The shorter, the smaller the stack frames will be (this might be because I'm only running in debug mode because I expected Android to rename symbols...)



## On Statics
- Statics are not shared with the continuations (ie: uses its local copy) as expected
- You don't need the same static function definition (all it cares is the name,return type, and arg type). This means you can essentially emulate an "extern" function
- package names matter (should be the same)
