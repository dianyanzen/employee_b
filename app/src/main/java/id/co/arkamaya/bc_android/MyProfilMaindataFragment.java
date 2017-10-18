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
import pojo.GetProfileMain;
import pojo.GetProfileMainMarried;
import pojo.GetProfileMainReligion;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by root on 06/10/17.
 */

public class MyProfilMaindataFragment extends Fragment {
    private View v;
    SharedPreferences pref;
    private ProgressDialog progress;
    private String employee_id,user;
    EditText txtBornDate,txtmeriedsince,txtTitle,txtBornPlace;
    TextView lblmeriedsince;
    Button BtnSave,BtnCancel;
    Spinner spnGender,spnMeried,spnReligion;
    String MariedStat;
    ArrayAdapter<String> adaAdapterMaried,adaAdapterGender,adaAdapterReligion;

    String data_gender = "";
    String data_meried = "";
    String data_religion = "";
    int idxGender,idxReligion;
    int idxMarried;
    Button btnBornDate,btnmeriedsince;
    public String ENDPOINT="http://bc-id.co.id/";

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
    public MyProfilMaindataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_myprof_maindata, container, false);
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        v=view;
        pref = PreferenceManager.getDefaultSharedPreferences(v.getContext().getApplicationContext());
//        employee_id=pref.getString("EmployeeCd", null);

//        employee_cd=pref.getString("EmployeeId", null);
        conDetector =  new ConnectionDetector(v.getContext().getApplicationContext());
        employee_id=pref.getString("EmployeeId", null);
        ENDPOINT= pref.getString("URLEndPoint", "");
        user= pref.getString("User",null);
        Log.d("Yudha", employee_id);
        extras = getActivity().getIntent().getExtras();
        /* Start insert Able Text Edit */
        txtTitle =  (EditText)getActivity().findViewById(R.id.txttitle);
        spnGender = (Spinner)getActivity().findViewById(R.id.spnGender);
        txtBornDate = (EditText)getActivity().findViewById(R.id.txtBornDate);
        txtBornPlace =  (EditText)getActivity().findViewById(R.id.txtBornPlace);
        spnReligion =  (Spinner)getActivity().findViewById(R.id.spnReligion);
        spnMeried = (Spinner)getActivity().findViewById(R.id.spnmariedstatus);
        txtmeriedsince = (EditText)getActivity().findViewById(R.id.txtmeriedsince);
        /* End */
        lblmeriedsince = (TextView)getActivity().findViewById(R.id.lblmeriedsince);
        //setMeriedsince();
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




        getGender();
        getMeried();
        getMainReligion();
        BtnCancel =(Button)getActivity().findViewById(R.id.btnCancelMain);
        BtnSave =(Button)getActivity().findViewById(R.id.btnSaveMain);
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
        btnBornDate =(Button)getActivity().findViewById(R.id.btnBornDate);
        btnmeriedsince = (Button) getActivity().findViewById(R.id.btnmeriedsince);
        btnBornDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate1();

            }
        });
        getProfileMaindata(employee_id);


    }

    public void setMeriedsince(){
        MariedStat = ((String) spnMeried.getSelectedItem());
        if (MariedStat.equals("Single")) {
            txtmeriedsince.setText("");
            btnmeriedsince.setEnabled(false);
        }else {
            btnmeriedsince.setEnabled(true);
            btnmeriedsince.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDate2();

                }
            });
        }
    }

    public void getGender(){
        List<String> list = new ArrayList<String>();
        list.clear();
        spnGender.setAdapter(null);
        list.add("Male");
        list.add("Female");

        adaAdapterMaried = new ArrayAdapter<String>
                (getActivity().getApplicationContext(), R.layout.spinner_simple_item, R.id.listCombo, list);

        adaAdapterMaried.setDropDownViewResource
                (R.layout.spinner_simple_item);

        spnGender.setAdapter(adaAdapterMaried);

        String data = data_gender;

        if (data != "") {
            int idxLocation = adaAdapterMaried.getPosition(data_gender);
            spnGender.setSelection(idxLocation);
        }
    }
    public void getMeried(){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIEditProfile restInterface = restAdapter.create(APIEditProfile.class);

        restInterface.getProfileMainMarried(new  Callback<List<GetProfileMainMarried>>() {
            @Override
            public void success(List<GetProfileMainMarried> m, Response response) {
                List<String> list = new ArrayList<String>();

                for (int i = 0; i < m.size(); i++) {
                    list.add(m.get(i).getMarriedStatus());
                }
        adaAdapterGender = new ArrayAdapter<String>
                (getActivity().getApplicationContext(), R.layout.spinner_simple_item, R.id.listCombo, list);

        adaAdapterGender.setDropDownViewResource
                (R.layout.spinner_simple_item);

        spnMeried.setAdapter(adaAdapterGender);

        String data = data_meried;

        if (data != "") {
            int idxLocation = adaAdapterGender.getPosition(data_meried);
            spnMeried.setSelection(idxLocation);
        }
        spnMeried.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                setMeriedsince();

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
    public void getMainReligion(){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIEditProfile restInterface = restAdapter.create(APIEditProfile.class);

        restInterface.getProfileMainReligion(new  Callback<List<GetProfileMainReligion>>() {
            @Override
            public void success(List<GetProfileMainReligion> m, Response response) {
                List<String> list = new ArrayList<String>();

                for (int i = 0; i < m.size(); i++) {
                    list.add(m.get(i).getReligions());
                }
                adaAdapterReligion = new ArrayAdapter<String>
                        (getActivity().getApplicationContext(), R.layout.spinner_simple_item, R.id.listCombo, list);

                adaAdapterReligion.setDropDownViewResource
                        (R.layout.spinner_simple_item);

                spnReligion.setAdapter(adaAdapterReligion);

                String data = data_religion;

                if (data != "") {
                    int idxLocation = adaAdapterReligion.getPosition(data_religion);
                    spnReligion.setSelection(idxLocation);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                // Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private int GCD(int a, int b) {
        return (b == 0 ? a : GCD(b, a % b));
    }
    public void setDate1() {
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
        date.setCallBack(onBornDate);
        date.show(getFragmentManager(), "Date Picker");
        //getActivity().showDialog(100);
        //Toast.makeText(getActivity(),"Gajalan",Toast.LENGTH_LONG).show();
    }
    public void setDate2() {
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
        date.setCallBack(onMeriedSince);
        date.show(getFragmentManager(), "Date Picker");
        //getActivity().showDialog(100);
        //Toast.makeText(getActivity(),"Gajalan",Toast.LENGTH_LONG).show();
    }
    DatePickerDialog.OnDateSetListener onBornDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int day) {
            txtBornDate.setText(new StringBuilder().append(year).append("-")
                    .append(monthOfYear+ 1).append("-").append(day));
            //Toast.makeText(getActivity().getApplicationContext(), String.valueOf(year) + "-" + String.valueOf(monthOfYear) + "-" + String.valueOf(dayOfMonth), Toast.LENGTH_LONG).show();
        }
    };
    DatePickerDialog.OnDateSetListener onMeriedSince = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int day) {
            txtmeriedsince.setText(new StringBuilder().append(year).append("-")
                    .append(monthOfYear+ 1).append("-").append(day));
            //Toast.makeText(getActivity().getApplicationContext(), String.valueOf(year) + "-" + String.valueOf(monthOfYear) + "-" + String.valueOf(dayOfMonth), Toast.LENGTH_LONG).show();
        }
    };
    private void getProfileMaindata(String employee_id){
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
        restInterface.getProfileMain(employee_id, new Callback<GetProfileMain>() {
            @Override
            public void success(GetProfileMain m, Response response) {
                try {
                    txtTitle.setText(m.getTitle().toString());
                    txtBornDate.setText(m.getBornDt().toString());
                    txtBornPlace.setText(m.getBornPlace().toString());
                    String Religion = m.getReligion().toString();
                    if (Religion.equalsIgnoreCase("islam")) {
                        idxReligion = 0;
                    }else if (Religion.equalsIgnoreCase("kristen")){
                        idxReligion = 1;
                    }else if (Religion.equalsIgnoreCase("hindu")){
                        idxReligion = 2;
                    }else if (Religion.equalsIgnoreCase("budha")){
                        idxReligion = 3;
                    }else{
                        idxReligion = 4;
                    }
                    txtmeriedsince.setText(m.getMarriedSince().toString());
                    String Gender = m.getGender().toString();
                    if (Gender.equalsIgnoreCase("female")){
                     idxGender = 1;
                    }else{
                     idxGender = 0;
                    }
                    String Married = m.getMarriedStatus().toString();
                    if (Married.equalsIgnoreCase("divorce")) {
                        idxMarried = 2;
                        btnmeriedsince.setEnabled(true);
                        btnmeriedsince.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setDate2();
                            }
                        });
                    }else if (Married.equalsIgnoreCase("married")){
                        idxMarried = 1;
                        btnmeriedsince.setEnabled(true);
                        btnmeriedsince.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setDate2();
                            }
                        });
                    }else{
                        idxMarried = 0;
                        txtmeriedsince.setText("");
                        btnmeriedsince.setEnabled(false);
                    }
                    Log.e("Yanzen",String.valueOf(idxGender)+" "+Gender );
                    Log.e("Yanzen",String.valueOf(idxMarried)+" "+Married );
                    spnReligion.setSelection(idxReligion);
                    spnGender.setSelection(idxGender);
                    spnMeried.setSelection(idxMarried);
                }catch (Exception e){

                }



                if (progress != null) {
                    progress.dismiss();
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
    private void onSaveClick(){
        String user_title  = txtTitle.getText().toString();
        String user_born_dt  = txtBornDate.getText().toString();
        String user_born_place  = txtBornPlace.getText().toString();
        String user_married_since  = txtmeriedsince.getText().toString();
        String user_gender = ((String) spnGender.getSelectedItem());
        String user_religion = ((String) spnReligion.getSelectedItem());
        String user_married_status = ((String) spnMeried.getSelectedItem());
        if(user_born_dt.equals("")|| user_born_place.equals("")) {
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
            restInterface.onUpdateMainData(employee_id,user,user_title,user_gender,user_born_dt,user_born_place,user_religion,user_married_status,user_married_since
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
