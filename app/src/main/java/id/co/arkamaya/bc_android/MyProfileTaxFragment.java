package id.co.arkamaya.bc_android;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.co.arkamaya.cico.R;
import pojo.CheckLogin;
import pojo.GetProfileTax;
import pojo.GetProfileTaxMarital;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by root on 09/10/17.
 */

public class MyProfileTaxFragment extends Fragment {
    private View v;
    SharedPreferences pref;
    private ProgressDialog progress;
    private String employee_id,user;
    public String ENDPOINT="http://bc-id.co.id/";
    EditText txtIdNpwp,txtNpwpDt,txtIdBpjsKerja,txtIdBpjsSehat;
    Button btnNwpwDate, BtnSavetax, BtnCancel;
    ArrayAdapter<String> adaAdapterMarital;
    String data_marital = "";
    Spinner spnMarital;
    int idxMarital;
    int aspectX;
    int aspectY;
    int outputX;
    int outputY;
    ConnectionDetector conDetector;
    Bundle extras;
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
    public MyProfileTaxFragment() {
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
        return inflater.inflate(R.layout.fragment_myprof_tax, container, false);
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        v = view;
        pref = PreferenceManager.getDefaultSharedPreferences(v.getContext().getApplicationContext());
        conDetector = new ConnectionDetector(v.getContext().getApplicationContext());
        employee_id = pref.getString("EmployeeId", null);
        ENDPOINT = pref.getString("URLEndPoint", "");
        user = pref.getString("User", null);
        Log.d("Yudha", employee_id);
        extras = getActivity().getIntent().getExtras();
        /* Start insert Able Text Edit */
        txtIdNpwp = (EditText) getActivity().findViewById(R.id.txtidnpwp);
        txtNpwpDt = (EditText) getActivity().findViewById(R.id.txtnpwpdate);
        txtIdBpjsKerja = (EditText) getActivity().findViewById(R.id.txtketenagakerjaan);
        txtIdBpjsSehat = (EditText) getActivity().findViewById(R.id.txtbpjskesehatan);
        btnNwpwDate = (Button) getActivity().findViewById(R.id.btnDatePicker);
        btnNwpwDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNpwpDate();

            }
        });

        spnMarital = (Spinner) getActivity().findViewById(R.id.spnmaritalstatus);
        getMarital();
        spnMarital.setEnabled(false);
        txtIdBpjsKerja.setEnabled(false);
        txtIdBpjsSehat.setEnabled(false);
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
        BtnCancel =(Button)getActivity().findViewById(R.id.btnCancelTax );
        BtnSavetax =(Button)getActivity().findViewById(R.id.btnSaveTax);
        BtnSavetax.setOnClickListener(new View.OnClickListener() {
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
        BtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        getProfileTax(employee_id);
    }
    private int GCD(int a, int b) {
        return (b == 0 ? a : GCD(b, a % b));
    }
    public void getMarital(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIEditProfile restInterface = restAdapter.create(APIEditProfile.class);

        restInterface.getProfileTaxMarital(new  Callback<List<GetProfileTaxMarital>>() {
            @Override
            public void success(List<GetProfileTaxMarital> m, Response response) {
                List<String> list = new ArrayList<String>();

                for (int i = 0; i < m.size(); i++) {
                    list.add(m.get(i).getTaxMarital());
                }
                adaAdapterMarital = new ArrayAdapter<String>
                        (getActivity().getApplicationContext(), R.layout.spinner_simple_item, R.id.listCombo, list);

                adaAdapterMarital.setDropDownViewResource
                        (R.layout.spinner_simple_item);

                spnMarital.setAdapter(adaAdapterMarital);

                String data = data_marital;

                if (data != "") {
                    int idxLocation = adaAdapterMarital.getPosition(data_marital);
                    spnMarital.setSelection(idxLocation);
                }
                spnMarital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                // Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void setNpwpDate() {
        DatePickerDialogFragment date = new DatePickerDialogFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(onNpwpDate);
        date.show(getFragmentManager(), "Date Picker");
        //getActivity().showDialog(100);
        //Toast.makeText(getActivity(),"Gajalan",Toast.LENGTH_LONG).show();
    }
    DatePickerDialog.OnDateSetListener onNpwpDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int day) {
            txtNpwpDt.setText(new StringBuilder().append(year).append("-")
                    .append(monthOfYear+ 1).append("-").append(day));
            //Toast.makeText(getActivity().getApplicationContext(), String.valueOf(year) + "-" + String.valueOf(monthOfYear) + "-" + String.valueOf(dayOfMonth), Toast.LENGTH_LONG).show();
        }
    };
    private void getProfileTax(String employee_id){
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Load Data..");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIEditProfile restInterface = restAdapter.create(APIEditProfile.class);
        restInterface.getProfileTax(employee_id, new Callback<GetProfileTax>() {
            @Override
            public void success(GetProfileTax m, Response response) {
                try {
                /* Start insert Able Text Edit */
                    txtIdNpwp.setText(m.getUserNpwpNumber().toString());
                    txtNpwpDt.setText(m.getUserNpwpDt().toString());
                    txtIdBpjsKerja.setText(m.getUserBpjsKerja().toString());
                    txtIdBpjsSehat.setText(m.getUserBpjsSehat().toString());
                    String Marital = m.getUserMarital().toString();
                    if (Marital.equalsIgnoreCase("k3")) {
                        idxMarital = 4;
                    }else if (Marital.equalsIgnoreCase("k2")){
                        idxMarital = 3;
                    }else if (Marital.equalsIgnoreCase("k1")){
                        idxMarital = 2;
                    }else if (Marital.equalsIgnoreCase("k0")){
                        idxMarital = 1;
                    }else {
                        idxMarital = 0;
                    }
                    spnMarital.setSelection(idxMarital);

                }catch (Exception e){

                }



                if (progress != null) {
                    progress.dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {

                if (progress != null) {
                    progress.dismiss();
                }
            }
        });

    }
    private void onSaveClick(){
        String user_npwp  = txtIdNpwp.getText().toString();
        String user_npwp_dt  = txtNpwpDt.getText().toString();
        String user_marital  = ((String) spnMarital.getSelectedItem());
        String user_bpjs_ketenagakerjaan  = txtIdBpjsKerja.getText().toString();
        String user_bpjs_kesehatan = txtIdBpjsSehat.getText().toString();
        if(employee_id.equals("")) {
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
            restInterface.onUpdateTax(employee_id,user_npwp,user_npwp_dt,user_marital,user_bpjs_ketenagakerjaan,user_bpjs_kesehatan
                    , new Callback<CheckLogin>() {
                        @Override
                        public void success(CheckLogin m, Response response) {

                            if (progress != null) {
                                progress.dismiss();
                            }

                            Toast.makeText(getActivity().getApplicationContext(), m.getMsgText(), Toast.LENGTH_SHORT).show();

                            if (m.getMsgType().toLowerCase().equals("info")) {
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
