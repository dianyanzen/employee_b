package id.co.arkamaya.cico;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pojo.CheckLogin;
import pojo.ComboboxCommon;
import pojo.GetListDailyReport;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class DailyReportActivity extends AppCompatActivity {

    private String BugsId;
    SharedPreferences pref;
    private ProgressDialog progress;

    public Spinner txtProject;
    public Spinner txtFucntion;
    public Spinner txtPhase;
    public Spinner txtCategory;
    public Spinner txtStatus;

    public EditText txtBugsID;
    public EditText txtMode;
    public EditText txtCounterMesure;
    int DATE_PICKER_TO = 0;
    int DATE_PICKER_FROM = 1;

    public EditText txtTask;

    public EditText txtCreatedDate;
    public Button btnCreatedDate;
    public EditText txtDueDate;
    public Button btnDueDate;
    public Button btnSave;
    public Button btnDelete;
    public Button btnSolved;

    String application_Cd = "";
    public String data_project = "";
    public String data_function = "";
    public String data_phase = "";
    public String data_category = "";
    public String data_status = "";

    ArrayAdapter<String> adaAdapterCombo;
    RemoteViews remoteViews;

    public String PMSENDPOINT="http://192.168.3.109:8080/arkapms";

    private String modeEdit;
    /**UserInfo**/
    String User="";
    String EmployeeId="";

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
        setContentView(R.layout.activity_daily_report);

        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        EmployeeId=pref.getString("EmployeeId", null);
        PMSENDPOINT= pref.getString("URLPMSEndPoint", "");//"http://192.168.3.109:8080/arkapms";//pref.getString("URLEndPoint", "");
        User= pref.getString("User",null);
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

        txtProject = (Spinner)findViewById(R.id.txtProject);
        txtFucntion = (Spinner)findViewById(R.id.txtFucntion);
        txtPhase = (Spinner)findViewById(R.id.txtPhase);
        txtCategory = (Spinner)findViewById(R.id.txtCategory);
        txtStatus = (Spinner)findViewById(R.id.txtStatus);
        txtBugsID = (EditText)findViewById(R.id.txtBugsID);
        txtTask = (EditText)findViewById(R.id.txtTask);
        txtCreatedDate = (EditText)findViewById(R.id.txtCreatedDate);
        txtDueDate = (EditText)findViewById(R.id.txtDueDate);
        txtMode = (EditText)findViewById(R.id.txtMode);
        btnCreatedDate = (Button)findViewById(R.id.btnCreatedDate);
        btnDueDate = (Button)findViewById(R.id.btnDueDate);
        remoteViews = new RemoteViews(getPackageName(), R.layout.activity_daily_report);
        btnDelete = (Button)findViewById(R.id.btnDelete);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnSolved = (Button)findViewById(R.id.btnSolved);
        txtCounterMesure = (EditText)findViewById(R.id.txtCounterMesure);

        if (extras != null) {
            modeEdit=extras.getString("Mode");
            if(modeEdit.equals("edit")){
                modeEdit="edit";
                BugsId = extras.getString("BugsId");
                txtCounterMesure.setEnabled(true);
                btnSave.setText("Update");
                btnDelete.setEnabled(true);
                btnSolved.setEnabled(true);
                getDailyReportbyId(BugsId);
            }else{
                modeEdit="new";
                btnSave.setText("Save");
                btnDelete.setEnabled(false);
                btnDelete.setBackgroundColor(Color.parseColor("#9E9E9E"));
                btnSolved.setEnabled(false);
                btnSolved.setBackgroundColor(Color.parseColor("#9E9E9E"));
                txtCounterMesure.setEnabled(true);
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                if(mMonth < 10) {
                    if(mDay < 10) {
                        txtCreatedDate.setText(mYear + "-" + "0" + (mMonth + 1) + "-" + "0" + mDay);
                    }else{
                        txtCreatedDate.setText(mYear + "-" + "0" + (mMonth + 1) + "-" + mDay);
                    }
                }else{
                    if(mDay < 10) {
                        txtCreatedDate.setText(mYear + "-" + (mMonth + 1) + "-" + "0" + mDay);
                    }else{
                        txtCreatedDate.setText(mYear + "-" + (mMonth + 1) + "-" + mDay);
                    }
                }

                if(mMonth < 10) {
                    if(mDay < 10) {
                        txtDueDate.setText(mYear + "-" + "0" + (mMonth + 1) + "-" + "0" + mDay);
                    }else{
                        txtDueDate.setText(mYear + "-" + "0" + (mMonth + 1) + "-" + mDay);
                    }
                }else{
                    if(mDay < 10) {
                        txtDueDate.setText(mYear + "-" + (mMonth + 1) + "-" + "0" + mDay);
                    }else{
                        txtDueDate.setText(mYear + "-" + (mMonth + 1) + "-" + mDay);
                    }
                }

                getProject_list(data_project);

                txtProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    //public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        // your code here
                    //}

                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        String data_spl = parent.getItemAtPosition(pos).toString();
                        String[] separated = data_spl.split(" - ");
                        //Toast.makeText(getApplicationContext(), "The planet is " + separated[0], Toast.LENGTH_LONG).show();

                        application_Cd = separated[0];

                        getFunction_list(application_Cd, data_function);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }

                });

                //String application_Cd = txtProject.getSelectedItem().toString();
                getFunction_list(application_Cd, data_function);
                getPhase_list(data_phase);
                getCategory_list(data_category);
                getStatus_list(data_status);
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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setMessage("Are you sure?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (conDetector.isConnectingToInternet()) {
                                    onSaveDailyReport();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Internet Connection Error..", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setMessage("Are you sure?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (conDetector.isConnectingToInternet()) {
                                    onDeleteDailyReport();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Internet Connection Error..", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        btnSolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setMessage("Are you sure?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (conDetector.isConnectingToInternet()) {
                                    onSolveDailyReport();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Internet Connection Error..", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        Button btnCreatedDate=(Button)findViewById(R.id.btnCreatedDate);
        btnCreatedDate.performClick();
        btnCreatedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });

        Button btnDueDate=(Button)findViewById(R.id.btnDueDate);
        btnDueDate.performClick();
        btnDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDueDate();
            }
        });

    }

    private int GCD(int a, int b) {
        return (b == 0 ? a : GCD(b, a % b));
    }

    private void getProject_list(final String data_project){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(PMSENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIDailyReport restInterface = restAdapter.create(APIDailyReport.class);

        restInterface.getProject_list(EmployeeId, new Callback<List<ComboboxCommon>>() {
            @Override
            public void success(List<ComboboxCommon> m, Response response) {
                List<String> list = new ArrayList<String>();

                for (int i = 0; i < m.size(); i++) {
                    list.add(m.get(i).getId() + " - " + m.get(i).getName());
                }

                adaAdapterCombo = new ArrayAdapter<String>
                        (getApplicationContext(), R.layout.spinner_simple_item, R.id.listCombo, list);

                adaAdapterCombo.setDropDownViewResource
                        (R.layout.spinner_simple_item);

                txtProject.setAdapter(adaAdapterCombo);

                txtProject.setEnabled(true);

                String data = data_project;

                if (data != "") {
                    int idxLocation = adaAdapterCombo.getPosition(data_project);
                    txtProject.setSelection(idxLocation);
                    txtProject.setEnabled(false);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFunction_list(String application_Cd, final String data_function){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(PMSENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIDailyReport restInterface = restAdapter.create(APIDailyReport.class);

        restInterface.getFunction_list(application_Cd, new Callback<List<ComboboxCommon>>() {
            @Override
            public void success(List<ComboboxCommon> m, Response response) {

                List<String> list = new ArrayList<String>();

                for (int i = 0; i < m.size(); i++) {
                    list.add(m.get(i).getId() + " - " + m.get(i).getName());
                }

                adaAdapterCombo = new ArrayAdapter<String>
                        (getApplicationContext(), R.layout.spinner_simple_item, R.id.listCombo, list);

                adaAdapterCombo.setDropDownViewResource
                        (R.layout.spinner_simple_item);

                txtFucntion.setAdapter(adaAdapterCombo);

                String data = data_function;

                if (data != "") {
                    int idxLocation = adaAdapterCombo.getPosition(data_function);
                    txtFucntion.setSelection(idxLocation);
                    txtFucntion.setEnabled(false);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCategory_list(final String data_category){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(PMSENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIDailyReport restInterface = restAdapter.create(APIDailyReport.class);

        restInterface.getCategory_list(EmployeeId, new Callback<List<ComboboxCommon>>() {
            @Override
            public void success(List<ComboboxCommon> m, Response response) {
                List<String> list = new ArrayList<String>();

                for (int i = 0; i < m.size(); i++) {
                    list.add(m.get(i).getId() + " - " + m.get(i).getName());
                }

                adaAdapterCombo = new ArrayAdapter<String>
                        (getApplicationContext(), R.layout.spinner_simple_item, R.id.listCombo, list);

                adaAdapterCombo.setDropDownViewResource
                        (R.layout.spinner_simple_item);

                txtCategory.setAdapter(adaAdapterCombo);

                String data = data_category;

                if (data != "") {
                    int idxLocation = adaAdapterCombo.getPosition(data_category);
                    txtCategory.setSelection(idxLocation);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPhase_list(final String data_phase){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(PMSENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIDailyReport restInterface = restAdapter.create(APIDailyReport.class);

        restInterface.getPhase_list(EmployeeId, new Callback<List<ComboboxCommon>>() {
            @Override
            public void success(List<ComboboxCommon> m, Response response) {
                List<String> list = new ArrayList<String>();

                for (int i = 0; i < m.size(); i++) {
                    list.add(m.get(i).getId() + " - " + m.get(i).getName());
                }

                adaAdapterCombo = new ArrayAdapter<String>
                        (getApplicationContext(), R.layout.spinner_simple_item, R.id.listCombo, list);

                adaAdapterCombo.setDropDownViewResource
                        (R.layout.spinner_simple_item);

                txtPhase.setAdapter(adaAdapterCombo);

                String data = data_phase;

                if (data != "") {
                    int idxLocation = adaAdapterCombo.getPosition(data_phase);
                    txtPhase.setSelection(idxLocation);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getStatus_list(final String data_status){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(PMSENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIDailyReport restInterface = restAdapter.create(APIDailyReport.class);

        restInterface.getStatus_list(EmployeeId, new Callback<List<ComboboxCommon>>() {
            @Override
            public void success(List<ComboboxCommon> m, Response response) {
                List<String> list = new ArrayList<String>();
                list.add("00 - New");
                list.add("04 - Solved");
//                for (int i = 0; i < m.size(); i++) {
//                    list.add(m.get(i).getId() + " - " + m.get(i).getName());
//                }

                adaAdapterCombo = new ArrayAdapter<String>
                        (getApplicationContext(), R.layout.spinner_simple_item, R.id.listCombo, list);

                adaAdapterCombo.setDropDownViewResource
                        (R.layout.spinner_simple_item);

                txtStatus.setAdapter(adaAdapterCombo);

                String data = data_status;

                if (data != "") {
                    int idxLocation = adaAdapterCombo.getPosition(data_status);
                    txtStatus.setSelection(idxLocation);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDailyReportbyId(final String BugsId){
        //Toast.makeText(getApplicationContext().getApplicationContext(), BugsId, Toast.LENGTH_SHORT).show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(PMSENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIDailyReport restInterface = restAdapter.create(APIDailyReport.class);

        restInterface.getDailyReportbyId(BugsId, EmployeeId, new Callback<List<GetListDailyReport>>() {

            @Override
            public void success(List<GetListDailyReport> m, Response response) {

                txtBugsID.setText(m.get(0).getRegNo());
                data_project = m.get(0).getApplicationCd() + " - " + m.get(0).getApplicationNm();
                data_function = m.get(0).getFunctionCd() + " - " + m.get(0).getFunctionNm();
                data_phase = m.get(0).getPhaseCd() + " - " + m.get(0).getPhaseNm();
                data_category = m.get(0).getTaskCatCd() + " - " + m.get(0).getTaskCatNm();
                data_status = m.get(0).getTaskTypeCd() + " - " + m.get(0).getTaskTypeNm();
                txtCreatedDate.setText(m.get(0).getCreatedDt());
                txtDueDate.setText(m.get(0).getDueDate());
                txtTask.setText(m.get(0).getIssueDescription());
                txtMode.setText("Edit");
                txtCreatedDate.setEnabled(false);
                btnCreatedDate.setEnabled(false);
                if(m.get(0).getRemark() != null) {
                    txtCounterMesure.setText(m.get(0).getRemark().toString());
                }else{
                    txtCounterMesure.setText(" ");
                }
                txtBugsID.setEnabled(false);

                txtDueDate.setEnabled(false);
                btnDueDate.setEnabled(false);

                String dafg = BugsId;

                if (Integer.parseInt(m.get(0).getTaskCatCd()) != 1) {
                    txtCounterMesure.setEnabled(false);
                    //Toast.makeText(getApplicationContext().getApplicationContext(), "countermesure true " + m.get(0).getTaskCatCd(), Toast.LENGTH_SHORT).show();
                } else {
                    txtCounterMesure.setEnabled(true);
                    //Toast.makeText(getApplicationContext().getApplicationContext(), "countermesure false " + m.get(0).getTaskCatCd(), Toast.LENGTH_SHORT).show();
                }

                if(m.get(0).getTaskTypeCd().equals("04")){
                    btnSave.setEnabled(false);
                    btnDelete.setEnabled(false);
                    btnSolved.setEnabled(false);
                    txtTask.setEnabled(false);
                    txtCounterMesure.setEnabled(false);
                    txtStatus.setEnabled(false);
                }else{
                    btnSave.setEnabled(true);
                    btnDelete.setEnabled(true);
                    btnSolved.setEnabled(true);
                    txtTask.setEnabled(true);
                    txtCounterMesure.setEnabled(true);
                    txtStatus.setEnabled(true);
                }

                getProject_list(data_project);
                application_Cd = m.get(0).getApplicationCd();
                getFunction_list(application_Cd, data_function);
                getPhase_list(data_phase);
                getCategory_list(data_category);
                getStatus_list(data_status);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onSaveDailyReport(){
        String bugs_id = txtBugsID.getText().toString();
        String task = txtTask.getText().toString();
        String created_date = txtCreatedDate.getText().toString();
        String due_date = txtDueDate.getText().toString();
        String mode = txtMode.getText().toString();

        String Project = txtProject.getSelectedItem().toString();
        String[] item_project = Project.split(" - ");

        String Function = txtFucntion.getSelectedItem().toString();
        String[] item_function = Function.split(" - ");

        String Phase = txtPhase.getSelectedItem().toString();
        String[] item_phase = Phase.split(" - ");

        String Category = txtCategory.getSelectedItem().toString();
        String[] item_category = Category.split(" - ");

        String Status = txtStatus.getSelectedItem().toString();
        String[] item_status = Status.split(" - ");

        String countermesure = txtCounterMesure.getText().toString();

        if(Integer.parseInt(item_category[0]) == 1) {
            if(!countermesure.equals("")){

                progress = new ProgressDialog(this);
                progress.setMessage("Processing..");
                progress.setIndeterminate(true);
                progress.setCancelable(false);
                progress.show();
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(PMSENDPOINT)
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .build();

                APIDailyReport restInterface = restAdapter.create(APIDailyReport.class);

                restInterface.onSaveDailyReport(bugs_id, task, created_date, due_date, item_project[0], item_function[0], item_phase[0], item_category[0], item_status[0], mode, countermesure, EmployeeId, new Callback<CheckLogin>() {
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
            }else{
                Toast.makeText(getApplicationContext(), "Countermesure harus diisi", Toast.LENGTH_SHORT).show();
            }
        }else{
            progress = new ProgressDialog(this);
            progress.setMessage("Processing..");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(PMSENDPOINT)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();

            APIDailyReport restInterface = restAdapter.create(APIDailyReport.class);

            restInterface.onSaveDailyReport(bugs_id, task, created_date, due_date, item_project[0], item_function[0], item_phase[0], item_category[0], item_status[0], mode, countermesure, EmployeeId, new Callback<CheckLogin>() {
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

    private void onDeleteDailyReport(){
        String bugs_id = txtBugsID.getText().toString();
        String Project = txtProject.getSelectedItem().toString();
        String[] item_project = Project.split(" - ");

        progress = new ProgressDialog(this);
        progress.setMessage("Processing..");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(PMSENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIDailyReport restInterface = restAdapter.create(APIDailyReport.class);

        restInterface.onDeleteDailyReport(bugs_id, item_project[0], EmployeeId, new Callback<CheckLogin>() {
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

    private void onSolveDailyReport(){
        String bugs_id = txtBugsID.getText().toString();
        String Project = txtProject.getSelectedItem().toString();
        String[] item_project = Project.split(" - ");
        String Phase = txtPhase.getSelectedItem().toString();
        String[] item_phase = Phase.split(" - ");
        String Category = txtCategory.getSelectedItem().toString();
        String[] item_category = Category.split(" - ");
        String due_date = txtDueDate.getText().toString();
        String countermesure = txtCounterMesure.getText().toString();

        if(Integer.parseInt(item_category[0]) == 1){
            //Toast.makeText(getApplicationContext(), "item category " + Integer.parseInt(item_category[0]), Toast.LENGTH_SHORT).show();

            if(!countermesure.equals("")){

                //Toast.makeText(getApplicationContext(), "check countermesure " + countermesure, Toast.LENGTH_SHORT).show();
                progress = new ProgressDialog(this);
                progress.setMessage("Processing..");
                progress.setIndeterminate(true);
                progress.setCancelable(false);
                progress.show();
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(PMSENDPOINT)
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .build();

                APIDailyReport restInterface = restAdapter.create(APIDailyReport.class);

                restInterface.onSolveDailyReport(bugs_id, item_project[0], item_phase[0], due_date, countermesure, EmployeeId, new Callback<CheckLogin>() {
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

            }else{
                Toast.makeText(getApplicationContext(), "Countermesure harus diisi", Toast.LENGTH_SHORT).show();
            }
        }else{
            //Toast.makeText(getApplicationContext(), "langsung solved kerena statusnya " + Integer.parseInt(item_category[0]), Toast.LENGTH_SHORT).show();
            progress = new ProgressDialog(this);
            progress.setMessage("Processing..");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(PMSENDPOINT)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();

            APIDailyReport restInterface = restAdapter.create(APIDailyReport.class);

            restInterface.onSolveDailyReport(bugs_id, item_project[0], item_phase[0], due_date, countermesure, EmployeeId, new Callback<CheckLogin>() {
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

    @SuppressWarnings("deprecation")
    public void setDate() {
        showDialog(1);
    }

    @SuppressWarnings("deprecation")
    public void setDueDate() {
        showDialog(0);
        //Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
        //        .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        switch(id){
            case 1:
                return new DatePickerDialog(this, myDateListener, year, month, day);
            case 0:
                return new DatePickerDialog(this, myDuelistener, year, month, day);
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

    private DatePickerDialog.OnDateSetListener myDuelistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDueDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        if(month < 10) {
            txtCreatedDate.setText(new StringBuilder().append(year).append("-").append("0")
                    .append(month).append("-").append(day));
        }else{
            txtCreatedDate.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-").append(day));
        }
    }

    private void showDueDate(int year, int month, int day) {
        if(month < 10) {
            txtDueDate.setText(new StringBuilder().append(year).append("-").append("0")
                    .append(month).append("-").append(day));
        }else{
            txtDueDate.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-").append(day));
        }
    }
}
