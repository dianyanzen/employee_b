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
import pojo.GetProfileIdCardList;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by root on 16/10/17.
 */

public class MyProfileAddIdCard extends AppCompatActivity {
    private String CardId;
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
    EditText txtidcnumber,txtidcissuedate,txtidcplace,txtidcexpdate;
    Spinner spnidctype,spnidcdescript;
    Button btnCancel,btnidcissuedate,btnidcexpdate,btnSave;
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
        setContentView(R.layout.activity_idcard);
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        EmployeeId = pref.getString("EmployeeId", null);
        ENDPOINT = pref.getString("URLEndPoint", "");
        User = pref.getString("User", null);
        conDetector = new ConnectionDetector(getApplicationContext());
        spnidctype = (Spinner)findViewById(R.id.spnidctype);
        spnidcdescript = (Spinner)findViewById(R.id.spnidcdescript);
        getType();
        txtidcnumber = (EditText)findViewById(R.id.txtidcnumber);
        txtidcissuedate = (EditText)findViewById(R.id.txtidcissuedate);
        txtidcplace = (EditText)findViewById(R.id.txtidcplace);
        txtidcexpdate = (EditText)findViewById(R.id.txtidcexpdate);
        btnidcissuedate = (Button)findViewById(R.id.btnidcissuedate);
        btnidcissuedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setissuedate();
            }
        });
        btnidcexpdate = (Button)findViewById(R.id.btnidcexpdate);
        btnidcexpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setexpdate();
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
                CardId = extras.getString("CardId");
                try {
                    getCardById(CardId);

                } catch (Exception e) {

                }

            } else {
                modeEdit = "new";
                CardId = "";
                txtidcnumber.setText("");
                txtidcissuedate.setText("");
                txtidcplace.setText("");
                txtidcexpdate.setText("");
            }

        } else {
            Toast.makeText(this, "Invalid parameter", Toast.LENGTH_SHORT).show();
            finish();
        }
        /*spnidctype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_search = false;
            }
        });*/
        btnCancel =(Button)findViewById(R.id.btnidcCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSave = (Button)findViewById(R.id.btnidcSave);
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
        String card_id = CardId;
        String card_type = ((String) spnidctype.getSelectedItem());
        String card_description = ((String) spnidcdescript.getSelectedItem());
        String card_number = txtidcnumber.getText().toString();
        String card_issue_date = txtidcissuedate.getText().toString();
        String card_place = txtidcplace.getText().toString();
        String card_expired_date = txtidcexpdate.getText().toString();
        String employee_id = EmployeeId;
        if(employee_id.equals("") || card_number.equals("") || card_type.equals("") || card_description.equals("") || card_place.equals((""))) {
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

            APIEditProfile restInterface = restAdapter.create(APIEditProfile.class);
            restInterface.onAddCard(card_id
                    ,employee_id
                    ,User
                    ,card_type
                    ,card_description
                    ,card_number
                    ,card_issue_date
                    ,card_place
                    ,card_expired_date
                    , new Callback<CheckLogin>() {
                        @Override
                        public void success(CheckLogin m, Response response) {

                            if (progress != null) {
                                progress.dismiss();
                            }

                            Toast.makeText(MyProfileAddIdCard.this, m.getMsgText(), Toast.LENGTH_SHORT).show();

                            if (m.getMsgType().toLowerCase().equals("info")) {
                                finish();
                            } else {

                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(MyProfileAddIdCard.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        spnidctype.setAdapter(null);
        list.add("KTP");
        list.add("SIM");

        adaAdapterType = new ArrayAdapter<String>
                (MyProfileAddIdCard.this, R.layout.spinner_simple_item, R.id.listCombo, list);

        adaAdapterType.setDropDownViewResource
                (R.layout.spinner_simple_item);

        spnidctype.setAdapter(adaAdapterType);

        String data = data_type;
        if (data != "") {
            int idxLocation = adaAdapterType.getPosition(data_type);
            spnidctype.setSelection(idxLocation);
        }
        /*spnidctype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_search = false;
            }
        });*/
        spnidctype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (is_search == false) {
                    getDescrip();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

    }
    public void getDescrip(){
        String IdType = ((String) spnidctype.getSelectedItem());
        if (IdType.equals("KTP")) {
            getDescripKtp();
        }else if (IdType.equals("SIM")) {
            getDescriptSim();
        }
    }
    public void getDescripKtp(){

        List<String> list = new ArrayList<String>();
        list.clear();
        spnidcdescript.setAdapter(null);
        list.add("E-KTP");


        adaAdapterdescript = new ArrayAdapter<String>
                (MyProfileAddIdCard.this, R.layout.spinner_simple_item, R.id.listCombo, list);

        adaAdapterdescript.setDropDownViewResource
                (R.layout.spinner_simple_item);

        spnidcdescript.setAdapter(adaAdapterdescript);

        String data = data_descript;

        if (data != "") {
            int idxLocation = adaAdapterdescript.getPosition(data_descript);
            spnidcdescript.setSelection(idxLocation);
        }
        spnidcdescript.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

    }
    public void getDescriptSim(){

        List<String> list = new ArrayList<String>();
        list.clear();
        spnidcdescript.setAdapter(null);
            list.add("SIM-A");
            list.add("SIM-B");
            list.add("SIM-C");

        adaAdapterdescript = new ArrayAdapter<String>
                (MyProfileAddIdCard.this, R.layout.spinner_simple_item, R.id.listCombo, list);

        adaAdapterdescript.setDropDownViewResource
                (R.layout.spinner_simple_item);

        spnidcdescript.setAdapter(adaAdapterdescript);

        String data = data_descript;

        if (data != "") {
            int idxLocation = adaAdapterdescript.getPosition(data_descript);
            spnidcdescript.setSelection(idxLocation);
        }
        spnidcdescript.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

    }
    private void getCardById(String CardId){
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

        APIEditProfile restInterface = restAdapter.create(APIEditProfile.class);

        restInterface.getCardListById(CardId, new Callback<GetProfileIdCardList>() {
            @Override
            public void success(GetProfileIdCardList m, Response response) {
                try {
                    txtidcnumber.setText(m.getIdNumber().toString());
                    txtidcissuedate.setText(m.getIssuedDt().toString());
                    txtidcplace.setText(m.getPlaced().toString());
                    txtidcexpdate.setText(m.getExpiredDt().toString());
                    String Type = m.getTypeCard().toString();
                    String Description = m.getDescription().toString();
                    if (Type.equalsIgnoreCase("sim")) {
                        idxType = 1;
                        getDescriptSim();
                    }else {
                        idxType = 0;
                        getDescripKtp();
                    }
                    Log.e("Descript",Description);
                    spnidctype.setSelection(idxType);

                    if (Description.equalsIgnoreCase("sim-c")) {
                        idxdescript = 2;
                    }else if (Description.equalsIgnoreCase("sim-b")) {
                        idxdescript = 1;
                    }else if (Description.equalsIgnoreCase("sim-a")){
                        idxdescript = 0;
                    }
                    Log.e("idxDescript",String.valueOf(idxdescript));
                    spnidcdescript.setSelection(idxdescript);
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
    public void setexpdate() {
        showDialog(100);
    }
    public void setissuedate() {
        showDialog(200);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Menu 1
        if (id == 100) {
            return new DatePickerDialog(this, myExpdate, year, month, day);
        }
        else if (id == 200) {
            return new DatePickerDialog(this, myIssuedate, year, month, day);
        }
        else{
            return null;
        }

    }
    private DatePickerDialog.OnDateSetListener myExpdate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showmyExpdate(arg1, arg2 + 1, arg3);
        }
    };
    private void showmyExpdate(int year, int month, int day) {
        txtidcexpdate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }
    private DatePickerDialog.OnDateSetListener myIssuedate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showmyIssuedate(arg1, arg2 + 1, arg3);
        }
    };
    private void showmyIssuedate(int year, int month, int day) {
        txtidcissuedate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }
}
