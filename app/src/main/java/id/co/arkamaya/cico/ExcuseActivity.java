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
import pojo.GetExcuseShift;
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
    String getshift = "";
    String getgroup = "";
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
    public String data_shift = "";
    int aspectX;
    int aspectY;
    int outputX;
    int outputY;

    ConnectionDetector conDetector;
    Bundle extras;
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
        setContentView(R.layout.activity_excuse);
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        EmployeeId=pref.getString("EmployeeId", null);
        ENDPOINT= pref.getString("URLEndPoint", "");
        User= pref.getString("User",null);
        EmployeeId=pref.getString("EmployeeId",null);
        conDetector =  new ConnectionDetector(getApplicationContext());

        extras = getIntent().getExtras();

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
        /* TODO Perform For Display Datepicker Menu 1*/
        BtnExcDate1.performClick();
        BtnExcDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateMenu1();
            }
        });
        BtnExcClockIn1.performClick();
        BtnExcClockIn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeInMenu1();
            }
        });
        BtnExcClockOut1.performClick();
        BtnExcClockOut1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeOutMenu1();
            }
        });

        /* TODO Perform For Display Datepicker Menu 2*/
        BtnExcDateFrom2.performClick();
        BtnExcDateFrom2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate1Menu2();
            }
        });
        BtnExcDateTo2.performClick();
        BtnExcDateTo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate2Menu2();
            }
        });
        BtnExcClockIn2.performClick();
        BtnExcClockIn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeInMenu2();
            }
        });
        /* TODO Perform For Display Datepicker Menu 3*/
        BtnExcDateFrom3.performClick();
        BtnExcDateFrom3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate1Menu3();
            }
        });
        BtnExcDateTo3.performClick();
        BtnExcDateTo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate2Menu3();
            }
        });
        /* TODO Perform For Display Datepicker Menu 4*/
        BtnExcDateFrom4.performClick();
        BtnExcDateFrom4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate1Menu4();
            }
        });
        BtnExcDateTo4.performClick();
        BtnExcDateTo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate2Menu4();
            }
        });
        /* TODO Perform For Display Datepicker Menu 5*/
        BtnExcDateFrom5.performClick();
        BtnExcDateFrom5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate1Menu5();
            }
        });
        BtnExcDateTo5.performClick();
        BtnExcDateTo5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate2Menu5();
            }
        });
        /* TODO Perform For Display Datepicker Menu Default*/
        BtnExcFromDt.performClick();
        BtnExcFromDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate1MenuDef();
            }
        });
        BtnExcToDt.performClick();
        BtnExcToDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate2MenuDef();
            }
        });
        /*

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
*/

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
    private void getspnGroup(final String data_group){

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
    private void getspnGroup2(final String data_group){

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

                spnGroup4.setAdapter(adaAdapterCombo);

                String data = data_group;

                if (data != "") {
                    int idxLocation = adaAdapterCombo.getPosition(data_group);
                    spnGroup4.setSelection(idxLocation);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getspnShift(final String data_shift){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        ApiExcuse restInterface = restAdapter.create(ApiExcuse.class);

        restInterface.getExcuseShift(new Callback<List<GetExcuseShift>>() {
            @Override
            public void success(List<GetExcuseShift> m, Response response) {
                List<String> list = new ArrayList<String>();

                for (int i = 0; i < m.size(); i++) {
                    list.add(m.get(i).getExcuseshift());
                }

                adaAdapterCombo = new ArrayAdapter<String>
                        (getApplicationContext(), R.layout.spinner_simple_item, R.id.listCombo, list);

                adaAdapterCombo.setDropDownViewResource
                        (R.layout.spinner_simple_item);

                spnShiftType4.setAdapter(adaAdapterCombo);

                String data = data_shift;

                if (data != "") {
                    int idxLocation = adaAdapterCombo.getPosition(data_shift);
                    spnShiftType4.setSelection(idxLocation);
                }
                spnShiftType4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        updateShiftType();
                        //Toast.makeText(getApplicationContext(), separated[0].replace(" ", "").toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                        updateShiftType();

                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void gethardcoreG1(){
        List<String> list = new ArrayList<String>();
        list.clear();
        spnshift4.setAdapter(null);
        list.add("1");
        list.add("2");

        adaAdapterCombo = new ArrayAdapter<String>
                (getApplicationContext(), R.layout.spinner_simple_item, R.id.listCombo, list);

        adaAdapterCombo.setDropDownViewResource
                (R.layout.spinner_simple_item);

        spnshift4.setAdapter(adaAdapterCombo);

        String data = data_shift;

        if (data != "") {
            int idxLocation = adaAdapterCombo.getPosition(data_shift);
            spnshift4.setSelection(idxLocation);
        }
    }
    public void gethardcoreNS(){
        List<String> list = new ArrayList<String>();
        list.clear();
        spnshift4.setAdapter(null);
        list.add("1");
        if (list.size() > 1){
            list.remove("2");
        }


        adaAdapterCombo = new ArrayAdapter<String>
                (getApplicationContext(), R.layout.spinner_simple_item, R.id.listCombo, list);

        adaAdapterCombo.setDropDownViewResource
                (R.layout.spinner_simple_item);

        spnshift4.setAdapter(adaAdapterCombo);

        String data = data_shift;

        if (data != "") {
            int idxLocation = adaAdapterCombo.getPosition(data_shift);
            spnshift4.setSelection(idxLocation);
        }
    }
    public void gethardcoreNS1(){
        List<String> list = new ArrayList<String>();
        list.clear();
        spnshift4.setAdapter(null);
        list.add("1");
        if (list.size() > 1){
            list.remove("2");
        }
        adaAdapterCombo = new ArrayAdapter<String>
                (getApplicationContext(), R.layout.spinner_simple_item, R.id.listCombo, list);

        adaAdapterCombo.setDropDownViewResource
                (R.layout.spinner_simple_item);

        spnshift4.setAdapter(adaAdapterCombo);

        String data = data_shift;

        if (data != "") {
            int idxLocation = adaAdapterCombo.getPosition(data_shift);
            spnshift4.setSelection(idxLocation);
        }
    }
    private void updateShiftType(){
        String ExcuseShiftType = ((String) spnShiftType4.getSelectedItem());
        if (ExcuseShiftType.equals("G1")) {
            gethardcoreG1();
//            Toast.makeText(ExcuseActivity.this, ExcuseShiftType, Toast.LENGTH_SHORT).show();
            if (lblshift4.getVisibility() == View.INVISIBLE) {
                lblshift4.setVisibility(View.VISIBLE);
                spnshift4.setVisibility(View.VISIBLE);

            }
            if (lblgroup4.getVisibility() == View.VISIBLE) {
                lblgroup4.setVisibility(View.INVISIBLE);
                spnGroup4.setVisibility(View.INVISIBLE);
            }
        }else if (ExcuseShiftType.equals("G2")) {
            getspnGroup2(data_group);
//            Toast.makeText(ExcuseActivity.this, ExcuseShiftType, Toast.LENGTH_SHORT).show();
            if (lblshift4.getVisibility() == View.VISIBLE) {
                lblshift4.setVisibility(View.INVISIBLE);
                spnshift4.setVisibility(View.INVISIBLE);
            }
            if (lblgroup4.getVisibility() == View.INVISIBLE) {
                lblgroup4.setVisibility(View.VISIBLE);
                spnGroup4.setVisibility(View.VISIBLE);

            }
        }else if (ExcuseShiftType.equals("NS")) {
            gethardcoreNS();
//            Toast.makeText(ExcuseActivity.this, ExcuseShiftType, Toast.LENGTH_SHORT).show();
            if (lblshift4.getVisibility() == View.INVISIBLE) {
                lblshift4.setVisibility(View.VISIBLE);
                spnshift4.setVisibility(View.VISIBLE);

            }
            if (lblgroup4.getVisibility() == View.VISIBLE) {
                lblgroup4.setVisibility(View.INVISIBLE);
                spnGroup4.setVisibility(View.INVISIBLE);

            }
        }else if (ExcuseShiftType.equals("NS1")) {
            gethardcoreNS1();
//            Toast.makeText(ExcuseActivity.this, ExcuseShiftType, Toast.LENGTH_SHORT).show();
            if (lblshift4.getVisibility() == View.INVISIBLE) {
                lblshift4.setVisibility(View.VISIBLE);
                spnshift4.setVisibility(View.VISIBLE);

            }
            if (lblgroup4.getVisibility() == View.VISIBLE) {
                lblgroup4.setVisibility(View.INVISIBLE);
                spnGroup4.setVisibility(View.INVISIBLE);
            }
        }else{
            if (lblgroup4.getVisibility() == View.VISIBLE) {
                lblgroup4.setVisibility(View.INVISIBLE);
            }
            if (lblshift4.getVisibility() == View.VISIBLE) {
                lblshift4.setVisibility(View.INVISIBLE);
            }

            if (spnGroup4.getVisibility() == View.VISIBLE) {
                spnGroup4.setVisibility(View.INVISIBLE);
            }
            if (spnshift4.getVisibility() == View.VISIBLE) {
                spnshift4.setVisibility(View.INVISIBLE);
            }

        }
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
        lblfromdate = (TextView)findViewById(R.id.lblfromdate);
        lbltodate = (TextView)findViewById(R.id.lbltodate);
            /* 2 Edittext Menu Index 4*/
        txtfromdate = (EditText)findViewById(R.id.txtfromdate);
        txttodate = (EditText)findViewById(R.id.txttodate);
            /* 2 Button Menu Index 4*/
        BtnExcFromDt = (Button)findViewById(R.id.BtnExcFromDt);
        BtnExcToDt = (Button)findViewById(R.id.BtnExcToDt);

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


            txtdatefrom4.setVisibility(View.INVISIBLE);
            txtdateto4.setVisibility(View.INVISIBLE);

            BtnExcDateFrom4.setVisibility(View.INVISIBLE);
            BtnExcDateTo4.setVisibility(View.INVISIBLE);

            spnShiftType4.setVisibility(View.INVISIBLE);

        }
        if (spnGroup4.getVisibility() == View.VISIBLE) {
            spnGroup4.setVisibility(View.INVISIBLE);
        }
        if (spnshift4.getVisibility() == View.VISIBLE) {
            spnshift4.setVisibility(View.INVISIBLE);
        }
        if (lblgroup4.getVisibility() == View.VISIBLE) {
            lblgroup4.setVisibility(View.INVISIBLE);
        }
        if (lblshift4.getVisibility() == View.VISIBLE) {
            lblshift4.setVisibility(View.INVISIBLE);
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
        modeEdit=extras.getString("Mode");
        String ExcuseType = ((String) spnExcuseType.getSelectedItem());
         /* TODO Perform For Display Menu 1*/
        if (ExcuseType.equals("Berita Acara Clock IN/OUT")) {
            lbldate1.setVisibility(View.VISIBLE);
            lblexcuseclockin1.setVisibility(View.VISIBLE);
            lblexcuseclockout1.setVisibility(View.VISIBLE);


            if(modeEdit.equals("edit")){
                txtdate1.setText("");
                txtexcuseclockin1.setText("");
                txtexcuseclockout1.setText("");
            }else{
                txtdate1.setText("");
                txtexcuseclockin1.setText("");
                txtexcuseclockout1.setText("");
            }

            txtdate1.setVisibility(View.VISIBLE);
            txtexcuseclockin1.setVisibility(View.VISIBLE);
            txtexcuseclockout1.setVisibility(View.VISIBLE);

            BtnExcDate1.setVisibility(View.VISIBLE);
            BtnExcClockIn1.setVisibility(View.VISIBLE);
            BtnExcClockOut1.setVisibility(View.VISIBLE);
//            Toast.makeText(ExcuseActivity.this, ExcuseType, Toast.LENGTH_SHORT).show();

        /* TODO Perform For Display Menu 2*/
        } else if (ExcuseType.equals("Change Clock-in")) {
            lbldatefrom2.setVisibility(View.VISIBLE);
            lbldateto2.setVisibility(View.VISIBLE);
            lblexcuseclockin2.setVisibility(View.VISIBLE);


            if(modeEdit.equals("edit")){
                txtdatefrom2.setText("");
                txtdateto2.setText("");
                txtexcuseclockin2.setText("");
            }else {
                txtdatefrom2.setText("");
                txtdateto2.setText("");
                txtexcuseclockin2.setText("");
            }
            txtdatefrom2.setVisibility(View.VISIBLE);
            txtdateto2.setVisibility(View.VISIBLE);
            txtexcuseclockin2.setVisibility(View.VISIBLE);

            BtnExcDateFrom2.setVisibility(View.VISIBLE);
            BtnExcDateTo2.setVisibility(View.VISIBLE);
            BtnExcClockIn2.setVisibility(View.VISIBLE);
//            Toast.makeText(ExcuseActivity.this, ExcuseType, Toast.LENGTH_SHORT).show();
        /* TODO Perform For Display Menu 3*/
        } else if (ExcuseType.equals("Change Group")) {
            lbldatefrom3.setVisibility(View.VISIBLE);
            lbldateto3.setVisibility(View.VISIBLE);
            lblgroup3.setVisibility(View.VISIBLE);

            if(modeEdit.equals("edit")){
                txtdatefrom3.setText("");
                txtdateto3.setText("");
            }else {
                txtdatefrom3.setText("");
                txtdateto3.setText("");
            }
            txtdatefrom3.setVisibility(View.VISIBLE);
            txtdateto3.setVisibility(View.VISIBLE);

            BtnExcDateFrom3.setVisibility(View.VISIBLE);
            BtnExcDateTo3.setVisibility(View.VISIBLE);
            spnGroup3.setVisibility(View.VISIBLE);
            getspnGroup(data_group);


//            Toast.makeText(ExcuseActivity.this, ExcuseType, Toast.LENGTH_SHORT).show();
        /* TODO Perform For Display Menu 4*/
        } else if (ExcuseType.equals("Change Shift")) {
            lblshifttype4.setVisibility(View.VISIBLE);
            lbldatefrom4.setVisibility(View.VISIBLE);
            lbldateto4.setVisibility(View.VISIBLE);
            /*lblgroup4.setVisibility(View.VISIBLE);
            lblshift4.setVisibility(View.VISIBLE);*/

            if(modeEdit.equals("edit")){
                txtdatefrom4.setText("");
                txtdateto4.setText("");
            }else {
                txtdatefrom4.setText("");
                txtdateto4.setText("");
            }
            txtdatefrom4.setVisibility(View.VISIBLE);
            txtdateto4.setVisibility(View.VISIBLE);

            BtnExcDateFrom4.setVisibility(View.VISIBLE);
            BtnExcDateTo4.setVisibility(View.VISIBLE);

            spnShiftType4.setVisibility(View.VISIBLE);
            getspnShift(data_shift);
            /*spnGroup4.setVisibility(View.VISIBLE);
            spnshift4.setVisibility(View.VISIBLE);*/
//            Toast.makeText(ExcuseActivity.this, ExcuseType, Toast.LENGTH_SHORT).show();
        /* TODO Perform For Display Menu 5*/
        } else if (ExcuseType.equals("Change Working Day")) {
            lbldatefrom5.setVisibility(View.VISIBLE);
            lbldateto5.setVisibility(View.VISIBLE);


            if(modeEdit.equals("edit")){
                txtdatefrom5.setText("");
                txtdateto5.setText("");
            }else {
                txtdatefrom5.setText("");
                txtdateto5.setText("");
            }

            txtdatefrom5.setVisibility(View.VISIBLE);
            txtdateto5.setVisibility(View.VISIBLE);

            BtnExcDateFrom5.setVisibility(View.VISIBLE);
            BtnExcDateTo5.setVisibility(View.VISIBLE);

//            Toast.makeText(ExcuseActivity.this, ExcuseType, Toast.LENGTH_SHORT).show();
        /* TODO Perform For Display Menu 6*/
        } else if (ExcuseType.equals("Coba aja")) {
            lblfromdate.setVisibility(View.VISIBLE);
            lbltodate.setVisibility(View.VISIBLE);

            if(modeEdit.equals("edit")){
                txtfromdate.setText("");
                txttodate.setText("");
            }else {
                txtfromdate.setText("");
                txttodate.setText("");
            }
            txtfromdate.setVisibility(View.VISIBLE);
            txttodate.setVisibility(View.VISIBLE);

            BtnExcFromDt.setVisibility(View.VISIBLE);
            BtnExcToDt.setVisibility(View.VISIBLE);
//            Toast.makeText(ExcuseActivity.this, ExcuseType, Toast.LENGTH_SHORT).show();
        /* TODO Perform For Display Menu 7*/
        } else if (ExcuseType.equals("Leave")) {
            lblfromdate.setVisibility(View.VISIBLE);
            lbltodate.setVisibility(View.VISIBLE);

            if(modeEdit.equals("edit")){

            }else {
                txtfromdate.setText("");
                txttodate.setText("");
            }
            txtfromdate.setVisibility(View.VISIBLE);
            txttodate.setVisibility(View.VISIBLE);

            BtnExcFromDt.setVisibility(View.VISIBLE);
            BtnExcToDt.setVisibility(View.VISIBLE);
//            Toast.makeText(ExcuseActivity.this, ExcuseType, Toast.LENGTH_SHORT).show();
        /* TODO Perform For Display Menu Default*/
        } else {
            lblfromdate.setVisibility(View.VISIBLE);
            lbltodate.setVisibility(View.VISIBLE);

            if(modeEdit.equals("edit")){

            }else {
                txtfromdate.setText("");
                txttodate.setText("");
            }
            txtfromdate.setVisibility(View.VISIBLE);
            txttodate.setVisibility(View.VISIBLE);

            BtnExcFromDt.setVisibility(View.VISIBLE);
            BtnExcToDt.setVisibility(View.VISIBLE);
//            Toast.makeText(ExcuseActivity.this, ExcuseType, Toast.LENGTH_SHORT).show();
        }
    }
    protected void onSaveExcuse(){
        String ExcuseType = ((String) spnExcuseType.getSelectedItem());
        String excuse_reason = txtExcuseDescription.getText().toString();
        String excuse_type = spnExcuseType.getSelectedItem().toString();
        String excuse_id = ExcuseId;
        if (ExcuseType.equals("Berita Acara Clock IN/OUT")) {
            String date_from = "";
            String date_to = "";
            String shift_type = "";
            String shift = "";
            String date_event = txtdate1.getText().toString();
            String group = "";
            String clock_in = txtexcuseclockin1.getText().toString();
            String clock_out = txtexcuseclockout1.getText().toString();

            if(date_event.equals("")|| clock_in.equals("") ||clock_out.equals("") || excuse_reason.equals("")) {
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

                restInterface.onSaveExcuse(EmployeeId,User,excuse_id,excuse_type,excuse_reason,shift_type,shift,date_from, date_to, date_event,group,clock_in,clock_out
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

        } else if (ExcuseType.equals("Change Clock-in")) {
            String date_from = txtdatefrom2.getText().toString();
            String date_to = txtdateto2.getText().toString();
            String shift_type = "";
            String shift = "";
            String date_event = "";
            String group = "";
            String clock_in = txtexcuseclockin2.getText().toString();
            String clock_out = "";

            if(date_from.equals("")|| date_to.equals("") ||clock_in.equals("") || excuse_reason.equals("")) {
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

                restInterface.onSaveExcuse(EmployeeId,User,excuse_id,excuse_type,excuse_reason,shift_type,shift,date_from, date_to, date_event,group,clock_in,clock_out
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
        } else if (ExcuseType.equals("Change Group")) {
            String date_from = txtdatefrom3.getText().toString();
            String date_to = txtdateto3.getText().toString();
            String shift_type = "";
            String shift = "";
            String date_event = "";
            String group = ((String) spnGroup3.getSelectedItem());
            String clock_in = "";
            String clock_out = "";

            if(date_from.equals("")|| date_to.equals("") ||group.equals("") || excuse_reason.equals("")) {
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

                restInterface.onSaveExcuse(EmployeeId,User,excuse_id,excuse_type,excuse_reason,shift_type,shift,date_from, date_to, date_event,group,clock_in,clock_out
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
        } else if (ExcuseType.equals("Change Shift")) {
            String date_from = txtdatefrom4.getText().toString();
            String date_to = txtdateto4.getText().toString();
            String shift_type = ((String) spnShiftType4.getSelectedItem());
                if (shift_type.equals("G1")) {
                    getshift = ((String) spnshift4.getSelectedItem());
                    getgroup = "";
                }else if (shift_type.equals("G2")) {
                    getshift = "";
                    getgroup = ((String) spnGroup4.getSelectedItem());
                }else if (shift_type.equals("NS")) {
                    getshift = "1";
                    getgroup = "";
                }else{
                    getshift = "1";
                    getgroup = "";
                }
            String shift = getshift;
            String group = getgroup;
            String date_event = "";

            String clock_in = "";
            String clock_out = "";

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

                restInterface.onSaveExcuse(EmployeeId,User,excuse_id,excuse_type,excuse_reason,shift_type,shift,date_from, date_to, date_event,group,clock_in,clock_out
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
        } else if (ExcuseType.equals("Change Working Day")) {
            String date_from = txtdatefrom5.getText().toString();
            String date_to = txtdateto5.getText().toString();
            String shift_type = "";
            String shift = "";
            String date_event = "";
            String group = "";
            String clock_in = "";
            String clock_out = "";

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

                restInterface.onSaveExcuse(EmployeeId,User,excuse_id,excuse_type,excuse_reason,shift_type,shift,date_from, date_to, date_event,group,clock_in,clock_out
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
        } else {
            String date_from = txtfromdate.getText().toString();
            String date_to = txttodate.getText().toString();
            String shift_type = "";
            String shift = "";
            String date_event = "";
            String group = "";
            String clock_in = "";
            String clock_out = "";

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

                restInterface.onSaveExcuse(EmployeeId,User,excuse_id,excuse_type,excuse_reason,shift_type,shift,date_from, date_to, date_event,group,clock_in,clock_out
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
    public void setDateMenu1() { showDialog(100); }
    public void setTimeInMenu1() {
        showDialog(101);
    }
    public void setTimeOutMenu1() {
        showDialog(102);
    }

    public void setDate1Menu2() { showDialog(200); }
    public void setDate2Menu2() { showDialog(201); }
    public void setTimeInMenu2() {
        showDialog(202);
    }

    public void setDate1Menu3() { showDialog(300); }
    public void setDate2Menu3() { showDialog(301); }

    public void setDate1Menu4() { showDialog(400); }
    public void setDate2Menu4() { showDialog(401); }

    public void setDate1Menu5() { showDialog(500); }
    public void setDate2Menu5() { showDialog(501); }

    public void setDate1MenuDef() { showDialog(900); }
    public void setDate2MenuDef() { showDialog(901); }



    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Menu 1
        if (id == 100) {
            return new DatePickerDialog(this, myDate1Menu1, year, month, day);
        }else if(id == 101){
            return new TimePickerDialog(this, myTimeInMenu1 , hour, minute, false);
        }else if(id == 102){
            return new TimePickerDialog(this, myTimeOutMenu1 , hour, minute, false);
        }
        // TODO Menu 2
        if (id == 200) {
            return new DatePickerDialog(this, myDate1Menu2, year, month, day);
        }else if(id == 201){
            return new DatePickerDialog(this, myDate2Menu2, year, month, day);
        }else if(id == 202){
            return new TimePickerDialog(this, myTimeInMenu2 , hour, minute, false);
        }
        // TODO Menu 3
        if (id == 300) {
            return new DatePickerDialog(this, myDate1Menu3, year, month, day);
        }else if(id == 301){
            return new DatePickerDialog(this, myDate2Menu3, year, month, day);
        }
        // TODO Menu 4
        if (id == 400) {
            return new DatePickerDialog(this, myDate1Menu4, year, month, day);
        }else if(id == 401){
            return new DatePickerDialog(this, myDate2Menu4, year, month, day);
        }
        // TODO Menu 5
        if (id == 500) {
            return new DatePickerDialog(this, myDate1Menu5, year, month, day);
        }else if(id == 501){
            return new DatePickerDialog(this, myDate2Menu5, year, month, day);
        }
        // TODO Menu Def
        if (id == 900) {
            return new DatePickerDialog(this, myDate1MenuDef, year, month, day);
        }else if(id == 901){
            return new DatePickerDialog(this, myDate2MenuDef, year, month, day);
        }

        else{
            return null;
        }

    }
    // TODO menu 1
    private DatePickerDialog.OnDateSetListener myDate1Menu1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate1Menu1(arg1, arg2 + 1, arg3);
        }
    };
    private TimePickerDialog.OnTimeSetListener myTimeInMenu1 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
            showTimeInMenu1(arg1, arg2);
        }
    };

    private TimePickerDialog.OnTimeSetListener myTimeOutMenu1 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
            showTimeOutMenu1(arg1, arg2);
        }
    };

    private void showDate1Menu1(int year, int month, int day) {
        txtdate1.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }

    private void showTimeInMenu1(int hour, int minute) {
        txtexcuseclockin1.setText(new StringBuilder().append(hour).append(":")
                .append(minute));
    }

    private void showTimeOutMenu1(int hour, int minute) {
        txtexcuseclockout1.setText(new StringBuilder().append(hour).append(":")
                .append(minute));
    }
    // TODO menu 2
    private DatePickerDialog.OnDateSetListener myDate1Menu2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate1Menu2(arg1, arg2 + 1, arg3);
        }
    };
    private DatePickerDialog.OnDateSetListener myDate2Menu2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate2Menu2(arg1, arg2 + 1, arg3);
        }
    };
    private TimePickerDialog.OnTimeSetListener myTimeInMenu2 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
            showTimeInMenu2(arg1, arg2);
        }
    };



    private void showDate1Menu2(int year, int month, int day) {
        txtdatefrom2.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }
    private void showDate2Menu2(int year, int month, int day) {
        txtdateto2.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }
    private void showTimeInMenu2(int hour, int minute) {
        txtexcuseclockin2.setText(new StringBuilder().append(hour).append(":")
                .append(minute));
    }
    // TODO menu 3
    private DatePickerDialog.OnDateSetListener myDate1Menu3 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate1Menu3(arg1, arg2 + 1, arg3);
        }
    };
    private DatePickerDialog.OnDateSetListener myDate2Menu3 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate2Menu3(arg1, arg2 + 1, arg3);
        }
    };



    private void showDate1Menu3(int year, int month, int day) {
        txtdatefrom3.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }
    private void showDate2Menu3(int year, int month, int day) {
        txtdateto3.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }
    // TODO menu 4
    private DatePickerDialog.OnDateSetListener myDate1Menu4 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate1Menu4(arg1, arg2 + 1, arg3);
        }
    };
    private DatePickerDialog.OnDateSetListener myDate2Menu4 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate2Menu4(arg1, arg2 + 1, arg3);
        }
    };



    private void showDate1Menu4(int year, int month, int day) {
        txtdatefrom4.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }
    private void showDate2Menu4(int year, int month, int day) {
        txtdateto4.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }
    // TODO menu 5
    private DatePickerDialog.OnDateSetListener myDate1Menu5 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate1Menu5(arg1, arg2 + 1, arg3);
        }
    };
    private DatePickerDialog.OnDateSetListener myDate2Menu5 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate2Menu5(arg1, arg2 + 1, arg3);
        }
    };



    private void showDate1Menu5(int year, int month, int day) {
        txtdatefrom5.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }
    private void showDate2Menu5(int year, int month, int day) {
        txtdateto5.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }
    // TODO menu Def
    private DatePickerDialog.OnDateSetListener myDate1MenuDef = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate1MenuDef(arg1, arg2 + 1, arg3);
        }
    };
    private DatePickerDialog.OnDateSetListener myDate2MenuDef = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate2MenuDef(arg1, arg2 + 1, arg3);
        }
    };



    private void showDate1MenuDef(int year, int month, int day) {
        txtfromdate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }
    private void showDate2MenuDef(int year, int month, int day) {
        txttodate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }

}