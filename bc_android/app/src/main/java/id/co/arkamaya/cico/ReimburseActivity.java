package id.co.arkamaya.cico;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pojo.CheckLogin;
import pojo.GetReimburseList;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class ReimburseActivity extends AppCompatActivity {

    private String ReimburseId;
    SharedPreferences pref;
    private ProgressDialog progress;

    private Spinner spnReimburseType;
    private EditText txtReimburseDate;
    private EditText txtReimburseDescription;
    private EditText txtReimburseAmount;
    private EditText txtReimburseFile;

    ArrayAdapter<String> dataAdapterReimburseType;

    public String ENDPOINT="http://192.168.1.146:9888/";
    final public String IMAGE_PATH="assets/files/reimburse/";

    private static final int CAMERA_REQUEST = 1888;
    final int CAMERA_CAPTURE = 1;
    final int CROP_PIC = 2;
    private Uri picUri;
    private Uri outputFileUri;
    private String modeEdit;
    final int REQUEST_CODE = 99;
    int preference = ScanConstants.OPEN_CAMERA;

    int aspectX;
    int aspectY;
    int outputX;
    int outputY;

    ConnectionDetector conDetector;

    /**UserInfo**/
    String User="";
    String EmployeeId="";

    String old_reimburse_file;

    /**DatePicker**/
    private int year, month, day;

    private class ScreenResolution {
        int width, height;
        public ScreenResolution(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    ScreenResolution deviceDimensions() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        // getsize() is available from API 13
        if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return new ScreenResolution(size.x, size.y);
        } else {
            Display display = getWindowManager().getDefaultDisplay();
            // getWidth() & getHeight() are depricated
            return new ScreenResolution(display.getWidth(), display.getHeight());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reimburse);
        conDetector =  new ConnectionDetector(getApplicationContext());
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        EmployeeId=pref.getString("EmployeeId", null);
        ENDPOINT= pref.getString("URLEndPoint", "");
        User= pref.getString("User",null);
        EmployeeId=pref.getString("EmployeeId",null);

        Bundle extras = getIntent().getExtras();

        ScreenResolution sr = deviceDimensions();
        // use Euclid's theorem to calculate the proper aspect ratio i.e. screen
        // i.e. for resolution 480 * 800, aspect Ratio 3:5
        int gcd = GCD(sr.width, sr.height);
        aspectX = sr.height / gcd;
        aspectY = sr.width / gcd;
        // subtract to keep the output image's width & height possibly low as android
        // default crop is not well suited to pick big image
        outputX = sr.height - aspectX * 30;
        outputY = sr.width - aspectY * 30;

        Log.d("Yudha","ox "+outputX+" oy "+outputY+" ax "+aspectX+" ay "+aspectY);


        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


        /*Spinner datasource setting*/
        spnReimburseType = (Spinner) findViewById(R.id.spnReimburseType);
        List<String> list = new ArrayList<String>();
        list.add("TRANSPORT");
        list.add("HEALTHY");
        list.add("OTHERS");

        dataAdapterReimburseType = new ArrayAdapter<String>
                (this , android.R.layout.simple_spinner_item,list);

        dataAdapterReimburseType.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spnReimburseType.setAdapter(dataAdapterReimburseType);
        /*End Spinner datasource setting*/

        txtReimburseDate=(EditText)findViewById(R.id.txtReimburseDate);
        txtReimburseDescription=(EditText)findViewById(R.id.txtReimburseDescription);
        txtReimburseAmount=(EditText)findViewById(R.id.txtReimburseAmount);
        txtReimburseFile=(EditText)findViewById(R.id.txtReimburseFile);


        if (extras != null) {
            modeEdit=extras.getString("Mode");
            if(modeEdit.equals("edit")){
                modeEdit="edit";
                ReimburseId = extras.getString("ReimburseId");
                /*Toast.makeText(this, "Reimburse id "+ReimburseId, Toast.LENGTH_SHORT).show();*/
                getReimburseById(ReimburseId);
            }else{
                modeEdit="new";
                txtReimburseDate.setText("");
                txtReimburseDescription.setText("");
                txtReimburseAmount.setText("");
                txtReimburseFile.setText("");
                old_reimburse_file="";
                setDate();
            }

        }else{
            Toast.makeText(this, "Invalid parameter", Toast.LENGTH_SHORT).show();
            finish();
        }

        Button btnCancel =(Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnReimburseFile=(Button) findViewById(R.id.btnReimburseFile);
        btnReimburseFile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {

                /*ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "ReimburseFile");
                values.put(MediaStore.Images.Media.DESCRIPTION, "EvidenceReimburse");
                picUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                //startActivityForResult(intent, CAMERA_CAPTURE);*/

                /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(Environment.getExternalStorageDirectory(),
                        "/temporary_reimburse.jpg");
                try {
                    f.createNewFile();
                } catch (IOException ex) {
                    Log.e("io", ex.getMessage());
                }

                picUri = Uri.fromFile(f);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                intent.putExtra("return-data", true);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, CAMERA_CAPTURE);
                }*/

                Intent intent = new Intent(v.getContext(), ScanActivity.class);
                intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });

        Button btnViewReimburseFile=(Button) findViewById(R.id.btnViewReimburseFile);
        btnViewReimburseFile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String ReimburseFile = txtReimburseFile.getText().toString();
                if(ReimburseFile.equals(old_reimburse_file)){
                    ReimburseFile=ENDPOINT + IMAGE_PATH +ReimburseFile;
                }
                Intent intent = new Intent(v.getContext(), ReimburseFileViewerActivity.class);
                intent.putExtra("ReimburseFile", ReimburseFile);
                intent.putExtra("Mode",modeEdit);
                intent.putExtra("OldImage",ENDPOINT+IMAGE_PATH+ old_reimburse_file);
                startActivity(intent);
            }
        });

        Button btnSave=(Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                    .setMessage("Are you sure?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if(conDetector.isConnectingToInternet()){
                                onSaveReimburse();
                            }else{
                                Toast.makeText(getApplicationContext(), "Internet Connection Error..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
            }
        });

        Button btnDatePicker=(Button)findViewById(R.id.btnDatePicker);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });



    }

    protected void onSaveReimburse(){
        String reimburse_date= txtReimburseDate.getText().toString();
        String reimburse_type=spnReimburseType.getSelectedItem().toString();
        String reimburse_description=txtReimburseDescription.getText().toString();
        String reimburse_amount= txtReimburseAmount.getText().toString();
        String reimburse_file=txtReimburseFile.getText().toString();
        String reimburse_id=ReimburseId;

        /*Cek mandatory field*/
        String fieldName="";

        if(reimburse_date.equals("")){
            fieldName="Reimburse Date";
        }

        if(reimburse_type.equals("")){
            if(fieldName.equals("")){
                fieldName="Reimburse Type";
            }else{
                fieldName=fieldName+", Reimburse Type";
            }
        }

        if(reimburse_description.equals("")){
            if(fieldName.equals("")){
                fieldName="Reimburse Description";
            }else{
                fieldName=fieldName+", Reimburse Description";
            }
        }

        if(reimburse_amount.equals("")){
            if(fieldName.equals("")){
                fieldName="Reimburse Ammount";
            }else{
                fieldName=fieldName+", Reimburse Ammount";
            }
        }

        if(reimburse_file.equals("")){
            if(fieldName.equals("")){
                fieldName="Reimburse File";
            }else{
                fieldName=fieldName+", Reimburse File";
            }
        }
        /*End Cek mandatory field*/

        if(!fieldName.equals("")) {
            Toast.makeText(getApplicationContext(), "Incomplete data : "+fieldName, Toast.LENGTH_LONG).show();
        }else{
            Log.d("Yudha",old_reimburse_file +"-"+reimburse_file);
            TypedFile reimburse_typed_file;
            if(reimburse_file.equals(old_reimburse_file.toString())){
                //reimburse_typed_file= new TypedFile("multipart/form-data", new File(reimburse_file));
                /*-------------------------------*/
                progress = new ProgressDialog(this);
                progress.setMessage("Processing..");
                progress.setIndeterminate(true);
                progress.setCancelable(false);
                progress.show();
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(ENDPOINT)
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .build();

                APIReimburse restInterface = restAdapter.create(APIReimburse.class);

                restInterface.onSaveReimburseWoImage(reimburse_id, reimburse_date, reimburse_type, reimburse_description,
                        reimburse_amount, User, old_reimburse_file, reimburse_file, EmployeeId, new Callback<CheckLogin>() {
                            @Override
                            public void success(CheckLogin m, Response response) {

                                if (progress != null) {
                                    progress.dismiss();
                                }

                                Toast.makeText(getApplicationContext(), m.getMsgText(), Toast.LENGTH_SHORT).show();

                                if (m.getMsgType().toLowerCase().equals("info")) {
                                    finish();
                                } else {

                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                if (progress != null) {
                                    progress.dismiss();
                                }
                            }
                        });
            }else{
                reimburse_typed_file= new TypedFile("multipart/form-data", new File(reimburse_file));
                /*-------------------------------*/
                progress = new ProgressDialog(this);
                progress.setMessage("Processing..");
                progress.setIndeterminate(true);
                progress.setCancelable(false);
                progress.show();
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(ENDPOINT)
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .build();

                APIReimburse restInterface = restAdapter.create(APIReimburse.class);

                restInterface.onSaveReimburse(reimburse_typed_file, reimburse_id, reimburse_date, reimburse_type, reimburse_description,
                        reimburse_amount, User, old_reimburse_file,reimburse_file, EmployeeId, new Callback<CheckLogin>() {
                            @Override
                            public void success(CheckLogin m, Response response) {

                                if (progress != null) {
                                    progress.dismiss();
                                }

                                Toast.makeText(getApplicationContext(), m.getMsgText(), Toast.LENGTH_SHORT).show();

                                if (m.getMsgType().toLowerCase().equals("info")) {
                                    finish();
                                } else {

                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                if (progress != null) {
                                    progress.dismiss();
                                }
                            }
                        });
            }

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            Bitmap bitmap = null;
            try {
                ImageView img = (ImageView)this.findViewById(R.id.imgReimburse);
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                getContentResolver().delete(uri, null, null);
                img.setImageBitmap(bitmap);
                OutputStream fOut = null;
                try {
                    File root = new File(Environment.getExternalStorageDirectory()
                            + File.separator + "reimburse" + File.separator);
                    root.mkdirs();
                    File sdImageMainDirectory = new File(root, "myPicName.jpg");
                    outputFileUri = Uri.fromFile(sdImageMainDirectory);
                    fOut = new FileOutputStream(sdImageMainDirectory);

                    //Toast.makeText(this, outputFileUri.getPath(),
                    //        Toast.LENGTH_SHORT).show();
                    txtReimburseFile.setText(outputFileUri.getPath());
                } catch (Exception e) {
                    Toast.makeText(this, "Error occured. Please try again later.",
                            Toast.LENGTH_SHORT).show();
                }

                try {
                    //Compress the file
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
                    //fOut.flush(thePic);
                    fOut.flush();
                    fOut.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_CAPTURE) {
                //performCrop();
                // get the returned data
                //Bundle extras = data.getExtras();
                // get the cropped bitmap
                //Bitmap thePic = extras.getParcelable("data");
                Bitmap thePic = BitmapFactory.decodeFile(picUri.getPath());
                ImageView img = (ImageView)this.findViewById(R.id.imgReimburse);
                img.setImageBitmap(thePic);

                OutputStream fOut = null;
                try {
                    File root = new File(Environment.getExternalStorageDirectory()
                            + File.separator + "reimburse" + File.separator);
                    root.mkdirs();
                    File sdImageMainDirectory = new File(root, "myPicName.jpg");
                    outputFileUri = Uri.fromFile(sdImageMainDirectory);
                    fOut = new FileOutputStream(sdImageMainDirectory);

                    //Toast.makeText(this, outputFileUri.getPath(),
                    //        Toast.LENGTH_SHORT).show();
                    txtReimburseFile.setText(outputFileUri.getPath());
                } catch (Exception e) {
                    Toast.makeText(this, "Error occured. Please try again later.",
                            Toast.LENGTH_SHORT).show();
                }

                try {
                    //Compress the file
                    thePic.compress(Bitmap.CompressFormat.JPEG, 70, fOut);
                    //fOut.flush(thePic);
                    fOut.flush();
                    fOut.close();
                } catch (Exception e) {
                }
            }
            // user is returning from cropping the image
            else if (requestCode == CROP_PIC) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");
                ImageView img = (ImageView)this.findViewById(R.id.imgReimburse);
                img.setImageBitmap(thePic);

                OutputStream fOut = null;
                try {
                    File root = new File(Environment.getExternalStorageDirectory()
                            + File.separator + "reimburse" + File.separator);
                    root.mkdirs();
                    File sdImageMainDirectory = new File(root, "myPicName.jpg");
                    outputFileUri = Uri.fromFile(sdImageMainDirectory);
                    fOut = new FileOutputStream(sdImageMainDirectory);

                    //Toast.makeText(this, outputFileUri.getPath(),
                    //        Toast.LENGTH_SHORT).show();
                    txtReimburseFile.setText(outputFileUri.getPath());
                } catch (Exception e) {
                    Toast.makeText(this, "Error occured. Please try again later.",
                            Toast.LENGTH_SHORT).show();
                }

                try {
                    //Compress the file
                    thePic.compress(Bitmap.CompressFormat.PNG, 50, fOut);
                    //fOut.flush(thePic);
                    fOut.flush();
                    fOut.close();
                } catch (Exception e) {
                }
            }
        }


    }*/

    private int GCD(int a, int b) {
        return (b == 0 ? a : GCD(b, a % b));
    }

    /**
     * this function does the crop operation.
     */
    private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", aspectX);
            cropIntent.putExtra("aspectY", aspectY);
            // indicate output X and Y
            cropIntent.putExtra("outputX",outputX/4);
            cropIntent.putExtra("outputY", outputY/4);
            // retrieve data on return

            File f = new File(Environment.getExternalStorageDirectory(),
                    "/temporary_holder.jpg");
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Log.e("io", ex.getMessage());
            }

            picUri = Uri.fromFile(f);

            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);

            cropIntent.putExtra("return-data", true);
            cropIntent.putExtra("scale", true);

            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void getReimburseById(String ReimburseId){
        /*-------------------------------*/
        progress = new ProgressDialog(this);
        progress.setMessage("Load Data..");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIReimburse restInterface = restAdapter.create(APIReimburse.class);

        restInterface.getReimburseById(ReimburseId, new Callback<GetReimburseList>() {
            @Override
            public void success(GetReimburseList m, Response response) {
                txtReimburseDate.setText(m.getReimburseDt().toString());
                int idxLocation = dataAdapterReimburseType.getPosition(m.getReimburseType().toString());
                Log.e("data",toString().valueOf(idxLocation));
                spnReimburseType.setSelection(0);
                txtReimburseDescription.setText(m.getReimburseDescription().toString());
                txtReimburseAmount.setText(m.getReimburseAmount().toString());
                txtReimburseFile.setText(m.getReimburseFile().toString());
                old_reimburse_file = m.getReimburseFile().toString();
                if (progress != null) {
                    progress.dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                if (progress != null) {
                    progress.dismiss();
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    public void setDate() {
        showDialog(999);
        //Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
        //        .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {

        txtReimburseDate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }
}
