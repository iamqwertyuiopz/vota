package net.ecuatecnologia.golosovaniye;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import es.dmoral.toasty.Toasty;

public class SuccessActivity extends AppCompatActivity {

    static TextView receptor;

    Spinner genero, dignidad;

    EditText dGenero, dDignidad;

    Button  UploadImageOnServerButton;

    ImageView ShowSelectedImage;

    EditText imageName;
    TextView imageName2;
    TextView imageName3;

    String ImageTag = "image_tag" ;

    String ImageName = "image_data" ;

    String DignidadCodigo = "dignidad_codigo";

    String Genero = "genero";

    String NumeroJunta = "numero_junta";

    ProgressDialog progressDialog ;

    ByteArrayOutputStream byteArrayOutputStream ;

    byte[] byteArray ;

    String ConvertImage ;

    String GetImageNameFromEditText, GetJunta, GetGenero, GetDignidad;

    HttpURLConnection httpURLConnection ;

    URL url;

    OutputStream outputStream;

    BufferedWriter bufferedWriter ;

    int RC ;

    BufferedReader bufferedReader ;

    StringBuilder stringBuilder;

    boolean check = true;

    private int GALLERY = 1;

    private static final int PICTURE_RESULT = 122 ;
    private ContentValues values;
    private Uri imageUri;
    private Button myButton;
    private Button myButton2;
    private Button LogOut;
    private ImageView myImageView;
    private Bitmap thumbnail;

    String imageurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        requestMultiplePermissions();

        genero = (Spinner) findViewById(R.id.sGenero);

        dignidad = (Spinner) findViewById(R.id.sDignidad);

        dGenero = (EditText) findViewById(R.id.datasGenero);

        dDignidad = (EditText) findViewById(R.id.datasDignidad);

        receptor = (TextView) findViewById(R.id.receptor);

        //Recepcion de datos.
        Bundle parametros = this.getIntent().getExtras();
        if(parametros !=null){
            String dato = parametros.getString("dato");
            receptor.setText(dato);
        }

        UploadImageOnServerButton = (Button)findViewById(R.id.btnSubir);

        ShowSelectedImage = (ImageView)findViewById(R.id.imgPrev);

        imageName=  (EditText)findViewById(R.id.descImg);
        imageName2=  (TextView)findViewById(R.id.receptor);
        imageName3=  (TextView)findViewById(R.id.subguion);

        byteArrayOutputStream = new ByteArrayOutputStream();

        myImageView = (ImageView)findViewById(R.id.imgPrev);
        myButton = (Button)findViewById(R.id.btnCam);
        myButton2 = (Button)findViewById(R.id.btnGal);
        LogOut = (Button)findViewById(R.id.btnLogout);

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(SuccessActivity.this, MainActivity.class);
                startActivity(mainIntent);
                SuccessActivity.this.finish();
                Toasty.warning(SuccessActivity.this, "Sesión Cerrada", Toast.LENGTH_LONG).show();
            }
        });

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "Imagen");
                values.put(MediaStore.Images.Media.DESCRIPTION, "App " + System.currentTimeMillis());
                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, PICTURE_RESULT);
            }
        });
        myButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(galleryIntent, GALLERY);
            }
        });

        ArrayAdapter<CharSequence> adapterd = ArrayAdapter.createFromResource(this,R.array.combo_dignidades,R.layout.spinner_item);
        ArrayAdapter<CharSequence> adapterg = ArrayAdapter.createFromResource(this,R.array.combo_genero,R.layout.spinner_item);

        genero.setAdapter(adapterg);
        dignidad.setAdapter(adapterd);

        genero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Resources res = getResources();
                String[] generosValue = res.getStringArray(R.array.combo_genero_value);

                dGenero.setText(generosValue[position]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dignidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Resources resD = getResources();
                String[] dignidadValue = resD.getStringArray(R.array.combo_dignidades_value);

                dDignidad.setText(dignidadValue[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        UploadImageOnServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetImageNameFromEditText = imageName2.getText().toString()+imageName3.getText().toString()+imageName.getText().toString()+imageName3.getText().toString()+Calendar.getInstance()
                        .getTimeInMillis();

                GetJunta = imageName.getText().toString();

                GetGenero = dGenero.getText().toString();

                GetDignidad = dDignidad.getText().toString();

                UploadImageToServer();
            }
        });

    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {

                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    thumbnail = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    ShowSelectedImage.setImageBitmap(thumbnail);
                    UploadImageOnServerButton.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(SuccessActivity.this, "Falló!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        switch (requestCode) {
            case PICTURE_RESULT:
                if (requestCode == PICTURE_RESULT)
                    if (resultCode == Activity.RESULT_OK) {
                        try {
                            thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            myImageView.setImageBitmap(thumbnail);
                            //Obtiene la ruta donde se encuentra guardada la imagen.
                            imageurl = getRealPathFromURI(imageUri);
                            UploadImageOnServerButton.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
        }
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void UploadImageToServer(){


        //PORCENTAJE DE CALIDAD                                aqui
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);

        byteArray = byteArrayOutputStream.toByteArray();

        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(SuccessActivity.this,"Subiendo","Por favor espera",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                progressDialog.dismiss();

                Toasty.success(SuccessActivity.this,string1,Toast.LENGTH_LONG).show();
                Intent mIntent = getIntent();
                finish();
                startActivity(mIntent);

            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put(ImageTag, GetImageNameFromEditText);

                HashMapParams.put(ImageName, ConvertImage);

                HashMapParams.put(NumeroJunta, GetJunta);

                HashMapParams.put(Genero, GetGenero);

                HashMapParams.put(DignidadCodigo, GetDignidad);

                String FinalData = imageProcessClass.ImageHttpRequest("http://apps.lxndr.live/php/index.php", HashMapParams);

                return FinalData;

            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();

    }

    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {
                url = new URL(requestURL);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(20000);

                httpURLConnection.setConnectTimeout(20000);

                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoInput(true);

                httpURLConnection.setDoOutput(true);

                outputStream = httpURLConnection.getOutputStream();

                bufferedWriter = new BufferedWriter(

                        new OutputStreamWriter(outputStream, "UTF-8"));

                bufferedWriter.write(bufferedWriterDataFN(PData));

                bufferedWriter.flush();

                bufferedWriter.close();

                outputStream.close();

                RC = httpURLConnection.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReader.readLine()) != null){

                        stringBuilder.append(RC2);

                    }

                }

            } catch (Exception e) {
                e.printStackTrace();

            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            stringBuilder = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilder.append("&");

                stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilder.append("=");

                stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));

            }

            return stringBuilder.toString();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera

            }
            else {

                Toast.makeText(SuccessActivity.this, "Por favor permite el uso de la cámara para continuar", Toast.LENGTH_LONG).show();

            }
        }
    }
}