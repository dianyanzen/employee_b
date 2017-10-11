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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pojo.CheckLogin;
import pojo.ComboboxCommon;
import pojo.GetExcuseGroup;
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

    /* Menu Index 0 */
    private TextView lbldate1,lblexcuseclockin1,lblexcuseclockout1;
    private EditText txtdate1,txtexcuseclockin1,txtexcuseclockout1;
    private Button BtnExcDate1,BtnExcClockIn1,BtnExcClockOut1;

    /* Menu Index 1 */
    private TextView lbldatefrom2,lbldateto2,lblexcuseclockin2;
    private EditText txtdatefrom2,txtdateto2,txtexcuseclockin2;
    private Button BtnExcDateFrom2,BtnExcDateTo2,BtnExcClockIn2;

    /* Menu Index 2 */
    private TextView lbldatefrom3,lbldateto3,lblgroup3;
    private EditText txtdatefrom3,txtdateto3;
    private Button BtnExcDateFrom3,BtnExcDateTo3;
    private Spinner spnGroup3;

    /* Menu Index 3 */
    private TextView lbldatefrom5,lbldateto5;
    private EditText txtdateto5,txtdatefrom5;
    private Button BtnExcDateFrom5,BtnExcDateTo5;

    /* Menu Index 4 */
    private TextView lblshifttype4,lbldatefrom4,lbldateto4,lblgroup4,lblshift4;
    private EditText txtdatefrom4,txtdateto4;
    private Button BtnExcDateFrom4,BtnExcDateTo4;
    private Spinner spnShiftType4,spnGroup4,spnshift4;

    /* Menu Default */
    private TextView lblfromdate,lbltodate;
    private EditText txtExcuseDescription,txtfromdate,txttodate;
    private Button BtnExcFromDt,BtnExcToDt;
    private Spinner spnExcuseType;

    ArrayAdapter<String> dataAdapterExcuseType;
    ArrayAdapter<String> adaAdapterCombo;
    public String ENDPOINT="http://192.168.88.153:8080/arka_portal";

    private String modeEdit;
    /**UserInfo**/
    String User="";
    String EmployeeId="";
    public String data_type = "";
    public String data_group = "";
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
        declareview();


//        txtExcuse = (EditText)findViewById(R.id.txtExcuseId);

        if (extras != null) {

            modeEdit=extras.getString("Mode");
            if(modeEdit.equals("edit")){
                modeEdit="edit";
                ExcuseId = extras.getString("ExcuseId");
                getExcuseType(data_type);
                getExcuseById(ExcuseId);
                //updateType();
            }else{
                modeEdit="new";
                ExcuseId="";
//                txtfromdate.setText("");
//                txttodate.setText("");
                txtExcuseDescription.setText("");
                getExcuseType(data_type);
                //updateType();
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
                        updateType();
                        //Toast.makeText(getApplicationContext(), separated[0].replace(" ", "").toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                        updateType();

                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getspnGroup3(final String data_group){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        ApiExcuse restInterface = restAdapter.create(ApiExcuse.class);

        restInterface.getExcuseGroup(new Callback<List<GetExcuseGroup>>() {
            @Override
            public void success(List<GetExcuseGroup> m, Response response) {
                List<String> list = new ArrayList<String>();

                for (int i = 0; i < m.size(); i++) {
                    list.add(m.get(i).getExcuseGroup());
                }

                adaAdapterCombo = new ArrayAdapter<String>
                        (getApplicationContext(), R.layout.spinner_simple_item, R.id.listCombo, list);

                adaAdapterCombo.setDropDownViewResource
                        (R.layout.spinner_simple_item);

                spnGroup3.setAdapter(adaAdapterCombo);

                String data = data_group;

                if (data != "") {
                    int idxLocation = adaAdapterCombo.getPosition(data_group);
                    spnGroup3.setSelection(idxLocation);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void declareview(){
         /* Main View */
        spnExcuseType = (Spinner) findViewById(R.id.spnType);
        txtExcuseDescription = (EditText)findViewById(R.id.txtExcuseDescription);
         /* 3 Textview Menu Index 0*/
        lbldate1 = (TextView)findViewById(R.id.lbldate1);
        lblexcuseclockin1 = (TextView)findViewById(R.id.lblexcuseclockin1);
        lblexcuseclockout1 = (TextView)findViewById(R.id.lblexcuseclockout1);
            /* 3 Edittext Menu Index 0*/
        txtdate1 = (EditText)findViewById(R.id.txtdate1);
        txtexcuseclockin1 = (EditText)findViewById(R.id.txtexcuseclockin1);
        txtexcuseclockout1 = (EditText)findViewById(R.id.txtexcuseclockout1);
            /* 3 Button Menu Index 0*/
        BtnExcDate1 = (Button)findViewById(R.id.BtnExcDate1);
        BtnExcClockIn1 = (Button)findViewById(R.id.BtnExcClockIn1);
        BtnExcClockOut1 = (Button)findViewById(R.id.BtnExcClockOut1);

         /* 3 Textview Menu Index 1*/
        lbldatefrom2 = (TextView)findViewById(R.id.lbldatefrom2);
        lbldateto2 = (TextView)findViewById(R.id.lbldateto2);
        lblexcuseclockin2 = (TextView)findViewById(R.id.lblexcuseclockin2);
            /* 3 Edittext Menu Index 1*/
        txtdatefrom2 = (EditText)findViewById(R.id.txtdatefrom2);
        txtdateto2 = (EditText)findViewById(R.id.txtdateto2);
        txtexcuseclockin2 = (EditText)findViewById(R.id.txtexcuseclockin2);
            /* 3 Button Menu Index 1*/
        BtnExcDateFrom2 = (Button)findViewById(R.id.BtnExcDateFrom2);
        BtnExcDateTo2 = (Button)findViewById(R.id.BtnExcDateTo2);
        BtnExcClockIn2 = (Button)findViewById(R.id.BtnExcClockIn2);

        /* 3 Textview Menu Index 2*/
        lbldatefrom3 = (TextView)findViewById(R.id.lbldatefrom3);
        lbldateto3 = (TextView)findViewById(R.id.lbldateto3);
        lblgroup3 = (TextView)findViewById(R.id.lblgroup3);
            /* 2 Edittext Menu Index 2*/
        txtdatefrom3 = (EditText)findViewById(R.id.txtdatefrom3);
        txtdateto3 = (EditText)findViewById(R.id.txtdateto3);
            /* 2 Button Menu Index 2*/
        BtnExcDateFrom3 = (Button)findViewById(R.id.BtnExcDateFrom3);
        BtnExcDateTo3 = (Button)findViewById(R.id.BtnExcDateTo3);
        /* 1 Spin Menu Index 2*/
        spnGroup3 = (Spinner)findViewById(R.id.spnGroup3);

         /* 5 Textview Menu Index 3*/
        lblshifttype4 = (TextView)findViewById(R.id.lblshifttype4);
        lbldatefrom4 = (TextView)findViewById(R.id.lbldatefrom4);
        lbldateto4 = (TextView)findViewById(R.id.lbldateto4);
        lblgroup4 = (TextView)findViewById(R.id.lblgroup4);
        lblshift4 = (TextView)findViewById(R.id.lblshift4);
            /* 2 Edittext Menu Index 3*/
        txtdatefrom4 = (EditText)findViewById(R.id.txtdatefrom4);
        txtdateto4 = (EditText)findViewById(R.id.txtdateto4);
            /* 2 Button Menu Index 3*/
        BtnExcDateFrom4 = (Button)findViewById(R.id.BtnExcDateFrom4);
        BtnExcDateTo4 = (Button)findViewById(R.id.BtnExcDateTo4);
            /* 3 Spin Menu Index 3*/
        spnShiftType4 = (Spinner)findViewById(R.id.spnShiftType4);
        spnGroup4 = (Spinner)findViewById(R.id.spnGroup4);
        spnshift4 = (Spinner)findViewById(R.id.spnshift4);

        /* 2 Textview Menu Index 4*/
        lbldatefrom5 = (TextView)findViewById(R.id.lbldatefrom5);
        lbldateto5 = (TextView)findViewById(R.id.lbldateto5);
            /* 2 Edittext Menu Index 4*/
        txtdateto5 = (EditText)findViewById(R.id.txtdateto5);
        txtdatefrom5 = (EditText)findViewById(R.id.txtdatefrom5);
            /* 2 Button Menu Index 4*/
        BtnExcDateFrom5 = (Button)findViewById(R.id.BtnExcDateFrom5);
        BtnExcDateTo5 = (Button)findViewById(R.id.BtnExcDateTo5);

        /* 2 Textview Menu Default*/
        lblfromdate = (TextView)findViewById(R.id.lbldatefrom5);
        lbltodate = (TextView)findViewById(R.id.lbldateto5);
            /* 2 Edittext Menu Index 4*/
        txtfromdate = (EditText)findViewById(R.id.txtdateto5);
        txttodate = (EditText)findViewById(R.id.txtdatefrom5);
            /* 2 Button Menu Index 4*/
        BtnExcFromDt = (Button)findViewById(R.id.BtnExcDateFrom5);
        BtnExcToDt = (Button)findViewById(R.id.BtnExcDateTo5);

    }
    private void clearVisible(){
        if (lbldate1.getVisibility() == View.VISIBLE) {
            lbldate1.setVisibility(View.INVISIBLE);
            lblexcuseclockin1.setVisibility(View.INVISIBLE);
            lblexcuseclockout1.setVisibility(View.INVISIBLE);

            txtdate1.setVisibility(View.INVISIBLE);
            txtexcuseclockin1.setVisibility(View.INVISIBLE);
            txtexcuseclockout1.setVisibility(View.INVISIBLE);

            BtnExcDate1.setVisibility(View.INVISIBLE);
            BtnExcClockIn1.setVisibility(View.INVISIBLE);
            BtnExcClockOut1.setVisibility(View.INVISIBLE);
        }
        if (lbldatefrom2.getVisibility() == View.VISIBLE) {
            lbldatefrom2.setVisibility(View.INVISIBLE);
            lbldateto2.setVisibility(View.INVISIBLE);
            lblexcuseclockin2.setVisibility(View.INVISIBLE);

            txtdatefrom2.setVisibility(View.INVISIBLE);
            txtdateto2.setVisibility(View.INVISIBLE);
            txtexcuseclockin2.setVisibility(View.INVISIBLE);

            BtnExcDateFrom2.setVisibility(View.INVISIBLE);
            BtnExcDateTo2.setVisibility(View.INVISIBLE);
            BtnExcClockIn2.setVisibility(View.INVISIBLE);
        }
        if (lbldatefrom3.getVisibility() == View.VISIBLE) {
            lbldatefrom3.setVisibility(View.INVISIBLE);
            lbldateto3.setVisibility(View.INVISIBLE);
            lblgroup3.setVisibility(View.INVISIBLE);

            txtdatefrom3.setVisibility(View.INVISIBLE);
            txtdateto3.setVisibility(View.INVISIBLE);

            BtnExcDateFrom3.setVisibility(View.INVISIBLE);
            BtnExcDateTo3.setVisibility(View.INVISIBLE);
            spnGroup3.setVisibility(View.INVISIBLE);
        }
        if (lbldatefrom5.getVisibility() == View.VISIBLE) {
            lbldatefrom5.setVisibility(View.INVISIBLE);
            lbldateto5.setVisibility(View.INVISIBLE);

            txtdateto5.setVisibility(View.INVISIBLE);
            txtdatefrom5.setVisibility(View.INVISIBLE);

            BtnExcDateFrom5.setVisibility(View.INVISIBLE);
            BtnExcDateTo5.setVisibility(View.INVISIBLE);
        }
        if (lbldatefrom4.getVisibility() == View.VISIBLE) {
            lblshifttype4.setVisibility(View.INVISIBLE);
            lbldatefrom4.setVisibility(View.INVISIBLE);
            lbldateto4.setVisibility(View.INVISIBLE);
            if (lblgroup4.getVisibility() == View.VISIBLE) {
                lblgroup4.setVisibility(View.INVISIBLE);
            }
            if (lblshift4.getVisibility() == View.VISIBLE) {
                lblshift4.setVisibility(View.INVISIBLE);
            }

            txtdatefrom4.setVisibility(View.INVISIBLE);
            txtdateto4.setVisibility(View.INVISIBLE);

            BtnExcDateFrom4.setVisibility(View.INVISIBLE);
            BtnExcDateTo4.setVisibility(View.INVISIBLE);

            spnShiftType4.setVisibility(View.INVISIBLE);
            if (spnGroup4.getVisibility() == View.VISIBLE) {
                spnGroup4.setVisibility(View.INVISIBLE);
            }
            if (spnshift4.getVisibility() == View.VISIBLE) {
                spnshift4.setVisibility(View.INVISIBLE);
            }
        }
        if (lblfromdate.getVisibility() == View.VISIBLE) {
            lblfromdate.setVisibility(View.INVISIBLE);
            lbltodate.setVisibility(View.INVISIBLE);

            txtfromdate.setVisibility(View.INVISIBLE);
            txttodate.setVisibility(View.INVISIBLE);

            BtnExcFromDt.setVisibility(View.INVISIBLE);
            BtnExcToDt.setVisibility(View.INVISIBLE);
        }
    }
    private void updateType() {
        clearVisible();

        String ExcuseType = ((String) spnExcuseType.getSelectedItem());
        if (ExcuseType.equals("Berita Acara Clock IN/OUT")) {
            lbldate1.setVisibility(View.VISIBLE);
            lblexcuseclockin1.setVisibility(View.VISIBLE);
            lblexcuseclockout1.setVisibility(View.VISIBLE);

            txtdate1.setVisibility(View.VISIBLE);
            txtexcuseclockin1.setVisibility(View.VISIBLE);
            txtexcuseclockout1.setVisibility(View.VISIBLE);

            BtnExcDate1.setVisibility(View.VISIBLE);
            BtnExcClockIn1.setVisibility(View.VISIBLE);
            BtnExcClockOut1.setVisibility(View.VISIBLE);
//            Toast.makeText(ExcuseActivity.this, ExcuseType, Toast.LENGTH_SHORT).show();

        } else if (ExcuseType.equals("Change Clock-in")) {
            lbldatefrom2.setVisibility(View.VISIBLE);
            lbldateto2.setVisibility(View.VISIBLE);
            lblexcuseclockin2.setVisibility(View.VISIBLE);

            txtdatefrom2.setVisibility(View.VISIBLE);
            txtdateto2.setVisibility(View.VISIBLE);
            txtexcuseclockin2.setVisibility(View.VISIBLE);

            BtnExcDateFrom2.setVisibility(View.VISIBLE);
            BtnExcDateTo2.setVisibility(View.VISIBLE);
            BtnExcClockIn2.setVisibility(View.VISIBLE);
//            Toast.makeText(ExcuseActivity.this, ExcuseType, Toast.LENGTH_SHORT).show();
        } else if (ExcuseType.equals("Change Group")) {
            lbldatefrom3.setVisibility(View.VISIBLE);
            lbldateto3.setVisibility(View.VISIBLE);
            lblgroup3.setVisibility(View.VISIBLE);

            txtdatefrom3.setVisibility(View.VISIBLE);
            txtdateto3.setVisibility(View.VISIBLE);

            BtnExcDateFrom3.setVisibility(View.VISIBLE);
            BtnExcDateTo3.setVisibility(View.VISIBLE);
            spnGroup3.setVisibility(View.VISIBLE);
            getspnGroup3(data_group);


//            Toast.makeText(ExcuseActivity.this, ExcuseType, Toast.LENGTH_SHORT).show();
        } else if (ExcuseType.equals("Change Shift")) {
            lblshifttype4.setVisibility(View.VISIBLE);
            lbldatefrom4.setVisibility(View.VISIBLE);
            lbldateto4.setVisibility(View.VISIBLE);
            /*lblgroup4.setVisibility(View.VISIBLE);
            lblshift4.setVisibility(View.VISIBLE);*/

            txtdatefrom4.setVisibility(View.VISIBLE);
            txtdateto4.setVisibility(View.VISIBLE);

            BtnExcDateFrom4.setVisibility(View.VISIBLE);
            BtnExcDateTo4.setVisibility(View.VISIBLE);

            spnShiftType4.setVisibility(View.VISIBLE);
            /*spnGroup4.setVisibility(View.VISIBLE);
            spnshift4.setVisibility(View.VISIBLE);*/
//            Toast.makeText(ExcuseActivity.this, ExcuseType, Toast.LENGTH_SHORT).show();
        } else if (ExcuseType.equals("Change Working Day")) {
            lbldatefrom5.setVisibility(View.VISIBLE);
            lbldateto5.setVisibility(View.VISIBLE);

            txtdateto5.setVisibility(View.VISIBLE);
            txtdatefrom5.setVisibility(View.VISIBLE);

            BtnExcDateFrom5.setVisibility(View.VISIBLE);
            BtnExcDateTo5.setVisibility(View.VISIBLE);

//            Toast.makeText(ExcuseActivity.this, ExcuseType, Toast.LENGTH_SHORT).show();
        } else {
            lblfromdate.setVisibility(View.VISIBLE);
            lbltodate.setVisibility(View.VISIBLE);

            txtfromdate.setVisibility(View.VISIBLE);
            txttodate.setVisibility(View.VISIBLE);

            BtnExcFromDt.setVisibility(View.VISIBLE);
            BtnExcToDt.setVisibility(View.VISIBLE);
//            Toast.makeText(ExcuseActivity.this, ExcuseType, Toast.LENGTH_SHORT).show();
        }
    }
    protected void onSaveExcuse(){
        /*String date_from = txtfromdate.getText().toString();
        String date_to = txttodate.getText().toString();
        String excuse_reason = txtExcuseDescription.getText().toString();
        String excuse_type = spnExcuseType.getSelectedItem().toString();
        String excuse_id = ExcuseId;*/

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

        /*if(date_from.equals("")|| date_to.equals("") || excuse_reason.equals("")) {
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
        }*/
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
                /*
                 txtfromdate.setText(m.getExcuseDt().toString());
                 txttodate.setText(m.getExcuseTodt().toString());
                 */
                txtExcuseDescription.setText(m.getExcuseDescription().toString());
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
        /*txttodate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));*/
    }
    private void showdtfrom(int year, int month, int day) {
        /*txtfromdate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));*/
    }
}