package id.co.arkamaya.cico;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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

import pojo.CheckLogin;
import pojo.GetComplaintList;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ComplaintFragment extends Fragment {
    private View v;
    SharedPreferences pref;
    private ProgressDialog progress;
    private ListView list;
    private String employee_id;

    ConnectionDetector conDetector;
    public String ENDPOINT="http://192.168.1.152:8080/";
    public ComplaintFragment() {
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
        return inflater.inflate(R.layout.fragment_complaint, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        v=view;
        list=(ListView)v.findViewById(R.id.list);
        pref = PreferenceManager.getDefaultSharedPreferences(v.getContext().getApplicationContext());
        employee_id=pref.getString("EmployeeCd", null);
        Log.d("Yudha", employee_id);
//        employee_cd=pref.getString("EmployeeId", null);
        ENDPOINT= pref.getString("URLEndPoint", "");
        conDetector =  new ConnectionDetector(v.getContext().getApplicationContext());

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = id;//view.getId();
                Button btnDeleteComplaint = (Button)view.findViewById(R.id.btnDelete);

                if(conDetector.isConnectingToInternet()){
                    if(viewId==btnDeleteComplaint.getId())
                    {

                        View vp=(View)v.getParent();
                        TextView txtComplaintId = (TextView) view.findViewById(R.id.txtComplaintId);
                        final String ComplaintId = String.valueOf(txtComplaintId.getText()); //txtReimburseId.getText().toString();
                        //Toast.makeText(v.getContext().getApplicationContext(),ReimburseId, Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(v.getContext())
                                .setMessage("Are you sure?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        onDeleteComplaint(ComplaintId, employee_id);
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }else{
                        TextView txtComplaintId = (TextView) view.findViewById(R.id.txtComplaintId);
                        final String ComplaintId = txtComplaintId.getText().toString();
                        Intent intent = new Intent(v.getContext(), ComplaintActivity.class);
                        intent.putExtra("ComplaintId", ComplaintId);
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
                Intent intent = new Intent(v.getContext(), ComplaintActivity.class);
                intent.putExtra("Mode", "new");
                startActivity(intent);
            }
        });

        Button btnResync = (Button) view.findViewById(R.id.btnResync);
        btnResync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getComplaintList();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void  onDeleteComplaint(String ComplaintId, String EmployeeId){
        progress = new ProgressDialog(v.getContext());
        progress.setMessage("Processing..");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIComplaint restInterface = restAdapter.create(APIComplaint.class);

        restInterface.onDeleteComplaint(ComplaintId, EmployeeId, new Callback<CheckLogin>() {
            @Override
            public void success(CheckLogin m, Response response) {

                if (progress != null) {
                    progress.dismiss();
                }

                Toast.makeText(v.getContext().getApplicationContext(), m.getMsgText(), Toast.LENGTH_SHORT).show();

                if (m.getMsgType().toLowerCase().equals("info")) {
                    getComplaintList();
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

    private void getComplaintList(){

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

        APIComplaint restInterface = restAdapter.create(APIComplaint.class);
        Log.d("Yudha", employee_id);
        restInterface.getComplaintList(employee_id, new Callback<List<GetComplaintList>>() {
            @Override
            public void success(List<GetComplaintList> m, Response response) {
                list.setAdapter(null);
                if (m.size() > 0) {
                    AdapterComplaintListView adapt = new AdapterComplaintListView(getActivity().getApplicationContext(), R.layout.list_adapter_complaint, m);
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
        getComplaintList();
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }

}
