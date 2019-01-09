package catchem.catchem2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.FirebaseApp;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
//import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import catchem.catchem2.menu.Menu;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private GestureDetectorCompat gestureDetector;
    public static BDD uneBDD;

    private CameraSource cameraSource;
    private SurfaceView surfaceView;
    private LinearLayout plateView;
    private Button plate1,plate2;
    private LinearLayout underView;
    private Point windowSize;
    private ImageView didactView;
    private CountDownTimer countdown,countdown2;
    private LinearLayout plateDidact, stateButtons, typePlace, validPlate, infosPlate;
    private EditText validEditPlate;
    private Button retour, suivant;
    private ImageView pictView;
    private Bitmap savePict;
    private String nom, prenom;
    private ToggleButton handi, plrs, hors;

    public static final int requestPermissionID = 1;
    public int state = 0;
    private boolean isTimeP1;
    int x =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //============ Récupération de la taille de l'écran ============
        Display display = getWindowManager().getDefaultDisplay();
        windowSize = new Point();
        display.getSize(windowSize);
        //==--------== Fin de récupération de la taille de l'écran ==--------==
        //============ Récupération des views ============
        pictView = findViewById(R.id.pictView);
        validEditPlate = findViewById(R.id.validPlateEdit);
        plateView = findViewById(R.id.plaqueView);
        underView = (LinearLayout) findViewById(R.id.underView);
        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        didactView = (ImageView) findViewById(R.id.didactView);
        plateDidact = findViewById(R.id.platedidact);
        stateButtons = findViewById(R.id.statebuttons);
        infosPlate = findViewById(R.id.infosPlate);
        typePlace = findViewById(R.id.typePlace);
        validPlate = findViewById(R.id.validPlate);
        retour = findViewById(R.id.retour);
        suivant = findViewById(R.id.suivant);
        handi = findViewById(R.id.handiBtn);
        plrs = findViewById(R.id.plrsPlacesBtn);
        hors = findViewById(R.id.horsParkBtn);
        //==--------== Fin de récupération des views ==--------==
        //============ Positionnement des views ============
        pictView.setMinimumHeight(windowSize.y);
        pictView.setY(-windowSize.y/4);
        plateView.setMinimumHeight(windowSize.y/12);
        didactView.setMinimumHeight(windowSize.y/2-windowSize.y/12);
        didactView.setMaxHeight(windowSize.y/2-windowSize.y/12);
        didactView.setLayoutParams(new LinearLayout.LayoutParams(windowSize.y/2-windowSize.y/12,windowSize.y/2-windowSize.y/12));
        surfaceView.setMinimumHeight(windowSize.y);
        surfaceView.setY(-windowSize.y/2);
        underView.setMinimumHeight(windowSize.y/2);
            //==**== Elements cachés ==**==
        pictView.setVisibility(View.GONE);
        stateButtons.setVisibility(View.GONE);
        infosPlate.setVisibility(View.GONE);
        typePlace.setVisibility(View.GONE);
        validPlate.setVisibility(View.GONE);
            //==**== Fin ==**==
        //==--------== Fin de positionnement des views ==--------==
        //============ Création des autres éléments ============
        isTimeP1 = true;
        gestureDetector = new GestureDetectorCompat(this, this);
        plate1 = new Button(this);
        plate2 = new Button(this);
        uneBDD = new BDD(this);
            //==**== Paramétrage des éléments ==**==
        gestureDetector.setOnDoubleTapListener(this);
        plate1.setTextSize(TypedValue.COMPLEX_UNIT_PX,windowSize.y/32);
        plate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture(plate1);
            }
        });
        plate2.setTextSize(TypedValue.COMPLEX_UNIT_PX,windowSize.y/32);
        plate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture(plate2);
            }
        });
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state>0)state--;
                switchState();
            }
        });
        suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state<4)state++;
                switchState();
            }
        });
        handi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(handi.isChecked())
                    handi.setBackgroundColor(getResources().getColor(R.color.colorAccent, getTheme()));
                else
                    handi.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark, getTheme()));
            }
        });
        plrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(plrs.isChecked())
                    plrs.setBackgroundColor(getResources().getColor(R.color.colorAccent, getTheme()));
                else
                    plrs.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark, getTheme()));
            }
        });
        hors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hors.isChecked())
                    hors.setBackgroundColor(getResources().getColor(R.color.colorAccent, getTheme()));
                else
                    hors.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark, getTheme()));
            }
        });
            //==**== Fin ==**==
        //==--------== Fin de création des autres éléments ==--------==
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    requestPermissionID);
        }
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    requestPermissionID);
        }
        //Initialisation de la caméra
        startCameraSource();
    }


    @Override
    protected void onStart() {
        super.onStart();
        MainActivity.uneBDD.getMailSignalement(); // recuperer mail pour afficher instantanément
    }

    private void startCameraSource() {
        //Create the TextRecognizer
        final com.google.android.gms.vision.text.TextRecognizer textRecognizer = new com.google.android.gms.vision.text.TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Log.w("LogTest", "Detector dependencies not loaded yet");
        } else {
            //Initialisation de cameraSource.
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(windowSize.x, windowSize.y)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(20.0f)
                    .build();
            //Vérifier les permissions et lancer la caméra
            surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {
                        //Vérifier les permissions
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    requestPermissionID);
                            return;
                        }
                        //Lancement de la caméra
                        cameraSource.start(surfaceView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }
                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });

            //Initialiser le TextRecognizer.
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                }
                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0) {
                        plateView.post(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < items.size(); i++) {
                                    TextBlock item = items.valueAt(i);
                                    String plate = "";
                                    try {
                                        Pattern p = Pattern.compile("([A-Z]{2}[- ]+[0-9]{3}[- ]+[A-Z]{2})|([0-9]{3}[- ]+[A-Z]{3}[- ]+[0-9]{2})");
                                        Matcher m = p.matcher(item.getValue());
                                        while (m.find()) {
                                            Log.i("TestLog","====\n\nFind A GROUP "+m.group().replaceAll("[- ]+","-"));
                                            String group = m.group().replaceAll("[- ]+","-");
                                            if(plateView.indexOfChild(plate1)==-1) {
                                                plate1.setText(group);
                                                plateView.addView(plate1);
                                                launchCountdownP1();
                                            } else if(!plate1.getText().equals(group)) {
                                                plate2.setText(group);
                                                plateView.addView(plate2);
                                                launchCountdownP2();
                                            } else if(!plate1.getText().equals(group) && !plate2.getText().equals(group)) {
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
                                    } catch (Exception e) {
                                        Log.e("TestLog",e.getMessage());
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void takePicture(final Button plate) {
        cameraSource.takePicture(null, new CameraSource.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes) {
                savePict = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Log.e("LogTest","BITS "+savePict.getByteCount());
                surfaceView.setVisibility(View.GONE);
                pictView.setVisibility(View.VISIBLE);
                pictView.setImageBitmap(savePict);
                validEditPlate.setText(plate.getText());
                state = 1;
                switchState();
            }
        });
    }

    private void launchCountdownP1() {
        launchCountdownPlate(countdown, plate1);
    }

    private void launchCountdownP2() {
        launchCountdownPlate(countdown2, plate2);
    }

    private void launchCountdownPlate(CountDownTimer count, final Button plate) {
        if(count!=null)
            count.cancel();
        count = new CountDownTimer(2*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                endCountDown(plate);
            }
        };
        count.start();
    }

    public void endCountDown(Button plate) {
        plateView.removeView(plate);
        if(plate == plate1)countdown = null;
        else countdown2 = null;
    }

    public void switchState() {
        if(state<4) {
            plateDidact.setVisibility(View.GONE);
            stateButtons.setVisibility(View.GONE);
            typePlace.setVisibility(View.GONE);
            validPlate.setVisibility(View.GONE);
            surfaceView.setVisibility(View.GONE);
            infosPlate.setVisibility(View.GONE);
        }
        switch(state) {
            case 0:
                underView.setMinimumHeight(windowSize.y/2);
                plateDidact.setVisibility(View.VISIBLE);
                surfaceView.setVisibility(View.VISIBLE);
                pictView.setVisibility(View.GONE);
                break;
            case 1:
                underView.setMinimumHeight(windowSize.y/3);
                stateButtons.setVisibility(View.VISIBLE);
                validPlate.setVisibility(View.VISIBLE);
                break;
            case 2:
                majInfosPlate();
                underView.setMinimumHeight(windowSize.y/3);
                infosPlate.setVisibility(View.VISIBLE);
                break;
            case 3:
                underView.setMinimumHeight(windowSize.y/3);
                stateButtons.setVisibility(View.VISIBLE);
                typePlace.setVisibility(View.VISIBLE);
                break;
            case 4:
                //Vérifier problème cr"er pdf et meil si besoin
                String surn = ((TextView) findViewById(R.id.surname)).getText().toString(),
                        firn = ((TextView) findViewById(R.id.firstname)).getText().toString(),
                        plaque = validEditPlate.getText().toString();
                String outpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/PDF/"+plaque+".png";
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(new File(outpath));
                    savePict.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    new Pdf(surn, firn, plaque, genererListeInfraction(), nbSautLigne(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(MainActivity.this,Validation.class);
                intent.putExtra("plaque",plaque);
                intent.putExtra("nom",nom);
                intent.putExtra("prenom",prenom);
                intent.putExtra("validation", true);
                intent.putExtra("handi", handi.isChecked());
                intent.putExtra("plrs", plrs.isChecked());
                intent.putExtra("hors", hors.isChecked());
                startActivity(intent);
                break;
        }
    }

    private String genererListeInfraction() {
        String liste ="";
        if (handi.isChecked()) liste += " - Garé sur une place handicapée \n                                                          ";
        if (plrs.isChecked()) liste +=" - Garé sur plusieures places \n                                                          ";
        if (hors.isChecked()) liste += " - Garé en dehors d'une place de parking ";
        return liste;
    }
    private int nbSautLigne() {
        if (handi.isChecked()) x+=1;
        if (plrs.isChecked()) x+=1;
        if (hors.isChecked())x+=1;
        return x;
    }

    private void majInfosPlate() {
        final TextView first = ((TextView)findViewById(R.id.firstname));
        final TextView surn = ((TextView)findViewById(R.id.surname));
        ((TextView)findViewById(R.id.plateTitle)).setText("Plaque : "+validEditPlate.getText().toString().replaceAll("[- ]+","-"));
        surn.setText("Chargement...");
        first.setText("Chargement...");
        uneBDD.recherchePlaque(validEditPlate.getText().toString().replaceAll("[- ]+"," "),
                surn, first, stateButtons);
        Timer time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(stateButtons.getVisibility()==View.VISIBLE) {
                            if(first.getText().equals("")) {
                                nom = "???";
                                prenom = "???";
                            } else {
                                nom = first.getText().toString();
                                prenom = surn.getText().toString();
                            }
                            first.setText("Prénom : "+first.getText());
                            surn.setText("Nom : "+surn.getText());
                            Log.e("LogTest",nom+" ; "+prenom);
                            cancel();
                        }
                    }
                });
            }
        },10,200);
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
        if(state==0)
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
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}