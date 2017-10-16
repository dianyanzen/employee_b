package id.co.arkamaya.bc_android;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import id.co.arkamaya.cico.R;
import pojo.DashboardSummary;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DashboardFragment extends Fragment {
    private View v;
    SharedPreferences pref;
    private ProgressDialog progress;
    private ListView list;
    private String employe_id;

    ConnectionDetector conDetector;
    public String ENDPOINT="http://bc-id.co.id/";

    TextView num_of_day_period;
    TextView cnt_of_day_work_period;
    TextView cnt_of_leave_day_work_period;
    //TextView cnt_of_sick_day_work_period;
    TextView cnt_ot_hour_period;
    //TextView cnt_reimburse_amount_period;
    //TextView cnt_remaining_days_off;
    TextView cnt_less_work_hour;
    //TextView num_of_task_assign;

    final Handler handlerWorkHour = new Handler();

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();

        return inflater.inflate(R.layout.fragment_dashboard, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        v=view;

        num_of_day_period = (TextView) v.findViewById(R.id.num_of_day_period);
        cnt_of_day_work_period = (TextView) v.findViewById(R.id.cnt_of_day_work_period);
        cnt_of_leave_day_work_period = (TextView) v.findViewById(R.id.cnt_of_leave_day_work_period);

        //cnt_of_sick_day_work_period = (TextView) v.findViewById(R.id.cnt_of_sick_day_work_period);
        cnt_ot_hour_period = (TextView) v.findViewById(R.id.cnt_ot_hour_period);
        //cnt_reimburse_amount_period = (TextView) v.findViewById(R.id.cnt_reimburse_amount_period);
        //cnt_remaining_days_off = (TextView) v.findViewById(R.id.cnt_remaining_days_off);
        cnt_less_work_hour = (TextView) v.findViewById(R.id.cnt_less_work_hour);

        //num_of_task_assign = (TextView) v.findViewById(R.id.num_of_task_assign);

        pref = PreferenceManager.getDefaultSharedPreferences(v.getContext().getApplicationContext());
        employe_id=pref.getString("EmployeeId", null);
        ENDPOINT= pref.getString("URLEndPoint", "");
        conDetector =  new ConnectionDetector(v.getContext().getApplicationContext());
    }

    private void getDataSummary(){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIDashboard restInterface = restAdapter.create(APIDashboard.class);

        restInterface.getSummaryList(employe_id, new Callback<List<DashboardSummary>>() {
            @Override
            public void success(List<DashboardSummary> m, Response response) {

                num_of_day_period.setText(m.get(0).getNumOfDayPeriod());
                cnt_of_day_work_period.setText(m.get(0).getCntOfDayWorkPeriod());
                cnt_of_leave_day_work_period.setText(m.get(0).getCntOfLeaveDayWorkPeriod());

                //cnt_of_sick_day_work_period.setText(m.get(0).getCntOfSickDayWorkPeriod());
                cnt_ot_hour_period.setText(m.get(0).getCntOtHourPeriod());
                //cnt_reimburse_amount_period.setText(m.get(0).getCntReimburseAmountPeriod());
                //"1.000.000"

                //cnt_remaining_days_off.setText(m.get(0).getCntRemainingDaysOff());
                cnt_less_work_hour.setText(m.get(0).getCntLessWorkHour());

                //num_of_task_assign.setText(m.get(0).getCntOfTaskAssign());
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        getDataSummary();
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }
}
