package id.co.arkamaya.bc_android;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
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
import pojo.CheckLogin;
import pojo.GetScheduleActivityList;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by root on 01/11/17.
 */

public class ScheduleActivityMgrFragment extends android.support.v4.app.Fragment {
    private View v;
    SharedPreferences pref;
    private ProgressDialog progress;
    private ListView list;
    private String employee_id;

    ConnectionDetector conDetector;
    public String ENDPOINT="http://bc-id.co.id/";

    public ScheduleActivityMgrFragment() {
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
        return inflater.inflate(R.layout.fragment_schedule_activity, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        v=view;
        list=(ListView)v.findViewById(R.id.list);
        pref = PreferenceManager.getDefaultSharedPreferences(v.getContext().getApplicationContext());
        employee_id=pref.getString("EmployeeId", null);
        Log.d("Yudha", employee_id);
//        employee_cd=pref.getString("EmployeeId", null);
        ENDPOINT= pref.getString("URLEndPoint", "");
        conDetector =  new ConnectionDetector(v.getContext().getApplicationContext());

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = id;//view.getId();
                AppCompatImageView btnDeleteScheduleActivity = (AppCompatImageView)view.findViewById(R.id.btnDelete);
                AppCompatImageView btnEditScheduleActivity = (AppCompatImageView)view.findViewById(R.id.btnEdit);
                AppCompatImageView btnAproveScheduleActivity = (AppCompatImageView)view.findViewById(R.id.btnAprove);
                AppCompatImageView btnRejectScheduleActivity = (AppCompatImageView)view.findViewById(R.id.btnReject);

                if(conDetector.isConnectingToInternet()){
                    if(viewId==btnDeleteScheduleActivity.getId())
                    {
                        View vp=(View)v.getParent();
                        TextView txtScheduleActivityId = (TextView) view.findViewById(R.id.txtScheduleActivityId);
                        final String ScheduleActivityId = String.valueOf(txtScheduleActivityId.getText()); //txtReimburseId.getText().toString();
                        //Toast.makeText(v.getContext().getApplicationContext(),ReimburseId, Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(v.getContext())
                                .setMessage("Are you sure?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        onDeleteScheduleActivity(ScheduleActivityId, employee_id);
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }else if(viewId==btnAproveScheduleActivity.getId()){
                        View vp=(View)v.getParent();
                        TextView txtScheduleActivityId = (TextView) view.findViewById(R.id.txtScheduleActivityId);
                        final String ScheduleActivityId = String.valueOf(txtScheduleActivityId.getText()); //txtReimburseId.getText().toString();
                        //Toast.makeText(v.getContext().getApplicationContext(),ReimburseId, Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(v.getContext())
                                .setMessage("Are you sure?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        onAprove(ScheduleActivityId, employee_id);
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }else if(viewId==btnRejectScheduleActivity.getId()){
                        View vp=(View)v.getParent();
                        TextView txtScheduleActivityId = (TextView) view.findViewById(R.id.txtScheduleActivityId);
                        final String ScheduleActivityId = String.valueOf(txtScheduleActivityId.getText()); //txtReimburseId.getText().toString();
                        //Toast.makeText(v.getContext().getApplicationContext(),ReimburseId, Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(v.getContext())
                                .setMessage("Are you sure?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        onReject(ScheduleActivityId, employee_id);
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }else if(viewId==btnEditScheduleActivity.getId()){
                        TextView txtScheduleActivityId = (TextView) view.findViewById(R.id.txtScheduleActivityId);
                        final String ScheduleActivityId = txtScheduleActivityId.getText().toString();
                        Intent intent = new Intent(v.getContext(), ScheduleActivityActivity.class);
                        intent.putExtra("ScheduleId", ScheduleActivityId);
                        intent.putExtra("Mode","edit");
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(v.getContext().getApplicationContext(), "Internet Connection Error..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ScheduleActivityActivity.class);
                intent.putExtra("Mode", "new");
                startActivity(intent);
            }
        });

        Button btnResync = (Button) view.findViewById(R.id.btnResync);
        btnResync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getScheduleActivityList();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
    private void  onAprove(String ScheduleActivityId, String EmployeeId) {
        progress = new ProgressDialog(v.getContext());
        progress.setMessage("Processing..");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        ApiScheduleActivity restInterface = restAdapter.create(ApiScheduleActivity.class);

        restInterface.onAproveScheduleActivity(ScheduleActivityId, EmployeeId, new Callback<CheckLogin>() {
            @Override
            public void success(CheckLogin m, Response response) {

                if (progress != null) {
                    progress.dismiss();
                }

                Toast.makeText(v.getContext().getApplicationContext(), m.getMsgText(), Toast.LENGTH_SHORT).show();

                if (m.getMsgType().toLowerCase().equals("info")) {
                    getScheduleActivityList();
                } else {

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
    private void  onReject(String ScheduleActivityId, String EmployeeId) {
        progress = new ProgressDialog(v.getContext());
        progress.setMessage("Processing..");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        ApiScheduleActivity restInterface = restAdapter.create(ApiScheduleActivity.class);

        restInterface.onRejectScheduleActivity(ScheduleActivityId, EmployeeId, new Callback<CheckLogin>() {
            @Override
            public void success(CheckLogin m, Response response) {

                if (progress != null) {
                    progress.dismiss();
                }

                Toast.makeText(v.getContext().getApplicationContext(), m.getMsgText(), Toast.LENGTH_SHORT).show();

                if (m.getMsgType().toLowerCase().equals("info")) {
                    getScheduleActivityList();
                } else {

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
    private void  onDeleteScheduleActivity(String ScheduleActivityId, String EmployeeId){
        progress = new ProgressDialog(v.getContext());
        progress.setMessage("Processing..");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        ApiScheduleActivity restInterface = restAdapter.create(ApiScheduleActivity.class);

        restInterface.onDeleteScheduleActivity(ScheduleActivityId, EmployeeId, new Callback<CheckLogin>() {
            @Override
            public void success(CheckLogin m, Response response) {

                if (progress != null) {
                    progress.dismiss();
                }

                Toast.makeText(v.getContext().getApplicationContext(), m.getMsgText(), Toast.LENGTH_SHORT).show();

                if (m.getMsgType().toLowerCase().equals("info")) {
                    getScheduleActivityList();
                } else {

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

    private void getScheduleActivityList(){

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

        ApiScheduleActivity restInterface = restAdapter.create(ApiScheduleActivity.class);
        Log.d("Yudha", employee_id);
        restInterface.getScheduleActivityMgrList(employee_id, new Callback<List<GetScheduleActivityList>>() {
            @Override
            public void success(List<GetScheduleActivityList> m, Response response) {
                list.setAdapter(null);
                if (m.size() > 0) {
                    AdapterScheduleActivityListView adapt = new AdapterScheduleActivityListView(getActivity().getApplicationContext(), R.layout.list_adapter_schedule_activity, m);
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
        getScheduleActivityList();
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }


}