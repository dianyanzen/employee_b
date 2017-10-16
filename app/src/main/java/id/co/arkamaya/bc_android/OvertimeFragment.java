package id.co.arkamaya.bc_android;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import pojo.CheckLogin;
import pojo.GetOvertimeList;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OvertimeFragment extends Fragment{
    private View v;
    SharedPreferences pref;
    private ProgressDialog progress;
    private ListView list;
    private String employe_id;


    ConnectionDetector conDetector;
    public String ENDPOINT="http://192.168.88.153:8080/arka_portal";

    public OvertimeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overtime, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        v=view;
        list=(ListView)v.findViewById(R.id.list);
        pref = PreferenceManager.getDefaultSharedPreferences(v.getContext().getApplicationContext());
        employe_id=pref.getString("EmployeeId", null);
        ENDPOINT= pref.getString("URLEndPoint", "");
        conDetector =  new ConnectionDetector(v.getContext().getApplicationContext());

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = id;//view.getId();
                Button btnDeleteOvertime = (Button)view.findViewById(R.id.btnDelete);

                if(conDetector.isConnectingToInternet()){
                    if(viewId==btnDeleteOvertime.getId())
                    {

                        View vp=(View)v.getParent();
                        TextView txtOvertimeId = (TextView) view.findViewById(R.id.txtOvertimeId);
                        final String OvertimeId = String.valueOf(txtOvertimeId.getText()); //txtReimburseId.getText().toString();
                        //Toast.makeText(v.getContext().getApplicationContext(),ReimburseId, Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(v.getContext())
                                .setMessage("Are you sure?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        onDeleteOvertime(OvertimeId, employe_id);
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }else{
                        TextView txtOvertimeId = (TextView) view.findViewById(R.id.txtOvertimeId);
                        final String OvertimeId = txtOvertimeId.getText().toString();
                        Intent intent = new Intent(v.getContext(), OvertimeActivity.class);
                        intent.putExtra("OvertimeId", OvertimeId);
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
                Intent intent = new Intent(v.getContext(), OvertimeActivity.class);
                intent.putExtra("Mode", "new");
                startActivity(intent);
            }
        });

        Button btnResync = (Button) view.findViewById(R.id.btnResync);
        btnResync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOvertimeList();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void  onDeleteOvertime(String OvertimeId, String EmployeeId){
        progress = new ProgressDialog(v.getContext());
        progress.setMessage("Processing..");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIOvertime restInterface = restAdapter.create(APIOvertime.class);

        restInterface.onDeleteOvertime(OvertimeId, EmployeeId, new Callback<CheckLogin>() {
            @Override
            public void success(CheckLogin m, Response response) {

                if (progress != null) {
                    progress.dismiss();
                }

                Toast.makeText(v.getContext().getApplicationContext(), m.getMsgText(), Toast.LENGTH_SHORT).show();

                if (m.getMsgType().toLowerCase().equals("info")) {
                    getOvertimeList();
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

    private void getOvertimeList(){

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

        APIOvertime restInterface = restAdapter.create(APIOvertime.class);

        restInterface.getOvertimeList(employe_id, new Callback<List<GetOvertimeList>>() {
            @Override
            public void success(List<GetOvertimeList> m, Response response) {
                list.setAdapter(null);
                if (m.size() > 0) {
                    AdapterOvertimeListView adapt = new AdapterOvertimeListView(getActivity().getApplicationContext(), R.layout.list_adapter_overtime, m);
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
        getOvertimeList();
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }
}
