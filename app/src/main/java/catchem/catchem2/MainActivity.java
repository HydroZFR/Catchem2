package catchem.catchem2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import catchem.catchem2.Menu;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import catchem.catchem2.R;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private GestureDetectorCompat gestureDetector;
    private CameraSource cameraSource;
    private SurfaceView surfaceView;
    private LinearLayout plateView;
    private Button plate1,plate2;
    private LinearLayout underView;
    private Point windowSize;
    private ImageView didactView;
    private CountDownTimer countdown,countdown2;

    public static final int requestPermissionID = 1;
    private boolean isTimeP1;
    private ImageView pictView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isTimeP1 = true;
        gestureDetector = new GestureDetectorCompat(this, this);
        gestureDetector.setOnDoubleTapListener(this);
        Display display = getWindowManager().getDefaultDisplay();
        windowSize = new Point();
        display.getSize(windowSize);
        pictView = (ImageView) findViewById(R.id.pictView);
        pictView.setMinimumHeight(windowSize.y);
        pictView.setY(-windowSize.y/2);
        pictView.setVisibility(View.GONE);
        plate1 = new Button(this);
        plate1.setTextSize(TypedValue.COMPLEX_UNIT_PX,windowSize.y/32);
        plate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraSource.takePicture(null, new CameraSource.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        surfaceView.setVisibility(View.GONE);
                        pictView.setVisibility(View.VISIBLE);
                        pictView.setImageBitmap(bitmap);
                    }
                });
            }
        });
        plate2 = new Button(this);
        plate2.setTextSize(TypedValue.COMPLEX_UNIT_PX,windowSize.y/32);
        plate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraSource.takePicture(null, new CameraSource.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        surfaceView.setVisibility(View.GONE);
                        pictView.setVisibility(View.VISIBLE);
                        pictView.setImageBitmap(bitmap);
                    }
                });
            }
        });
        plateView = (LinearLayout)findViewById(R.id.plaqueView);
        plateView.setMinimumHeight(windowSize.y/12);
        underView = (LinearLayout) findViewById(R.id.underView);
        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        didactView = (ImageView) findViewById(R.id.didactView);
        didactView.setMinimumHeight(windowSize.y/2-windowSize.y/12);
        didactView.setMaxHeight(windowSize.y/2-windowSize.y/12);
        surfaceView.setMinimumHeight(windowSize.y);
        surfaceView.setY(-windowSize.y/2);
        underView.setMinimumHeight(windowSize.y/2);
        startCameraSource();
    }

    private void startCameraSource() {

        //Create the TextRecognizer
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Log.w("AAAA", "Detector dependencies not loaded yet");
        } else {

            //Initialize camerasource to use high resolution and set Autofocus on.
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(windowSize.x, windowSize.y)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(20.0f)
                    .build();

            /**
             * Add call back to SurfaceView and check if camera permission is granted.
             * If permission is granted we can start our cameraSource and pass it to surfaceView
             */
            surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    requestPermissionID);
                            return;
                        }
                        cameraSource.start(surfaceView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                /**
                 * Release resources for cameraSource
                 */
                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });

            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                }

                /**
                 * Detect all the text from camera using TextBlock and the values into a stringBuilder
                 * which will then be set to the textView.
                 */
                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0) {

                        plateView.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("TestLog", "List Text");
                                for (int i = 0; i < items.size(); i++) {
                                    TextBlock item = items.valueAt(i);
                                    Log.e("TestLog", "Bloc n"+i+" : "+item.getValue());
                                    String plate = "";
                                    try {
                                        Pattern p = Pattern.compile("([A-Z]{2}[- ]+[0-9]{3}[- ]+[A-Z]{2})|([0-9]{3}[- ]+[A-Z]{3}[- ]+[0-9]{2})");
                                        Matcher m = p.matcher(item.getValue());
                                        while (m.find()) {
                                            Log.i("TestLog","====\n\nFind A GROUP "+m.group().replaceAll("[- ]+","-"));
                                            String group = m.group().replaceAll("[- ]+","-");
                                            if(plate1.getText().equals("")) {
                                                Log.d("TestLog","P-1 different");
                                                plate1.setText(group);
                                                plateView.addView(plate1);
                                                launchCountdownP1();
                                            } else if(!plate1.getText().equals(group)) {
                                                Log.d("TestLog","P-2 different");
                                                plate2.setText(group);
                                                plateView.addView(plate2);
                                                launchCountdownP2();
                                            } else if(!plate1.getText().equals(group) && !plate2.getText().equals(group)) {
                                                Log.d("TestLog","the 2 different");
                                                if(isTimeP1) {
                                                    plate1.setText(group);
                                                    isTimeP1 = false;
                                                    launchCountdownP1();
                                                }
                                                else {
                                                    plate2.setText(group);
                                                    isTimeP1 = true;
                                                    launchCountdownP2();
                                                }
                                            }
                                        }
                                        Log.e("TestLog","Plate COunt "+plateView.getChildCount());
                                    } catch (Exception e) {
                                        Log.e("TestLog","\n\n\nMatches Error");
                                        Log.e("TestLog",e.getMessage());
                                        e.printStackTrace();
                                    }
                                    Log.e("TestLog", "New Text");
                                    Log.e("TestLog", "Debut : " + item.getValue());
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void launchCountdownP1() {
        if(countdown!=null)
            countdown.cancel();
        countdown = new CountDownTimer(2*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                plateView.removeView(plate1);
                plate1.setText("");
                countdown = null;
            }
        };
        countdown.start();
    }

    private void launchCountdownP2() {
        if(countdown2!=null)
            countdown2.cancel();
        countdown2 = new CountDownTimer(2*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                plateView.removeView(plate2);
                plate2.setText("");
                countdown2 = null;
            }
        };
        countdown2.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getY() > e2.getY()) {
            this.swipeUp();
        }
        return false;
    }


    public void swipeUp(){
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
        overridePendingTransition(R.anim.swipe_up, R.anim.staticview);
    }
}
