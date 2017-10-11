package id.co.arkamaya.cico;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

import pojo.CheckLogin;
import pojo.DashboardSummary;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 4/20/2016.
 */
public class AdapterDashboardListView extends ArrayAdapter<DashboardSummary> {
    private Context context;
    private List<DashboardSummary> dashboard_summary;
    SharedPreferences pref;
    View adapterView;
    private ProgressDialog progress;
    public String ENDPOINT="http://192.168.88.153:8080/arka_portal";
    public String EmployeeId;
    private ListView list;
    public AdapterDashboardListView(Context context, int resource, List<DashboardSummary> object){
        super(context, resource, object);
        this.context = context;
        this.dashboard_summary = object;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapterView = inflater.inflate(R.layout.list_adapter_dashboard, parent, false);
        DashboardSummary m = dashboard_summary.get(position);

        pref = PreferenceManager.getDefaultSharedPreferences(adapterView.getContext().getApplicationContext());
        EmployeeId=pref.getString("EmployeeId", null);
        ENDPOINT=pref.getString("URLEndPoint", "");

        TextView txtProdMonth = (TextView)adapterView.findViewById(R.id.txtProdMonth);
        TextView txtDate = (TextView)adapterView.findViewById(R.id.txtDate);
        TextView txtHour = (TextView)adapterView.findViewById(R.id.txtOvertimeHour);
        TextView txtOvertimeDescription = (TextView)adapterView.findViewById(R.id.txtOvertimeDescription);
        TextView txtOvertimeId = (TextView)adapterView.findViewById(R.id.txtOvertimeId);

        txtProdMonth.setText(m.getNumOfDayPeriod());
        txtDate.setText(m.getCntOfDayWorkPeriod());
        txtOvertimeDescription.setText(m.getCntOfLeaveDayWorkPeriod());
        txtHour.setText(m.getCntOfSickDayWorkPeriod());
        txtOvertimeId.setText(m.getCntOtHourPeriod());

        final ViewGroup prnt=parent;
        final int pos=position;

        return adapterView;
    }
}
