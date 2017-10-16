package id.co.arkamaya.bc_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import id.co.arkamaya.cico.R;
import pojo.CheckLogin;
import pojo.GetOvertimeList;
import pojo.GetWorkingReport;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 3/14/2016.
 */
public class AdapterWorkingReportListView extends ArrayAdapter<GetWorkingReport>{
    private Context context;
    private List<GetWorkingReport> workingReportList;
    SharedPreferences pref;
    View adapterView;
    private ProgressDialog progress;
    public String ENDPOINT="http://bc-id.co.id/";
    public String EmployeeId;
    private ListView list;
    public AdapterWorkingReportListView(Context context, int resource, List<GetWorkingReport> object){
        super(context, resource, object);
        this.context = context;
        this.workingReportList = object;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapterView = inflater.inflate(R.layout.list_adapter_working_report, parent, false);
        GetWorkingReport m = workingReportList.get(position);

        pref = PreferenceManager.getDefaultSharedPreferences(adapterView.getContext().getApplicationContext());
        EmployeeId=pref.getString("EmployeeId", null);
        ENDPOINT=pref.getString("URLEndPoint", "");

        TextView txtWR_Date = (TextView)adapterView.findViewById(R.id.txtWR_Date);
        TextView txtWR_TimeIn = (TextView)adapterView.findViewById(R.id.txtWR_TimeIn);
        TextView txtWR_TimeOut = (TextView)adapterView.findViewById(R.id.txtWR_TimeOut);
        TextView txtWR_WH = (TextView)adapterView.findViewById(R.id.txtWR_WH);

        //txtWR_Date.setBackgroundColor(Color.parseColor("red"));

        if(m.getCountHour() <  8){
            txtWR_Date.setBackgroundColor(Color.parseColor("yellow"));
            txtWR_TimeIn.setBackgroundColor(Color.parseColor("yellow"));
            txtWR_TimeOut.setBackgroundColor(Color.parseColor("yellow"));
            txtWR_WH.setBackgroundColor(Color.parseColor("yellow"));
        }else {
            txtWR_Date.setBackgroundColor(Color.parseColor("white"));
            txtWR_TimeIn.setBackgroundColor(Color.parseColor("white"));
            txtWR_TimeOut.setBackgroundColor(Color.parseColor("white"));
            txtWR_WH.setBackgroundColor(Color.parseColor("white"));
        }

        if(m.getScheduleDt() != null){
            txtWR_Date.setText(m.getScheduleDt());
        }else{
            txtWR_Date.setText("-");
        }

        if(m.getClockIn() != null){
            txtWR_TimeIn.setText(m.getClockIn());
        }else{
            txtWR_TimeIn.setText("-");
        }

        if(m.getClockOut() != null){
            txtWR_TimeOut.setText(m.getClockOut());
        }else{
            txtWR_TimeOut.setText("-");
        }

        if(m.getDiffHour() != null){
            txtWR_WH.setText(m.getDiffHour());
        }else{
            txtWR_WH.setText("-");
        }

        final ViewGroup prnt=parent;
        final int pos=position;

        return adapterView;
    }
}