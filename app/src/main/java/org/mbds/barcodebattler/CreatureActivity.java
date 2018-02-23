package org.mbds.barcodebattler;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.mbds.barcodebattler.battle.BattleActivity;
import org.mbds.barcodebattler.data.ICreature;
import org.mbds.barcodebattler.util.BaseActivity;
import org.mbds.barcodebattler.util.MyBluetoothService;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class CreatureActivity extends BaseActivity {
    private ICreature creature;
    private BluetoothAdapter mBluetoothAdapter;
    private static int REQUEST_ENABLE_BT = 1;
    private String deviceHardwareAddress;
    private String deviceName;
    private UUID MY_UUID = UUID.fromString("dc888650-ba34-11e7-8f1a-0800200c9a66");
    private static String NAME = "Barcode Battler";
    private static final String TAG = CreatureActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_superhero);

        TextView name = (TextView) findViewById(R.id.name);
        TextView energy = (TextView) findViewById(R.id.energy);
        TextView strike = (TextView) findViewById(R.id.strike);
        TextView defense = (TextView) findViewById(R.id.defense);
        ImageView icon = (ImageView) findViewById(R.id.icon);
        final Button addToBattle = (Button) findViewById(R.id.addToBattle);
        final Button addToNetworkBattle = (Button) findViewById(R.id.addToNetworkBattle);

        Bundle extras = getIntent().getExtras();
        creature = extras.getParcelable("creature");

        assert creature != null;
        name.setText(String.valueOf(creature.getName()));
        energy.setText(String.valueOf(creature.getEnergy()));
        strike.setText(String.valueOf(creature.getStrike()));
        defense.setText(String.valueOf(creature.getDefense()));

        String mDrawableName = creature.getImageName();
        int resID = getResources().getIdentifier(mDrawableName, "drawable", getApplicationContext().getPackageName());

        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                resID);

        icon.setImageBitmap(bitmap);

        addToBattle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToBattle.setEnabled(false);

                Intent intent = new Intent(CreatureActivity.this, BattleActivity.class);

                intent.putExtra("creatureP1", creature);
//                setResult(RESULT_OK, intent);
                CreatureActivity.this.startActivity(intent);
//                finish();
            }
        });

        addToNetworkBattle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    // si l’adapter est null, le téléphone ne supporte pas le bluetooth
                    Toast.makeText(getApplicationContext(), "le téléphone ne supporte pas le bluetooth",  Toast.LENGTH_SHORT).show();
                }

                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    CreatureActivity.this.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode ==  RESULT_OK) { // enabling Bluetooth succeeded

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                for (BluetoothDevice device : pairedDevices) {
                    deviceName = device.getName();
                    Log.i("connected to: ", deviceName + " (" + deviceHardwareAddress + ")");
                    deviceHardwareAddress = device.getAddress(); // MAC address

                    if (deviceHardwareAddress.equals("78:52:1A:0B:C3:A8")) { // TOOD: pas seulement mon tel
                        new AcceptThread().run();
                    }
                }
            }
            else {
                // TODO: discover devices
            }
        }
        else if (resultCode == RESULT_CANCELED) {

        }
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket
            // because BluetmmServerSocket is final.
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code.
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);

            } catch (IOException e) {
                Log.e(TAG, "Socket's listen() method failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned.
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Socket's accept() method failed", e);
                    break;
                }

                if (socket != null) {
                    // A connection was accepted. Perform work associated with
                    // the connection in a separate thread.
                    manageMyConnectedSocket(socket);

                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                    Log.e(TAG, "Socket's accept() method failed", e);
                        break;
                    }
                    break;
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }

        private void manageMyConnectedSocket(BluetoothSocket socket) {
            MyBluetoothService.ConnectedThread thread = new MyBluetoothService().new ConnectedThread(socket);
            String message= creature.getName();
            byte[] send = message.getBytes();
            thread.write(send);
            Toast.makeText(getApplicationContext(), "Donnees envoyees",  Toast.LENGTH_SHORT).show();
//        thread.cancel();
//        Toast.makeText(getApplicationContext(), "Bluetooth Closed",  Toast.LENGTH_SHORT).show();
        }
    }
}
