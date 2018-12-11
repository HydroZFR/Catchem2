package catchem.catchem2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import catchem.catchem2.menu.Menu;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private GestureDetectorCompat gestureDetector;
    private EditText editTextNom;
    private EditText editTextPrenom;
    private Button buttonEnvoyer;
//    private final static String KEY_NOM = "nom";
//    private final static String KEY_PRENOM = "prenom";
//    private final static String KEY_PLAQUE = "plaque";
//    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static BDD uneBDD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextNom = (EditText) findViewById(R.id.edit_text_nom);
        editTextPrenom = (EditText) findViewById(R.id.edit_text_prenom);
        buttonEnvoyer = (Button) findViewById(R.id.button_envoyer);

        gestureDetector = new GestureDetectorCompat(this, this);
        gestureDetector.setOnDoubleTapListener(this);
        uneBDD = new BDD();
    }

    public void envoyer(View v) {
        String nom = editTextNom.getText().toString();
        String prenom = editTextPrenom.getText().toString();
        String[] immatriculation = new String[2];
        immatriculation[0]= "Ab 275 PM";
        immatriculation[1]= "HH 999 HH";
        //uneBDD.ajouterPersonne("Pineau", "Quentin", immatriculation);
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
