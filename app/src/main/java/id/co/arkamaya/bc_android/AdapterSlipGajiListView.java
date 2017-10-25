package id.co.arkamaya.bc_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import id.co.arkamaya.cico.R;
import pojo.GetSalaryPdf;

/**
 * Created by root on 25/10/17.
 */

public class AdapterSlipGajiListView extends ArrayAdapter<GetSalaryPdf> {
    private Context context;
    private List<GetSalaryPdf> salaryList;
    SharedPreferences pref;
    View adapterView;
    private ProgressDialog progress;
    public String ENDPOINT="http://bc-id.co.id/";
    public String EmployeeId;
    private ListView list;
    public AdapterSlipGajiListView(Context context, int resource, List<GetSalaryPdf> object){
        super(context, resource, object);
        this.context = context;
        this.salaryList = object;


    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapterView = inflater.inflate(R.layout.list_adapter_salary, parent, false);
        GetSalaryPdf m = salaryList.get(position);

        pref = PreferenceManager.getDefaultSharedPreferences(adapterView.getContext().getApplicationContext());
        EmployeeId=pref.getString("EmployeeId", null);
        ENDPOINT=pref.getString("URLEndPoint", "");

        TextView txtDate=(TextView)adapterView.findViewById(R.id.lblPdfDate);
        TextView txtName=(TextView)adapterView.findViewById(R.id.txtPdfName);
        TextView txtPdfId=(TextView)adapterView.findViewById(R.id.txtPdfId);
        TextView txtPdfFileName=(TextView)adapterView.findViewById(R.id.txtPdfFileName);
        ImageButton btnDownload=(ImageButton)adapterView.findViewById((R.id.btnDownload));



        txtDate.setText(m.getPeriod());
        txtName.setText(m.getEmployeeName());
        txtPdfId.setText(m.getAttachmentId());
        txtPdfFileName.setText(m.getFileName());

        final ViewGroup prnt=parent;
        final int pos=position;

        btnDownload.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View vp=(View)v.getParent();
                        TextView txtPdfId = (TextView) vp.findViewById(R.id.txtPdfId);
                        final String PdfID = txtPdfId.getText().toString();
                        ((ListView) prnt).performItemClick(vp, pos, v.getId()); // Let the event be handled in onItemClick()
                    }
                }
        );


        return adapterView;
    }
}
