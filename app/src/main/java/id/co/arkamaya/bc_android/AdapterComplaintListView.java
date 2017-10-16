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
import pojo.GetComplaintList;

/**
 * Created by wawan on 3/11/2016.
 */
public class AdapterComplaintListView extends ArrayAdapter<GetComplaintList> {
    private Context context;
    private List<GetComplaintList> complaintList;
    SharedPreferences pref;
    View adapterView;
    private ProgressDialog progress;
    public String ENDPOINT="http://bc-id.co.id/";
    public String EmployeeId;
    private ListView list;
    public AdapterComplaintListView(Context context, int resource, List<GetComplaintList> object){
        super(context, resource, object);
        this.context = context;
        this.complaintList = object;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapterView = inflater.inflate(R.layout.list_adapter_complaint, parent, false);
        GetComplaintList m = complaintList.get(position);

        pref = PreferenceManager.getDefaultSharedPreferences(adapterView.getContext().getApplicationContext());
        EmployeeId=pref.getString("EmployeeId", null);
        ENDPOINT=pref.getString("URLEndPoint", "");

        TextView txtProdMonth=(TextView)adapterView.findViewById(R.id.txtProdMonth);
        TextView txtDate=(TextView)adapterView.findViewById(R.id.txtDate);
        TextView txtComplaintType=(TextView)adapterView.findViewById(R.id.lblComplaintType);
        TextView txtComplaintDescription=(TextView)adapterView.findViewById(R.id.txtComplaintDescription);
        TextView txtComplaintId=(TextView)adapterView.findViewById(R.id.txtComplaintId);
        TextView txtComplaintStatus=(TextView)adapterView.findViewById(R.id.txtComplaintStatus);
        Button btnDelete=(Button)adapterView.findViewById((R.id.btnDelete));

        txtProdMonth.setText(m.getComplaintProdMonth());
        txtDate.setText(m.getComplaintProdDate());
        txtComplaintType.setText(m.getExcuseNm());
        txtComplaintDescription.setText(m.getReason());
        txtComplaintId.setText(m.getComplaintNo());
        txtComplaintStatus.setText(m.getComplaintStatus());

        final ViewGroup prnt=parent;
        final int pos=position;

        btnDelete.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        TextView txtComplaintId = (TextView) adapterView.findViewById(R.id.txtComplaintId);
//                        //final String ReimburseId = txtReimburseId.getText().toString();
//                        //Toast.makeText(v.getContext(), "Delete button clicked " +ReimburseId+"-"+EmployeeId, Toast.LENGTH_SHORT).show();
//                        ((ListView) prnt).performItemClick(v, pos, 0); // Let the event be handled in onItemClick()
//                    }
//                }
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View vp=(View)v.getParent();
                        TextView txtComplaintId = (TextView) vp.findViewById(R.id.txtComplaintId);
                        final String ComplaintId = txtComplaintId.getText().toString();
                        //Toast.makeText(v.getContext(), "Delete button clicked " +ReimburseId+"-"+EmployeeId, Toast.LENGTH_SHORT).show();
                        ((ListView) prnt).performItemClick(vp, pos, v.getId()); // Let the event be handled in onItemClick()
                    }
                }
        );

        return adapterView;
    }
}
