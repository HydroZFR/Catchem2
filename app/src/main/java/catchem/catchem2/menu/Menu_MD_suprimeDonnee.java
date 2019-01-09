package catchem.catchem2.menu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import catchem.catchem2.MainActivity;
import catchem.catchem2.R;


public class Menu_MD_suprimeDonnee extends AppCompatActivity {

    EditText editTextNom, editTextPrenom;
    Button buttonRechercher;
    LinearLayout linearLayoutAffichage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__md_suprime_donnee);
        editTextNom = findViewById(R.id.editText_Nom);
        editTextPrenom = findViewById(R.id.editText_Prenom);
        buttonRechercher = findViewById(R.id.button_Rechercher);
        linearLayoutAffichage = findViewById(R.id.linearLayout_Affichage);
        buttonRechercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afficherLaRecherche();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        linearLayoutAffichage.removeAllViews();
        editTextNom.setText("");
        editTextPrenom.setText("");
    }

    private void afficherLaRecherche() {
        MainActivity.uneBDD.rechercheSupprimer(editTextNom.getText().toString(), editTextPrenom.getText().toString(), linearLayoutAffichage);
    }

}
