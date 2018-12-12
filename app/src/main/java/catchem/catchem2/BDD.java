package catchem.catchem2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import catchem.catchem2.menu.Menu;
import catchem.catchem2.menu.Menu_MD_modifierDonnee;
import catchem.catchem2.menu.Menu_ModifierDonnee;


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
            if (unString != null) {
                user.put(KEY_PLAQUE + i, unString);
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
        db.collection("mail").document("theMail").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    mailRecupere = documentSnapshot.getString(KEY_Mail);
                }
            }
        });
        return mailRecupere;
    }

    public void rechercheModifier(final String nom, final String prenom, final LinearLayout affichage) {
        db.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> listDocuments;
                listDocuments = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot unDocument : listDocuments) {
                    if (unDocument.getString("nom").equals(nom) && unDocument.getString("prenom").equals(prenom)) {
                        Log.i("test quentin", "" + unDocument.getString("immatriculation1"));
                        Button unButton = new Button(affichage.getContext());
                        unButton.setText(nom + " " + prenom);
                        affichage.addView(unButton);
                        popUp(unButton, affichage.getContext(), unDocument);
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



    public static void popUp(Button unButton, final Context context, final DocumentSnapshot unDocument) {
        final Dialog popUp = new Dialog(context);
        unButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.setContentView(R.layout.popup_modifier_donnee);
                EditText nom = (EditText) popUp.findViewById(R.id.EdittextNomPopup);
                nom.setText(unDocument.getString("nom"));
                EditText prenom = (EditText) popUp.findViewById(R.id.editTextPrenomPopup);
                prenom.setText(unDocument.getString("prenom"));
                EditText imma1 = (EditText) popUp.findViewById(R.id.editTextImma1Popup);
                imma1.setText(unDocument.getString("immatriculation1"));
                popUp.dismiss();
                popUp.show();
            }
        });
    }


}
