package id.co.arkamaya.bc_android;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import id.co.arkamaya.cico.R;
import pojo.CheckLogin;
import pojo.GetSalaryPdf;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by root on 17/10/17.
 */

public class SlipGajiFragment extends android.support.v4.app.Fragment {
    private View v;
    SharedPreferences pref;
    private ProgressDialog progress;
    private ListView list;
    private String employee_id;

    ConnectionDetector conDetector;
    public String ENDPOINT="http://bc-id.co.id/";

    public SlipGajiFragment() {
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
        return inflater.inflate(R.layout.fragment_slip_gaji, container, false);
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
                AppCompatImageView btnDownloadSlipGaji = (AppCompatImageView)view.findViewById(R.id.btnDownload);

                if(conDetector.isConnectingToInternet()){
                    if(viewId==btnDownloadSlipGaji.getId())
                    {

                        View vp = (View) v.getParent();
                        TextView txtPdfId = (TextView) view.findViewById(R.id.txtPdfId);
                        TextView txtPdfperiod = (TextView) view.findViewById(R.id.lblPdfDate);
                        TextView txtPdfFilename = (TextView) view.findViewById(R.id.txtPdfFileName);
                        final String PdfId = String.valueOf(txtPdfId.getText()); //txtReimburseId.getText().toString();
                        final String PdfPeriod = String.valueOf(txtPdfperiod.getText()); //txtReimburseId.getText().toString();
                        final String PdfFilename = String.valueOf(txtPdfFilename.getText()); //txtReimburseId.getText().toString();
//                        Toast.makeText(v.getContext().getApplicationContext(),PdfId, Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(v.getContext())
                                .setMessage("Unduh Pay Slip Anda ?")
                                .setCancelable(false)
                                .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        onDownloadPdf(PdfId, employee_id,PdfPeriod,PdfFilename);
                                    }
                                })
                                .setNegativeButton("Tidak", null)
                                .show();
                    }
                    
                }else{
                    Toast.makeText(v.getContext().getApplicationContext(), "Internet Connection Error..", Toast.LENGTH_SHORT).show();
                }
            }
        });
        final SwipeRefreshLayout dorefresh = (SwipeRefreshLayout)v.findViewById(R.id.swipeRefresh);
        dorefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        dorefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                refreshItem();
            }

            void refreshItem() {
                getSlipGajiList();
                onItemLoad();
            }

            void onItemLoad() {
                dorefresh.setRefreshing(false);
                Toast.makeText(getActivity(), "List Slip Gaji Telah Di Perbarui", Toast.LENGTH_LONG).show();
            }

        });
       
        super.onViewCreated(view, savedInstanceState);
    }

    private void  onDownloadPdf(String SlipGajiId, String EmployeeId,String SlipGajiDate,String SlipGajiFileName){

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ENDPOINT+"assets/file/upload_salary/"+SlipGajiDate+"/"+SlipGajiFileName));
        Log.e("URL PDF",SlipGajiId+"<Url> "+ENDPOINT+"assets/file/upload_salary/"+SlipGajiDate+"/"+SlipGajiFileName);
        startActivity(browserIntent);
    }
    private void getSlipGajiList(){

        progress = new ProgressDialog(v.getContext());
        progress.setMessage("Syncrhonizing...");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        ApiSalary restInterface = restAdapter.create(ApiSalary.class);
        Log.d("Yudha", employee_id);
        restInterface.getSlipGajiList(employee_id, new Callback<List<GetSalaryPdf>>() {
            @Override
            public void success(List<GetSalaryPdf> m, Response response) {
                list.setAdapter(null);
                if (m.size() > 0) {
                    AdapterSlipGajiListView adapt = new AdapterSlipGajiListView(getActivity().getApplicationContext(), R.layout.list_adapter_salary, m);
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
        getSlipGajiList();
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }

}