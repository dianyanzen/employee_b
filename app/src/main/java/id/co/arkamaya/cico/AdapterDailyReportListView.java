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
import pojo.GetListDailyReport;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 3/11/2016.
 */
public class AdapterDailyReportListView extends ArrayAdapter<GetListDailyReport> {
    private Context context;
    private List<GetListDailyReport> dailyreportList;
    SharedPreferences pref;
    View adapterView;
    private ProgressDialog progress;
    public String PMSENDPOINT="http://192.168.3.109:8080/arkapms";
    public String EmployeeId;
    private ListView list;
    public AdapterDailyReportListView(Context context, int resource, List<GetListDailyReport> object){
        super(context, resource, object);
        this.context = context;
        this.dailyreportList = object;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapterView = inflater.inflate(R.layout.list_adapter_daily_report, parent, false);
        GetListDailyReport m = dailyreportList.get(position);

        pref = PreferenceManager.getDefaultSharedPreferences(adapterView.getContext().getApplicationContext());
        EmployeeId=pref.getString("EmployeeId", null);
        PMSENDPOINT =pref.getString("URLPMSEndPoint", ""); //"http://192.168.3.109:8080/arkapms";//pref.getString("URLEndPoint", "");

        TextView txtRegNo = (TextView)adapterView.findViewById(R.id.txtRegNo);
        TextView txtApplicationCd = (TextView)adapterView.findViewById(R.id.txtApplicationCd);
        TextView txtFucntionCd = (TextView)adapterView.findViewById(R.id.txtFucntionCd);
        TextView txtMonthDt = (TextView)adapterView.findViewById(R.id.txtMonthDt);
        TextView txtDayDt = (TextView)adapterView.findViewById(R.id.txtDayDt);
        TextView txtBugsId = (TextView)adapterView.findViewById(R.id.txtBugsId);

        txtRegNo.setText(m.getBugsId());
        txtApplicationCd.setText(m.getApplicationCd());
        txtFucntionCd.setText(m.getFunctionCd());
        txtMonthDt.setText(m.getMonthDt());
        txtDayDt.setText(m.getDayDt());
        txtBugsId.setText(m.getRegNo());

        final ViewGroup prnt=parent;
        final int pos=position;

        return adapterView;
    }
}
