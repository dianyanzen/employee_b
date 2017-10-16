package id.co.arkamaya.bc_android;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import id.co.arkamaya.cico.R;
import pojo.GetWorkingReport;
import pojo.GetTotaLWHWorkingReport;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import java.util.Calendar;

public class WorkingReportFragment extends Fragment {
    private View v;
    SharedPreferences pref;
    private ProgressDialog progress;
    private ListView list;
    private String employe_id;
    TextView total_wh;
    EditText src_OvertimeDate;
    String data;

    ConnectionDetector conDetector;
    public String ENDPOINT="http://192.168.88.153:8080/arka_portal";
    private int year, month, day;

    public WorkingReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_working_report, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        v=view;
        list=(ListView)v.findViewById(R.id.list);
        pref = PreferenceManager.getDefaultSharedPreferences(v.getContext().getApplicationContext());
        employe_id=pref.getString("EmployeeId", null);
        ENDPOINT= pref.getString("URLEndPoint", "");
        conDetector =  new ConnectionDetector(v.getContext().getApplicationContext());

        total_wh = (TextView) view.findViewById(R.id.txttotalWH);
        src_OvertimeDate = (EditText) view.findViewById(R.id.src_OvertimeDate);
        Button btnDatePicker = (Button) view.findViewById(R.id.btnDatePicker);
        Button btnSearch = (Button) view.findViewById(R.id.btnSearch);
        //Button btnResync = (Button) view.findViewById(R.id.btnResync);
        Button btnClear = (Button) view.findViewById(R.id.btnClear);

        /*
        btnResync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = src_OvertimeDate.getText().toString();
                getWorkingReportList(data);
                getTotalWH_Month(data);
            }
        });
        */

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = src_OvertimeDate.getText().toString();
                //.makeText(getActivity().getApplicationContext(), data, Toast.LENGTH_LONG).show();
                getWorkingReportList(data);
                getTotalWH_Month(data);
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                src_OvertimeDate.setText("");
                data = src_OvertimeDate.getText().toString();
                //.makeText(getActivity().getApplicationContext(), data, Toast.LENGTH_LONG).show();
                getWorkingReportList(data);
                getTotalWH_Month(data);
            }
        });

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void showDatePicker() {
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
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            src_OvertimeDate.setText(String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1));
            //Toast.makeText(getActivity().getApplicationContext(), String.valueOf(year) + "-" + String.valueOf(monthOfYear) + "-" + String.valueOf(dayOfMonth), Toast.LENGTH_LONG).show();
        }
    };

    private void getWorkingReportList(String data){

        /*-------------------------------*/
        progress = new ProgressDialog(v.getContext());
        progress.setMessage("Syncrhonizing...");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIWorkingReport restInterface = restAdapter.create(APIWorkingReport.class);

        restInterface.getWorkingReportList(employe_id, data, new Callback<List<GetWorkingReport>>() {
            @Override
            public void success(List<GetWorkingReport> m, Response response) {
                list.setAdapter(null);
                if (m.size() > 0) {
                    AdapterWorkingReportListView adapt = new AdapterWorkingReportListView(getActivity().getApplicationContext(), R.layout.list_adapter_working_report, m);
                    list.setAdapter(adapt);
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

    private void getTotalWH_Month(String data){

        /*-------------------------------
        progress = new ProgressDialog(v.getContext());
        progress.setMessage("Syncrhonizing...");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        */
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIWorkingReport restInterface = restAdapter.create(APIWorkingReport.class);

        restInterface.getTotalWH_Month(employe_id, data, new Callback<List<GetTotaLWHWorkingReport>>() {
            @Override
            public void success(List<GetTotaLWHWorkingReport> m, Response response) {
                if (m.get(0).getTotalMvTime() != null) {
                    total_wh.setText(m.get(0).getTotalMvTime());
                } else {
                    total_wh.setText("0");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        getWorkingReportList(data);
        getTotalWH_Month(data);
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }
}
