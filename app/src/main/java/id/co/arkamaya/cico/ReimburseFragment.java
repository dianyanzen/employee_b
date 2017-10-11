package id.co.arkamaya.cico;


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

import pojo.CheckLogin;
import pojo.GetReimburseList;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReimburseFragment extends Fragment {

    private View v;
    SharedPreferences pref;
    private ProgressDialog progress;
    private ListView list;
    private String employe_id;

    ConnectionDetector conDetector;
    public String ENDPOINT="http://192.168.1.146:9888/";

    public ReimburseFragment() {
        // Required empty public constructor
    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        long viewId = view.getId();
//        Toast.makeText(v.getContext(), "ItemClicked", Toast.LENGTH_SHORT).show();
//        if (viewId == R.id.btnDelete) {
//            Toast.makeText(v.getContext(), "Delete button clicked", Toast.LENGTH_SHORT).show();
//        }else {
//            Toast.makeText(v.getContext(), "ListView clicked" + id, Toast.LENGTH_SHORT).show();
//        }
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reimburse, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        v=view;
        list=(ListView)v.findViewById(R.id.list);
        pref = PreferenceManager.getDefaultSharedPreferences(v.getContext().getApplicationContext());
        employe_id=pref.getString("EmployeeId", null);
        ENDPOINT= pref.getString("URLEndPoint", "");
        conDetector =  new ConnectionDetector(v.getContext().getApplicationContext());
        //getReimburseList();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = id;//view.getId();
                Button btnDeleteReimburse=(Button)view.findViewById(R.id.btnDelete);
                //Toast.makeText(v.getContext().getApplicationContext(), ((TextView) parent.findViewById(R.id.txtReimburseId)).getText() +"--"+ ((TextView) view.findViewById(R.id.txtReimburseId)).getText(), Toast.LENGTH_SHORT).show();

                if(conDetector.isConnectingToInternet()){
                    if(viewId==btnDeleteReimburse.getId())
                    {
                        View vp=(View)v.getParent();
                        TextView txtReimburseId = (TextView) view.findViewById(R.id.txtReimburseId);
                        final String ReimburseId = String.valueOf(txtReimburseId.getText()); //txtReimburseId.getText().toString();
                        //Toast.makeText(v.getContext().getApplicationContext(),ReimburseId, Toast.LENGTH_SHORT).show();

                        new AlertDialog.Builder(v.getContext())
                                .setMessage("Are you sure?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        onDeleteReimburse(ReimburseId,employe_id);
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }else{
                        TextView txtReimburseId = (TextView) view.findViewById(R.id.txtReimburseId);
                        final String ReimburseId = txtReimburseId.getText().toString();
                        Intent intent = new Intent(v.getContext(), ReimburseActivity.class);
                        intent.putExtra("ReimburseId", ReimburseId);
                        intent.putExtra("Mode","edit");
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(v.getContext().getApplicationContext(), "Internet Connection Error..", Toast.LENGTH_SHORT).show();
                }



                //Toast.makeText(v.getContext(), "clicked "+viewId+"-"+ btnDeleteReimburse.getId(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(v.getContext(), "ListView clicked "+ReimburseId , Toast.LENGTH_SHORT).show();
            }
        });

        Button btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ReimburseActivity.class);
                intent.putExtra("Mode", "new");
                startActivity(intent);
            }
        });

        Button btnResync = (Button) view.findViewById(R.id.btnResync);
        btnResync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReimburseList();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }


    private void  onDeleteReimburse(String ReimburseId, String EmployeeId){
        progress = new ProgressDialog(v.getContext());
        progress.setMessage("Processing..");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIReimburse restInterface = restAdapter.create(APIReimburse.class);

        restInterface.onDeleteReimburse(ReimburseId, EmployeeId, new Callback<CheckLogin>() {
            @Override
            public void success(CheckLogin m, Response response) {

                if (progress != null) {
                    progress.dismiss();
                }

                Toast.makeText(v.getContext().getApplicationContext(), m.getMsgText(), Toast.LENGTH_SHORT).show();

                if (m.getMsgType().toLowerCase().equals("info")) {
                    getReimburseList();
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

    private void getReimburseList(){

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

        APIReimburse restInterface = restAdapter.create(APIReimburse.class);

        restInterface.getReimburseList(employe_id, new Callback<List<GetReimburseList>>() {
            @Override
            public void success(List<GetReimburseList> m, Response response) {
                list.setAdapter(null);
                if (m.size() > 0) {
                    AdapterReimburseListView adapt = new AdapterReimburseListView(getActivity().getApplicationContext(), R.layout.list_adapter_reimburse, m);
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
        getReimburseList();
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }
}
