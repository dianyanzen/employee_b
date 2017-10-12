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
    public String ENDPOINT="http://192.168.88.153:8080/arka_portal";
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

        TextView txtProdMonth = (TextView)adapterView.findViewById(R.id.txtProdMonth);
        TextView txtDate = (TextView)adapterView.findViewById(R.id.txtDate);
        TextView txtHour = (TextView)adapterView.findViewById(R.id.txtOvertimeHour);
        TextView txtOvertimeDescription = (TextView)adapterView.findViewById(R.id.txtOvertimeDescription);
        TextView txtOvertimeId = (TextView)adapterView.findViewById(R.id.txtOvertimeId);
        TextView txtOvertimeStatus =(TextView)adapterView.findViewById(R.id.txtOvertimeStatus);
        Button btnDelete = (Button)adapterView.findViewById((R.id.btnDelete));

        if(m.getOvertimeStatus() != "" && m.getOvertimeStatus() != null){
            txtOvertimeStatus.setVisibility(adapterView.VISIBLE);
            btnDelete.setVisibility(adapterView.INVISIBLE);
        }else{
            txtOvertimeStatus.setVisibility(adapterView.INVISIBLE);
            btnDelete.setVisibility(adapterView.VISIBLE);
        }

        txtProdMonth.setText(m.getOvertimeProdMonth());
        txtDate.setText(m.getOvertimeProdDate());
        txtOvertimeDescription.setText(m.getOtDescription());
        txtHour.setText(m.getOtHour());
        txtOvertimeId.setText(m.getOtId());
        txtOvertimeStatus.setText(m.getOvertimeStatus());

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

        return adapterView;
    }
}
