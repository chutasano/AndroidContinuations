package chuta.androidcontinuations;

/**
 * Created by chuta on 1/4/2017.
 */

public class SaveContinuationException extends Exception {
    public FrameList new_frames;

    public FrameList old_frames;

    // Push a single new frame.
    public void Extend(ContinuationFrame extension) {
        this.new_frames = new FrameList(extension, this.new_frames);
    }

    // Tack on a pile of old frames.
    public void Append(FrameList old_frames) {
        this.old_frames = old_frames;
    }

    public Continuation toContinuation() throws Exception {
        return new Continuation(new_frames, old_frames);
    }
}