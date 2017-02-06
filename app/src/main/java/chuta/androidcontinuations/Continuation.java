package chuta.androidcontinuations;

import android.util.Log;

/**
 * Created by chuta on 1/4/2017.
 */




public class Continuation
{
    public static Object EstablishInitialContinuation (Thunk thunk) //static
    {
        while (true) {
            try {
                return InitialContinuationAux (thunk);
            }
            catch (WithinInitialContinuationException wic) {
                thunk = wic.thunk;
            }
        }
    }
    public static Object InitialContinuationAux (Thunk thunk) throws WithinInitialContinuationException //static
    {
        try {
            return thunk.run();
        }
        catch (SaveContinuationException sce) {
            try {
                Continuation k = sce.toContinuation();
                Thunk currentk = () -> k.Reload(k);
                throw new WithinInitialContinuationException(currentk);
            }
            catch (Exception e)
            {
                Log.e("AHH", e.toString());
                return null;
            }
        }
    }

    FrameList frames;

    public Continuation (FrameList new_frames, FrameList old_frames) throws Exception
    {
        // The new frames don't know what the continuation is below them.
        // We take them one by one and push them onto the old_frames
        // while setting their continuation to the list of frames below.
        frames = old_frames;
        while (new_frames != null) {
            ContinuationFrame new_frame = new_frames.first;
            new_frames = new_frames.rest;
            if (new_frame.continuation != null)
                throw new Exception ("Continuation not empty?");
            new_frame.continuation = frames;
            frames = new FrameList (new_frame, frames);
        }
    }

    Object Reload (Object restart_value) throws SaveContinuationException
    {
        // Reverse the frames in order to reload them.
        FrameList rev = FrameList.reverse (frames);
        return rev.first.Reload (rev.rest, restart_value);
    }

    static void BeginUnwind() throws SaveContinuationException
    {
        throw new SaveContinuationException();
    }

    public static Object CWCC (ContinuationReceiver receiver) throws SaveContinuationException
    {
        try {
            BeginUnwind();
        }
        catch (SaveContinuationException sce) {
            sce.Extend (new CWCC_frame0 (receiver));
            throw sce;
        }
        // Should never get here.
        return null;
    }

}