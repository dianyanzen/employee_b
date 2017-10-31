package id.co.arkamaya.bc_android;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import id.co.arkamaya.cico.R;
import pojo.CheckLogin;
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
                AppCompatImageView btnDeleteLeave = (AppCompatImageView)view.findViewById(R.id.btnDelete);
                AppCompatImageView btnEditLeave = (AppCompatImageView)view.findViewById(R.id.btnEdit);
                AppCompatImageView btnRejectLeave = (AppCompatImageView)view.findViewById(R.id.btnReject);
                AppCompatImageView btnApproveLeave = (AppCompatImageView)view.findViewById(R.id.btnAprove);

                if(conDetector.isConnectingToInternet()){
                    if(viewId==btnDeleteLeave.getId())
                    {

                        View vp=(View)v.getParent();
                        TextView txtLeaveId = (TextView) view.findViewById(R.id.txtLeaveId);
                        final String LeaveId = String.valueOf(txtLeaveId.getText()); //txtReimburseId.getText().toString();
                        //Toast.makeText(v.getContext().getApplicationContext(),ReimburseId, Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(v.getContext())
                                .setMessage("Are you sure?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        onDeleteLeave(LeaveId, employee_id);
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }else if(viewId==btnApproveLeave.getId()){
                        View vp=(View)v.getParent();
                        TextView txtLeaveId = (TextView) view.findViewById(R.id.txtLeaveId);
                        final String LeaveId = String.valueOf(txtLeaveId.getText()); //txtReimburseId.getText().toString();
                        //Toast.makeText(v.getContext().getApplicationContext(),ReimburseId, Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(v.getContext())
                                .setMessage("Are you sure?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        onAproveLeave(LeaveId, employee_id);
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }else if(viewId==btnRejectLeave.getId()){
                        View vp=(View)v.getParent();
                        TextView txtLeaveId = (TextView) view.findViewById(R.id.txtLeaveId);
                        final String LeaveId = String.valueOf(txtLeaveId.getText()); //txtReimburseId.getText().toString();
                        //Toast.makeText(v.getContext().getApplicationContext(),ReimburseId, Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(v.getContext())
                                .setMessage("Are you sure?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        onRejectLeave(LeaveId, employee_id);
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }else if(viewId==btnEditLeave.getId()){
                        TextView txtLeaveId = (TextView) view.findViewById(R.id.txtLeaveId);
                        final String LeaveId = txtLeaveId.getText().toString();
                        Intent intent = new Intent(v.getContext(), LeaveActivity.class);
                        intent.putExtra("LeaveId", LeaveId);
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
                Intent intent = new Intent(v.getContext(), LeaveActivity.class);
                intent.putExtra("Mode", "new");
                startActivity(intent);
            }
        });

        Button btnResync = (Button) view.findViewById(R.id.btnResync);
        btnResync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLeaveList();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
    private void  onAproveLeave(String LeaveId, String EmployeeId) {
        progress = new ProgressDialog(v.getContext());
        progress.setMessage("Processing..");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        ApiLeave restInterface = restAdapter.create(ApiLeave.class);

        restInterface.onAproveLeave(LeaveId, EmployeeId, new Callback<CheckLogin>() {
            @Override
            public void success(CheckLogin m, Response response) {

                if (progress != null) {
                    progress.dismiss();
                }

                Toast.makeText(v.getContext().getApplicationContext(), m.getMsgText(), Toast.LENGTH_SHORT).show();

                if (m.getMsgType().toLowerCase().equals("info")) {
                    getLeaveList();
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
    private void  onRejectLeave(String LeaveId, String EmployeeId) {
        progress = new ProgressDialog(v.getContext());
        progress.setMessage("Processing..");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        ApiLeave restInterface = restAdapter.create(ApiLeave.class);

        restInterface.onRejectLeave(LeaveId, EmployeeId, new Callback<CheckLogin>() {
            @Override
            public void success(CheckLogin m, Response response) {

                if (progress != null) {
                    progress.dismiss();
                }

                Toast.makeText(v.getContext().getApplicationContext(), m.getMsgText(), Toast.LENGTH_SHORT).show();

                if (m.getMsgType().toLowerCase().equals("info")) {
                    getLeaveList();
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
    private void  onDeleteLeave(String LeaveId, String EmployeeId){
        progress = new ProgressDialog(v.getContext());
        progress.setMessage("Processing..");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        ApiLeave restInterface = restAdapter.create(ApiLeave.class);

        restInterface.onDeleteLeave(LeaveId, EmployeeId, new Callback<CheckLogin>() {
            @Override
            public void success(CheckLogin m, Response response) {

                if (progress != null) {
                    progress.dismiss();
                }

                Toast.makeText(v.getContext().getApplicationContext(), m.getMsgText(), Toast.LENGTH_SHORT).show();

                if (m.getMsgType().toLowerCase().equals("info")) {
                    getLeaveList();
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
    private void getLeaveList(){

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
