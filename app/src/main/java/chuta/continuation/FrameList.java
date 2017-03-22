package chuta.continuation;

import java.io.Serializable;

/**
 * Created by chuta on 1/4/2017.
 */

public class FrameList implements Serializable
{
    public ContinuationFrame first;
    public FrameList rest;

    public FrameList (ContinuationFrame _first, FrameList _rest)
    {
        first = _first;
        rest = _rest;
    }

    public static FrameList reverse (FrameList original)
    {
        FrameList result = null;
        while (original != null) {
            result = new FrameList (original.first, result);
            original = original.rest;
        }
        return result;
    }
}