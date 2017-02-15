package chuta.androidcontinuations;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import static chuta.androidcontinuations.ContinuationDefinitions.fib_an;
import static chuta.androidcontinuations.ContinuationDefinitions.fib_new0;


public class MainActivity extends AppCompatActivity {
    private final String TAG = "continuations";
    Thunk fun = () -> fib_new0(37);
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
    }

    public void buttonClick2(View v)
    {
        method2();
    }

    public void buttonClick3(View v)
    {
        method3();
    }

    private void method1()
    {
        Log.d(TAG, "Fibcontinuation start");
        Thunk fun1 = () -> fib_an(37);
        int temp = (int) Continuation.EstablishInitialContinuation(fun1);
        Log.d(TAG, "Fibcontinuation end");
        String print = Integer.toString(temp);
        Log.d(TAG, print);
        Log.d(TAG, "Fib start");
        int a = fib_nonc(37);
        Log.d(TAG, "Fib end");
        Log.d(TAG, Integer.toString(a));
        Log.d(TAG, "Fibcontinuation start");
        Thunk fun2 = () -> fib_an(8);
        int temp2 = (int) Continuation.EstablishInitialContinuation(fun2);
        Log.d(TAG, Integer.toString(temp2));
        Log.d(TAG, "Fibcontinuation end");
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

    }


}
