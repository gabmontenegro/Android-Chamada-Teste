package com.ufam.gabriel.chamadateste;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private TelephonyManager mTelephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button mDialButton = (Button) findViewById(R.id.button_chamada);
        final EditText mPhoneNoEt = (EditText) findViewById(R.id.editText_num_tel);

        mTelephonyManager = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);

        mDialButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                String phoneNo = mPhoneNoEt.getText().toString();
                if(!TextUtils.isEmpty(phoneNo))
                {
                    if(isPermissionGranted())
                    {
                        call_action(phoneNo);
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Digite um número de telefone!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    protected void onResume()
    {
        super.onResume();
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }
    @Override
    protected void onStop()
    {
        super.onStop();
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
    }

    public void call_action(String phonenumb)
    {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phonenumb));
        startActivity(callIntent);
    }

    //Método para verificar as permissões de uso do telefone
    public  boolean isPermissionGranted()
    {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }

    PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);

            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Toast.makeText(MainActivity.this, "CALL_STATE_IDLE", Toast.LENGTH_SHORT).show();
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Toast.makeText(MainActivity.this, "CALL_STATE_RINGING", Toast.LENGTH_SHORT).show();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Toast.makeText(MainActivity.this, "CALL_STATE_OFFHOOK", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


}