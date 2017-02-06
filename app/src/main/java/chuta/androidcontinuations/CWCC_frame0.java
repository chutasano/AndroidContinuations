package chuta.androidcontinuations;

/**
 * Created by chuta on 1/5/2017.
 */

class CWCC_frame0 extends ContinuationFrame
{
    ContinuationReceiver receiver;

    public CWCC_frame0(ContinuationReceiver receiver)
    {
        this.receiver = receiver;
    }

    @Override
    public Object Invoke (Object return_value)
    {
        return receiver.run((Continuation) return_value);
    }
}