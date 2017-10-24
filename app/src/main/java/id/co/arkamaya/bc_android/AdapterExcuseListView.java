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
import android.widget.TextView;

import java.util.List;

import id.co.arkamaya.cico.R;
import pojo.GetExcuseList;

/**
 * Created by root on 10/10/17.
 */

public class AdapterExcuseListView extends ArrayAdapter<GetExcuseList> {
    private Context context;
    private List<GetExcuseList> excuseList;
    SharedPreferences pref;
    View adapterView;
    private ProgressDialog progress;
    public String ENDPOINT="http://bc-id.co.id/";
    public String EmployeeId;
    private ListView list;
    public AdapterExcuseListView(Context context, int resource, List<GetExcuseList> object){
        super(context, resource, object);
        this.context = context;
        this.excuseList = object;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapterView = inflater.inflate(R.layout.list_adapter_excuse, parent, false);
        GetExcuseList m = excuseList.get(position);

        pref = PreferenceManager.getDefaultSharedPreferences(adapterView.getContext().getApplicationContext());
        EmployeeId=pref.getString("EmployeeId", null);
        ENDPOINT=pref.getString("URLEndPoint", "");

        TextView txtProdMonth=(TextView)adapterView.findViewById(R.id.txtProdMonth);
        TextView txtEmployeeNm=(TextView)adapterView.findViewById(R.id.txtEmployeeName);
        TextView txtEmployeeId=(TextView)adapterView.findViewById(R.id.txtEmployeeId);
        TextView txtDate=(TextView)adapterView.findViewById(R.id.txtDate);
        TextView txtExcuseType=(TextView)adapterView.findViewById(R.id.txtExcueseType);
        TextView txtExcuseDescription=(TextView)adapterView.findViewById(R.id.txtExcuseDescription);
        TextView txtExcuseId=(TextView)adapterView.findViewById(R.id.txtExcuseId);
        TextView txtExcuseStatus=(TextView)adapterView.findViewById(R.id.txtExcuseStatus);
        Button btnDelete=(Button)adapterView.findViewById((R.id.btnDelete));
        ImageButton btnAprove=(ImageButton)adapterView.findViewById((R.id.btnAprove));
        ImageButton btnReject=(ImageButton)adapterView.findViewById((R.id.btnReject));
        Log.e("Data",m.getEmployeeId().toString()+" - "+EmployeeId);
        String GetemployeID =m.getEmployeeId();

        if ( !GetemployeID.equals(EmployeeId) ) {
            btnDelete.setVisibility(adapterView.INVISIBLE);
            if (m.getExcuseStatus() != "" && m.getExcuseStatus() != null) {
                txtExcuseStatus.setVisibility(adapterView.VISIBLE);
                btnAprove.setVisibility(adapterView.INVISIBLE);
                btnReject.setVisibility(adapterView.INVISIBLE);
            } else {
                txtExcuseStatus.setVisibility(adapterView.INVISIBLE);
                btnAprove.setVisibility(adapterView.VISIBLE);
                btnReject.setVisibility(adapterView.VISIBLE);
            }

        }
        else {
            if (m.getExcuseStatus() != "" && m.getExcuseStatus() != null) {
                txtExcuseStatus.setVisibility(adapterView.VISIBLE);
                btnDelete.setVisibility(adapterView.INVISIBLE);
            } else {
                txtExcuseStatus.setVisibility(adapterView.INVISIBLE);
                btnDelete.setVisibility(adapterView.VISIBLE);
            }
            btnAprove.setVisibility(adapterView.INVISIBLE);
            btnReject.setVisibility(adapterView.INVISIBLE);
        }


        txtEmployeeId.setText(m.getEmployeeId());
        txtEmployeeNm.setText(m.getEmployeeName());
        txtProdMonth.setText(m.getExcuseProdMonth());
        txtDate.setText(m.getExcuseProdDate());
        txtExcuseType.setText(m.getExcuseType());
        txtExcuseDescription.setText(m.getExcuseDescription());
        txtExcuseId.setText(m.getExcuseId());
        txtExcuseStatus.setText(m.getExcuseStatus());
        if (m.getExcuseStatus().equalsIgnoreCase("rejected") ){
            txtExcuseStatus.setTextColor(Color.RED);
        }

        final ViewGroup prnt=parent;
        final int pos=position;

        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View vp=(View)v.getParent();
                        TextView txtExcuseId = (TextView) vp.findViewById(R.id.txtExcuseId);
                        final String ExcuseId = txtExcuseId.getText().toString();
                        ((ListView) prnt).performItemClick(vp, pos, v.getId()); // Let the event be handled in onItemClick()
                    }
                }
        );
        btnAprove.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View vp=(View)v.getParent();
                        TextView txtExcuseId = (TextView) vp.findViewById(R.id.txtExcuseId);
                        final String ExcuseId = txtExcuseId.getText().toString();
                        ((ListView) prnt).performItemClick(vp, pos, v.getId()); // Let the event be handled in onItemClick()
                    }
                }
        );
        btnReject.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View vp=(View)v.getParent();
                        TextView txtExcuseId = (TextView) vp.findViewById(R.id.txtExcuseId);
                        final String ExcuseId = txtExcuseId.getText().toString();
                        ((ListView) prnt).performItemClick(vp, pos, v.getId()); // Let the event be handled in onItemClick()
                    }
                }
        );

        return adapterView;
    }
}
