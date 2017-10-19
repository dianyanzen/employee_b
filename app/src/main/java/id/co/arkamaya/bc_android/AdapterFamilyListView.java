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
import pojo.GetProfileFamilyList;

/**
 * Created by root on 18/10/17.
 */

public class AdapterFamilyListView extends ArrayAdapter<GetProfileFamilyList> {
    private Context context;
    private List<GetProfileFamilyList> familyList;
    SharedPreferences pref;
    View adapterView;
    private ProgressDialog progress;
    public String ENDPOINT = "http://bc-id.co.id/";
    public String EmployeeId;
    private ListView list;

    public AdapterFamilyListView(Context context, int resource, List<GetProfileFamilyList> object) {
        super(context, resource, object);
        this.context = context;
        this.familyList = object;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapterView = inflater.inflate(R.layout.list_adapter_family, parent, false);
        GetProfileFamilyList m = familyList.get(position);

        pref = PreferenceManager.getDefaultSharedPreferences(adapterView.getContext().getApplicationContext());
        EmployeeId = pref.getString("EmployeeId", null);
        ENDPOINT = pref.getString("URLEndPoint", "");

        TextView txtFamilyName = (TextView) adapterView.findViewById(R.id.txtFamilyName);
        TextView lblFamilyType = (TextView) adapterView.findViewById(R.id.lblFamilyType);
        TextView txtFamilyId = (TextView) adapterView.findViewById(R.id.txtFamilyId);
        Button btnDelete = (Button) adapterView.findViewById((R.id.btnDeleteFamily));

        txtFamilyName.setText(m.getFullName());
        lblFamilyType.setText(m.getRelationship());
        txtFamilyId.setText(m.getFamilyId());

        final ViewGroup prnt = parent;
        final int pos = position;

        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View vp = (View) v.getParent();
                        TextView txtFamilyId = (TextView) vp.findViewById(R.id.txtFamilyId);
                        final String FamilyId = txtFamilyId.getText().toString();
                        ((ListView) prnt).performItemClick(vp, pos, v.getId()); // Let the event be handled in onItemClick()
                    }
                }
        );

        return adapterView;
    }
}