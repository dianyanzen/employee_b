package id.co.arkamaya.bc_android;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import id.co.arkamaya.cico.R;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView =null;
    Toolbar toolbar=null;
    SharedPreferences pref;
    TextView txtEmployeeName;


    final Handler handlerRunable = new Handler();
    public Runnable rInetCheck=null;
    ConnectionDetector conDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conDetector=new ConnectionDetector(this);
        //inetCheck();
        //set the fragment initially
        MainFragment fragment = new MainFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
        if(conDetector.isConnectingToInternet()) {


        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        txtEmployeeName = (TextView) header.findViewById(R.id.txtEmployeeName);

        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (!pref.contains("URLEndPoint")) {
            Toast.makeText(getApplicationContext(), "Web Service URL Not Set..", Toast.LENGTH_SHORT).show();
            moveToSettingActivity();
        }

        Toast.makeText(getApplicationContext(), "Hallo " + pref.getString("User", null) + "...", Toast.LENGTH_SHORT).show();
        txtEmployeeName.setText(pref.getString("User", null));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


    }

    public void moveToSettingActivity(){
        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
        startActivity(intent);
        finish();
    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click back once again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            moveToSettingActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cico) {
            MainFragment fragment= new MainFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(MainActivity.this, MyProfileFragment.class));
        } else if (id == R.id.nav_schedule) {
            ScheduleActivityFragment fragment= new ScheduleActivityFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_leave) {
            LeaveFragment fragment= new LeaveFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_excuse) {
            ExcuseFragment fragment= new ExcuseFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
       /* } else if (id == R.id.nav_reimburse) {
            ReimburseFragment fragment= new ReimburseFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();*/
        }else if(id==R.id.nav_otrequest){
            OvertimeFragment fragment= new OvertimeFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }else if(id==R.id.nav_viewattendance){
            WorkingReportFragment fragment= new WorkingReportFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }else if (id==R.id.nav_dashboard){
            DashboardFragment fragment = new DashboardFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
       /* } else if (id==R.id.nav_daily_report){
            DailyReportFragment fragment = new DailyReportFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }else if (id==R.id.nav_info){
            startActivity(new Intent(MainActivity.this, Activity_InfoUser.class));*/
        }
        else if (id==R.id.nav_logOut){
            SharedPreferences.Editor editor = pref.edit();
            editor.remove("User");
            editor.remove("LocationType");
            editor.remove("Place");
            editor.remove("Project");
            editor.commit();
            moveToLoginActivity();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void moveToLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    //private AlertDialog.Builder alertNoConDlg;
    AlertDialog alertNoConDlg;
    public void showAlertNoConnection() {
        if(alertNoConDlg == null){
            alertNoConDlg = new AlertDialog.Builder(getApplicationContext()).create();
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

    @Override
    protected void onStart() {
        conDetector =  new ConnectionDetector(getApplicationContext());
        super.onStart();
    }

    @Override
    public void onStop() {
        handlerRunable.removeCallbacks(rInetCheck);
        super.onStop();
    }

    private void inetCheck(){

        //if(rWorkHour==null){
        rInetCheck = new Runnable() {
            public void run() {
                if(!conDetector.isConnectingToInternet()){
                    //showAlertNoConnection();
                    Toast.makeText(getApplicationContext(),  "No Internet Connection",Toast.LENGTH_LONG).show();
                }else{
                    if(alertNoConDlg!=null){
                        if(alertNoConDlg.isShowing()){
                            alertNoConDlg.dismiss();
                        }
                    }
                }
                //showAlertNoConnection();

            }

        };
        handlerRunable.postDelayed(rInetCheck, 1000);

        //}

    }
}
