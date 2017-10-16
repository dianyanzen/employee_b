package id.co.arkamaya.bc_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import id.co.arkamaya.cico.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AppCompatActivity {
    //public static final String ENDPOINT="http://192.168.1.146:86/arka_portal/index.php/";
    public String ENDPOINT="http://192.168.88.158:86/arka_portal/index.php/";
    public String PMSENDPOINT="http://192.168.88.158:86/arka_portal/index.php/";
    private ProgressDialog progress;
    EditText txtUserName;
    EditText txtPassword;
    Button btnSetting;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUserName = (EditText)findViewById((R.id.txtUserName));
        txtPassword = (EditText)findViewById((R.id.txtPassword));

        final Button button = (Button) findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickAction(v);
            }
        });

        btnSetting=(Button)findViewById((R.id.btnSetting));
        btnSetting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                moveToSettingActivity();
            }
        });


        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if(pref.contains("URLEndPoint")) {
            ENDPOINT = pref.getString("URLEndPoint", "");
        }else if(pref.contains("URLPMSEndPoint")){
            PMSENDPOINT = pref.getString("URLPMSEndPoint", "");
        }else{
            Toast.makeText(getApplicationContext(), "Web Service URL Not Set..", Toast.LENGTH_SHORT).show();
            moveToSettingActivity();
        }

        if(pref.contains("User")){
            moveToMainActivity();
        }

    }

    public void moveToSettingActivity(){
        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
        startActivity(intent);
        //finish();
    }

    public void moveToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickAction(View v){

        final String USER_NAME=txtUserName.getText().toString();
        final String PASSWORD=txtPassword.getText().toString();

        progress = new ProgressDialog(v.getContext());
        progress.setMessage("Login...");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        Log.d("Yudha", USER_NAME + "-" + PASSWORD);
        APILogin restInterface = adapter.create(APILogin.class);

        restInterface.checkLogin(USER_NAME,PASSWORD, new Callback<pojo.CheckLogin>() {
            @Override
            public void success(pojo.CheckLogin m, Response response) {
                //String body = new String(((TypedByteArray) response.getBody()).getBytes());
                Toast.makeText(getApplicationContext(), m.getMsgText(), Toast.LENGTH_SHORT).show();

                if(m.getMsgType().toLowerCase().equals("info")){

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("User", m.getEmployeeName());
                    editor.putString("EmployeeId", m.getEmployeeId());
                    editor.putString("EmployeeCd",USER_NAME);
                    editor.putString("LocationType", "");
                    editor.putString("Place", "");
                    editor.putString("Project", "");
                    editor.commit();
                    moveToMainActivity();
                }
                if (progress != null) {
                    progress.dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                if (progress != null) {
                    progress.dismiss();
                }
            }
        });
    }
}
