package chuta.androidcontinuations;

import android.util.Log;

import chuta.continuation.Continuation;
import chuta.continuation.ContinuationReceiver;
import chuta.continuation.SaveContinuationException;

/**
 * Created by chuta on 1/4/2017.
 */

public class ContinuationDefinitions {

    static int timer = 0;
    static int breaks = 500;

    public static Object asdf(Continuation cont)
    {
        Log.d("TAG", "Paused: " + cont.getFrames().toString());
        return cont;
    }
    public static Object pause () throws SaveContinuationException
    {
        ContinuationReceiver fun = (Continuation cont) -> asdf(cont);
        return Continuation.CWCC (fun);
    }


    public static Object fib_new0(int x) throws SaveContinuationException
    {
        timer++;
        if (timer == breaks)
        {
            timer = 0;
            try
            {
                Continuation.BeginUnwind();
            }
            catch(SaveContinuationException e)
            {
                e.Extend(new fib_frame_newa(x));
                throw e;
            }
        }
        return fib_new1(x);
    }

    public static int fib_an (int x) throws SaveContinuationException
    {
        if (x < 2)
            return 1;
        else {
            int temp0;
            try {
                temp0 = fib_an(x - 2);
            }
            catch (SaveContinuationException e)
            {
                e.Extend(new fib_frame0(x));
                throw e;
            }
            return fib_an0 (temp0, x);
        }
    }

    public static int fib_an0 (int temp0, int x) throws SaveContinuationException
    {
        int temp1;
        try {
            temp1 = fib_an (x - 1);
        }
        catch (SaveContinuationException e)
        {
            e.Extend(new fib_frame1(temp0));
            throw e;
        }
        return fib_an1 (temp1, temp0);
    }

    public static int fib_an1 (int temp1, int temp0)
    {
        return temp0 + temp1;
    }



    public static Object fib_new1 (int x) throws SaveContinuationException
    {
        if (x < 2)
            return 1;
        else {
            int temp0;
            try {
                temp0 = (int)fib_new0(x - 2);
            }
            catch (SaveContinuationException e)
            {
                e.Extend(new fib_frame_new0(x));
                throw e;
            }
            return fib_new2 (temp0, x);
        }
    }

    public static Object fib_new2 (int temp0, int x) throws SaveContinuationException
    {
        int temp1;
        try {
            temp1 = (int)fib_new0 (x - 1);
        }
        catch (SaveContinuationException e)
        {
            e.Extend(new fib_frame_new1(temp0));
            throw e;
        }
        return fib_new3 (temp1, temp0);
    }

    public static Object fib_new3 (int temp1, int temp0)
    {
        return temp0 + temp1;
    }
}
