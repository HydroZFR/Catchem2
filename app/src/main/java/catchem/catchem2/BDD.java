package catchem.catchem2;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1beta1.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        this.recupererMail();
    }

    public void ajouterPersonne(String nom, String prenom, String[] immatriculation) {
        Map<String, Object> user = new HashMap<>();
        user.put(KEY_NOM, nom);
        user.put(KEY_PRENOM, prenom);
        int i = 0;
        for (String unString : immatriculation) {
            i++;
            if (unString != null){
                user.put(KEY_PLAQUE+i, unString);
            }
        }
        db.collection("users").document().set(user);
    }

    public void setMailSignalement(String pmail) {
        Map<String, Object> mail = new HashMap<>();
        mail.put(KEY_Mail, pmail);
        db.collection("mail").document("theMail").set(mail);
    }

    public String getMailSignalement() {
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

    public void rechercheModifier(final String nom, final String prenom, final LinearLayout affichage){
        db.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> listDocuments;
                listDocuments = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot unDocument : listDocuments) {
                    if (unDocument.getString("nom").equals(nom) && unDocument.getString("prenom").equals(prenom)){
                        Log.i("test quentin",""+unDocument.getString("immatriculation1"));
                        Button unButton = new Button(affichage.getContext());
                        unButton.setText(nom + " " + prenom);
                        affichage.addView(unButton);
                        popUp(unButton);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("ERREUR", "Problème recherche");
            }
        });

    }

    public void popUp(final Button unButton){
        unButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }
}
