package arya.ocrexample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ImageView image;
    Button openCamera;
    TextView resultText;
    byte[] byteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.imageResult);
        openCamera = (Button) findViewById(R.id.btn_camera);
        resultText = (TextView) findViewById(R.id.result);
//        byteArray = getIntent().getByteArrayExtra("btmp");

        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Scanner.class));
            }
        });

        requestPermission();
        setImage();
    }

    public void requestPermission() {
        final int reqcode = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] per = {Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(per, reqcode);
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                }
            }
        }
    }

    private void setImage(){
        if (byteArray==null){
            image.setImageBitmap(null);
        }else{

            Bitmap btmp = BitmapFactory.decodeByteArray(byteArray,0, byteArray.length);
            image.setImageBitmap(btmp);
        }
    }
}
