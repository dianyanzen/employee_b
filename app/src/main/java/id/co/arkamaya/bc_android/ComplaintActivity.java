package id.co.arkamaya.bc_android;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.co.arkamaya.cico.R;
import pojo.CheckLogin;
import pojo.GetComplaintList;
import pojo.GetComplaintType;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by wawan on 3/11/2016.
 */
public class ComplaintActivity extends AppCompatActivity {


    private String ComplaintId;
    SharedPreferences pref;
    private ProgressDialog progress;

    private Spinner spnComplaintType;
    private EditText txtComplaintDate;
    private EditText txtClockIn;
    private EditText txtClockOut;
    private EditText txtComplaintDescription;
    private Button btnClockIn;
    private Button btnClockOut;
    ArrayAdapter<String> dataAdapterComplaintType;
    ArrayAdapter<String> dataAdapterComplaintNm;

    String excuse_code_xx = "";

    public String ENDPOINT="http://bc-id.co.id/";

    private String modeEdit;

    int aspectX;
    int aspectY;
    int outputX;
    int outputY;


    /**UserInfo**/
    String User="";
    String EmployeeId="";


    /**DatePicker**/
    private int year, month, day;

    private int hour, minute;
    public String data_complaint = "";

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
        setContentView(R.layout.activity_complaint);

        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        EmployeeId=pref.getString("EmployeeId", null);
        ENDPOINT= pref.getString("URLEndPoint", "");
        User= pref.getString("User",null);
        EmployeeId=pref.getString("EmployeeCd",null);

        Bundle extras = getIntent().getExtras();

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);

        btnClockIn = (Button)findViewById(R.id.btnClockIn);
        btnClockOut = (Button)findViewById(R.id.btnClockOut);
        spnComplaintType = (Spinner) findViewById(R.id.spnComplaintType);
        txtComplaintDate=(EditText)findViewById(R.id.txtComplaintDate);
        txtClockIn = (EditText)findViewById(R.id.txtClockIn);
        txtClockOut = (EditText)findViewById(R.id.txtClockOut);
        txtComplaintDescription=(EditText)findViewById(R.id.txtComplaintDescription);

        if (extras != null) {
            modeEdit=extras.getString("Mode");
            if(modeEdit.equals("edit")){
                modeEdit="edit";
                ComplaintId = extras.getString("ComplaintId");
                /*Toast.makeText(this, "Reimburse id "+ReimburseId, Toast.LENGTH_SHORT).show();*/
                getComplaintById(ComplaintId);
            }else{
                modeEdit="new";
                txtComplaintDate.setText("");
                txtClockIn.setText("");
                txtComplaintDescription.setText("");
                setDate();
                getComplaintType(data_complaint);

                spnComplaintType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        String data_spl = parent.getItemAtPosition(pos).toString();
                        String[] separated = data_spl.split(" - ");
                        //String data = ECS20151201;
                        if (separated[0].equals("ECS20151201")) {
                            btnClockIn.setEnabled(true);
                            btnClockOut.setEnabled(false);
                        } else if (separated[0].equals("ECS20151202")) {
                            btnClockIn.setEnabled(false);
                            btnClockOut.setEnabled(true);
                        } else if (separated[0].equals("ECS20151207")) {
                            btnClockIn.setEnabled(true);
                            btnClockOut.setEnabled(true);
                        }else{
                            btnClockIn.setEnabled(false);
                            btnClockOut.setEnabled(false);
                            //Toast.makeText(getApplicationContext(), pos, Toast.LENGTH_LONG).show();
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }
                });
            }

        }else{
            Toast.makeText(this, "Parameter Salah...", Toast.LENGTH_SHORT).show();
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
                        .setMessage("Simpan Data?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                onSaveComplaint();
                            }
                        })
                        .setNegativeButton("Tidak", null)
                        .show();
            }
        });

        Button btnDatePicker=(Button)findViewById(R.id.btnDatePicker);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });

        btnClockIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime_In();
            }
        });

        btnClockOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime_Out();
            }
        });

    }

    private void getComplaintType(final String data_complaint){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIComplaint restInterface = restAdapter.create(APIComplaint.class);

        restInterface.getComplaintType(new Callback<List<GetComplaintType>>() {
            @Override
            public void success(List<GetComplaintType> m, Response response) {
                List<String> list = new ArrayList<String>();
                List<String> list_cd = new ArrayList<String>();

                for (int i = 0; i < m.size(); i++) {
                    list.add(m.get(i).getExcuseCd() + " - " + m.get(i).getExcuseNm());
                    list_cd.add(m.get(i).getExcuseCd());
                    excuse_code_xx = excuse_code_xx + "{" + m.get(i).getExcuseCd().toString() + "}" + ",";
                }


                //Toast.makeText(getApplicationContext(), list_cd.get(0), Toast.LENGTH_LONG).show();

                dataAdapterComplaintType = new ArrayAdapter<String>
                        (getApplicationContext(), R.layout.spinner_simple_item, R.id.listCombo, list);

                dataAdapterComplaintType.setDropDownViewResource
                        (R.layout.spinner_simple_item);

                spnComplaintType.setAdapter(dataAdapterComplaintType);

                String data = data_complaint;

                if (data != "") {
                    int idxLocation = dataAdapterComplaintType.getPosition(data_complaint);
                    spnComplaintType.setSelection(idxLocation);
                }

                spnComplaintType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        String data_spl = parent.getItemAtPosition(pos).toString();
                        String[] separated = data_spl.split(" - ");
                        //String data = ECS20151201;
                        if (separated[0].equals("ECS20151201")) {
                            txtClockOut.setText("");
                            btnClockIn.setEnabled(true);
                            btnClockOut.setEnabled(false);
                            //Toast.makeText(getApplicationContext(), pos, Toast.LENGTH_LONG).show();
                        } else if (separated[0].equals("ECS20151202")) {
                            txtClockIn.setText("");
                            btnClockIn.setEnabled(false);
                            btnClockOut.setEnabled(true);
                            //Toast.makeText(getApplicationContext(), pos, Toast.LENGTH_LONG).show();
                        } else if (separated[0].equals("ECS20151207")) {
                            btnClockIn.setEnabled(true);
                            btnClockOut.setEnabled(true);
                            //Toast.makeText(getApplicationContext(), pos, Toast.LENGTH_LONG).show();
                        }else{
                            txtClockIn.setText("");
                            txtClockOut.setText("");
                            btnClockIn.setEnabled(false);
                            btnClockOut.setEnabled(false);
                            //Toast.makeText(getApplicationContext(), pos, Toast.LENGTH_LONG).show();
                        }

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

    protected void onSaveComplaint(){
        String complaint_date = txtComplaintDate.getText().toString();
        //String complaint_type = spnComplaintType.getText().toString();
        String type = spnComplaintType.getSelectedItem().toString();
        String[] complaint_type = type.split(" - ");

        String complaint_description = txtComplaintDescription.getText().toString();
        String complaint_id = ComplaintId;
        String complaint_clock_in = txtClockIn.getText().toString();
        String complaint_clock_out = txtClockOut.getText().toString();

        if(complaint_date.equals("")|| complaint_type[0].equals("") || complaint_description.equals("")) {
            Toast.makeText(getApplicationContext(), "Data tidak lengkap..", Toast.LENGTH_LONG).show();
        }else{
            progress = new ProgressDialog(this);
            progress.setMessage("Sedang Memproses..");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(ENDPOINT)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();

            APIComplaint restInterface = restAdapter.create(APIComplaint.class);

            restInterface.onSaveComplaint(complaint_id, EmployeeId, complaint_date, complaint_type[0], complaint_description, complaint_clock_in, complaint_clock_out, new Callback<CheckLogin>() {
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
    }

    private int GCD(int a, int b) {
        return (b == 0 ? a : GCD(b, a % b));
    }

    private void getComplaintById(String ComplaintId){
        /*-------------------------------*/
        progress = new ProgressDialog(this);
        progress.setMessage("Menyiapkan Data..");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIComplaint restInterface = restAdapter.create(APIComplaint.class);

        restInterface.getComplaintById(ComplaintId, new Callback<GetComplaintList>() {
            @Override
            public void success(GetComplaintList m, Response response) {
                txtComplaintDate.setText(m.getComplaintDt().toString());
                if(!m.getClockIn().equals("kosong")) {
                    txtClockIn.setText(m.getClockIn().toString());
                }else{
                    txtClockIn.setText("".toString());
                }
//
                if(m.getClockOut().toString().equals("kosong")) {
                    txtClockOut.setText("");
                }else{
                    txtClockOut.setText(m.getClockOut().toString());
                }

                String data_spn = m.getExcuseCd() + " - "+ m.getExcuseNm();

                getComplaintType(data_spn);

                txtComplaintDescription.setText(m.getReason().toString());
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
        showDialog(999);
        //Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
        //        .show();
    }

    public void setTime_In(){
        showDialog(222);
    }

    public void setTime_Out(){
        showDialog(333);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }else if(id == 222){
            return new TimePickerDialog(this, myTimeListener_In , hour, minute, false);
        }else{
            return new TimePickerDialog(this, myTimeListener_Out , hour, minute, false);
        }
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private TimePickerDialog.OnTimeSetListener myTimeListener_In = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
            // TODO Auto-generated method stub
            showTime_In(arg1, arg2);
        }
    };

    private TimePickerDialog.OnTimeSetListener myTimeListener_Out = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
            // TODO Auto-generated method stub
            showTime_Out(arg1, arg2);
        }
    };

    private void showDate(int year, int month, int day) {

        txtComplaintDate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }

    private void showTime_In(int hour, int minute) {

        txtClockIn.setText(new StringBuilder().append(hour).append(":")
                .append(minute));
    }

    private void showTime_Out(int hour, int minute) {

        txtClockOut.setText(new StringBuilder().append(hour).append(":")
                .append(minute));
    }
}
