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
import pojo.GetProfileEducationList;

/**
 * Created by root on 19/10/17.
 */

public class AdapterEducationListView extends ArrayAdapter<GetProfileEducationList> {
    private Context context;
    private List<GetProfileEducationList> educationList;
    SharedPreferences pref;
    View adapterView;
    private ProgressDialog progress;
    public String ENDPOINT = "http://bc-id.co.id/";
    public String EmployeeId;
    private ListView list;

    public AdapterEducationListView(Context context, int resource, List<GetProfileEducationList> object) {
        super(context, resource, object);
        this.context = context;
        this.educationList = object;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapterView = inflater.inflate(R.layout.list_adapter_education, parent, false);
        GetProfileEducationList m = educationList.get(position);

        pref = PreferenceManager.getDefaultSharedPreferences(adapterView.getContext().getApplicationContext());
        EmployeeId = pref.getString("EmployeeId", null);
        ENDPOINT = pref.getString("URLEndPoint", "");

        TextView txtEducationName = (TextView) adapterView.findViewById(R.id.txtEducationName);
        TextView lblEducationType = (TextView) adapterView.findViewById(R.id.lblEducationType);
        TextView txtEducationId = (TextView) adapterView.findViewById(R.id.txtEducationId);
        Button btnDelete = (Button) adapterView.findViewById((R.id.btnDeleteEducation));

        txtEducationName.setText(m.getInstitution());
        lblEducationType.setText(m.getLevel());
        txtEducationId.setText(m.getEducationId());

        final ViewGroup prnt = parent;
        final int pos = position;

        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View vp = (View) v.getParent();
                        TextView txtEducationId = (TextView) vp.findViewById(R.id.txtEducationId);
                        final String EducationId = txtEducationId.getText().toString();
                        ((ListView) prnt).performItemClick(vp, pos, v.getId()); // Let the event be handled in onItemClick()
                    }
                }
        );

        return adapterView;
    }
}