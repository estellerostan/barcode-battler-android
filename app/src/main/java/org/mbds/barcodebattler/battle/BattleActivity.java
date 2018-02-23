package org.mbds.barcodebattler.battle;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.mbds.barcodebattler.CreatureActivity;
import org.mbds.barcodebattler.MainActivity;
import org.mbds.barcodebattler.R;
import org.mbds.barcodebattler.data.Creature;
import org.mbds.barcodebattler.data.ICreature;
import org.mbds.barcodebattler.util.BarcodeBattlerDatabaseAdapter;
import org.mbds.barcodebattler.util.ImageHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BattleActivity extends AppCompatActivity {

    private static final String TAG = BattleActivity.class.getSimpleName();

    // Player 1 stats view
    TextView P1StatsCreatureName;
    TextView P1HP;
    TextView P1ST;
    TextView P1DF;
    // Player 1 creature image
    TextView P1Name;
    ImageView P1Image;

    List<ICreature> P1creatures;

    //---------------------------

    // Player 2 stats view
    TextView P2StatsCreatureName;
    TextView P2HP;
    TextView P2ST;
    TextView P2DF;
    // Player 2 creature image
    TextView P2Name;
    ImageView P2Image;

    List<ICreature> P2creatures;

    //TextView gameMsg;
    EditText gameMsg;

    ICreature creature;
    ICreature enemy;
    Button buttonTurn;

    Battle battle;

    BarcodeBattlerDatabaseAdapter databaseAdapter;

    // Bluetooth pour combat en réseau
    private BluetoothAdapter mBluetoothAdapter;
    private static int REQUEST_ENABLE_BT = 1;
    private String deviceName;
    private String deviceHardwareAddress;
    private String readMessage;
    private UUID MY_UUID = UUID.fromString("dc888650-ba34-11e7-8f1a-0800200c9a66");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        databaseAdapter = new BarcodeBattlerDatabaseAdapter(getApplicationContext());
        databaseAdapter.open();

        final String superheroType = "SUPERHERO";

        P1creatures = new ArrayList<>();

        P2creatures = new ArrayList<>();

        P1StatsCreatureName = (TextView) findViewById(R.id.monsterInfoP1);
        P2StatsCreatureName = (TextView) findViewById(R.id.monsterInfoP2);

        P1HP = (TextView) findViewById(R.id.P1MonsterHP);
        P1ST = (TextView) findViewById(R.id.P1monsterST);
        P1DF = (TextView) findViewById(R.id.P1monsterDF);
        P1Name = (TextView) findViewById(R.id.monsterNameP1);

        P2HP = (TextView) findViewById(R.id.P2MonsterHP);
        P2ST = (TextView) findViewById(R.id.P2monsterST);
        P2DF = (TextView) findViewById(R.id.P2monsterDF);
        P2Name = (TextView) findViewById(R.id.monsterNameP2);

        P1Image = (ImageView) findViewById(R.id.monsterImageP1);
        P2Image = (ImageView) findViewById(R.id.monsterImageP2);

        gameMsg = (EditText) findViewById(R.id.game_msg);
        gameMsg.setEnabled(false);

        databaseAdapter = new BarcodeBattlerDatabaseAdapter(getApplicationContext());
        databaseAdapter.open();

        if (getCallingActivity().getClassName().toString().equals(MainActivity.class.getName())) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                // si l’adapter est null, le téléphone ne supporte pas le bluetooth
                Toast.makeText(getApplicationContext(), "le téléphone ne supporte pas le bluetooth",  Toast.LENGTH_SHORT).show();
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
//            BattleActivity.this.finish();
        } else {
            if ( getIntent().getExtras() == null ) {
                String msg = getResources().getString(R.string.msg_empty_battlefield);
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                BattleActivity.this.finish();
            } else
            {
                // -- View Component init : --
                initPlayerCreature(Player.PLAYER1);
                initPlayerCreature(Player.PLAYER2);
                battle = new Battle(P1creatures, P2creatures, this);
            }

            buttonTurn = (Button) findViewById(R.id.btn_turn);
            buttonTurn.setText(R.string.btn_start);


            buttonTurn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (battle.getBattleState().getPlayerTurn()) {
                        case NONE:
                            if ( battle.getBattleWinner() == Player.NONE ) {
                                Log.d(TAG, "[game] Click Button START");
                                battle.startBattle();
                            } else {
                                Log.d(TAG, "[game] Close battle");
                                BattleActivity.this.finish();
                            }
                            break;
                        case PLAYER1:
                            if ( battle.isTurnEnded() ) {
                                Log.d(TAG, "Current P1 turn ended");
                                battle.nextTurn();
                            } else {
                                Log.d(TAG, "Current P1 turn");
                                battle.playTurn(Player.PLAYER1);
                            }
                            break;
                        case PLAYER2:
                            Log.d(TAG, "Click player2");
                            break;
                    }
                }
            });
        }
    }

    public void initPlayerCreature(Player player) {

        String mDrawableN;
        int resI;
        Bitmap bitma;

        switch (player) {
            case PLAYER1:
                // Load P! from various sources
                if (getIntent().getExtras() != null) {
                    creature = getIntent().getExtras().getParcelable("creatureP1");

                    P1Name.setText(creature.getName());
                    P1StatsCreatureName.setText(creature.getName());
                    P1HP.setText(String.valueOf(creature.getEnergy()));
                    P1ST.setText(String.valueOf(creature.getStrike()));
                    P1DF.setText(String.valueOf(creature.getDefense()));

                    String mDrawableName = creature.getImageName();
                    int resID = getResources().getIdentifier(mDrawableName, "drawable", getApplicationContext().getPackageName());

                    Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            resID);
                    P1Image.setImageBitmap(bitmap);

                    P1creatures.add(creature);
                    ArrayList<ICreature> superheroes = databaseAdapter.getSuperheroes();
                    superheroes.remove(creature.getId());
                    P1creatures.addAll(superheroes);

                    // TOOD: inserer en db le match courant ?
                    Toast.makeText(this, "Début du combat", Toast.LENGTH_LONG).show();

                } else {
                    LoadPreferences();
                    Toast.makeText(this, "Reprise du combat", Toast.LENGTH_LONG).show();
                }
                break;
            case PLAYER2:
                enemy = databaseAdapter.getEnemies().get(0);

                P2Name.setText(enemy.getName());
                P2StatsCreatureName.setText(enemy.getName());
                P2HP.setText(String.valueOf(enemy.getEnergy()));
                P2ST.setText(String.valueOf(enemy.getStrike()));
                P2DF.setText(String.valueOf(enemy.getDefense()));

                mDrawableN = enemy.getImageName();
                resI = getResources().getIdentifier(mDrawableN, "drawable", getApplicationContext().getPackageName());

                    bitma = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            resI);
                    P2Image.setImageBitmap(bitma);

                for (ICreature enemy : databaseAdapter.getEnemies()
                        ) {
                    enemy = databaseAdapter.getRandomEnemy();
                    // Load P2
                    if (enemy != null) {
                        P2creatures.add(enemy);
                    } else {
                        LoadPreferences();
                    }
                }
                break;
        }
    }

    public void setPlayerCreature(Player player, boolean died) {
        switch (player) {
            case PLAYER1:
                creature = battle.getBattleState().getPlayer1CurrentCreature();
                P1Name.setText(battle.getPlayerCreatureName(Player.PLAYER1));
                P1StatsCreatureName.setText(battle.getPlayerCreatureName(Player.PLAYER1));
                P1Name.setText(battle.getPlayerCreatureName(Player.PLAYER1));
                P1HP.setText(Integer.toString(creature.getEnergy()));
                P1ST.setText(Integer.toString(creature.getStrike()));
                P1DF.setText(Integer.toString(creature.getDefense()));

                if (died) {
                    String drawable = battle.getBattleState().getPlayer1CurrentCreature().getImageName();
                    int resID = getResources().getIdentifier(drawable, "drawable", getApplicationContext().getPackageName());

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeResource(getApplicationContext().getResources(), resID, options);

                    P1Image.setImageBitmap(
                            ImageHelper.decodeSampledBitmapFromResource(getApplicationContext().getResources(), resID, 100, 100));
                }
                break;
            case PLAYER2:
                creature = battle.getBattleState().getPlayer2CurrentCreature();
                P2StatsCreatureName.setText(battle.getPlayerCreatureName(Player.PLAYER2));
                P2Name.setText(battle.getPlayerCreatureName(Player.PLAYER2));
                P2HP.setText(Integer.toString(creature.getEnergy()));
                P2ST.setText(Integer.toString(creature.getStrike()));
                P2DF.setText(Integer.toString(creature.getDefense()));

                if (died) {
                    String drawable = battle.getBattleState().getPlayer2CurrentCreature().getImageName();
                    int resID = getResources().getIdentifier(drawable, "drawable", getApplicationContext().getPackageName());

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeResource(getApplicationContext().getResources(), resID, options);

                    P2Image.setImageBitmap(
                            ImageHelper.decodeSampledBitmapFromResource(getApplicationContext().getResources(), resID, 100, 100));
                }

                break;
        }
    }

    public void updateGameMSG(String s) {
        //gameMsg.setText(s);
        gameMsg.append("\n" + s);
        gameMsg.setSelection(gameMsg.length());
    }

    public Button getButtonTurn() {
        return buttonTurn;
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
                    deviceHardwareAddress = device.getAddress(); // MAC address

                    if (deviceHardwareAddress.equals("C8:38:70:24:9D:EE") || deviceHardwareAddress.equals("78:52:1A:0B:C3:A8") ) {
                        ConnectThread thread = new ConnectThread(device);
                        thread.run();
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

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            manageMyConnectedSocket(mmSocket);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }

    private void manageMyConnectedSocket(BluetoothSocket mmSocket) {
        InputStream socketInputStream = null;
        try {
            socketInputStream = mmSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buffer = new byte[1024];
        int numBytes;

        // Keep looping to listen for received messages
        while (true) {
            try {
                numBytes = socketInputStream.read(buffer);            //read bytes from input buffer
                readMessage = new String(buffer, 0, numBytes);
//                readMessage = mHandler.obtainMessage(
//                        MessageConstants.MESSAGE_READ, numBytes, -1,
//                        buffer);
                // Send the obtained bytes to the UI Activity via handler
                Log.i("logging", readMessage + "");
            } catch (IOException e) {
                break;
            }
        }
        P1Name.setText(readMessage);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        if (battle != null) {
            if (battle.getBattleState().getPlayer1CurrentCreature() != null) {
                savedInstanceState.putString("name", battle.getBattleState().getPlayer1CurrentCreature().getName());
                savedInstanceState.putInt("energy", battle.getBattleState().getPlayer1CurrentCreature().getEnergy());
                savedInstanceState.putInt("strike", battle.getBattleState().getPlayer1CurrentCreature().getStrike());
                savedInstanceState.putInt("defense", battle.getBattleState().getPlayer1CurrentCreature().getDefense());
                savedInstanceState.putString("imageName", battle.getBattleState().getPlayer1CurrentCreature().getImageName());
            }
        }
    }

    private void SavePreferences() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor creatures = sharedPreferences.edit();
        creatures.putString("hero_name", this.battle.getBattleState().getPlayer1CurrentCreature().getName());
        creatures.putInt("hero_energy", this.battle.getBattleState().getPlayer1CurrentCreature().getEnergy());
        creatures.putInt("hero_strike", this.battle.getBattleState().getPlayer1CurrentCreature().getStrike());
        creatures.putInt("hero_defense", this.battle.getBattleState().getPlayer1CurrentCreature().getDefense());
        creatures.putString("hero_imageName", this.battle.getBattleState().getPlayer1CurrentCreature().getImageName());

        creatures.putString("enemy_name", this.battle.getBattleState().getPlayer2CurrentCreature().getName());
        creatures.putInt("enemy_energy", this.battle.getBattleState().getPlayer2CurrentCreature().getEnergy());
        creatures.putInt("enemy_strike", this.battle.getBattleState().getPlayer2CurrentCreature().getStrike());
        creatures.putInt("enemy_defense", this.battle.getBattleState().getPlayer2CurrentCreature().getDefense());
        creatures.putString("enemy_imageName",this.battle.getBattleState().getPlayer2CurrentCreature().getImageName());
        creatures.apply();
    }

    private void LoadPreferences() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

        List<ICreature> creaturesP1 = new ArrayList<ICreature>();
        List<ICreature> creaturesP2 = new ArrayList<ICreature>();

        if (sharedPreferences.getString("hero_name", null) != null) {

            String h_name = sharedPreferences.getString("hero_name", null);
            int h_energy = sharedPreferences.getInt("hero_energy", 0);
            int h_strike = sharedPreferences.getInt("hero_strike", 0);
            int h_defense = sharedPreferences.getInt("hero_defense", 0);
            String h_imageName = sharedPreferences.getString("hero_imageName", null);

            //String name, int energy, int strike, int defense, String imageName, String type
            Creature c = new Creature("", h_name, h_energy, h_strike, h_defense, h_imageName, "");
            creaturesP1.add(c);
        }

        if (sharedPreferences.getString("enemy_name", null) != null) {

            String e_name = sharedPreferences.getString("enemy_name", null);
            int e_energy = sharedPreferences.getInt("enemy_energy", 0);
            int e_strike = sharedPreferences.getInt("enemy_strike", 0);
            int e_defense = sharedPreferences.getInt("enemy_defense", 0);
            String e_imageName = sharedPreferences.getString("enemy_imageName", null);

            //String name, int energy, int strike, int defense, String imageName, String type
            Creature c = new Creature("", e_name, e_energy, e_strike, e_defense, e_imageName, "");
            creaturesP2.add(c);
        }
    }

    @Override
    public void onBackPressed() {
        if (creature != null) {
            SavePreferences();
        }
        super.onBackPressed();
    }
}