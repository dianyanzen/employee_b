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
import pojo.GetProfileFamilyList;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by root on 16/10/17.
 */

public class MyProfileAddFamily extends AppCompatActivity {

    private String FamilyId;
    String getRelation = "";
    SharedPreferences pref;
    private ProgressDialog progress;
    public String ENDPOINT = "http://bc-id.co.id/";
    int idxGender,idxRelation;
    ArrayAdapter<String> adaAdapterRelation,adaAdapterGender;
    private String modeEdit;
    /**
     * UserInfo
     **/
    String User = "";
    String EmployeeId = "";
    public String data_relation = "";
    public String data_gender = "";
    int aspectX;
    int aspectY;
    int outputX;
    int outputY;
    EditText txtfmlborndt,txtfmlfullname,txtfmlbornplace,txtfmlnationality;
    Spinner spnRelation,spnGender;
    Button btnCancel,btnBornDt,btnSave;
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
        setContentView(R.layout.activity_family);
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        EmployeeId = pref.getString("EmployeeId", null);
        ENDPOINT = pref.getString("URLEndPoint", "");
        User = pref.getString("User", null);
        conDetector = new ConnectionDetector(getApplicationContext());
        spnRelation = (Spinner)findViewById(R.id.spnfmlrelation);
        spnGender = (Spinner)findViewById(R.id.spnfmlgender);
        getRelation();
        getGender();
        txtfmlborndt = (EditText)findViewById(R.id.txtfmlborndate);
        txtfmlfullname = (EditText)findViewById(R.id.txtfmlfullname);
        txtfmlbornplace = (EditText)findViewById(R.id.txtfmlbornplace);
        txtfmlnationality = (EditText)findViewById(R.id.txtfmlnationality);
        btnBornDt = (Button)findViewById(R.id.btnfmlborndate);
        btnBornDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setborndt();
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
                FamilyId = extras.getString("FamilyId");
                try {
                    getFamilyById(FamilyId);
                } catch (Exception e) {

                }

            } else {
                modeEdit = "new";
                FamilyId = "";
                txtfmlborndt.setText("");
                txtfmlbornplace.setText("");
                txtfmlfullname.setText("");
                txtfmlnationality.setText("");
            }

        } else {
            Toast.makeText(this, "Invalid parameter", Toast.LENGTH_SHORT).show();
            finish();
        }
        btnCancel =(Button)findViewById(R.id.btnfmlCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSave = (Button)findViewById(R.id.btnfmlSave);
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
        String family_id = FamilyId;
        String family_relation = ((String) spnRelation.getSelectedItem());
        String family_gender = ((String) spnGender.getSelectedItem());
        String family_full_name = txtfmlfullname.getText().toString();
        String family_born_place = txtfmlbornplace.getText().toString();
        String family_born_date = txtfmlborndt.getText().toString();
        String family_nationality = txtfmlnationality.getText().toString();
        String employee_id = EmployeeId;
        if(employee_id.equals("") || family_full_name.equals("") || family_born_place.equals("") || family_born_date.equals("")) {
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
            restInterface.onAddFamily(family_id
                    ,employee_id
                    ,User
                    ,family_relation
                    ,family_gender
                    ,family_full_name
                    ,family_born_place
                    ,family_born_date
                    ,family_nationality
                    , new Callback<CheckLogin>() {
                        @Override
                        public void success(CheckLogin m, Response response) {

                            if (progress != null) {
                                progress.dismiss();
                            }

                            Toast.makeText(MyProfileAddFamily.this, m.getMsgText(), Toast.LENGTH_SHORT).show();

                            if (m.getMsgType().toLowerCase().equals("info")) {
                                finish();
                            } else {

                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(MyProfileAddFamily.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void getRelation(){
        /*RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIEditProfile restInterface = restAdapter.create(APIEditProfile.class);

        restInterface.getProfileFamilyRelation(new  Callback<List<GetProfileFamilyRelation>>() {
            @Override
            public void success(List<GetProfileFamilyRelation> m, Response response) {
                List<String> list = new ArrayList<String>();

                for (int i = 0; i < m.size(); i++) {
                    list.add(m.get(i).getRelationship());
                }*/
                List<String> list = new ArrayList<String>();
                list.clear();
                spnRelation.setAdapter(null);
                list.add("ANAK");
                list.add("AYAH");
                list.add("IBU");
                list.add("ISTRI");
                list.add("SUAMI");
                list.add("FAMILY LAINNYA");

                adaAdapterRelation = new ArrayAdapter<String>
                        (MyProfileAddFamily.this, R.layout.spinner_simple_item, R.id.listCombo, list);

                adaAdapterRelation.setDropDownViewResource
                        (R.layout.spinner_simple_item);

                spnRelation.setAdapter(adaAdapterRelation);

                String data = data_relation;

                if (data != "") {
                    int idxLocation = adaAdapterRelation.getPosition(data_relation);
                    spnRelation.setSelection(idxLocation);
                }
                spnRelation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {

                    }
                });
            /*}

            @Override
            public void failure(RetrofitError error) {
                // Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/

    }
    public void getGender(){
        List<String> list = new ArrayList<String>();
        list.clear();
        spnGender.setAdapter(null);
        list.add("Male");
        list.add("Female");
        adaAdapterGender = new ArrayAdapter<String>
                (MyProfileAddFamily.this, R.layout.spinner_simple_item, R.id.listCombo, list);

        adaAdapterGender.setDropDownViewResource
                (R.layout.spinner_simple_item);

        spnGender.setAdapter(adaAdapterGender);

        String data = data_gender;

        if (data != "") {
            int idxLocation = adaAdapterGender.getPosition(data_gender);
            spnGender.setSelection(idxLocation);
        }
        spnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }
    @SuppressWarnings("deprecation")
    public void setborndt() {
        showDialog(100);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Menu 1
        if (id == 100) {
            return new DatePickerDialog(this, myDate1Menu1, year, month, day);
        }
        else{
            return null;
        }

    }
    private DatePickerDialog.OnDateSetListener myDate1Menu1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate1Menu1(arg1, arg2 + 1, arg3);
        }
    };
    private void showDate1Menu1(int year, int month, int day) {
        txtfmlborndt.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }
    private void getFamilyById(String FamilyId){
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

        restInterface.getFamilyListById(FamilyId, new Callback<GetProfileFamilyList>() {
            @Override
            public void success(GetProfileFamilyList m, Response response) {
                try {
                    txtfmlfullname.setText(m.getFullName().toString());
                    txtfmlborndt.setText(m.getBornDate().toString());
                    txtfmlbornplace.setText(m.getBornPlace().toString());
                    txtfmlnationality.setText(m.getNationality().toString());
                    String Gender = m.getGender().toString();
                    String Relation = m.getRelationship().toString();
                    if (Gender.equalsIgnoreCase("female")) {
                        idxGender = 1;
                    }else {
                        idxGender = 0;
                    }
                    if (Relation.equalsIgnoreCase("family lainnya")) {
                        idxRelation = 5;
                    }else if (Relation.equalsIgnoreCase("suami")) {
                        idxRelation = 4;
                    }else if (Relation.equalsIgnoreCase("istri")) {
                        idxRelation = 3;
                    }else if (Relation.equalsIgnoreCase("ibu")) {
                        idxRelation = 2;
                    }else if (Relation.equalsIgnoreCase("ayah")) {
                        idxRelation = 1;
                    }else {
                        idxRelation = 0;
                    }
                    spnRelation.setSelection(idxRelation);
                    spnGender.setSelection(idxGender);
                    btnSave.setText("UPDATE");
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

}