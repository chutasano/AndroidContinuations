package chuta.androidcontinuations;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import static chuta.androidcontinuations.ContinuationDefinitions.fib_an;
import static chuta.androidcontinuations.ContinuationDefinitions.fib_new;


public class MainActivity extends AppCompatActivity {
    private final String TAG = "continuations";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonClick(View v)
    {
        method1();
    }

    public void buttonClick2(View v)
    {
        method2();
    }

    private void method1()
    {
        Log.d(TAG, "Fibcontinuation start");
        Thunk fun = () -> fib_an(37);
        int temp = (int) Continuation.EstablishInitialContinuation(fun);
        Log.d(TAG, "Fibcontinuation end");
        String print = Integer.toString(temp);
        Log.d(TAG, print);
    }

    private void method2()
    {
        Log.d(TAG, "Fib start");
        int a = fib_nonc(37);
        Log.d(TAG, "Fib end");
        Log.d(TAG, Integer.toString(a));
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
