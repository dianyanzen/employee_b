package id.co.arkamaya.cico;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import pojo.CheckLogin;
import pojo.GetOvertimeList;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DashboardActivity extends AppCompatActivity {

    private String OvertimeId;
    SharedPreferences pref;
    private ProgressDialog progress;

    private EditText txtOvertimeDate;
    private EditText txtOvertimeDescription;
    private EditText txtOvertime;

    public String ENDPOINT="http://192.168.88.153:8080/arka_portal";

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
        setContentView(R.layout.activity_dashboard);

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

        txtOvertimeDate = (EditText)findViewById(R.id.txtOvertimeDate);
        txtOvertimeDescription = (EditText)findViewById(R.id.txtOvertimeDescription);
        txtOvertime = (EditText)findViewById(R.id.txtOvertime);
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
}
