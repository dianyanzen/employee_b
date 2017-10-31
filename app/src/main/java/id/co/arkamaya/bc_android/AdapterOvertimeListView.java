package id.co.arkamaya.bc_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
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
import android.widget.Toast;

import java.util.List;

import id.co.arkamaya.cico.R;
import pojo.CheckLogin;
import pojo.GetOvertimeList;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 3/11/2016.
 */
public class AdapterOvertimeListView extends ArrayAdapter<GetOvertimeList> {
    private Context context;
    private List<GetOvertimeList> overtimeList;
    SharedPreferences pref;
    View adapterView;
    private ProgressDialog progress;
    public String ENDPOINT="http://bc-id.co.id/";
    public String EmployeeId;
    private ListView list;
    public AdapterOvertimeListView(Context context, int resource, List<GetOvertimeList> object){
        super(context, resource, object);
        this.context = context;
        this.overtimeList = object;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapterView = inflater.inflate(R.layout.list_adapter_overtime, parent, false);
        GetOvertimeList m = overtimeList.get(position);

        pref = PreferenceManager.getDefaultSharedPreferences(adapterView.getContext().getApplicationContext());
        EmployeeId=pref.getString("EmployeeId", null);
        ENDPOINT=pref.getString("URLEndPoint", "");
        RelativeLayout Overtime_list = (RelativeLayout)adapterView.findViewById(R.id.overtime_list);
        TextView txtEmployeeNm=(TextView)adapterView.findViewById(R.id.txtEmployeeName);
        TextView txtEmployeeId=(TextView)adapterView.findViewById(R.id.txtEmployeeId);
        TextView txtProdMonth = (TextView)adapterView.findViewById(R.id.txtProdMonth);
        TextView txtDate = (TextView)adapterView.findViewById(R.id.txtDate);
        TextView txtHour = (TextView)adapterView.findViewById(R.id.txtOvertimeHour);
        TextView txtOvertimeDescription = (TextView)adapterView.findViewById(R.id.txtOvertimeDescription);
        TextView txtOvertimeId = (TextView)adapterView.findViewById(R.id.txtOvertimeId);
        TextView txtOvertimeStatus =(TextView)adapterView.findViewById(R.id.txtOvertimeStatus);
        AppCompatImageView btnDelete=(AppCompatImageView)adapterView.findViewById((R.id.btnDelete));
        AppCompatImageView btnEdit=(AppCompatImageView)adapterView.findViewById((R.id.btnEdit));
        AppCompatImageView btnAprove=(AppCompatImageView)adapterView.findViewById((R.id.btnAprove));
        AppCompatImageView btnReject=(AppCompatImageView)adapterView.findViewById((R.id.btnReject));
        Log.e("Data",m.getEmployeeId().toString()+" - "+EmployeeId);
        String GetemployeID =m.getEmployeeId();

        if ( !GetemployeID.equals(EmployeeId) ) {
            btnDelete.setVisibility(adapterView.INVISIBLE);
            btnEdit.setVisibility(adapterView.INVISIBLE);
            Overtime_list.setBackgroundColor(adapterView.getResources().getColor(R.color.colorGrayLight));
            if (m.getOvertimeStatus() != "" && m.getOvertimeStatus() != null) {
                txtOvertimeStatus.setVisibility(adapterView.VISIBLE);
                btnAprove.setVisibility(adapterView.INVISIBLE);
                btnReject.setVisibility(adapterView.INVISIBLE);
            } else {
                txtOvertimeStatus.setVisibility(adapterView.INVISIBLE);
                btnAprove.setVisibility(adapterView.VISIBLE);
                btnReject.setVisibility(adapterView.VISIBLE);
            }

        }
        else {
            if (m.getOvertimeStatus() != "" && m.getOvertimeStatus() != null) {
                txtOvertimeStatus.setVisibility(adapterView.VISIBLE);
                btnDelete.setVisibility(adapterView.INVISIBLE);
                btnEdit.setVisibility(adapterView.INVISIBLE);
            } else {
                txtOvertimeStatus.setVisibility(adapterView.INVISIBLE);
                btnDelete.setVisibility(adapterView.VISIBLE);
                btnEdit.setVisibility(adapterView.VISIBLE);
            }
            btnAprove.setVisibility(adapterView.INVISIBLE);
            btnReject.setVisibility(adapterView.INVISIBLE);
        }
        txtEmployeeNm.setText(m.getEmployeeName());
        txtEmployeeId.setText(m.getEmployeeId());
        txtProdMonth.setText(m.getOvertimeProdMonth());
        txtDate.setText(m.getOvertimeProdDate());
        txtOvertimeDescription.setText(m.getOtDescription());
        txtHour.setText(m.getOtHour());
        txtOvertimeId.setText(m.getOtId());
        txtOvertimeStatus.setText(m.getOvertimeStatus());
        if (m.getOvertimeStatus().equalsIgnoreCase("rejected") ){
            txtOvertimeStatus.setTextColor(Color.RED);
        }

        final ViewGroup prnt=parent;
        final int pos=position;

        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View vp=(View)v.getParent();
                        TextView txtOvertimeId = (TextView) vp.findViewById(R.id.txtOvertimeId);
                        final String OvertimeId = txtOvertimeId.getText().toString();
                        //Toast.makeText(v.getContext(), "Delete button clicked " +ReimburseId+"-"+EmployeeId, Toast.LENGTH_SHORT).show();
                        ((ListView) prnt).performItemClick(vp, pos, v.getId()); // Let the event be handled in onItemClick()
                    }
                }
        );
        btnEdit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View vp=(View)v.getParent();
                        TextView txtOvertimeId = (TextView) vp.findViewById(R.id.txtOvertimeId);
                        final String OvertimeId = txtOvertimeId.getText().toString();
                        ((ListView) prnt).performItemClick(vp, pos, v.getId()); // Let the event be handled in onItemClick()
                    }
                }
        );
        btnAprove.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View vp=(View)v.getParent();
                        TextView txtOvertimeId = (TextView) vp.findViewById(R.id.txtOvertimeId);
                        final String OvertimeId = txtOvertimeId.getText().toString();
                        ((ListView) prnt).performItemClick(vp, pos, v.getId()); // Let the event be handled in onItemClick()
                    }
                }
        );
        btnReject.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View vp=(View)v.getParent();
                        TextView txtOvertimeId = (TextView) vp.findViewById(R.id.txtOvertimeId);
                        final String OvertimeId = txtOvertimeId.getText().toString();
                        ((ListView) prnt).performItemClick(vp, pos, v.getId()); // Let the event be handled in onItemClick()
                    }
                }
        );

        return adapterView;
    }
}
