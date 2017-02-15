package chuta.androidcontinuations;


import static chuta.androidcontinuations.ContinuationDefinitions.fib_an0;
import static chuta.androidcontinuations.ContinuationDefinitions.fib_an1;
import static chuta.androidcontinuations.ContinuationDefinitions.*;

/**
 * Created by chuta on 1/4/2017.
 */

public abstract class ContinuationFrame {
    public FrameList continuation;

    public Object Reload (FrameList frames_above, Object restart_value) throws SaveContinuationException
    {

        // The continutation for the call to Reload is the expression
        // continuation of this assignment followed by the subsequent
        // try/catch block.

        // This call does *not* have to be protected by a try/catch
        // because we have not made any progress at this point.  When we Invoke
        // the continuation function, we need to establish a try/catch in order
        // to tie this continuation in to the new frames about to be
        // created.
        Object continue_value = (frames_above == null)
                ? restart_value
                : frames_above.first.Reload (frames_above.rest, restart_value);

        try {
            return this.Invoke (continue_value);
        }
        catch (SaveContinuationException sce) {
            sce.Append (continuation);
            throw sce;
        }
    }
    abstract Object Invoke(Object continue_value) throws SaveContinuationException;
}


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
    Object Invoke(Object continue_value) throws SaveContinuationException {
        return fib_an0((int) continue_value, this.x);
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
    Object Invoke(Object continue_value) throws SaveContinuationException {
        return fib_an1((int) continue_value, this.temp0);
    }
}

class fib_frame_newa extends ContinuationFrame {
    int x;
    fib_frame_newa(int x) { this.x = x;}
    @Override
    Object Invoke(Object continue_value) throws SaveContinuationException {
        return fib_new1(this.x);
    }
}

class fib_frame_new0 extends ContinuationFrame {
    int x;
    fib_frame_new0(int x) { this.x = x;}
    @Override
    Object Invoke(Object continue_value) throws SaveContinuationException {
        return fib_new2((int) continue_value, this.x);
    }
}

class fib_frame_new1 extends ContinuationFrame {
    int x;
    fib_frame_new1(int x) { this.x = x;}
    @Override
    Object Invoke (Object continue_value) throws SaveContinuationException {
        return fib_new3((int) continue_value, this.x);
    }
}