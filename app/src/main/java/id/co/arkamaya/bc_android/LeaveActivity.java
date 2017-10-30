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
import pojo.GetLeaveList;
import pojo.GetLeaveType;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by root on 18/10/17.
 */

public class LeaveActivity extends AppCompatActivity {
    private String LeaveId;
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
    EditText txtlvdescrip,txtlvfromdt,txtlvtodt;
    Spinner spnlvtype;
    Button btnCancel,btnlvfromdt,btnlvtodt,btnSave;
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
        setContentView(R.layout.activity_leave);
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        EmployeeId = pref.getString("EmployeeId", null);
        ENDPOINT = pref.getString("URLEndPoint", "");
        User = pref.getString("User", null);
        conDetector = new ConnectionDetector(getApplicationContext());
        spnlvtype = (Spinner)findViewById(R.id.spnlvtype);
        getType();
        txtlvdescrip = (EditText)findViewById(R.id.txtlvdescrip);
        txtlvfromdt = (EditText)findViewById(R.id.txtlvfromdt);
        txtlvtodt = (EditText)findViewById(R.id.txtlvtodt);
        btnlvfromdt = (Button)findViewById(R.id.btnlvfromdt);
        btnlvfromdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setfromdate();
            }
        });
        btnlvtodt = (Button)findViewById(R.id.btnlvtodt);
        btnlvtodt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settodate();
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
                LeaveId = extras.getString("LeaveId");
                try {
                    getLeaveById(LeaveId);
                    Log.e("Dugi Dieu",LeaveId);

                } catch (Exception e) {
                    Log.e("yz",e.toString());
                }

            } else {
                modeEdit = "new";
                LeaveId = "";
                txtlvdescrip.setText("");
                txtlvfromdt.setText("");
                txtlvtodt.setText("");
            }

        } else {
            Toast.makeText(this, "Invalid parameter", Toast.LENGTH_SHORT).show();
            finish();
        }
        btnCancel =(Button)findViewById(R.id.btnCancelLv);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSave = (Button)findViewById(R.id.btnSaveLv);
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
        String leave_id = LeaveId;
        String leave_type = ((String) spnlvtype.getSelectedItem());
        String leave_descript = txtlvdescrip.getText().toString();
        String leave_from_date = txtlvfromdt.getText().toString();
        String leave_to_date = txtlvtodt.getText().toString();
        String employee_id = EmployeeId;
        if(employee_id.equals("") || leave_from_date.equals("") || leave_to_date.equals("") || leave_type.equals("")) {
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

            ApiLeave restInterface = restAdapter.create(ApiLeave.class);
            restInterface.onSaveLeave(leave_id
                    ,employee_id
                    ,User
                    ,leave_type
                    ,leave_descript
                    ,leave_from_date
                    ,leave_to_date
                    , new Callback<CheckLogin>() {
                        @Override
                        public void success(CheckLogin m, Response response) {

                            if (progress != null) {
                                progress.dismiss();
                            }

                            Toast.makeText(LeaveActivity.this, m.getMsgText(), Toast.LENGTH_SHORT).show();

                            if (m.getMsgType().toLowerCase().equals("info")) {
                                finish();
                            } else {

                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(LeaveActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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


        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        ApiLeave restInterface = restAdapter.create(ApiLeave.class);

        restInterface.getLeaveType(new Callback<List<GetLeaveType>>() {
            @Override
            public void success(List<GetLeaveType> m, Response response) {
                List<String> list = new ArrayList<String>();

                for (int i = 0; i < m.size(); i++) {
                    list.add(m.get(i).getLeaveNm());
                }


                adaAdapterType = new ArrayAdapter<String>
                (LeaveActivity.this, R.layout.spinner_simple_item, R.id.listCombo, list);

        adaAdapterType.setDropDownViewResource
                (R.layout.spinner_simple_item);

        spnlvtype.setAdapter(adaAdapterType);

        String data = data_type;
        if (data != "") {
            int idxLocation = adaAdapterType.getPosition(data_type);
            spnlvtype.setSelection(idxLocation);
        }
        /*spnlvtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_search = false;
            }
        });*/
        spnlvtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getLeaveById(String LeaveId){
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

        ApiLeave restInterface = restAdapter.create(ApiLeave.class);

        restInterface.getLeaveById(LeaveId, new Callback<GetLeaveList>() {
            @Override
            public void success(GetLeaveList m, Response response) {
                try {
                    txtlvdescrip.setText(m.getLeaveReason().toString());
                    txtlvfromdt.setText(m.getLeaveDt().toString());
                    txtlvtodt.setText(m.getLeaveDt().toString());
                    String Type = m.getTimeOffType().toString();
                    if (Type.equalsIgnoreCase("cuti")) {
                        idxType = 0;
                    }else if (Type.equalsIgnoreCase("cuti mendadak")) {
                        idxType = 1;
                    }else if (Type.equalsIgnoreCase("cuti 1/2 hari")) {
                        idxType = 2;
                    }else if (Type.equalsIgnoreCase("cuti dadakan")) {
                        idxType = 3;
                    }else if (Type.equalsIgnoreCase("cuti haid")) {
                        idxType = 4;
                    }else if (Type.equalsIgnoreCase("cuti hamil")) {
                        idxType = 5;
                    }else if (Type.equalsIgnoreCase("pernikahan")) {
                        idxType = 6;
                    }else if (Type.equalsIgnoreCase("kelahiran / gugur kandungan")) {
                        idxType = 7;
                    }else if (Type.equalsIgnoreCase("khitanan")) {
                        idxType = 8;
                    }else if (Type.equalsIgnoreCase("pemandian / baptis / gusaran anak")) {
                        idxType = 9;
                    }else if (Type.equalsIgnoreCase("pernikahan anak")) {
                        idxType = 10;
                    }else if (Type.equalsIgnoreCase("kematian keluarga")) {
                        idxType = 11;
                    }else if (Type.equalsIgnoreCase("musibah bencana alam")) {
                        idxType = 12;
                    }else if (Type.equalsIgnoreCase("kematian anggota keluarga")) {
                        idxType = 13;
                    }else if (Type.equalsIgnoreCase("balita sakit")) {
                        idxType = 14;
                    }else if (Type.equalsIgnoreCase("ibadah haji")) {
                        idxType = 15;
                    }else if (Type.equalsIgnoreCase("lain-lain")) {
                        idxType = 16;
                    }else if (Type.equalsIgnoreCase("cuti tidak dibayar")) {
                        idxType = 17;
                    }else if (Type.equalsIgnoreCase("cuti (1/2 hari) tidak dibayar")) {
                        idxType = 18;
                    }else if (Type.equalsIgnoreCase("sakit rawat jalan")) {
                        idxType = 19;
                    }else if (Type.equalsIgnoreCase("sakit rawat inap")) {
                        idxType = 20;
                    }else {
                        idxType = 0;
                    }
                    spnlvtype.setSelection(idxType);
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
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Menu 1
        if (id == 100) {
            return new DatePickerDialog(this, myFromdate, year, month, day);
        }
        else if (id == 200) {
            return new DatePickerDialog(this, myTodate, year, month, day);
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
        txtlvfromdt.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }
    private DatePickerDialog.OnDateSetListener myTodate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showmyTodate(arg1, arg2 + 1, arg3);
        }
    };
    private void showmyTodate(int year, int month, int day) {
        txtlvtodt.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }
}

