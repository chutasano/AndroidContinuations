package chuta.androidcontinuations;

/**
 * Created by chuta on 1/4/2017.
 */

public interface ContinuationReceiver {
    public Object run(Continuation arg);
}