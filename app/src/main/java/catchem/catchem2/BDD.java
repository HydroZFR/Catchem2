package catchem.catchem2;

import android.util.Log;

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
    private final static String KEY_PLAQUE = "immatriculation";
    private final static String KEY_Mail = "mail";
    private FirebaseFirestore db;
    private String mailRecupere;

    public BDD() {
        super();
        db = FirebaseFirestore.getInstance();
    }

    public void ajouterPersonne(String nom, String prenom, String[] immatriculation) {
        Map<String, Object> user = new HashMap<>();
        user.put(KEY_NOM, nom);
        user.put(KEY_PRENOM, prenom);
        int i = 0;
        for (String unString : immatriculation) {
            i++;
            user.put(KEY_PLAQUE+i, immatriculation[i-1]);
        }
        Log.i("test quentin", "je suis passer par la");
        db.collection("users").document().set(user);
    }

    public void setMailSignalement(String pmail) {
        Map<String, Object> mail = new HashMap<>();
        mail.put(KEY_Mail, pmail);
        db.collection("mail").document().set(mail);
    }

    public String getMailSignalement() throws InterruptedException {
        final Thread t1 = new Thread() {
            @Override
            public void run() {
                recupererMail();
            }
        };
        t1.start();
        t1.join();
        return mailRecupere;
    }

   public void recupererMail(){
        db.collection("mail").document("theMail").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                mailRecupere = documentSnapshot.getString("mail");
            }
        });
    }
}
