package id.co.arkamaya.bc_android;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Calendar;

import id.co.arkamaya.cico.R;
import pojo.CheckLogin;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DailyReportSendActivity extends AppCompatActivity {

    SharedPreferences pref;
    private ProgressDialog progress;

    public EditText txtBugsID;
    public EditText txtMode;

    public EditText txtTask;
    public EditText txtCc;
    public EditText txtNote;

    public EditText txtCreatedDate;
    public Button btnCreatedDate;
    public Button btnSend;
    public Button btnPreAlreadyDone;
    public Button btnPreNextTodo;

    ArrayAdapter<String> adaAdapterCombo;
    RemoteViews remoteViews;
    //private ListView list_already_done;
    //private ListView list_next_todo;
    public String PMSENDPOINT="http://bc-id.co.id/";

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
        setContentView(R.layout.activity_daily_report_send);

        //list_already_done=(ListView)findViewById(R.id.list_already_done);
        //list_next_todo=(ListView)findViewById(R.id.list_next_todo);
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        EmployeeId=pref.getString("EmployeeId", null);
        PMSENDPOINT = pref.getString("URLPMSEndPoint", "");//"http://192.168.3.109:8080/arkapms";//pref.getString("URLEndPoint", "");
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

        txtBugsID = (EditText)findViewById(R.id.txtBugsID);
        txtTask = (EditText)findViewById(R.id.txtTask);
        txtCreatedDate = (EditText)findViewById(R.id.txtCreatedDate);
        txtMode = (EditText)findViewById(R.id.txtMode);
        btnCreatedDate = (Button)findViewById(R.id.btnCreatedDate);
        remoteViews = new RemoteViews(getPackageName(), R.layout.activity_daily_report_send);
        btnSend = (Button)findViewById(R.id.btnSend);
        txtCc = (EditText)findViewById(R.id.txtCC);
        txtNote = (EditText)findViewById(R.id.txtNote);

        if (extras != null) {
            modeEdit=extras.getString("Mode");
            modeEdit="new";
            btnSend.setText("Send");
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

            //getListAlreadyDone();
            //getListNextTodo();

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

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setMessage("Are you sure?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (conDetector.isConnectingToInternet()) {
                                    onSendDailyReport();
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

        btnPreAlreadyDone = (Button)findViewById(R.id.btnPreAlreadyDone);
        btnPreAlreadyDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PreAlreadyDoneActivity.class);
                startActivity(intent);
            }
        });

        btnPreNextTodo = (Button)findViewById(R.id.btnPreNextTodo);
        btnPreNextTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PreNextTodoActivity.class);
                startActivity(intent);
            }
        });
    }

    private int GCD(int a, int b) {
        return (b == 0 ? a : GCD(b, a % b));
    }

    private void onSendDailyReport(){
        String task = txtTask.getText().toString();
        String created_date = txtCreatedDate.getText().toString();
        String mode = txtMode.getText().toString();
        String CC = txtCc.getText().toString();
        String Note = txtNote.getText().toString();

        progress = new ProgressDialog(this);
        progress.setMessage("Processing..");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setSecondaryProgress(1000);
        progress.show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(PMSENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIDailyReport restInterface = restAdapter.create(APIDailyReport.class);

        restInterface.onSendDailyReport(task, created_date, mode, CC, Note, EmployeeId, new Callback<CheckLogin>() {

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

    @SuppressWarnings("deprecation")
    public void setDate() {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == 999){
            return new DatePickerDialog(this, myDateListener, year, month, day);
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
        if(month < 10) {
            txtCreatedDate.setText(new StringBuilder().append(year).append("-").append("0")
                    .append(month).append("-").append(day));
        }else{
            txtCreatedDate.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-").append(day));
        }
    }
}
