package id.co.arkamaya.bc_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

import id.co.arkamaya.cico.R;

public class ReimburseFileViewerActivity extends AppCompatActivity {

    SharedPreferences pref;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reimburse_file_viewer);

        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Bundle extras = getIntent().getExtras();

        String ReimburseFile;

        if (extras != null) {
            ReimburseFile = extras.getString("ReimburseFile");
            Log.d("Yudha",ReimburseFile);
            viewReimburseFile(ReimburseFile,extras.getString("Mode"),extras.getString("OldImage"));
        }else{
            Toast.makeText(this, "invalid parameter..", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ReimburseActivity.class);
            startActivity(intent);
        }

        Button btnBack =(Button)findViewById(R.id.btnSave);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void viewReimburseFile(String ReimburseFile,String Mode,String OldImage){
        ImageView img = (ImageView)this.findViewById(R.id.imgReimburseFile);
        Log.d("Yudha",Mode+"-"+ReimburseFile+"-"+OldImage);
        if(Mode.equals("new")){
            File imgFile = new File(ReimburseFile);

            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                img.setImageBitmap(myBitmap);
            }
        }else{
            if(OldImage.equals(ReimburseFile)){
                //Picasso.with(this).load(ReimburseFile).into(img);
                progress = new ProgressDialog(this);
                progress.setMessage("Load Image...");
                progress.setIndeterminate(true);
                progress.setCancelable(false);
                progress.show();
                Picasso.with(this).load(ReimburseFile).into(target);

            }else{
                File imgFile = new File(ReimburseFile);

                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    img.setImageBitmap(myBitmap);
                }
            }

        }

    }
    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            // loading of the bitmap was a success
            // TODO do some action with the bitmap
            ImageView img = (ImageView)findViewById(R.id.imgReimburseFile);
            img.setImageBitmap(bitmap);
            if (progress != null) {
                progress.dismiss();
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            // loading of the bitmap failed
            // TODO do some action/warning/error message
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
}
