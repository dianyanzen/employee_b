package id.co.arkamaya.bc_android;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import id.co.arkamaya.cico.R;
import pojo.CheckLogin;
import pojo.GetOvertimeList;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OvertimeActivity extends AppCompatActivity {

    private String OvertimeId;
    SharedPreferences pref;
    private ProgressDialog progress;

    private EditText txtOvertimeDate,txtOvertimeDescription,txtovertimeFrom,txtovertimeTo;
    private CheckBox cbInc;
    private Button bntDatePicker;

    public String ENDPOINT="http://bc-id.co.id/";

    private String modeEdit;
    /**UserInfo**/
    String User="";
    String EmployeeId="";
    String CbInclude;
    int aspectX;
    int aspectY;
    int outputX;
    int outputY;

    ConnectionDetector conDetector;

    /**DatePicker**/
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overtime);

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

        txtOvertimeDate = (EditText)findViewById(R.id.txtOvertimeDate);
        txtOvertimeDescription = (EditText)findViewById(R.id.txtOvertimeDescription);
        txtovertimeFrom = (EditText)findViewById(R.id.txtOvertimeTime1);
        txtovertimeTo = (EditText)findViewById(R.id.txtOvertimeTime2);
        cbInc = (CheckBox)findViewById(R.id.cbInclude);
        if(cbInc.isChecked()){
            CbInclude = "1";
        }else{
            CbInclude = "0";
        }
//        txtOvertime = (EditText)findViewById(R.id.txtOvertime);

        if (extras != null) {
            modeEdit=extras.getString("Mode");
            if(modeEdit.equals("edit")){
                modeEdit="edit";
                OvertimeId = extras.getString("OvertimeId");
                getOvertimeById(OvertimeId);
            }else{
                modeEdit="new";
                txtOvertimeDate.setText("");
                txtOvertimeDescription.setText("");
                txtovertimeFrom.setText("");
                txtovertimeTo.setText("");
//                txtOvertime.setText("");
                setDate();
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
                                    onSaveOvertime();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Internet Connection Error..", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        Button btnDatePicker=(Button)findViewById(R.id.btnDatePicker);
        Button btnTimePicker1=(Button)findViewById(R.id.btnDatePicker2);
        Button btnTimePicker2=(Button)findViewById(R.id.btnDatePicker3);
        btnDatePicker.performClick();
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });
        btnTimePicker1.performClick();
        btnTimePicker1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime1();
            }
        });
        btnTimePicker2.performClick();
        btnTimePicker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime2();
            }
        });

    }

    protected void onSaveOvertime(){
        String overtime_date = txtOvertimeDate.getText().toString();
        String overtime_description = txtOvertimeDescription.getText().toString();
        String overtime_from = txtovertimeFrom.getText().toString();;
        String overtime_to = txtovertimeTo.getText().toString();;
        String overtime_inc = CbInclude;
        String overtime_id = OvertimeId;

        /*
        if(!overtime_date.equals("")|| !overtime_description.equals("") || !overtime_hour.equals("")) {

            if(Integer.parseInt(overtime_hour) > 1 & Integer.parseInt(overtime_hour) <= 24){
                progress = new ProgressDialog(this);
                progress.setMessage("Processing..");
                progress.setIndeterminate(true);
                progress.setCancelable(false);
                progress.show();
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(ENDPOINT)
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .build();

                APIOvertime restInterface = restAdapter.create(APIOvertime.class);

                restInterface.onSaveOvertime(overtime_id, overtime_date, overtime_description, overtime_hour,
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

        if(overtime_date.equals("")|| overtime_description.equals("") || overtime_from.equals("")) {
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

                APIOvertime restInterface = restAdapter.create(APIOvertime.class);

                restInterface.onSaveOvertime(overtime_id,EmployeeId,User, overtime_date,overtime_from,overtime_to,overtime_inc, overtime_description,
                          new Callback<CheckLogin>() {
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

    private void getOvertimeById(String OvertimeId){
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

        APIOvertime restInterface = restAdapter.create(APIOvertime.class);

        restInterface.getOvertimeById(OvertimeId, new Callback<GetOvertimeList>() {
            @Override
            public void success(GetOvertimeList m, Response response) {

                txtOvertimeDate.setText(m.getOtDt().toString());
                txtOvertimeDescription.setText(m.getOtDescription().toString());
                txtovertimeFrom.setText(m.getOtFrom().toString());
                txtovertimeTo.setText(m.getOtTo().toString());
                //txtOvertime.setText(m.getOtHour().toString());
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
    public void setDate() {
        showDialog(100);
    }
    public void setTime1() {
        showDialog(200);
    }
    public void setTime2() {
        showDialog(300);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 100) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }else if (id == 200){
            return new TimePickerDialog(this, myTime1 , hour, minute, false);
        }else if (id == 300){
            return new TimePickerDialog(this, myTime2 , hour, minute, false);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        txtOvertimeDate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }

    private TimePickerDialog.OnTimeSetListener myTime1 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
            showTime1(arg1, arg2);
        }
    };
    private void showTime1(int hour, int minute) {
        txtovertimeFrom.setText(new StringBuilder().append(hour).append(":")
                .append(minute));
    }

    private TimePickerDialog.OnTimeSetListener myTime2 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
            showTime2(arg1, arg2);
        }
    };
    private void showTime2(int hour, int minute) {
        txtovertimeTo.setText(new StringBuilder().append(hour).append(":")
                .append(minute));
    }
}