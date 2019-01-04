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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

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
        affichage.removeAllViews();
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
                        popUpModifier(unButton, affichage, unDocument);
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

    public void popUpModifier(Button unButton, final LinearLayout affichage, final DocumentSnapshot unDocument) {
        final Context context = affichage.getContext();
        final Dialog popUp = new Dialog(context);
        unButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.setContentView(R.layout.popup_modifier_donnee);
                final EditText nom = (EditText) popUp.findViewById(R.id.editTextNomPopup);
                nom.setText(unDocument.getString("nom"));
                final EditText prenom = (EditText) popUp.findViewById(R.id.editTextPrenomPopup);
                prenom.setText(unDocument.getString("prenom"));
                final EditText imma1 = (EditText) popUp.findViewById(R.id.editTextImma1Popup);
                imma1.setText(unDocument.getString("immatriculation1"));
                final EditText imma2 = (EditText) popUp.findViewById(R.id.editTextImma2Popup);
                imma2.setText(unDocument.getString("immatriculation2"));
                popUp.dismiss();
                Button buttonValider = popUp.findViewById(R.id.buttonValider);
                buttonValider.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean champsCorrectes = true;
                        if (nom.getText().toString().equals("")) {
                            nom.setError("Veuillez remplir ce champ");
                            champsCorrectes = false;
                        }
                        if (prenom.getText().toString().equals("")) {
                            prenom.setError("Veuillez remplir ce champ");
                            champsCorrectes = false;
                        }
                        if (!Utilitaire.syntaxImmatriculation(imma1) || imma1.getText().toString().equals("")) {
                            imma1.setError("Veuillez remplir ce champ ou le corriger");
                            champsCorrectes = false;
                        }
                        if (!imma2.getText().toString().equals("")) {
                            if (!Utilitaire.syntaxImmatriculation(imma2)) {
                                imma2.setError("Veuillez remplir correctement ce champ");
                                champsCorrectes = false;
                            }
                        }
                        if (champsCorrectes) {
                            update(unDocument.getReference(), KEY_NOM, nom.getText().toString());
                            update(unDocument.getReference(), KEY_PRENOM, prenom.getText().toString());
                            update(unDocument.getReference(), KEY_PLAQUE + "1", imma1.getText().toString());
                            update(unDocument.getReference(), KEY_PLAQUE + "2", imma2.getText().toString());
                            popUp.cancel();
                            rechercheModifier(nom.getText().toString(), prenom.getText().toString(), affichage);
                            Toast.makeText(context, "Données sauvegardées", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Données non sauvegardées", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                popUp.show();
            }
        });
    }

    public void update(DocumentReference documentRef, String key, String data) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, data);
        documentRef.set(map, SetOptions.merge());
    }

}
