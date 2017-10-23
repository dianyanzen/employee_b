package id.co.arkamaya.bc_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import id.co.arkamaya.cico.R;
import pojo.GetScheduleActivityList;

/**
 * Created by root on 20/10/17.
 */

public class AdapterScheduleActivityListView extends ArrayAdapter<GetScheduleActivityList> {
    private Context context;
    private List<GetScheduleActivityList> scheduleList;
    SharedPreferences pref;
    View adapterView;
    private ProgressDialog progress;
    public String ENDPOINT="http://bc-id.co.id/";
    public String EmployeeId;
    private ListView list;
    public AdapterScheduleActivityListView(Context context, int resource, List<GetScheduleActivityList> object){
        super(context, resource, object);
        this.context = context;
        this.scheduleList = object;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapterView = inflater.inflate(R.layout.list_adapter_schedule_activity, parent, false);
        GetScheduleActivityList m = scheduleList.get(position);

        pref = PreferenceManager.getDefaultSharedPreferences(adapterView.getContext().getApplicationContext());
        EmployeeId=pref.getString("EmployeeId", null);
        ENDPOINT=pref.getString("URLEndPoint", "");

        TextView txtProdMonth=(TextView)adapterView.findViewById(R.id.txtProdMonth);
        TextView txtDate=(TextView)adapterView.findViewById(R.id.txtDate);
        TextView txtScheduleActivityType=(TextView)adapterView.findViewById(R.id.txtScheduleActivityType);
        TextView txtScheduleActivityDescription=(TextView)adapterView.findViewById(R.id.txtScheduleActivityDescription);
        TextView txtScheduleActivityId=(TextView)adapterView.findViewById(R.id.txtScheduleActivityId);
        TextView txtScheduleActivityStatus=(TextView)adapterView.findViewById(R.id.txtScheduleActivityStatus);
        Button btnDelete=(Button)adapterView.findViewById((R.id.btnDelete));

        if(m.getScheduleStatus() != "" && m.getScheduleStatus() != null){
            txtScheduleActivityStatus.setVisibility(adapterView.VISIBLE);
            btnDelete.setVisibility(adapterView.INVISIBLE);
            if(m.getScheduleStatus().equalsIgnoreCase("rejected")){
                txtScheduleActivityStatus.setTextColor(Color.RED);
            }
        }else{
            txtScheduleActivityStatus.setVisibility(adapterView.INVISIBLE);
            btnDelete.setVisibility(adapterView.VISIBLE);
        }

        txtProdMonth.setText(m.getScheduleProdMonth());
        txtDate.setText(m.getScheduleProdDate());
        txtScheduleActivityType.setText(m.getScheduleType());
        txtScheduleActivityDescription.setText(m.getScheduleDescription());
        txtScheduleActivityId.setText(m.getScheduleId());
        txtScheduleActivityStatus.setText(m.getScheduleStatus());

        final ViewGroup prnt=parent;
        final int pos=position;

        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View vp=(View)v.getParent();
                        TextView txtScheduleActivityId = (TextView) vp.findViewById(R.id.txtScheduleActivityId);
                        final String ScheduleActivityId = txtScheduleActivityId.getText().toString();
                        ((ListView) prnt).performItemClick(vp, pos, v.getId()); // Let the event be handled in onItemClick()
                    }
                }
        );

        return adapterView;
    }
}
