package chuta.androidcontinuations;

/**
 * Created by chuta on 1/4/2017.
 */

public class ContinuationDefinitions {

    public static int fib_an (int x) throws SaveContinuationException
    {
        if (x < 2)
            return x;
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
}
