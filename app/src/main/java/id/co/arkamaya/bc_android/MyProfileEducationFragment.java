package id.co.arkamaya.bc_android;

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

import id.co.arkamaya.cico.R;
import pojo.CheckLogin;
import pojo.GetProfileEducationList;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by root on 09/10/17.
 */

public class MyProfileEducationFragment extends Fragment {
    private View vEdu;
    SharedPreferences pref;
    private ProgressDialog progress;
    private ListView listedu;
    private String employee_id;
    Button btnAdd, btnResync;
    ConnectionDetector conDetector;
    public String ENDPOINT="http://bc-id.co.id/";
    public MyProfileEducationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_myprof_education, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        vEdu = view;
        listedu = (ListView) getActivity().findViewById(R.id.listedu);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        employee_id = pref.getString("EmployeeId", null);
        Log.d("Yudha", employee_id);
        ENDPOINT = pref.getString("URLEndPoint", "");
        conDetector = new ConnectionDetector(getActivity().getApplicationContext());
        listedu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = id;//view.getId();
                Button btnDeleteEdu = (Button)view.findViewById(R.id.btnDeleteEducation);

                if(conDetector.isConnectingToInternet()){
                    if(viewId==btnDeleteEdu.getId())
                    {

                        View vp=(View)view.getParent();
                        TextView txtEduId = (TextView) view.findViewById(R.id.txtEducationId);
                        final String EduId = String.valueOf(txtEduId.getText()); //txtReimburseId.getText().toString();
                        //Toast.makeText(view.getContext().getApplicationContext(),ReimburseId, Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(getActivity())
                                .setMessage("Are you sure?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        onDeleteEdu(EduId, employee_id);
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }else{
                        TextView txtEduId = (TextView) view.findViewById(R.id.txtEducationId);
                        final String EduId = txtEduId.getText().toString();
                        Intent intent = new Intent(getActivity(), MyProfileAddEducation.class);
                        intent.putExtra("EduId", EduId);
                        intent.putExtra("Mode","edit");
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(getActivity(), "Tidak Dapat Terhubung Dengan Internet..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAdd =(Button)getActivity().findViewById(R.id.btnAddedu);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyProfileAddEducation.class);
                intent.putExtra("Mode", "new");
                startActivity(intent);
            }
        });

        btnResync = (Button) getActivity().findViewById(R.id.btnResyncedu);
        btnResync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEduList();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
    private void  onDeleteEdu(String EduId, String EmployeeId){
        //Toast.makeText(getActivity(), "Tidak Dapat Terhubung Dengan Internet..", Toast.LENGTH_SHORT).show();
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Processing..");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIEditProfile restInterface = restAdapter.create(APIEditProfile.class);

        restInterface.onDeleteEducation(EduId, EmployeeId, new Callback<CheckLogin>() {
            @Override
            public void success(CheckLogin m, Response response) {

                if (progress != null) {
                    progress.dismiss();
                }

                Toast.makeText(vEdu.getContext().getApplicationContext(), m.getMsgText(), Toast.LENGTH_SHORT).show();

                if (m.getMsgType().toLowerCase().equals("info")) {
                    getEduList();
                } else {

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

    private void getEduList(){

        /*-------------------------------*/
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Syncrhonizing...");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIEditProfile restInterface = restAdapter.create(APIEditProfile.class);
        Log.d("Yudha", employee_id);
        restInterface.getEducationList(employee_id, new Callback<List<GetProfileEducationList>>() {
            @Override
            public void success(List<GetProfileEducationList> m, Response response) {
                listedu.setAdapter(null);
                if (m.size() > 0) {
                    AdapterEducationListView adapt = new AdapterEducationListView(getActivity().getApplicationContext(), R.layout.list_adapter_family, m);
                    listedu.setAdapter(adapt);
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
        getEduList();
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }
}
