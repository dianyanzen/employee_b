package id.co.arkamaya.bc_android;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.co.arkamaya.cico.R;

/**
 * Created by root on 06/10/17.
 */

public class MyProfilMaindataFragment extends Fragment {
    private View v;
    SharedPreferences pref;
    private ProgressDialog progress;
    private String employee_id,user;
    EditText txtBornDate,txtmeriedsince;
    TextView lblmeriedsince;
    Spinner spnGender,spnMeried;
    ArrayAdapter<String> adaAdapterCombo;

    String data_gender = "";
    String data_meried = "";
    Button btnBornDate,btnmeriedsince;
    public String ENDPOINT="http://192.168.1.152:8080/";

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

        txtBornDate = (EditText)getActivity().findViewById(R.id.txtBornDate);
        txtmeriedsince = (EditText)getActivity().findViewById(R.id.txtmeriedsince);
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


        spnGender = (Spinner)getActivity().findViewById(R.id.spnGender);
        spnMeried = (Spinner)getActivity().findViewById(R.id.spnmariedstatus);
        getGender();
        getMeried();
        Button btnCancel =(Button)getActivity().findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
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



    }

    public void setMeriedsince(){
        String ExcuseType = ((String) spnMeried.getSelectedItem());
        if (ExcuseType.equals("Single")) {
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

        adaAdapterCombo = new ArrayAdapter<String>
                (getActivity().getApplicationContext(), R.layout.spinner_simple_item, R.id.listCombo, list);

        adaAdapterCombo.setDropDownViewResource
                (R.layout.spinner_simple_item);

        spnGender.setAdapter(adaAdapterCombo);

        String data = data_gender;

        if (data != "") {
            int idxLocation = adaAdapterCombo.getPosition(data_gender);
            spnGender.setSelection(idxLocation);
        }
    }
    public void getMeried(){
        List<String> list = new ArrayList<String>();
        list.clear();
        spnMeried.setAdapter(null);
        list.add("Single");
        list.add("Married");
        list.add("Divorce");

        adaAdapterCombo = new ArrayAdapter<String>
                (getActivity().getApplicationContext(), R.layout.spinner_simple_item, R.id.listCombo, list);

        adaAdapterCombo.setDropDownViewResource
                (R.layout.spinner_simple_item);

        spnMeried.setAdapter(adaAdapterCombo);

        String data = data_meried;

        if (data != "") {
            int idxLocation = adaAdapterCombo.getPosition(data_meried);
            spnMeried.setSelection(idxLocation);
        }
        spnMeried.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                setMeriedsince();
                //Toast.makeText(getApplicationContext(), separated[0].replace(" ", "").toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                //setMeriedsince();

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

}
