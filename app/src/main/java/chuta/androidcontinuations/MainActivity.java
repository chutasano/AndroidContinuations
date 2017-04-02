package chuta.androidcontinuations;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    private final String TAG = "continuations";
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothService mBluetoothService = null;
    private String mConnectedDeviceName = null;
    private ArrayAdapter<String> mLogAdapter;
    //Thunk fun = () -> fib_new0(37);
    Thunk fun3 = (Thunk & Serializable) () -> fib_new0(14);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public void onStart() {
        super.onStart();
        ensureDiscoverable();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mBluetoothService == null) {
            setup();
        }
    }

    void setup()
    {
        mLogAdapter =new ArrayAdapter<String>(this,R.layout.message);
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(mLogAdapter);
        mBluetoothService = new BluetoothService(this,mHandler);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mBluetoothService.stop();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mBluetoothService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mBluetoothService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth chat services
                mBluetoothService.start();
            }
        }
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

    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }



    private void sendBytes(byte[] data)
    {
        if (mBluetoothService.getState() != BluetoothService.STATE_CONNECTED) {
            return;
        }
        mBluetoothService.write(data);
    }

    private void setStatus(CharSequence subTitle)
    {
        TextView txt = (TextView)findViewById(R.id.textView);
        txt.setText(subTitle);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                    Log.d(TAG, "Connecting Secure");
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                    Log.d(TAG, "Connecting insecure");
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setup();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                }
                break;
        }
    }

    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mBluetoothService.connect(device, secure);
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            setStatus("Connected to " + mConnectedDeviceName);
                            mLogAdapter.clear();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            setStatus("Connecting");
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            setStatus("Not Connected");
                            break;
                    }
                    break;
                case BluetoothService.BYTES_SEND:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    mLogAdapter.add("Me: Sent Continuation: " + writeBuf.length + " bytes");
                    break;
                case BluetoothService.BYTES_RECEIVE:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    mLogAdapter.add(mConnectedDeviceName + ":  Received Continuation: " + readMessage);
                    fun3 = reinitializeThunk(readBuf);
                    break;
                case BluetoothService.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(BluetoothService.DEVICE_NAME);
                    break;
                case BluetoothService.MESSAGE_TOAST:
                    break;
            }
        }
    };

    private void method1() {
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
        //startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
        //reload method
        /*byte[] bytes = getContinuationBytes(fun3);
        fun3 = reinitializeThunk(bytes);
        if (fun3 == null)
        {
            Log.e(TAG, "Ooops, reloading failed...");
        }*/

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
        //send continuation
        sendBytes(getContinuationBytes(fun3));

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
