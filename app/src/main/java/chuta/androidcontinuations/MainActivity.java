package chuta.androidcontinuations;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import chuta.continuation.Continuation;
import chuta.continuation.Thunk;

import static chuta.androidcontinuations.ContinuationDefinitions.fib_new0;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "continuations";
    Thunk fun = () -> fib_new0(37);
    Thunk fun3 = (Thunk & Serializable)() -> fib_new0(14);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonClick(View v)
    {
        if (v == findViewById(R.id.button)) {
            method1();
        }
        else if (v == findViewById(R.id.button2)) {
            method2();
        }
        else if (v == findViewById(R.id.button3)) {
            method3();
        }
    }

    private byte[] getContinuationBytes(Thunk f)
    {
        try {
            //converts Thunk lambda to bytes
            byte[] bytes;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out;
            out = new ObjectOutputStream(bos);
            out.writeObject(f);
            out.flush();
            bytes = bos.toByteArray();
            bos.close();
            return bytes;
        }
        catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private Thunk reinitializeThunk(byte[] bytes) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInput in;
            Thunk fun = null;
            in = new ObjectInputStream(bis);
            try {
                fun = (Thunk) in.readObject();
            } catch (ClassNotFoundException e) {
                Log.e(TAG, e.toString());
            }
            in.close();
            return fun;
        }catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private void method1() {
        //reload method
        byte[] bytes = getContinuationBytes(fun3);
        fun3 = reinitializeThunk(bytes);
        if (fun3 == null)
        {
            Log.e(TAG, "Ooops, reloading failed...");
        }
    }

    private void runfib()
    {
        /*
        Log.d(TAG, "Fibcontinuation start");
        Thunk fun1 = () -> fib_an(37);
        int temp = (int) Continuation.EstablishInitialContinuation(fun1);
        Log.d(TAG, "Fibcontinuation end");
        String print = Integer.toString(temp);
        Log.d(TAG, print);
        */
    /*    Log.d(TAG, "Fib start");
        int a = fib_nonc(37);
        Log.d(TAG, "Fib end");
        Log.d(TAG, Integer.toString(a));
        Log.d(TAG, "Fibcontinuation start");
        Thunk fun2 = () -> fib_an(8);
        int temp2 = (int) Continuation.EstablishInitialContinuation(fun2);
        Log.d(TAG, Integer.toString(temp2));
        Log.d(TAG, "Fibcontinuation end");*/
    }

    private void method2()
    {
        Log.d(TAG, "Fibcontinuation2 start");
        Object temp = Continuation.EstablishInitialContinuation(fun);
        Log.d(TAG, "Fibcontinuation end");

        if (temp == null)
        {
            Log.d(TAG, "Fibcontinuation fail");
        }
        else if (temp instanceof Thunk)
        {
            Log.d(TAG, "got continuation");
            fun = (Thunk)temp;
        }
        else
        {
            String print = Integer.toString((int)temp);
            Log.d(TAG, print);
            fun = () -> fib_new0(37);
        }
    }

    int fib_nonc(int n)
    {
        if (n > 1) {
            return (fib_nonc(n - 1) + fib_nonc(n - 2));
        }
        else {
            return 1;
        }
    }
    private void method3()
    {
        Log.d(TAG, "Fibcontinuation3 start");
        Object temp = Continuation.EstablishInitialContinuation(fun3);
        Log.d(TAG, "Fibcontinuation end");

        if (temp == null)
        {
            Log.d(TAG, "Fibcontinuation fail");
        }
        else if (temp instanceof Thunk)
        {
            Log.d(TAG, "got continuation");
            fun3 = (Thunk)temp;
        }
        else
        {
            String print = Integer.toString((int)temp);
            Log.d(TAG, print);
            fun3 = (Thunk & Serializable)() -> fib_new0(14); //reset
        }
    }
}
