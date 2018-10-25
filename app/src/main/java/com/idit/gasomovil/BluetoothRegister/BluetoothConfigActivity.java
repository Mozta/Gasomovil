package com.idit.gasomovil.BluetoothRegister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.idit.gasomovil.R;

public class BluetoothConfigActivity extends AppCompatActivity {

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final String EXTRAS_DEVICE_ID = "DEVICE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_config);

        final String bluetoothName = getIntent().getStringExtra(EXTRAS_DEVICE_ID);
        final String bluetoothAddress = getIntent().getStringExtra(EXTRAS_DEVICE_ADDRESS);

        final TextView idText = findViewById(R.id.id_bt_txt);
        TextView macText = findViewById(R.id.mac_address_bt_txt);

        final EditText nameInput = findViewById(R.id.name_bt_field);
        Button saveButton = findViewById(R.id.bt_config_save_button);

        idText.setText(bluetoothName);
        macText.setText(bluetoothAddress);

        final Intent finishConfig = new Intent(this, BluetoothFinishActivity.class);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nameInput.getText().equals("")){
                    finishConfig.putExtra(BluetoothConfigActivity.EXTRAS_DEVICE_NAME, idText.getText().toString());
                }else {
                    finishConfig.putExtra(BluetoothConfigActivity.EXTRAS_DEVICE_NAME, nameInput.getText().toString());
                }
                finishConfig.putExtra(BluetoothConfigActivity.EXTRAS_DEVICE_ADDRESS, bluetoothAddress);
                finishConfig.putExtra(BluetoothConfigActivity.EXTRAS_DEVICE_ID, idText.getText());

                startActivity(finishConfig);
            }
        });
    }
}
