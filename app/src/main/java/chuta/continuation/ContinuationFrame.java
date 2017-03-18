package chuta.continuation;


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
    public abstract Object Invoke(Object continue_value) throws SaveContinuationException;
}