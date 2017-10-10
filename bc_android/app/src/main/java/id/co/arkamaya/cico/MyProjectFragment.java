package id.co.arkamaya.cico;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProjectFragment extends Fragment {
    EditText txt_response;
    public MyProjectFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_project, container, false);
        txt_response = (EditText) view.findViewById(R.id.txt_response);
        txt_response.setText("Hello");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_project, container, false);
    }

    void run()  {
        OkHttpClient client = new OkHttpClient();

        try{
            Request request = new Request.Builder()
                    .url("http://publicobject.com/helloworld.txt")
                    .build();

            Response response = client.newCall(request).execute();

            txt_response.setText(response.body().toString());
        }catch (Exception e){
            Log.d("Yudha",e.getCause().getMessage());
        }

    }

}

