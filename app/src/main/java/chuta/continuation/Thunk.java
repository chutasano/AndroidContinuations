package chuta.continuation;

import java.io.Serializable;

/**
 * Created by chuta on 1/4/2017.
 */

//@FunctionalInterface
public interface Thunk {
    public Object run() throws SaveContinuationException;
}