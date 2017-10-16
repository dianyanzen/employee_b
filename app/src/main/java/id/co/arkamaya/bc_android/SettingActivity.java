package id.co.arkamaya.bc_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import id.co.arkamaya.cico.R;

public class SettingActivity extends AppCompatActivity {
    SharedPreferences pref;
    EditText txtURLEndPoint;
    EditText txtURLPMSEndPoint;
    Button btnSaveURLEndPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        txtURLEndPoint= (EditText)findViewById(R.id.txtURLEndPoint);
        txtURLPMSEndPoint= (EditText)findViewById(R.id.txtURLPMSEndPoint);
        btnSaveURLEndPoint=(Button)findViewById(R.id.btnSaveURLEndPoint);

        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(pref.contains("URLEndPoint") && pref.contains("URLPMSEndPoint")){
            txtURLEndPoint.setText(pref.getString("URLEndPoint", ""));
            txtURLPMSEndPoint.setText(pref.getString("URLPMSEndPoint", ""));

        }

        btnSaveURLEndPoint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(txtURLEndPoint.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "URL End Point cant't be empty..", Toast.LENGTH_SHORT).show();
                }else if(txtURLPMSEndPoint.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "URL PMS End Point cant't be empty..", Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("URLEndPoint", txtURLEndPoint.getText().toString());
                    editor.putString("URLPMSEndPoint", txtURLPMSEndPoint.getText().toString());
                    editor.commit();
                    Intent intent = new Intent(v.getContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        if(txtURLEndPoint.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "URL End Point cant't be empty..", Toast.LENGTH_SHORT).show();
        }else if(txtURLPMSEndPoint.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "URL PMS End Point cant't be empty..", Toast.LENGTH_SHORT).show();
        }else{
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("URLEndPoint", txtURLEndPoint.getText().toString());
            editor.putString("URLPMSEndPoint", txtURLPMSEndPoint.getText().toString());
            editor.commit();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }


}
