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
import pojo.GetProfileIdCardList;

/**
 * Created by root on 19/10/17.
 */

public class AdapterIdCardListView extends ArrayAdapter<GetProfileIdCardList> {
    private Context context;
    private List<GetProfileIdCardList> idcardList;
    SharedPreferences pref;
    View adapterView;
    private ProgressDialog progress;
    public String ENDPOINT = "http://bc-id.co.id/";
    public String EmployeeId;
    private ListView list;

    public AdapterIdCardListView(Context context, int resource, List<GetProfileIdCardList> object) {
        super(context, resource, object);
        this.context = context;
        this.idcardList = object;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapterView = inflater.inflate(R.layout.list_adapter_idcard, parent, false);
        GetProfileIdCardList m = idcardList.get(position);

        pref = PreferenceManager.getDefaultSharedPreferences(adapterView.getContext().getApplicationContext());
        EmployeeId = pref.getString("EmployeeId", null);
        ENDPOINT = pref.getString("URLEndPoint", "");

        TextView txtCardName = (TextView) adapterView.findViewById(R.id.txtCardName);
        TextView lblCardType = (TextView) adapterView.findViewById(R.id.lblCardType);
        TextView txtCardId = (TextView) adapterView.findViewById(R.id.txtCardId);
        Button btnDelete = (Button) adapterView.findViewById((R.id.btnDeleteCard));

        txtCardName.setText(m.getIdNumber());
        lblCardType.setText(m.getTypeCard());
        txtCardId.setText(m.getCardId());

        final ViewGroup prnt = parent;
        final int pos = position;

        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View vp = (View) v.getParent();
                        TextView txtCardId = (TextView) vp.findViewById(R.id.txtCardId);
                        final String CardId = txtCardId.getText().toString();
                        ((ListView) prnt).performItemClick(vp, pos, v.getId()); // Let the event be handled in onItemClick()
                    }
                }
        );

        return adapterView;
    }
}