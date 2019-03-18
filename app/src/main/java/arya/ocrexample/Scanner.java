package arya.ocrexample;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Policy;

public class Scanner extends AppCompatActivity implements SurfaceHolder.Callback, Camera.PictureCallback {
    SurfaceView surfaceView, rect;
    Button Scanner;
    SurfaceHolder surfaceHolder, holderTransparent;
    Bitmap img;
    Camera camera;
    int deviceWidth,deviceHeight,RectLeft,RectTop,RectRight,RectBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        surfaceView = (SurfaceView) findViewById(R.id.camera);
        rect = (SurfaceView) findViewById(R.id.rectangle);
        Scanner = (Button) findViewById(R.id.btn_scan);

        deviceWidth = getDeviceWidth();
        deviceHeight = getDeviceHeight();

        setupSurfaceHolder();
        setupTransparentHolder();
        sendBitmap();
    }

    private int getDeviceHeight() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private int getDeviceWidth() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private void Draw(){
        Canvas canvas = holderTransparent.lockCanvas(null);
        Paint paint =  new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(5);

        RectLeft = 90;
        RectTop =350;
        RectRight = 650;
        RectBottom = RectTop + 100;
        Rect rec = new Rect((int)RectLeft, (int)RectTop, (int)RectRight, (int)RectBottom);
        canvas.drawRect(rec,paint);

        Paint paintText = new Paint();
        paintText.setColor(Color.WHITE);
        paintText.setTextSize(30);
        paintText.setFakeBoldText(true);
        canvas.drawText("Scan NIK Anda Di Dalam BOX", surfaceView.getWidth()/4, surfaceView.getHeight()/4, paintText);

        holderTransparent.unlockCanvasAndPost(canvas);
    }

    public void setupSurfaceHolder(){
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }

    public void setupTransparentHolder(){
        holderTransparent = rect.getHolder();
        holderTransparent.addCallback(this);
        holderTransparent.setFormat(PixelFormat.TRANSLUCENT);
        rect.setZOrderMediaOverlay(true);
    }

    public void startCamera(){
        camera = Camera.open();
        camera.setDisplayOrientation(90);
        try {
            Camera.Parameters parameters= camera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            camera.setParameters(parameters);
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    Toast.makeText(Scanner.this, "FOCUS", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void releaseCamera(){
        if (camera != null){
            camera.stopPreview();
            camera.release();
            camera= null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        {Draw();}
        startCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Bitmap bitmapOri = BitmapFactory.decodeByteArray(data,0, data.length);
        float scale = 1280/1000F;
        int left = (int) scale * (bitmapOri.getWidth()-400)/2;
        int top = (int) scale * (bitmapOri.getHeight()-616)/2;
        int width = (int) scale * 400;
        int height = (int) scale * 616;
        Matrix rotationMatrix = new Matrix();
        rotationMatrix.setRotate(90);
        img = Bitmap.createBitmap(bitmapOri, left, top, width,  height, rotationMatrix, false);
//        Log.i("BTMP", )
    }

    public void sendBitmap(){
        Scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                img.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                byte[] byteArray = stream.toByteArray();
                Intent intent = new Intent(Scanner.this, MainActivity.class);
//                intent.putExtra("btmp", byteArray);
                startActivity(intent);
            }
        });
    }

}
