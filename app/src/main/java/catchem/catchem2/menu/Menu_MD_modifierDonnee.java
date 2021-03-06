package catchem.catchem2.menu;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.firestore.DocumentSnapshot;

import catchem.catchem2.MainActivity;
import catchem.catchem2.R;


public class Menu_MD_modifierDonnee extends AppCompatActivity {

    private Button button_Rechercher;
    private EditText editText_Nom;
    private EditText editText_Prenom;
    private LinearLayout linearLayout_Affichage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__md_modifier_donnee);
        button_Rechercher = (Button) findViewById(R.id.rechercher);
        button_Rechercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afficherLaRecherche();
            }
        });
        editText_Nom = (EditText) findViewById(R.id.nom);
        editText_Prenom = (EditText) findViewById(R.id.prenom);
        linearLayout_Affichage = (LinearLayout) findViewById(R.id.layoutAffichage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        linearLayout_Affichage.removeAllViews();
        editText_Prenom.setText("");
        editText_Nom.setText("");
    }

    public void afficherLaRecherche(){
        MainActivity.uneBDD.rechercheModifier(editText_Nom.getText().toString(), editText_Prenom.getText().toString(), linearLayout_Affichage);
    }

}
