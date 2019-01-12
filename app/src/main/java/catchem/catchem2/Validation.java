package catchem.catchem2;

import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;

import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Validation extends AppCompatActivity {

    String plaque, nom, prenom, mailG;
    boolean handi,plrs,hors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);
        ImageView view = findViewById(R.id.viewvalid);
        //============ Récupération de la taille de l'écran ============
        Display display = getWindowManager().getDefaultDisplay();
        Point windowSize = new Point();
        display.getSize(windowSize);
        //==--------== Fin de récupération de la taille de l'écran ==--------==
        TextView valid = findViewById(R.id.isvalid);
        plaque = getIntent().getStringExtra("plaque");
        nom = getIntent().getStringExtra("nom");
        prenom = getIntent().getStringExtra("prenom");
        handi = getIntent().getBooleanExtra("handi",false);
        plrs = getIntent().getBooleanExtra("plrs",false);
        hors = getIntent().getBooleanExtra("hors",false);
        mailG = MainActivity.uneBDD.getMailSignalement();
        Log.e("LogTest","n"+nom);
        Log.e("LogTest","p"+prenom);
        Log.e("LogTest","p"+plaque);
        if(handi || plrs || hors || nom.equals("???")) {
            view.setImageResource(R.drawable.invalide);
            valid.setText("La personne est mal garée, un mail\nva être envoyé, veuillez patienter...");
        } else {
            valid.setText("La personne est bien garée\nretour au menu...");
        }
        new CountDownTimer(500,1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if(handi || plrs || hors || nom.equals("???")) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    genererPDF();
                                    envoyerMail(nom,prenom,plaque);

                                }
                            });
                                cancel();
                        }
                    },200,200);
                }
                else {
                    new CountDownTimer(2000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }
                        @Override
                        public void onFinish() {
                            Intent i = new Intent(Validation.this,MainActivity.class);
                            startActivity(i);
                        }
                    }.start();
                }
            }
        }.start();
    }

    private void genererPDF() {
        try {
            new Pdf(nom, prenom, plaque, genererListeInfraction(), nbSautLigne(), this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Validation.this,MainActivity.class);
        startActivity(i);
    }

    private void envoyerMail(String nom, String prenom, String plaque) {
        String filename = "DCIM/PDF/"+plaque+".pdf";
        File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
        // Uri path = Uri.fromFile(filelocation);


        Uri path = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".my.package.name.provider", filelocation);



        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

// set the type to 'email'
        emailIntent.setType("vnd.android.cursor.dir/email");
        String mail[] = getMails();


        emailIntent.putExtra(Intent.EXTRA_EMAIL, mail);
// the attachment
        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
// the mail subject
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Véhicule mal garé.");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Ceci est un mail envoyé automatiquement depuis l'application Catch'em. \n Merci de ne pas y repondre.\n\n\n\n Equipe Catch'em.");

        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private String[] getMails() {
        String[] m = new String[2];
        if(!nom.equals("???")){
            String emailEtudiant = prenom+"."+nom+".etu@univ-lemans.fr";
            Log.i("azerty", emailEtudiant);
            m[0]=mailG;m[1]=emailEtudiant;
        }else{
            m[0]=mailG;
        }
        return m;
    }

    private String genererListeInfraction() {
        String liste ="";
        if (handi) liste += " - Garé sur une place handicapée \n                                                          ";
        if (plrs) liste +=" - Garé sur plusieures places \n                                                          ";
        if (hors) liste += " - Garé en dehors d'une place de parking ";
        return liste;
    }

    private int nbSautLigne() {
        int x=0;
        if (handi) x+=1;
        if (plrs) x+=1;
        if (hors)x+=1;
        return x;
    }
}