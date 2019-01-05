package catchem.catchem2.mail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import java.io.File;

public class Mail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_home);


        try {
            sendMail();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void sendMail() {
        String filename = "DCIM/PDF/1.pdf";
        File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
        Uri path = Uri.fromFile(filelocation);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
// set the type to 'email'
        emailIntent.setType("vnd.android.cursor.dir/email");
        String to[] = {"evan.delaunay1@gmail.com"};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
// the attachment
        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
// the mail subject
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Véhicule mal garé.");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Ceci est un mail envoyé automatiquement depuis l'application Catch'em. \n Merci de ne pas y repondre.\n\n\n\n Equipe Catch'em.");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

}
