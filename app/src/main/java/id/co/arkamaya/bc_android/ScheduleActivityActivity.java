package id.co.arkamaya.bc_android;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.co.arkamaya.cico.R;
import pojo.CheckLogin;
import pojo.GetScheduleActivityList;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by root on 20/10/17.
 */

public class ScheduleActivityActivity extends AppCompatActivity {
    private String ScheduleId;
    String getType = "";
    String getdescript = "";
    SharedPreferences pref;
    private ProgressDialog progress;
    public String ENDPOINT = "http://bc-id.co.id/";
    int idxType,idxdescript;
    ArrayAdapter<String> adaAdapterType,adaAdapterdescript;
    private String modeEdit;
    Boolean is_search = false;
    /**
     * UserInfo
     **/
    String User = "";
    String EmployeeId = "";
    public String data_type = "";
    public String data_descript = "";
    int aspectX;
    int aspectY;
    int outputX;
    int outputY;
    EditText txtscddescrip,txtscdfromdt,txtscdscdduedt,txtscdscdtodt;
    Spinner spnscdtype;
    Button btnCancel,btnscdfromdt,btnscdtodt,btnscdduedt,btnSave;
    ConnectionDetector conDetector;
    Bundle extras;
    /**
     * DatePicker
     **/
    private int year, month, day;

    private int hour, minute;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity_add);
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        EmployeeId = pref.getString("EmployeeId", null);
        ENDPOINT = pref.getString("URLEndPoint", "");
        User = pref.getString("User", null);
        conDetector = new ConnectionDetector(getApplicationContext());
        spnscdtype = (Spinner)findViewById(R.id.spnscdtype);
        getType();
        txtscddescrip = (EditText)findViewById(R.id.txtscddescrip);
        txtscdfromdt = (EditText)findViewById(R.id.txtscdfromdt);
        txtscdscdduedt = (EditText)findViewById(R.id.txtscdscdduedt);
        txtscdscdtodt = (EditText)findViewById(R.id.txtscdscdtodt);
        btnscdfromdt = (Button)findViewById(R.id.btnscdfromdt);
        btnscdfromdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setfromdate();
            }
        });
        btnscdtodt = (Button)findViewById(R.id.btnscdtodt);
        btnscdtodt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settodate();
            }
        });
        btnscdduedt = (Button)findViewById(R.id.btnscdduedt);
        btnscdduedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setduedate();
            }
        });
        extras = getIntent().getExtras();

        ScreenResolution sr = deviceDimensions();
        int gcd = GCD(sr.width, sr.height);
        aspectX = sr.width / gcd;
        aspectY = sr.height / gcd;
        outputX = sr.width - aspectX * 30;
        outputY = sr.height - aspectY * 30;

        Log.d("Yudha", "ox " + outputX + " oy " + outputY + " ax " + aspectX + " ay " + aspectY);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        if (extras != null) {

            modeEdit = extras.getString("Mode");
            if (modeEdit.equals("edit")) {
                modeEdit = "edit";
                ScheduleId = extras.getString("ScheduleId");
                try {
                    getScheduleById(ScheduleId);

                } catch (Exception e) {

                }

            } else {
                modeEdit = "new";
                ScheduleId = "";
                txtscddescrip.setText("");
                txtscdfromdt.setText("");
                txtscdscdduedt.setText("");
                txtscdscdtodt.setText("");
            }

        } else {
            Toast.makeText(this, "Invalid parameter", Toast.LENGTH_SHORT).show();
            finish();
        }
        /*spnscdtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_search = false;
            }
        });*/
        btnCancel =(Button)findViewById(R.id.btnCancelScd);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSave = (Button)findViewById(R.id.btnSaveScd);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setMessage("Are you sure?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if(conDetector.isConnectingToInternet()){
                                    onSaveClick();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Internet Connection Error..", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }
    protected void onSaveClick(){
        String schedule_id = ScheduleId;
        String schedule_type = ((String) spnscdtype.getSelectedItem());
        String schedule_descript = txtscddescrip.getText().toString();
        String schedule_from_date = txtscdfromdt.getText().toString();
        String schedule_due_date = txtscdscdduedt.getText().toString();
        String schedule_to_date = txtscdscdtodt.getText().toString();
        String employee_id = EmployeeId;
        if(employee_id.equals("") || schedule_descript.equals("") || schedule_type.equals("") || schedule_to_date.equals("") || schedule_from_date.equals((""))) {
            Toast.makeText(this, "Data tidak lengkap..", Toast.LENGTH_LONG).show();
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

            ApiScheduleActivity restInterface = restAdapter.create(ApiScheduleActivity.class);
            restInterface.onSaveScheduleActivity(schedule_id
                    ,employee_id
                    ,User
                    ,schedule_type
                    ,schedule_descript
                    ,schedule_from_date
                    ,schedule_to_date
                    ,schedule_due_date
                    , new Callback<CheckLogin>() {
                        @Override
                        public void success(CheckLogin m, Response response) {

                            if (progress != null) {
                                progress.dismiss();
                            }

                            Toast.makeText(ScheduleActivityActivity.this, m.getMsgText(), Toast.LENGTH_SHORT).show();

                            if (m.getMsgType().toLowerCase().equals("info")) {
                                finish();
                            } else {

                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(ScheduleActivityActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void getType(){

        List<String> list = new ArrayList<String>();
        list.clear();
        spnscdtype.setAdapter(null);
        list.add("Dinas Luar");
        list.add("Training");

        adaAdapterType = new ArrayAdapter<String>
                (ScheduleActivityActivity.this, R.layout.spinner_simple_item, R.id.listCombo, list);

        adaAdapterType.setDropDownViewResource
                (R.layout.spinner_simple_item);

        spnscdtype.setAdapter(adaAdapterType);

        String data = data_type;
        if (data != "") {
            int idxLocation = adaAdapterType.getPosition(data_type);
            spnscdtype.setSelection(idxLocation);
        }
        /*spnscdtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_search = false;
            }
        });*/
        spnscdtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

    }
    
    private void getScheduleById(String ScheduleId){
        /*-------------------------------*/
        is_search = true;
        progress = new ProgressDialog(this);
        progress.setMessage("Load Data..");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        ApiScheduleActivity restInterface = restAdapter.create(ApiScheduleActivity.class);

        restInterface.getScheduleActivityById(ScheduleId, new Callback<GetScheduleActivityList>() {
            @Override
            public void success(GetScheduleActivityList m, Response response) {
                try {
                    txtscddescrip.setText(m.getScheduleDescription().toString());
                    txtscdfromdt.setText(m.getScheduleDt().toString());
                    txtscdscdduedt.setText(m.getApprovalDueDt().toString());
                    txtscdscdtodt.setText(m.getScheduleDt().toString());
                    String Type = m.getScheduleType().toString();
                    if (Type.equalsIgnoreCase("training")) {
                        idxType = 1;
                    }else {
                        idxType = 0;
                    }
                    spnscdtype.setSelection(idxType);
                    btnSave.setText("UPDATE");

                }catch (Exception e){
//                    Toast.makeText(getApplicationContext(), "Connection Error Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }


                if (progress != null) {

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progress.dismiss();
                            is_search = false;
                        }
                    }, 100);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                if (progress != null) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progress.dismiss();
                            is_search = false;
                        }
                    }, 100);
                }
            }
        });
    }

    @SuppressWarnings("deprecation")

    public void setfromdate() {
        showDialog(100);
    }
    public void settodate() {
        showDialog(200);
    }
    public void setduedate() {
        showDialog(300);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Menu 1
        if (id == 100) {
            return new DatePickerDialog(this, myFromdate, year, month, day);
        }
        else if (id == 200) {
            return new DatePickerDialog(this, myTodate, year, month, day);
        }
        else if (id == 300) {
            return new DatePickerDialog(this, myDuedate, year, month, day);
        }
        else{
            return null;
        }

    }
    private DatePickerDialog.OnDateSetListener myFromdate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showmyFromdate(arg1, arg2 + 1, arg3);
        }
    };
    private void showmyFromdate(int year, int month, int day) {
        txtscdfromdt.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }
    private DatePickerDialog.OnDateSetListener myTodate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showmyTodate(arg1, arg2 + 1, arg3);
        }
    };
    private void showmyTodate(int year, int month, int day) {
        txtscdscdtodt.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }
    private DatePickerDialog.OnDateSetListener myDuedate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showmyDuedate(arg1, arg2 + 1, arg3);
        }
    };
    private void showmyDuedate(int year, int month, int day) {
        txtscdscdduedt.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }
}
