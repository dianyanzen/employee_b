package id.co.arkamaya.bc_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.arkamaya.cico.R;
import pojo.GetListDailyReport;

/**
 * Created by user on 5/3/2016.
 */
public class AdapterNextTodoListView extends ArrayAdapter<GetListDailyReport> {
    private Context context;
    private List<GetListDailyReport> dailyreportList;
    SharedPreferences pref;
    View adapterView;
    private ProgressDialog progress;
    public String PMSENDPOINT="http://192.168.3.109:8080/arkapms";
    public String EmployeeId;
    private ListView list;
    ArrayAdapter<String> adaAdapterCombo;
    public AdapterNextTodoListView(Context context, int resource, List<GetListDailyReport> object){
        super(context, resource, object);
        this.context = context;
        this.dailyreportList = object;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapterView = inflater.inflate(R.layout.list_adapter_next_todo, parent, false);
        GetListDailyReport m = dailyreportList.get(position);

        pref = PreferenceManager.getDefaultSharedPreferences(adapterView.getContext().getApplicationContext());
        EmployeeId=pref.getString("EmployeeId", null);
        PMSENDPOINT = pref.getString("URLPMSEndPoint", ""); //"http://192.168.3.109:8080/arkapms";//pref.getString("URLEndPoint", "");

        TextView txtProject = (TextView)adapterView.findViewById(R.id.txtProject);
        TextView txtFunction = (TextView)adapterView.findViewById(R.id.txtFunction);
        TextView txtBugsId = (TextView)adapterView.findViewById(R.id.txtBugsId);
        Spinner txtFunctionList = (Spinner)adapterView.findViewById(R.id.txtFunctionList);

        txtProject.setText(m.getApplicationNm());
        txtFunction.setText(m.getFunctionCd() + " - " + m.getFunctionNm());
        txtBugsId.setText(m.getRegNo());

        String issueDescription  = m.getIssueDescription();
        String[] issueDesc = issueDescription.split(" ~ ");

        List<String> list = new ArrayList<String>();

        for (int i = 0; i < issueDesc.length; i++) {
            list.add(" * " + issueDesc[i]);
        }

        adaAdapterCombo = new ArrayAdapter<String>
                (getContext().getApplicationContext(), R.layout.simple_spinner_list, R.id.listCombo, list);

        adaAdapterCombo.setDropDownViewResource
                (R.layout.simple_spinner_list);

        txtFunctionList.setAdapter(adaAdapterCombo);

        final ViewGroup prnt=parent;
        final int pos=position;

        return adapterView;
    }
}
