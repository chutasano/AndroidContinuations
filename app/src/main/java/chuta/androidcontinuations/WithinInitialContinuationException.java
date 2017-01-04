package chuta.androidcontinuations;

/**
 * Created by chuta on 1/4/2017.
 */

class WithinInitialContinuationException extends Exception
{
    public Thunk thunk;

    public WithinInitialContinuationException (Thunk thunk)
    {
        this.thunk = thunk;
    }
}