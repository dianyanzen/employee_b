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
import pojo.GetProfileIdCardList;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by root on 09/10/17.
 */

public class MyProfileIdCardFragment extends Fragment {
    private View vcard;
    SharedPreferences pref;
    private ProgressDialog progress;
    private ListView listcard;
    private String employee_id;
    Button btnAdd, btnResync;
    ConnectionDetector conDetector;
    public String ENDPOINT="http://bc-id.co.id/";
    public MyProfileIdCardFragment() {
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
        return inflater.inflate(R.layout.fragment_myprof_idcard, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        vcard=view;
        listcard=(ListView)getActivity().findViewById(R.id.listidc);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        employee_id=pref.getString("EmployeeId", null);
        Log.d("Yudha", employee_id);
        ENDPOINT= pref.getString("URLEndPoint", "");
        conDetector =  new ConnectionDetector(getActivity().getApplicationContext());
        listcard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = id;//view.getId();
                Button btnDeleteCard = (Button)view.findViewById(R.id.btnDeleteCard);

                if(conDetector.isConnectingToInternet()){
                    if(viewId==btnDeleteCard.getId())
                    {

                        View vp=(View)view.getParent();
                        TextView txtCardId = (TextView) view.findViewById(R.id.txtCardId);
                        final String CardId = String.valueOf(txtCardId.getText()); //txtReimburseId.getText().toString();
                        //Toast.makeText(view.getContext().getApplicationContext(),ReimburseId, Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(getActivity())
                                .setMessage("Are you sure?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        onDeleteCard(CardId, employee_id);
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }else{
                        TextView txtCardId = (TextView) view.findViewById(R.id.txtCardId);
                        final String CardId = txtCardId.getText().toString();
                        Intent intent = new Intent(getActivity(), MyProfileAddIdCard.class);
                        intent.putExtra("CardId", CardId);
                        intent.putExtra("Mode","edit");
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(getActivity(), "Internet Connection Error..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAdd =(Button)getActivity().findViewById(R.id.btnAddidc);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyProfileAddIdCard.class);
                intent.putExtra("Mode", "new");
                startActivity(intent);
            }
        });

        btnResync = (Button) getActivity().findViewById(R.id.btnResyncidc);
        btnResync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCardList();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
    private void getCardList(){

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
        restInterface.getIdCardList(employee_id, new Callback<List<GetProfileIdCardList>>() {
            @Override
            public void success(List<GetProfileIdCardList> m, Response response) {
                listcard.setAdapter(null);
                if (m.size() > 0) {
                    AdapterIdCardListView adapt = new AdapterIdCardListView(getActivity().getApplicationContext(), R.layout.list_adapter_idcard, m);
                    listcard.setAdapter(adapt);
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
    private void  onDeleteCard(String IdCardId, String EmployeeId){
        //Toast.makeText(getActivity(), "Internet Connection Error..", Toast.LENGTH_SHORT).show();
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

        restInterface.onDeleteIdCard(IdCardId, EmployeeId, new Callback<CheckLogin>() {
            @Override
            public void success(CheckLogin m, Response response) {

                if (progress != null) {
                    progress.dismiss();
                }

                Toast.makeText(vcard.getContext().getApplicationContext(), m.getMsgText(), Toast.LENGTH_SHORT).show();

                if (m.getMsgType().toLowerCase().equals("info")) {
                    getCardList();
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
    @Override
    public void onStart() {
        getCardList();
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }

}
