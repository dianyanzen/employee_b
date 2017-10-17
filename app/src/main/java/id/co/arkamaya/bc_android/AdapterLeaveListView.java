package id.co.arkamaya.bc_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import pojo.GetLeaveList;

/**
 * Created by root on 17/10/17.
 */

public class AdapterLeaveListView  extends ArrayAdapter<GetLeaveList> {
    private Context context;
    private List<GetLeaveList> leaveList;
    SharedPreferences pref;
    View adapterView;
    private ProgressDialog progress;
    public String ENDPOINT="http://bc-id.co.id/";
    public String EmployeeId;
    private ListView list;
    public AdapterLeaveListView(Context context, int resource, List<GetLeaveList> object){
        super(context, resource, object);
        this.context = context;
        this.leaveList = object;


    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapterView = inflater.inflate(R.layout.list_adapter_leave, parent, false);
        GetLeaveList m = leaveList.get(position);

        pref = PreferenceManager.getDefaultSharedPreferences(adapterView.getContext().getApplicationContext());
        EmployeeId=pref.getString("EmployeeId", null);
        ENDPOINT=pref.getString("URLEndPoint", "");

        TextView txtProdMonth=(TextView)adapterView.findViewById(R.id.txtProdMonth);
        TextView txtDate=(TextView)adapterView.findViewById(R.id.txtDate);
        TextView txtLeaveType=(TextView)adapterView.findViewById(R.id.txtExcueseType);
        TextView txtLeaveDescription=(TextView)adapterView.findViewById(R.id.txtLeaveDescription);
        TextView txtLeaveId=(TextView)adapterView.findViewById(R.id.txtLeaveId);
        TextView txtLeaveStatus=(TextView)adapterView.findViewById(R.id.txtLeaveStatus);
        Button btnDelete=(Button)adapterView.findViewById((R.id.btnDelete));

        if(m.getLeaveStatus() != "" && m.getLeaveStatus() != null){
            txtLeaveStatus.setVisibility(adapterView.VISIBLE);
            btnDelete.setVisibility(adapterView.INVISIBLE);
        }else{
            txtLeaveStatus.setVisibility(adapterView.INVISIBLE);
            btnDelete.setVisibility(adapterView.VISIBLE);
        }

        txtProdMonth.setText(m.getLeaveProdMonth());
        txtDate.setText(m.getLeaveProdDate());
        txtLeaveType.setText(m.getLeaveType());
        txtLeaveDescription.setText(m.getLeaveDescription());
        txtLeaveId.setText(m.getLeaveId());
        txtLeaveStatus.setText(m.getLeaveStatus());

        final ViewGroup prnt=parent;
        final int pos=position;

        btnDelete.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        TextView txtLeaveId = (TextView) adapterView.findViewById(R.id.txtLeaveId);
//                        //final String ReimburseId = txtReimburseId.getText().toString();
//                        //Toast.makeText(v.getContext(), "Delete button clicked " +ReimburseId+"-"+EmployeeId, Toast.LENGTH_SHORT).show();
//                        ((ListView) prnt).performItemClick(v, pos, 0); // Let the event be handled in onItemClick()
//                    }
//                }
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View vp=(View)v.getParent();
                        TextView txtLeaveId = (TextView) vp.findViewById(R.id.txtLeaveId);
                        final String LeaveId = txtLeaveId.getText().toString();
                        //Toast.makeText(v.getContext(), "Delete button clicked " +ReimburseId+"-"+EmployeeId, Toast.LENGTH_SHORT).show();
                        ((ListView) prnt).performItemClick(vp, pos, v.getId()); // Let the event be handled in onItemClick()
                    }
                }
        );

        return adapterView;
    }
}
