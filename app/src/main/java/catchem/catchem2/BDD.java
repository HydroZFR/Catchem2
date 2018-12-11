package catchem.catchem2;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class BDD {
    private final static String KEY_NOM = "nom";
    private final static String KEY_PRENOM = "prenom";
    private final static String KEY_PLAQUE = "plaque";
    private final static String KEY_Mail = "mail";
    private FirebaseFirestore db;
    private String mailRecupere;

    public BDD() {
        super();
        db = FirebaseFirestore.getInstance();
      //  this.recupererMail();
    }

    public void ajouterPersonne(final MainActivity context, String nom, String prenom) {
        Map<String, Object> user = new HashMap<>();
        user.put(KEY_NOM, nom);
        user.put(KEY_PRENOM, prenom);
        db.collection("users").document().set(user);
    }

    public void setMailSignalement(String pmail){
        Map<String, Object> mail = new HashMap<>();
        mail.put(KEY_Mail, pmail);
        db.collection("mail").document().set(mail);
    }

    public String getMailSignalement(){

       return mailRecupere;
    }

    public void recupererMail(){
        db.collection("mail").document("theMail").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                mailRecupere = documentSnapshot.getString("mail");
                Log.i("test quentin", mailRecupere);
            }
        });
        Log.i("test quentin", "passage 2 " + mailRecupere);
    }
}
