package id.co.arkamaya.cico;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import pojo.GetReimburseList;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by dawala on 2/21/2016.
 */
public class AdapterReimburseListView extends ArrayAdapter<GetReimburseList> {
    private Context context;
    private List<GetReimburseList> reimburseList;
    SharedPreferences pref;
    View adapterView;
    private ProgressDialog progress;
    public String ENDPOINT="http://192.168.1.146:9888/";
    public String EmployeeId;
    private ListView list;
    public AdapterReimburseListView(Context context, int resource, List<GetReimburseList> object){
        super(context, resource, object);
        this.context = context;
        this.reimburseList = object;


    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapterView = inflater.inflate(R.layout.list_adapter_reimburse, parent, false);
        GetReimburseList m = reimburseList.get(position);

        pref = PreferenceManager.getDefaultSharedPreferences(adapterView.getContext().getApplicationContext());
        EmployeeId=pref.getString("EmployeeId", null);
        ENDPOINT=pref.getString("URLEndPoint", "");

        TextView txtProdMonth=(TextView)adapterView.findViewById(R.id.txtProdMonth);
        TextView txtDate=(TextView)adapterView.findViewById(R.id.txtDate);
        TextView txtReimburseType=(TextView)adapterView.findViewById(R.id.lblReimburseType);
        TextView txtReimburseDescription=(TextView)adapterView.findViewById(R.id.txtReimburseDescription);
        TextView txtReimburseId=(TextView)adapterView.findViewById(R.id.txtReimburseId);
        TextView txtReimburseStatus=(TextView)adapterView.findViewById(R.id.txtReimburseStatus);
        Button btnDelete=(Button)adapterView.findViewById((R.id.btnDelete));

        txtProdMonth.setText(m.getReimburseProdMonth());
        txtDate.setText(m.getReimburseProdDate());
        txtReimburseType.setText(m.getReimburseType());
        txtReimburseDescription.setText(m.getReimburseDescription());
        txtReimburseId.setText(m.getReimburseId());
        txtReimburseStatus.setText(m.getReimburseStatus());
        txtReimburseStatus.setVisibility(View.INVISIBLE);

        if(m.getReimburseStatus().equals("Approved")){
            txtReimburseStatus.setVisibility(View.VISIBLE);
            //btnDelete.setText("Approved");
            //btnDelete.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_white_24dp, 0, 0, 0);
            btnDelete.setVisibility(View.INVISIBLE);
            //btnDelete.setTextColor(Color.parseColor("#8ab23c"));
            //btnDelete.setBackgroundColor(Color.WHITE);
        }

        final ViewGroup prnt=parent;
        final int pos=position;

        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View vp=(View)v.getParent();
                        TextView txtReimburseId = (TextView) vp.findViewById(R.id.txtReimburseId);
                        final String ReimburseId = txtReimburseId.getText().toString();
                        //Toast.makeText(v.getContext(), "Delete button clicked " +ReimburseId+"-"+EmployeeId, Toast.LENGTH_SHORT).show();
                        ((ListView) prnt).performItemClick(vp, pos, v.getId()); // Let the event be handled in onItemClick()
                    }
                }
        );

        return adapterView;
    }



}
