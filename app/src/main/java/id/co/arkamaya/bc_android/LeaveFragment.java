package id.co.arkamaya.bc_android;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import id.co.arkamaya.cico.R;
import pojo.GetLeaveList;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by root on 17/10/17.
 */

public class LeaveFragment  extends Fragment {
    private View v;
    SharedPreferences pref;
    private ProgressDialog progress;
    private ListView list;
    private String employee_id;

    ConnectionDetector conDetector;
    public String ENDPOINT="http://bc-id.co.id/";

    public LeaveFragment() {
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
        return inflater.inflate(R.layout.fragment_leave, container, false);
    }
    private void getLeaveList(){

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

        ApiLeave restInterface = restAdapter.create(ApiLeave.class);
        Log.d("Yudha", employee_id);
        restInterface.getLeaveList(employee_id, new Callback<List<GetLeaveList>>() {
            @Override
            public void success(List<GetLeaveList> m, Response response) {
                list.setAdapter(null);
                if (m.size() > 0) {
                    AdapterLeaveListView adapt = new AdapterLeaveListView(getActivity().getApplicationContext(), R.layout.list_adapter_leave, m);
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
        getLeaveList();
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }

}
