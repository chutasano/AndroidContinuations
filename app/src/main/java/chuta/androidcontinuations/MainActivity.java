package chuta.androidcontinuations;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import static chuta.androidcontinuations.ContinuationDefinitions.fib_an;


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
        method2();
        method3();
    }

    private void method1()
    {
        Thunk fun = () -> fib_an(5);
        int temp = (int) Continuation.EstablishInitialContinuation(fun);
        String print = Integer.toString(temp);
        Log.d(TAG, print);
    }

    private void method2()
    {

    }

    private void method3()
    {

    }


}
