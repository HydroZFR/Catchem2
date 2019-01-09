package catchem.catchem2.menu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.io.File;

import catchem.catchem2.MainActivity;
import catchem.catchem2.R;

public class Menu extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{

    private GestureDetectorCompat gestureDetector;
    private Button modifieDonne;
    private Button modifieMail;
    private Button effacerDonnees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        gestureDetector = new GestureDetectorCompat(this, this);
        gestureDetector.setOnDoubleTapListener(this);

        modifieDonne = (Button) findViewById(R.id.ModifierDonnee);
        modifieDonne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, Menu_ModifierDonnee.class);
                startActivity(intent);
            }

        });

        modifieMail = (Button) findViewById(R.id.ModifeMail);
        modifieMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, Menu_ModifierMail.class);
                startActivity(intent);
            }

        });

        effacerDonnees = (Button) findViewById(R.id.effacerDonnes);
        effacerDonnees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder alertDialogSupressionPersonne = new AlertDialog.Builder(modifieDonne.getContext());
                alertDialogSupressionPersonne.setTitle("Suppression");
                alertDialogSupressionPersonne.setMessage("Voulez-vous vraiment supprimer tout les PDF et les images ?");
                alertDialogSupressionPersonne.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/PDF/");
                        if( path.exists() ) {
                            File[] files = path.listFiles();
                            for(int i=0; i<files.length; i++) {
                                files[i].delete();
                            }
                        }
                        // path.delete();
                        Context context = getApplicationContext();
                        CharSequence text = "Toutes les données ont été éffacées";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                    }
                });
                alertDialogSupressionPersonne.setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialogSupressionPersonne.show();

            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        MainActivity.uneBDD.getMailSignalement(); // recuperer mail pour afficher instantanément
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
        if (e1.getY() < e2.getY()) {
            try {
                this.swipeDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void swipeDown() throws InterruptedException {
        Intent intent4 = new Intent(this, MainActivity.class);
        startActivity(intent4);
        overridePendingTransition(R.anim.staticview, R.anim.swipe_down);
    }
}


