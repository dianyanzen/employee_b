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
import pojo.GetProfileEducationLevel;
import pojo.GetProfileEducationList;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by root on 16/10/17.
 */

public class MyProfileAddEducation extends AppCompatActivity {
    private String EduId;
    String getLevel = "";
    String getdescript = "";
    SharedPreferences pref;
    private ProgressDialog progress;
    public String ENDPOINT = "http://bc-id.co.id/";
    int idxLevel;
    ArrayAdapter<String> adaAdapterLevel;
    private String modeEdit;
    /**
     * UserInfo
     **/
    String User = "";
    String EmployeeId = "";
    public String data_level = "";
    int aspectX;
    int aspectY;
    int outputX;
    int outputY;
    EditText txteduinstitution,txtedufaculty,txtedugraduatedate,txtedugpa;
    Spinner spnedulevel;
    Button btnedugraduatedate,btneduSave,btneduCancel;
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
        setContentView(R.layout.activity_education);
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        EmployeeId = pref.getString("EmployeeId", null);
        ENDPOINT = pref.getString("URLEndPoint", "");
        User = pref.getString("User", null);
        conDetector = new ConnectionDetector(getApplicationContext());
        spnedulevel = (Spinner)findViewById(R.id.spnedulevel);
        getLevel();
        txteduinstitution = (EditText)findViewById(R.id.txteduinstitution);
        txtedufaculty = (EditText)findViewById(R.id.txtedufaculty);
        txtedugraduatedate = (EditText)findViewById(R.id.txtedugraduatedate);
        txtedugpa = (EditText)findViewById(R.id.txtedugpa);
        btnedugraduatedate = (Button)findViewById(R.id.btnedugraduatedate);
        btnedugraduatedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setgraduatedate();
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
                EduId = extras.getString("EduId");
                try {
                    getEducationById(EduId);

                } catch (Exception e) {

                }

            } else {
                modeEdit = "new";
                EduId = "";
                txteduinstitution.setText("");
                txtedugraduatedate.setText("");
                txtedufaculty.setText("");
                txtedugpa.setText("");
            }

        } else {
            Toast.makeText(this, "Invalid parameter", Toast.LENGTH_SHORT).show();
            finish();
        }
        /*spnedulevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_search = false;
            }
        });*/
        btneduCancel =(Button)findViewById(R.id.btneduCancel);
        btneduCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btneduSave = (Button)findViewById(R.id.btneduSave);
        btneduSave.setOnClickListener(new View.OnClickListener() {
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
        String education_id = EduId;
        String education_level = ((String) spnedulevel.getSelectedItem());
        String education_institution = txteduinstitution.getText().toString();
        String education_graduate_date = txtedugraduatedate.getText().toString();
        String education_faculty = txtedufaculty.getText().toString();
        String education_gpa = txtedugpa.getText().toString();
        String employee_id = EmployeeId;
        if(employee_id.equals("") || education_institution.equals("") || education_level.equals("") || education_graduate_date.equals("")) {
            Toast.makeText(this, "Data tidak lengkap..", Toast.LENGTH_LONG).show();
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

            APIEditProfile restInterface = restAdapter.create(APIEditProfile.class);
            restInterface.onAddEducation(education_id
                    ,employee_id
                    ,User
                    ,education_level
                    ,education_institution
                    ,education_faculty
                    ,education_graduate_date
                    ,education_gpa
                    , new Callback<CheckLogin>() {
                        @Override
                        public void success(CheckLogin m, Response response) {

                            if (progress != null) {
                                progress.dismiss();
                            }

                            Toast.makeText(MyProfileAddEducation.this, m.getMsgText(), Toast.LENGTH_SHORT).show();

                            if (m.getMsgType().toLowerCase().equals("info")) {
                                finish();
                            } else {

                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(MyProfileAddEducation.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void getLevel(){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIEditProfile restInterface = restAdapter.create(APIEditProfile.class);

        restInterface.getProfileEducationLevel(new Callback<List<GetProfileEducationLevel>>() {
            @Override
            public void success(List<GetProfileEducationLevel> m, Response response) {
                List<String> list = new ArrayList<String>();

                for (int i = 0; i < m.size(); i++) {
                    list.add(m.get(i).getEduLevel());
                }

        adaAdapterLevel = new ArrayAdapter<String>
                (MyProfileAddEducation.this, R.layout.spinner_simple_item, R.id.listCombo, list);

        adaAdapterLevel.setDropDownViewResource
                (R.layout.spinner_simple_item);

        spnedulevel.setAdapter(adaAdapterLevel);

        String data = data_level;
        if (data != "") {
            int idxLocation = adaAdapterLevel.getPosition(data_level);
            spnedulevel.setSelection(idxLocation);
        }

        spnedulevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void getEducationById(String EduId){
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

        APIEditProfile restInterface = restAdapter.create(APIEditProfile.class);

        restInterface.getEducationListById(EduId, new Callback<GetProfileEducationList>() {
            @Override
            public void success(GetProfileEducationList m, Response response) {
                try {
                    txteduinstitution.setText(m.getInstitution().toString());
                    txtedugraduatedate.setText(m.getGraduatedDt().toString());
                    txtedufaculty.setText(m.getFaculty().toString());
                    txtedugpa.setText(m.getGPA().toString());
                    String Level = m.getLevel().toString();
                    if (Level.equalsIgnoreCase("s3")) {
                        idxLevel = 9;
                    }else if (Level.equalsIgnoreCase("s2")){
                        idxLevel = 8;
                    }else if (Level.equalsIgnoreCase("s1")){
                        idxLevel = 7;
                    }else if (Level.equalsIgnoreCase("d3")){
                        idxLevel = 6;
                    }else if (Level.equalsIgnoreCase("d2")){
                        idxLevel = 5;
                    }else if (Level.equalsIgnoreCase("d1")){
                        idxLevel = 4;
                    }else if (Level.equalsIgnoreCase("sma/ma")){
                        idxLevel = 3;
                    }else if (Level.equalsIgnoreCase("smp/mts")){
                        idxLevel = 2;
                    }else if (Level.equalsIgnoreCase("sd/mi")){
                        idxLevel = 1;
                    }else {
                        idxLevel = 0;
                    }
                    spnedulevel.setSelection(idxLevel);
                    btneduSave.setText("UPDATE");

                }catch (Exception e){
//                    Toast.makeText(getApplicationContext(), "Connection Error Check Your Internet Connection", Toast.LENGTH_SHORT).show();
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
    public void setgraduatedate() {
        showDialog(100);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 100) {
         return new DatePickerDialog(this, myGraduatedate, year, month, day);
        }
        else{
            return null;
        }

    }
    private DatePickerDialog.OnDateSetListener myGraduatedate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showmyExpdate(arg1, arg2 + 1, arg3);
        }
    };
    private void showmyExpdate(int year, int month, int day) {
        txtedugraduatedate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }
}
