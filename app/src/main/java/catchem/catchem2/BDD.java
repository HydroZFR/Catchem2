package catchem.catchem2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BDD {
    private final static String KEY_NOM = "nom";
    private final static String KEY_PRENOM = "prenom";
    private final static String KEY_PLAQUE = "plaque";
    private FirebaseFirestore db;

    public BDD() {
        super();
        db = FirebaseFirestore.getInstance();
    }

    public void ajouterPersonne(final MainActivity context, String nom, String prenom) {
        Map<String, Object> user = new HashMap<>();
        user.put(KEY_NOM, nom);
        user.put(KEY_PRENOM, prenom);

        db.collection("users").document().set(user);
    }

    public void setMailSignalement(String mail){

    }
}
