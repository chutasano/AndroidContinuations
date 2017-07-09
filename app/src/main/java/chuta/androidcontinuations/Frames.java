package chuta.androidcontinuations;


import chuta.continuationslib.ContinuationFrame;
import chuta.continuationslib.SaveContinuationException;




/**
 * Created by chuta on 1/4/2017.
 */


//TODO generate per method

class fib_frame0 extends ContinuationFrame {
    // Live variables for fib_an0 minus the one assigned
    // by the continuation.

    int x;

    // Constructor
    fib_frame0(int _x) {
        this.x = _x;
    }

    // Restart method
    @Override
    public Object Invoke(Object continue_value) throws SaveContinuationException {
        return ContinuationDefinitions.fib_an0((int) continue_value, this.x);
    }
}

class fib_frame1 extends ContinuationFrame {
    // Live variables for fib_an1 minus the one assigned
    // by the continuation.

    int temp0;

    // Constructor
    fib_frame1(int _temp0) {
        this.temp0 = _temp0;
    }

    // Restart method
    @Override
    public Object Invoke(Object continue_value) throws SaveContinuationException {
        return ContinuationDefinitions.fib_an1((int) continue_value, this.temp0);
    }
}

class fib_frame_newa extends ContinuationFrame {
    int x;
    fib_frame_newa(int x) { this.x = x;}
    @Override
    public Object Invoke(Object continue_value) throws SaveContinuationException {
        return ContinuationDefinitions.fib_new1(this.x);
    }
}

class fib_frame_new0 extends ContinuationFrame {
    int x;
    fib_frame_new0(int x) { this.x = x;}
    @Override
    public Object Invoke(Object continue_value) throws SaveContinuationException {
        return ContinuationDefinitions.fib_new2((int) continue_value, this.x);
    }
}

class fib_frame_new1 extends ContinuationFrame {
    int x;
    fib_frame_new1(int x) { this.x = x;}
    @Override
    public Object Invoke (Object continue_value) throws SaveContinuationException {
        return ContinuationDefinitions.fib_new3((int) continue_value, this.x);
    }
}