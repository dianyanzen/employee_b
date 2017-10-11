package id.co.arkamaya.cico;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import pojo.GetListDailyReport;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PreAlreadyDoneActivity extends AppCompatActivity {
    private ListView list_already_done;
    public String PMSENDPOINT="http://192.168.3.109:8080/arkapms";
    SharedPreferences pref;
    private ProgressDialog progress;

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
        setContentView(R.layout.activity_pre_already_done);

        list_already_done=(ListView)findViewById(R.id.list_already_done);
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

        getListAlreadyDone();

        Button btnCancel =(Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private int GCD(int a, int b) {
        return (b == 0 ? a : GCD(b, a % b));
    }

    private void getListAlreadyDone(){
        //Toast.makeText(getActivity().getApplicationContext(), employe_id, Toast.LENGTH_SHORT).show();

        /*-------------------------------*/
        progress = new ProgressDialog(this);
        progress.setMessage("Syncrhonizing...");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(PMSENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIDailyReport restInterface = restAdapter.create(APIDailyReport.class);

        restInterface.getDailyReport_AlreadyDone_list(EmployeeId, new Callback<List<GetListDailyReport>>() {
            @Override
            public void success(List<GetListDailyReport> m, Response response) {
                list_already_done.setAdapter(null);
                if (m.size() > 0) {
                    AdapterAlreadyDoneListView adapt = new AdapterAlreadyDoneListView(getApplicationContext().getApplicationContext(), R.layout.list_adapter_already_done, m);
                    list_already_done.setAdapter(adapt);
                }

                if (progress != null) {
                    progress.dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                if (progress != null) {
                    progress.dismiss();
                }
            }
        });
    }
}
