package id.co.arkamaya.bc_android;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import id.co.arkamaya.cico.R;
import pojo.CheckLogin;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by root on 09/10/17.
 */

public class MyProfileUserFragment extends Fragment {
    private View v;
    SharedPreferences pref;
    private ProgressDialog progress;
    private String employee_id,user;
    EditText txtoldpass,txtpass1,txtpass2;
    Button BtnSave,BtnCancel;
    public String ENDPOINT="http://bc-id.co.id/";

    int aspectX;
    int aspectY;
    int outputX;
    int outputY;

    ConnectionDetector conDetector;
    Bundle extras;
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
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return new ScreenResolution(size.x, size.y);
        } else {
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            // getWidth() & getHeight() are depricated
            return new ScreenResolution(display.getWidth(), display.getHeight());
        }
    }
    public MyProfileUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_myprof_user, container, false);
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        v = view;
        pref = PreferenceManager.getDefaultSharedPreferences(v.getContext().getApplicationContext());
//        employee_id=pref.getString("EmployeeCd", null);

//        employee_cd=pref.getString("EmployeeId", null);
        conDetector = new ConnectionDetector(v.getContext().getApplicationContext());
        employee_id = pref.getString("EmployeeId", null);
        ENDPOINT = pref.getString("URLEndPoint", "");
        user = pref.getString("User", null);
        Log.d("Yudha", employee_id);
        extras = getActivity().getIntent().getExtras();
        /* Start insert Able Text Edit */
        txtoldpass = (EditText) getActivity().findViewById(R.id.txtoldpassword);
        txtpass1 = (EditText)getActivity().findViewById(R.id.txtnwpassword);
        txtpass2 = (EditText)getActivity().findViewById(R.id.txtconfirmpassword);

        ScreenResolution sr = deviceDimensions();
        int gcd = GCD(sr.width, sr.height);
        aspectX = sr.width / gcd;
        aspectY = sr.height / gcd;
        // subtract to keep the output image's width & height possibly low as android
        // default crop is not well suited to pick big image
        outputX = sr.width - aspectX * 30;
        outputY = sr.height - aspectY * 30;

        Log.d("Yudha", "ox " + outputX + " oy " + outputY + " ax " + aspectX + " ay " + aspectY);
        BtnCancel =(Button)getActivity().findViewById(R.id.btnCancelUser);
        BtnSave =(Button)getActivity().findViewById(R.id.btnSaveUser);
        BtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        BtnSave.setOnClickListener(new View.OnClickListener() {
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
                                    Toast.makeText(getActivity().getApplicationContext(), "Internet Connection Error..", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

    }
    private int GCD(int a, int b) {
        return (b == 0 ? a : GCD(b, a % b));
    }

    private void onSaveClick(){
        String user_password  = txtoldpass.getText().toString();
        String password1  = txtpass1.getText().toString();
        String password2  = txtpass2.getText().toString();
        if(user_password.equals("")|| password1.equals("") ||password2.equals("")) {
            Toast.makeText(getActivity().getApplicationContext(), "Data tidak lengkap..", Toast.LENGTH_LONG).show();
        }else{
            progress = new ProgressDialog(getActivity());
            progress.setMessage("Processing..");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(ENDPOINT)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();

            APIEditProfile restInterface = restAdapter.create(APIEditProfile.class);
            restInterface.onUpdatePassword(employee_id,user_password,password1,password2
                    , new Callback<CheckLogin>() {
                        @Override
                        public void success(CheckLogin m, Response response) {

                            if (progress != null) {
                                progress.dismiss();
                            }

                            Toast.makeText(getActivity().getApplicationContext(), m.getMsgText(), Toast.LENGTH_SHORT).show();

                            if (m.getMsgType().toLowerCase().equals("info")) {
                                txtoldpass.setText("");
                                txtpass1.setText("");
                                txtpass2.setText("");
                                getActivity().finish();
                            } else {

                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            if (progress != null) {
                                progress.dismiss();
                            }
                        }
                    });
        }
    }

}
