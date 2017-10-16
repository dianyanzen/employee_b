package id.co.arkamaya.bc_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import id.co.arkamaya.cico.R;
import pojo.GetListDailyReport;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DailyReportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DailyReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyReportFragment extends Fragment {
    private View v;
    SharedPreferences pref;
    private ProgressDialog progress;
    private ListView list;
    private String employe_id;

    Button btnResync;

    ConnectionDetector conDetector;
    public String PMSENDPOINT="http://192.168.3.109:8080/arkapms";

    final Handler handlerWorkHour = new Handler();

    public DailyReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();

        return inflater.inflate(R.layout.fragment_daily_report, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        v=view;
        list=(ListView)v.findViewById(R.id.list);
        pref = PreferenceManager.getDefaultSharedPreferences(v.getContext().getApplicationContext());
        employe_id=pref.getString("EmployeeId", null);
        PMSENDPOINT = pref.getString("URLPMSEndPoint", "");//"http://192.168.3.109:8080/arkapms";//pref.getString("URLEndPoint", "");
        conDetector =  new ConnectionDetector(v.getContext().getApplicationContext());

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = id;//view.getId();
                Button btnDeleteOvertime = (Button)view.findViewById(R.id.btnDelete);

                if(conDetector.isConnectingToInternet()){
                    TextView txtBugsId = (TextView) view.findViewById(R.id.txtBugsId);
                    final String RegNo = txtBugsId.getText().toString();
                    //Toast.makeText(v.getContext().getApplicationContext(),RegNo, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(), DailyReportActivity.class);
                    intent.putExtra("BugsId", RegNo);
                    intent.putExtra("Mode","edit");
                    startActivity(intent);
                }else{
                    Toast.makeText(v.getContext().getApplicationContext(), "Internet Connection Error..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DailyReportActivity.class);
                intent.putExtra("Mode", "new");
                startActivity(intent);
            }
        });

        Button btnSend = (Button) view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DailyReportSendActivity.class);
                intent.putExtra("Mode", "new");
                startActivity(intent);
            }
        });

        btnResync = (Button) view.findViewById(R.id.btnResync);
        btnResync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDailyReportList();
            }
        });

        //Toast.makeText(getActivity().getApplicationContext(), PMSENDPOINT, Toast.LENGTH_SHORT).show();
    }

    private void getDailyReportList(){
        //Toast.makeText(getActivity().getApplicationContext(), employe_id, Toast.LENGTH_SHORT).show();

        /*-------------------------------*/
        progress = new ProgressDialog(v.getContext());
        progress.setMessage("Syncrhonizing...");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(PMSENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIDailyReport restInterface = restAdapter.create(APIDailyReport.class);

        restInterface.getDailyReport_list(employe_id, new Callback<List<GetListDailyReport>>() {
            @Override
            public void success(List<GetListDailyReport> m, Response response) {
                list.setAdapter(null);
                if (m.size() > 0) {
                    AdapterDailyReportListView adapt = new AdapterDailyReportListView(getActivity().getApplicationContext(), R.layout.list_adapter_daily_report, m);
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

    @Override
    public void onStart() {
        getDailyReportList();
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }
}