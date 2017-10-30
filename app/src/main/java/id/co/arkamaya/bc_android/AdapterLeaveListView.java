package id.co.arkamaya.bc_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
        RelativeLayout Leave_list = (RelativeLayout)adapterView.findViewById(R.id.leave_list);
        TextView txtEmployeeNm=(TextView)adapterView.findViewById(R.id.txtEmployeeName);
        TextView txtEmployeeId=(TextView)adapterView.findViewById(R.id.txtEmployeeId);
        TextView txtProdMonth=(TextView)adapterView.findViewById(R.id.txtProdMonth);
        TextView txtDate=(TextView)adapterView.findViewById(R.id.txtDate);
        TextView txtLeaveType=(TextView)adapterView.findViewById(R.id.lblLeaveType);
        TextView txtLeaveDescription=(TextView)adapterView.findViewById(R.id.txtLeaveDescription);
        TextView txtLeaveId=(TextView)adapterView.findViewById(R.id.txtLeaveId);
        TextView txtLeaveStatus=(TextView)adapterView.findViewById(R.id.txtLeaveStatus);
        ImageButton btnDelete=(ImageButton)adapterView.findViewById((R.id.btnDelete));
        ImageButton btnEdit=(ImageButton)adapterView.findViewById((R.id.btnEdit));
        ImageButton btnAprove=(ImageButton)adapterView.findViewById((R.id.btnAprove));
        ImageButton btnReject=(ImageButton)adapterView.findViewById((R.id.btnReject));

        if ( !m.getEmployeeId().equals(EmployeeId) ) {
            btnDelete.setVisibility(adapterView.INVISIBLE);
            btnEdit.setVisibility(adapterView.INVISIBLE);
            Leave_list.setBackgroundColor(adapterView.getResources().getColor(R.color.colorGrayLight));
            if (m.getLeaveStatus() != "" && m.getLeaveStatus() != null) {
                txtLeaveStatus.setVisibility(adapterView.VISIBLE);
                btnAprove.setVisibility(adapterView.INVISIBLE);
                btnReject.setVisibility(adapterView.INVISIBLE);
            } else {
                txtLeaveStatus.setVisibility(adapterView.INVISIBLE);
                btnAprove.setVisibility(adapterView.VISIBLE);
                btnReject.setVisibility(adapterView.VISIBLE);
            }

        }
        else {
            if (m.getLeaveStatus() != "" && m.getLeaveStatus() != null) {
                txtLeaveStatus.setVisibility(adapterView.VISIBLE);
                btnDelete.setVisibility(adapterView.INVISIBLE);
                btnEdit.setVisibility(adapterView.INVISIBLE);
            } else {
                txtLeaveStatus.setVisibility(adapterView.INVISIBLE);
                btnDelete.setVisibility(adapterView.VISIBLE);
                btnEdit.setVisibility(adapterView.VISIBLE);
            }
            btnAprove.setVisibility(adapterView.INVISIBLE);
            btnReject.setVisibility(adapterView.INVISIBLE);
        }
        txtEmployeeNm.setText(m.getEmployeeName());
        txtEmployeeId.setText(m.getEmployeeId());
        txtProdMonth.setText(m.getLeaveProdMonth());
        txtDate.setText(m.getLeaveProdDate());
        txtLeaveType.setText(m.getTimeOffType());
        txtLeaveDescription.setText(m.getLeaveReason());
        txtLeaveId.setText(m.getLeaveId());
        txtLeaveStatus.setText(m.getLeaveStatus());
        if (m.getLeaveStatus().equalsIgnoreCase("rejected") ){
            txtLeaveStatus.setTextColor(Color.RED);
        }
        //Log.e("Employe Name",m.getEmployeeName());
        final ViewGroup prnt=parent;
        final int pos=position;

        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View vp=(View)v.getParent();
                        TextView txtLeaveId = (TextView) vp.findViewById(R.id.txtLeaveId);
                        final String LeaveId = txtLeaveId.getText().toString();
                        ((ListView) prnt).performItemClick(vp, pos, v.getId());
                    }
                }
        );
        btnAprove.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View vp=(View)v.getParent();
                        TextView txtLeaveId = (TextView) vp.findViewById(R.id.txtLeaveId);
                        final String LeaveId = txtLeaveId.getText().toString();
                        ((ListView) prnt).performItemClick(vp, pos, v.getId());
                    }
                }
        );
        btnReject.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View vp=(View)v.getParent();
                        TextView txtLeaveId = (TextView) vp.findViewById(R.id.txtLeaveId);
                        final String LeaveId = txtLeaveId.getText().toString();
                        ((ListView) prnt).performItemClick(vp, pos, v.getId());
                    }
                }
        );
        btnEdit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View vp=(View)v.getParent();
                        TextView txtLeaveId = (TextView) vp.findViewById(R.id.txtLeaveId);
                        final String LeaveId = txtLeaveId.getText().toString();
                        ((ListView) prnt).performItemClick(vp, pos, v.getId());
                    }
                }
        );


        return adapterView;
    }
}
