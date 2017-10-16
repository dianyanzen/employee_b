package id.co.arkamaya.bc_android;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import id.co.arkamaya.cico.R;
import pojo.GetProfileAddress;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by root on 09/10/17.
 */

public class MyProfileAddressFragment extends Fragment {
    private View v;
    SharedPreferences pref;
    private ProgressDialog progress;
    private String employee_id,user;
    public String ENDPOINT="http://bc-id.co.id/";
    EditText txtStreet,txtAddress,txtRegion,txtSubDistrict,txtProvince,txtCountry,txtPostalCode,txtPhone1,txtPhone2,txtWorkMail,txtBankAccout,txtContactPerson,txtContactPersonPhone;
    Button btnCancel,btnSave;
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
    public MyProfileAddressFragment() {
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
        return inflater.inflate(R.layout.fragment_myprof_address, container, false);
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
        txtStreet = (EditText)getActivity().findViewById(R.id.txtstreet);
        txtAddress = (EditText)getActivity().findViewById(R.id.txtaddress);
        txtRegion = (EditText)getActivity().findViewById(R.id.txtregion);
        txtSubDistrict = (EditText)getActivity().findViewById(R.id.txtsubdistrict);
        txtProvince = (EditText)getActivity().findViewById(R.id.txtprovince);
        txtCountry = (EditText)getActivity().findViewById(R.id.txtcountry);
        txtPostalCode = (EditText)getActivity().findViewById(R.id.txtpostalcode);
        txtPhone1 = (EditText)getActivity().findViewById(R.id.txtphone1);
        txtPhone2 = (EditText)getActivity().findViewById(R.id.txtphone2);
        txtWorkMail = (EditText)getActivity().findViewById(R.id.txtworkemail);
        txtBankAccout = (EditText)getActivity().findViewById(R.id.txtbankaccount);
        txtContactPerson = (EditText)getActivity().findViewById(R.id.txtcontactperson);
        txtContactPersonPhone = (EditText)getActivity().findViewById(R.id.txtcontactpersonphone);
        MyProfileAddressFragment.ScreenResolution sr = deviceDimensions();
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
        btnSave =(Button)getActivity().findViewById(R.id.btnSaveAddress);
        btnCancel =(Button)getActivity().findViewById(R.id.btnCancelAddress);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        getProfileAddress(employee_id);

    }
    private int GCD(int a, int b) {
        return (b == 0 ? a : GCD(b, a % b));
    }
    private void getProfileAddress(String employee_id){
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
        restInterface.getProfileAddress(employee_id, new Callback<GetProfileAddress>() {
            @Override
            public void success(GetProfileAddress m, Response response) {
                try {
                    txtStreet.setText(m.getUserStreet().toString());
                    txtAddress.setText(m.getUserAddreess().toString());
                    txtRegion.setText(m.getUserRegion().toString());
                    txtSubDistrict.setText(m.getUserSubDistrict().toString());
                    txtProvince.setText(m.getUserProvince().toString());
                    txtCountry.setText(m.getUserCountry().toString());
                    txtPostalCode.setText(m.getUserPostalCode().toString());
                    txtPhone1.setText(m.getUserHandphone1().toString());
                    txtPhone2.setText(m.getUserHandphone2().toString());
                    txtWorkMail.setText(m.getUserWorkEmail().toString());
                    txtBankAccout.setText(m.getUserBankAccountNumber().toString());
                    txtContactPerson.setText(m.getUserClosedPersonName().toString());
                    txtContactPersonPhone.setText(m.getUserClosedPersonPhone().toString());
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

}
