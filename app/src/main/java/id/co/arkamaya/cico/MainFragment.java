package id.co.arkamaya.cico;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import android.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import pojo.GetLastClockIn;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    //public static final String ENDPOINT="http://192.168.1.146:86/arka_portal/index.php/";
    public String ENDPOINT="http://192.168.88.158:86/arka_portal/index.php/";
//    EditText txtNotes;
    private View v;
    private ProgressDialog progress;
//    Spinner spnLocationType;
//    EditText txtProject;
//    EditText txtLatitude;
//    EditText txtLongitude;
//    EditText txtPlace;
//    EditText txtAddress;

    TextView txtClockIn;
    TextView txtClockOut;
    TextView txtWorkHour;
    /*Button btnClockIn;
    Button btnClockOut;
    Button btnResync;*/

    SharedPreferences pref;

    ArrayAdapter<String> dataAdapterLocation;

    /**GPS**/
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationManager locationManager;
    LocationListener locationListner;
    /**UserInfo**/
    String User="";
    String EmployeeId="";

    Boolean ConFlag=true;

    private LocationRequest mLocationRequest=new LocationRequest();

    final Handler handlerWorkHour = new Handler();
    public Runnable rWorkHour=null;

    /**Internet Connection Checker**/
    ConnectionDetector conDetector;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        //View view =  lf.inflate(R.layout.fragment_main, container, false); //pass the correct layout name for the fragment
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }

        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //workHourTimer();
        return inflater.inflate(R.layout.fragment_main, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        v=view;
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        EmployeeId=pref.getString("EmployeeId", null);
        ENDPOINT= pref.getString("URLEndPoint", "");
        User= pref.getString("User",null);
        EmployeeId=pref.getString("EmployeeId",null);


        /**Location data adapter**/
        /*spnLocationType = (Spinner)v.findViewById(R.id.spnLocationType);
        List<String> list = new ArrayList<String>();
        list.add("ONSITE");
        list.add("OFFICE");*/


        /*dataAdapterLocation = new ArrayAdapter<String>
                (v.getContext(), android.R.layout.simple_spinner_item,list);

        dataAdapterLocation.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
*/
        /**-------End Location data adapter---------**/

//        spnLocationType.setAdapter(dataAdapterLocation);

        /*txtProject = (EditText)v.findViewById(R.id.txtProject);
        txtNotes =(EditText)v.findViewById(R.id.txtNotes);
        txtPlace =(EditText)v.findViewById(R.id.txtPlace);*/

        txtClockIn=(TextView)v.findViewById(R.id.txtClockIn);
        txtClockOut=(TextView)v.findViewById(R.id.txtClockOut);
        txtWorkHour=(TextView)v.findViewById(R.id.txtWorkHour);
        getLastClockIn();
        /*//**Button**//*
        btnClockIn = (Button)view.findViewById(R.id.btnClockIn);
        btnClockIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBtnClockInAction(v);
            }
        });

        btnClockOut = (Button)view.findViewById(R.id.btnClockOut);
        btnClockOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onbtnClockOutAction(v);
            }
        });

        btnResync=(Button)view.findViewById(R.id.btnResync);
        btnResync.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getLastClockIn();
            }
        });*/

        /*btnClockIn.setBackgroundColor(Color.parseColor("#3f51b4"));
        btnClockOut.setBackgroundColor(Color.parseColor("#D32F2F"));

        *//**Latitude & Longiteud**//*
        txtLatitude=(EditText)view.findViewById(R.id.txtLatitude);
        txtLongitude=(EditText)view.findViewById(R.id.txtLongitude);
        txtAddress=(EditText)view.findViewById(R.id.txtAddress);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }else{

        }*/
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager)this.getContext().getSystemService(Context.LOCATION_SERVICE);



        /**Get last location**/
        locationListner = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLastLocation=location;

          /*      txtLatitude.setText(String.valueOf(mLastLocation.getLatitude()));
                txtLongitude.setText(String.valueOf(mLastLocation.getLongitude()));
                Log.d("Yudha", txtLatitude.getText() + "," + txtLongitude.getText());
*/
                Geocoder geocoder = new Geocoder(v.getContext(), Locale.getDefault());
                List<Address> addresses = null;

                try{
                    addresses = geocoder.getFromLocation( Double.valueOf(mLastLocation.getLatitude()),mLastLocation.getLongitude(), 1);
                    if (addresses == null || addresses.size()  == 0) {

                    } else {
                        Address address = addresses.get(0);
                        Address returnedAddress = addresses.get(0);
                        StringBuilder strReturnedAddress = new StringBuilder();
                        for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                            strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(" ");
                        }
                        /*Toast.makeText(v.getContext(),  strReturnedAddress.toString(),
                                Toast.LENGTH_LONG).show();*/

  //                      txtAddress.setText(strReturnedAddress.toString());
                    }

                }catch (Exception err){

                }

                //Toast.makeText(getActivity(), "Updating location: " +String.valueOf(mLastLocation.getLatitude() + "," + String.valueOf(mLastLocation.getLongitude())),
                //        Toast.LENGTH_LONG).show();


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1,locationListner);
        } catch (SecurityException e) {
        }

        pref = PreferenceManager.getDefaultSharedPreferences(v.getContext().getApplicationContext());
        if(pref.contains("URLEndPoint")){
            ENDPOINT= pref.getString("URLEndPoint", "");
        }else{
            Toast.makeText(v.getContext().getApplicationContext(), "Web Service URL Not Set..", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext().getApplicationContext(), SettingActivity.class);
            startActivity(intent);
            //getActivity().finish();

        }

        //Toast.makeText(v.getContext().getApplicationContext(), "Hallo "+pref.getString("User", null)+"...", Toast.LENGTH_SHORT).show();
        User= pref.getString("User",null);
        EmployeeId=pref.getString("EmployeeId",null);
        /**initial state**/
    /*    int idxLocation = dataAdapterLocation.getPosition(pref.getString("LocationType", "ONSITE"));
        spnLocationType.setSelection(idxLocation);
        txtPlace.setText(pref.getString("Place", ""));
        txtProject.setText(pref.getString("Project", ""));
        inputObjectState(true);

        spnLocationType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(spnLocationType.getSelectedItem().equals("OFFICE")){
                    txtPlace.setText("Bandung");
                    txtProject.requestFocus();
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(txtProject, InputMethodManager.SHOW_IMPLICIT);
                }else{
                    txtPlace.setText(pref.getString("Place", ""));
                    txtProject.setText(pref.getString("Project", ""));
                }
            }
*/
   /*         @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });*/
    }






    private void workHourTimer(){

        //if(rWorkHour==null){
            rWorkHour = new Runnable() {
                public void run() {
                    if(!txtClockIn.getText().toString().equals("00:00") && txtClockOut.getText().toString().equals("00:00") ){
                        String cis=txtClockIn.getText().toString();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        ParsePosition pp = new ParsePosition(0);
                        java.util.Date ci = format.parse(cis, pp);

                        Calendar cd = Calendar.getInstance();

                        Long diffInMillisec = cd.getTimeInMillis() - ci.getTime();
                        Log.d("Yudha",cd.getTime().toString() +" "+Long.toString(ci.getTime()) );


                        long hours = diffInMillisec / (60 * 60 * 1000);
                        diffInMillisec -= hours * (60 * 60 * 1000);

                        long minutes = diffInMillisec / (60 * 1000);
                        diffInMillisec -= minutes * (60 * 1000);

                        long seconds = diffInMillisec / 1000;

                        txtWorkHour.setText(Long.toString(hours) + ":" + Long.toString(minutes) + ":" + Long.toString(seconds));

                    }


                    if(!conDetector.isConnectingToInternet()){
                        showAlertNoConnection();
                    }else{
                        if(alertNoConDlg!=null){
                            if(alertNoConDlg.isShowing()){
                                alertNoConDlg.dismiss();
                                mGoogleApiClient.connect();
                              /*  getLastClockIn();*/
                            }
                        }
                    }
                    handlerWorkHour.postDelayed(this, 1000);
                }
            };

            handlerWorkHour.postDelayed(rWorkHour, 1000);
        //}

    }

    private void onBtnClockInAction(View v){
        final String username=User;
        final String employee_id=EmployeeId;
        final String source="Mobile";
        /*final String long_in=txtLongitude.getText().toString();
        final String lat_in=txtLatitude.getText().toString();
        final String schedule_type=spnLocationType.getSelectedItem().toString();
        final String schedule_project=txtProject.getText().toString();
        final String schedule_description=txtNotes.getText().toString();
        final String place = txtPlace.getText().toString();
        final String address=txtAddress.getText().toString();*/
        final View v_inner=v;
        /*Log.d("Yudha", "Parameter Posting : " + username + ";" + employee_id + ";" + source + ";" + long_in + ";" + lat_in + ";" + schedule_type + ";" + schedule_project + ";" + schedule_description + ";" + place);*/

        String fieldName="";

        /*if(place.equals("")){
            fieldName="Place";
        }

        if(schedule_project.equals("")){
            if(fieldName.equals("")){
                fieldName="Project Name";
            }else{
                fieldName=fieldName+" & Project Name";
            }
        }*/



        /*if(!fieldName.equals("")) {
            Toast.makeText(v_inner.getContext().getApplicationContext(), "Incomplete Data : "+fieldName, Toast.LENGTH_LONG).show();
            if(lat_in.equals("")||long_in.equals("")){
                Toast.makeText(v_inner.getContext().getApplicationContext(), "Your location is not set, please try again..", Toast.LENGTH_LONG).show();
            }
        }else{
            new AlertDialog.Builder(v.getContext())
                    .setMessage("Are you sure to Clock In?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.d("Yudha", "Send Clockin");
                            progress = new ProgressDialog(v_inner.getContext());
                            progress.setMessage("Please Wait...");
                            progress.setIndeterminate(true);
                            progress.setCancelable(false);
                            progress.show();

                            RestAdapter adapter = new RestAdapter.Builder()
                                    .setEndpoint(ENDPOINT)
                                    .setLogLevel(RestAdapter.LogLevel.FULL)
                                    .build();
                            APICico restInterface = adapter.create(APICico.class);

                            restInterface.clockIn(username, employee_id, source, long_in, lat_in, schedule_type
                                    , schedule_project, schedule_description, place,address, new Callback<pojo.CheckLogin>() {
                                @Override
                                public void success(pojo.CheckLogin m, Response response) {
                                    //String body = new String(((TypedByteArray) response.getBody()).getBytes());
                                    Toast.makeText(v_inner.getContext().getApplicationContext(), m.getMsgText(), Toast.LENGTH_SHORT).show();

                                    if (progress != null) {
                                        progress.dismiss();
                                    }
                                    if (m.getMsgType().toLowerCase().equals("info")) {
                                        getLastClockIn();
                                        workHourTimer();

                                        //update default value
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("LocationType", spnLocationType.getSelectedItem().toString());
                                        editor.putString("Place", place);
                                        editor.putString("Project", schedule_project);
                                        editor.commit();
                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Toast.makeText(v_inner.getContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    if (progress != null) {
                                        progress.dismiss();
                                    }
                                }
                            });
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
*/

    }
/*

    private void onbtnClockOutAction(View v){

        final String employee_id=EmployeeId;
        final String long_out=txtLongitude.getText().toString();
        final String lat_out=txtLatitude.getText().toString();
        final String clock_in=txtClockIn.getText().toString();

        final View v_inner=v;

        new AlertDialog.Builder(v.getContext())
                .setMessage("Are you sure to Clock Out?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        progress = new ProgressDialog(v_inner.getContext());
                        progress.setMessage("Please Wait...");
                        progress.setIndeterminate(true);
                        progress.setCancelable(false);
                        progress.show();

                        RestAdapter adapter = new RestAdapter.Builder()
                                .setEndpoint(ENDPOINT)
                                .setLogLevel(RestAdapter.LogLevel.FULL)
                                .build();
                        APICico restInterface = adapter.create(APICico.class);

                        restInterface.clockOut(employee_id,clock_in,long_out,lat_out , new Callback<pojo.CheckLogin>() {
                            @Override
                            public void success(pojo.CheckLogin m, Response response) {
                                Toast.makeText(v_inner.getContext().getApplicationContext(), m.getMsgText(), Toast.LENGTH_SHORT).show();

                                if (progress != null) {
                                    progress.dismiss();
                                }
                                if (m.getMsgType().toLowerCase().equals("info")) {

                                    mGoogleApiClient.disconnect();
                                    handlerWorkHour.removeCallbacks(rWorkHour);

                                    MainFragment fragment= new MainFragment();
                                    android.support.v4.app.FragmentTransaction fragmentTransaction =
                                            getActivity().getSupportFragmentManager().beginTransaction();

                                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                                    fragmentTransaction.commit();
                                    if(!txtPlace.getText().toString().equals("")){
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("LocationType",spnLocationType.getSelectedItem().toString());
                                        editor.putString("Place", txtPlace.getText().toString());
                                        editor.putString("Project", txtProject.getText().toString());
                                        editor.commit();
                                    }
                                    //getLastClockIn();
                                }

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(v_inner.getContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                if (progress != null) {
                                    progress.dismiss();
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
*/

    private void getLastClockIn(){
        final String employee_id=EmployeeId;

        progress = new ProgressDialog(v.getContext());
        progress.setMessage("Synchronizing..");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        Log.d("Yudha", "Syncronize clock In data employee id: " + employee_id);
        APICico restInterface = adapter.create(APICico.class);

        restInterface.getLastClockIn(employee_id, new Callback<pojo.GetLastClockIn>() {
            @Override
            public void success(pojo.GetLastClockIn m, Response response) {
                //String body = new String(((TypedByteArray) response.getBody()).getBytes());
                Toast.makeText(v.getContext().getApplicationContext(), m.getMsgText(), Toast.LENGTH_SHORT).show();
                if (m.getMsgType().toLowerCase().equals("found")) {
                    txtClockIn.setText(m.getClockIn().toString());
                    txtClockOut.setText(m.getClockOut().toString());
                    txtWorkHour.setText(m.getWorkHour().toString());

                } else if (m.getMsgType().toLowerCase().equals("notFound")) {
                    handlerWorkHour.removeCallbacks(rWorkHour);
                }

                if (progress != null) {
                    progress.dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(v.getContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                if (progress != null) {
                    progress.dismiss();
                }
            }
        });
    }

    /*private void inputObjectState(boolean s){
        spnLocationType.setEnabled(s);
        txtPlace.setEnabled(s);
        txtProject.setEnabled(s);
        txtNotes.setEnabled(s);
        btnClockIn.setEnabled(s);
        if(s){btnClockIn.setBackgroundColor(Color.parseColor("#3f51b4"));}else{btnClockIn.setBackgroundColor(Color.parseColor("#9E9E9E"));}
        if(!s){
            btnClockOut.setEnabled(true);
            btnClockOut.setBackgroundColor(Color.parseColor("#D32F2F"));
        } else {
            btnClockOut.setEnabled(false);
            btnClockOut.setBackgroundColor(Color.parseColor("#9E9E9E"));
        }
    }*/


    /**API gps location**/
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        /**for make sure when clockin is on last location**/
        try {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            //Toast.makeText(getActivity(), String.valueOf(mLastLocation.getLatitude()),
            //        Toast.LENGTH_LONG).show();
            if (mLastLocation != null) {
/*                txtLatitude.setText(String.valueOf(mLastLocation.getLatitude()));
                txtLongitude.setText(String.valueOf(mLastLocation.getLongitude()));*/
                Geocoder geocoder = new Geocoder(v.getContext(), Locale.getDefault());
                List<Address> addresses = null;

                try{
                    addresses = geocoder.getFromLocation( Double.valueOf(mLastLocation.getLatitude()),mLastLocation.getLongitude(), 1);
                    if (addresses == null || addresses.size()  == 0) {

                    } else {
                        Address address = addresses.get(0);
                        Address returnedAddress = addresses.get(0);
                        StringBuilder strReturnedAddress = new StringBuilder();
                        for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                            strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(" ");
                        }
                        //Toast.makeText(v.getContext(),  strReturnedAddress.toString(),
                        //        Toast.LENGTH_LONG).show();

/*                        txtAddress.setText(strReturnedAddress.toString());*/
                    }

                }catch (Exception err){

                }


                //Toast.makeText(getActivity(), String.valueOf(mLastLocation.getLatitude() + "," + String.valueOf(mLastLocation.getLongitude())),
                //        Toast.LENGTH_LONG).show();

            }
        } catch (SecurityException e) {
            Log.d("Yudha",e.getMessage());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getActivity(),"GPS Connection is suspended",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getActivity(),"GPS Connection is failed"+connectionResult.getErrorMessage(),
                Toast.LENGTH_LONG).show();
    }

    /**END API GPS Location**/

    @Override
    public void onStart() {
        if(isMockSettingsON(v.getContext())){
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Please disable mock up location, Setting->Additional Setting->Developer Options->Allow Mock Locations(Off)!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getActivity().finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        conDetector =  new ConnectionDetector(this.getContext());
        if(conDetector.isConnectingToInternet()){
            mGoogleApiClient.connect();
           /* getLastClockIn();*/

        }else{
            showAlertNoConnection();
            workHourTimer();

        }
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        handlerWorkHour.removeCallbacks(rWorkHour);
        super.onStop();
    }


    //private AlertDialog.Builder alertNoConDlg;
    AlertDialog alertNoConDlg;
    public void showAlertNoConnection() {
        if(alertNoConDlg == null){
            alertNoConDlg = new AlertDialog.Builder(getActivity()).create();
            alertNoConDlg.setTitle("Error Connection");
            alertNoConDlg.setMessage("No Internet Connection");
            alertNoConDlg.setCancelable(false);
        }else{
            if(alertNoConDlg.isShowing()){
                //alertNoConDlg.dismiss();
            }else{
                alertNoConDlg.show();
            }

        }

    }

    public static boolean isMockSettingsON(Context context) {
        // returns true if mock location enabled, false if not enabled.
        if (Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
            return false;
        else
            return true;
    }

    public static boolean areThereMockPermissionApps(Context context) {

        int count = 0;

        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages =
                pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo applicationInfo : packages) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName,
                        PackageManager.GET_PERMISSIONS);

                // Get Permissions
                String[] requestedPermissions = packageInfo.requestedPermissions;

                if (requestedPermissions != null) {
                    for (int i = 0; i < requestedPermissions.length; i++) {
                        if (requestedPermissions[i]
                                .equals("android.permission.ACCESS_MOCK_LOCATION")
                                && !applicationInfo.packageName.equals(context.getPackageName())) {
                            Log.d("Yudha",packageInfo.packageName);
                            count++;
                        }
                    }
                }
            } catch (Exception e) {
                //Log.e("Got exception " + e.getMessage().toString());
            }
        }

        if (count > 0)
            return true;
        return false;
    }

}
