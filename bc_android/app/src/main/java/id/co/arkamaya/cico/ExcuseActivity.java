package id.co.arkamaya.cico;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pojo.CheckLogin;
import pojo.GetExcuseList;
import pojo.GetExcuseType;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by root on 10/10/17.
 */

public class ExcuseActivity extends AppCompatActivity {

    private String ExcuseId;
    SharedPreferences pref;
    private ProgressDialog progress;
    private Spinner spnExcuseType;
    private EditText txttodate;
    private EditText txtfromdate;
    private EditText txtExcuseDescription;
    private EditText txtExcuse;
    private Button bntDatePicker;
    ArrayAdapter<String> dataAdapterExcuseType;
    public String ENDPOINT="http://192.168.88.153:8080/arka_portal";

    private String modeEdit;
    /**UserInfo**/
    String User="";
    String EmployeeId="";
    public String data_type = "";
    int aspectX;
    int aspectY;
    int outputX;
    int outputY;

    ConnectionDetector conDetector;

    /**DatePicker**/
    private int year, month, day;

    private class ScreenResolution {
        int width, height;
        public ScreenResolution(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    ScreenResolution deviceDimensions() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        // getsize() is available from API 13
        if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return new ScreenResolution(size.x, size.y);
        } else {
            Display display = getWindowManager().getDefaultDisplay();
            // getWidth() & getHeight() are depricated
            return new ScreenResolution(display.getWidth(), display.getHeight());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excuse);
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        EmployeeId=pref.getString("EmployeeId", null);
        ENDPOINT= pref.getString("URLEndPoint", "");
        User= pref.getString("User",null);
        EmployeeId=pref.getString("EmployeeId",null);
        conDetector =  new ConnectionDetector(getApplicationContext());

        Bundle extras = getIntent().getExtras();

        ScreenResolution sr = deviceDimensions();
        // use Euclid's theorem to calculate the proper aspect ratio i.e. screen
        // i.e. for resolution 480 * 800, aspect Ratio 3:5
        int gcd = GCD(sr.width, sr.height);
        aspectX = sr.width / gcd;
        aspectY = sr.height / gcd;
        // subtract to keep the output image's width & height possibly low as android
        // default crop is not well suited to pick big image
        outputX = sr.width - aspectX * 30;
        outputY = sr.height - aspectY * 30;

        Log.d("Yudha", "ox " + outputX + " oy " + outputY + " ax " + aspectX + " ay " + aspectY);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        txttodate = (EditText)findViewById(R.id.txttodate);
        txtfromdate = (EditText)findViewById(R.id.txtfromdate);
        spnExcuseType = (Spinner) findViewById(R.id.spnType);
        txtExcuseDescription = (EditText)findViewById(R.id.txtExcuseDescription);
//        txtExcuse = (EditText)findViewById(R.id.txtExcuseId);

        if (extras != null) {

            modeEdit=extras.getString("Mode");
            if(modeEdit.equals("edit")){
                modeEdit="edit";
                ExcuseId = extras.getString("ExcuseId");
                getExcuseType(data_type);
                getExcuseById(ExcuseId);
            }else{
                modeEdit="new";
                ExcuseId="";
                txtfromdate.setText("");
                txttodate.setText("");
                txtExcuseDescription.setText("");
                getExcuseType(data_type);
                /*spnExcuseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        String data_spl = parent.getItemAtPosition(pos).toString();
                        String[] separated = data_spl.split(" - ");

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }
                });*/


                //setDate();
            }

        }else{
            Toast.makeText(this, "Invalid parameter", Toast.LENGTH_SHORT).show();
            finish();
        }

        Button btnCancel =(Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnSave=(Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setMessage("Are you sure?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if(conDetector.isConnectingToInternet()){
                                    onSaveExcuse();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Internet Connection Error..", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        Button btnDatePicker1=(Button)findViewById(R.id.BtnExcFromDt);
        btnDatePicker1.performClick();
        btnDatePicker1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDatefrom();
            }
        });
        Button btnDatePicker2=(Button)findViewById(R.id.BtnExcToDt);
        btnDatePicker2.performClick();
        btnDatePicker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateto();
            }
        });

    }
    private void getExcuseType(final String data_type){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        ApiExcuse restInterface = restAdapter.create(ApiExcuse.class);

        restInterface.getExcuseType(new Callback<List<GetExcuseType>>() {
            @Override
            public void success(List<GetExcuseType> m, Response response) {
                List<String> list = new ArrayList<String>();

                for (int i = 0; i < m.size(); i++) {
                    list.add(m.get(i).getExcuseType());
                }


                //Toast.makeText(getApplicationContext(), list_cd.get(0), Toast.LENGTH_LONG).show();

                dataAdapterExcuseType = new ArrayAdapter<String>
                        (getApplicationContext(), R.layout.spinner_simple_item, R.id.listCombo, list);

                dataAdapterExcuseType.setDropDownViewResource
                        (R.layout.spinner_simple_item);

                spnExcuseType.setAdapter(dataAdapterExcuseType);

                String data = data_type;

                if (data != "") {
                    int idxLocation = dataAdapterExcuseType.getPosition(data_type);
                    spnExcuseType.setSelection(idxLocation);
                }

                spnExcuseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        String data_spl = parent.getItemAtPosition(pos).toString();
                        String[] separated = data_spl.split(" - ");
                        //String data = ECS20151201;

                        //Toast.makeText(getApplicationContext(), separated[0].replace(" ", "").toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onSaveExcuse(){
        String date_from = txtfromdate.getText().toString();
        String date_to = txttodate.getText().toString();
        String excuse_reason = txtExcuseDescription.getText().toString();
        String excuse_type = spnExcuseType.getSelectedItem().toString();
        String excuse_id = ExcuseId;

        /*
        if(!excuse_date.equals("")|| !excuse_description.equals("") || !excuse_hour.equals("")) {

            if(Integer.parseInt(excuse_hour) > 1 & Integer.parseInt(excuse_hour) <= 24){
                progress = new ProgressDialog(this);
                progress.setMessage("Processing..");
                progress.setIndeterminate(true);
                progress.setCancelable(false);
                progress.show();
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(ENDPOINT)
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .build();

                APIExcuse restInterface = restAdapter.create(APIExcuse.class);

                restInterface.onSaveExcuse(excuse_id, excuse_date, excuse_description, excuse_hour,
                    User, EmployeeId, new Callback<CheckLogin>() {
                        @Override
                        public void success(CheckLogin m, Response response) {

                            if (progress != null) {
                                progress.dismiss();
                            }

                            Toast.makeText(getApplicationContext(), m.getMsgText(), Toast.LENGTH_SHORT).show();

                            if (m.getMsgType().toLowerCase().equals("info")) {
                                finish();
                            } else {

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
            }else {
                Toast.makeText(getApplicationContext(), "Tidak boleh mengisi lebih dari 24 jam ", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(getApplicationContext(), "Data tidak lengkap..", Toast.LENGTH_LONG).show();
        }
        */

        if(date_from.equals("")|| date_to.equals("") || excuse_reason.equals("")) {
            Toast.makeText(getApplicationContext(), "Data tidak lengkap..", Toast.LENGTH_LONG).show();
        }else{
                progress = new ProgressDialog(this);
                progress.setMessage("Processing..");
                progress.setIndeterminate(true);
                progress.setCancelable(false);
                progress.show();
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(ENDPOINT)
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .build();

                ApiExcuse restInterface = restAdapter.create(ApiExcuse.class);

                restInterface.onSaveExcuse(excuse_id,EmployeeId, User, date_from, date_to, excuse_type,excuse_reason
                        , new Callback<CheckLogin>() {
                            @Override
                            public void success(CheckLogin m, Response response) {

                                if (progress != null) {
                                    progress.dismiss();
                                }

                                Toast.makeText(getApplicationContext(), m.getMsgText(), Toast.LENGTH_SHORT).show();

                                if (m.getMsgType().toLowerCase().equals("info")) {
                                    finish();
                                } else {

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

        }
    }

    private int GCD(int a, int b) {
        return (b == 0 ? a : GCD(b, a % b));
    }

    /**
     * this function does the crop operation.
     */

    private void getExcuseById(String ExcuseId){
        /*-------------------------------*/
        progress = new ProgressDialog(this);
        progress.setMessage("Load Data..");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        ApiExcuse restInterface = restAdapter.create(ApiExcuse.class);

        restInterface.getExcuseById(ExcuseId, new Callback<GetExcuseList>() {
            @Override
            public void success(GetExcuseList m, Response response) {

                //txtExcuse.setText(m.getExcuseId().toString());

                txtfromdate.setText(m.getExcuseDt().toString());
                 txttodate.setText(m.getExcuseTodt().toString());
                txtExcuseDescription.setText(m.getExcuseDescription().toString());
//                String Fragment GetExcuseList
                int idxLocation = dataAdapterExcuseType.getPosition(m.getExcuseType().toString());
                spnExcuseType.setSelection(idxLocation);
                try{
                    Log.e("data",toString().valueOf(idxLocation));
                }catch (Exception e){

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

    @SuppressWarnings("deprecation")
    public void setDateto() {
        showDialog(999);
    }
    public void setDatefrom() {
        showDialog(222);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListenerto, year, month, day);
        }else if (id == 222){
            return new DatePickerDialog(this, myDateListenerfrom, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListenerto = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showdtto(arg1, arg2 + 1, arg3);
        }
    };
    private DatePickerDialog.OnDateSetListener myDateListenerfrom = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showdtfrom(arg1, arg2 + 1, arg3);
        }
    };

    private void showdtto(int year, int month, int day) {
        txttodate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }
    private void showdtfrom(int year, int month, int day) {
        txtfromdate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }
}