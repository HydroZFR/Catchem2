package catchem.catchem2.menu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import catchem.catchem2.MainActivity;
import catchem.catchem2.R;
import catchem.catchem2.Utilitaire;

public class Menu_MD_ajouterDonner extends AppCompatActivity {

    private Button ajouteImma;
    private Button valider;
    private LinearLayout linearyLayoutImma;
    private int nbLigne = 0;
    private EditText nom;
    private EditText prenom;
    private EditText immatriculation1;
    private EditText nouvelleImma = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_md_ajouter_donner);
        this.nom = (EditText) findViewById(R.id.nom);
        this.prenom = (EditText) findViewById(R.id.prenom);
        this.immatriculation1 = (EditText) findViewById(R.id.immatriculation);
        this.ajouteImma = (Button) findViewById(R.id.ajouteImma);
        this.ajouteImma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ajouterImmatriculation();
            }
        });
        this.valider = (Button) findViewById(R.id.valider);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ajouter();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.nom.setText("");
        this.prenom.setText("");
        this.immatriculation1.setText("");
        if (this.nouvelleImma != null) {
            linearyLayoutImma.removeView(this.nouvelleImma);
            nbLigne = 0;
        }
    }

    public void ajouterImmatriculation() {
        if (nbLigne == 0) {
            nouvelleImma = new EditText(this);
            if (nouvelleImma.getParent() != null) {
                nouvelleImma.getParent().clearChildFocus(nouvelleImma);
                ViewGroup parentViewGroup = (ViewGroup) nouvelleImma.getParent();
                if (parentViewGroup != null) {
                    parentViewGroup.removeView(nouvelleImma);
                }
            }
            linearyLayoutImma = (LinearLayout) findViewById(R.id.linearyLayoutImma);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            nouvelleImma.setLayoutParams(p);
            nouvelleImma.setHint("ex : HH-888-HH");
            nouvelleImma.setId(nbLigne);
            linearyLayoutImma.addView(nouvelleImma);
        }
        nbLigne++;
    }

    public void ajouter() {
        if (this.verifChamp() == true) {
            String nom = this.nom.getText().toString();
            String prenom = this.prenom.getText().toString();
            String immatriculation = this.immatriculation1.getText().toString();
            Log.i("test", "nom = " + nom);
            Log.i("test", "prenom = " + prenom);
            Log.i("test", "Immatriculation = " + immatriculation);
            String[] tabImmatriculation = new String[2];
            tabImmatriculation[0] = immatriculation;
            if (this.nouvelleImma != null ){ //On vérifie si la personne a bien créer une deuxième imatriculation
                if (this.nouvelleImma.getText().toString() == "") {
                    String immatriculation2 = this.nouvelleImma.getText().toString();
                    Log.i("test", "Immatriculation2 = " + immatriculation2);
                    tabImmatriculation[1] = immatriculation2;
                }
            }
            MainActivity.uneBDD.ajouterPersonne(nom, prenom, tabImmatriculation);
            Toast.makeText(this, "Enregsitré", Toast.LENGTH_SHORT).show();
            this.nom.setText("");
            this.prenom.setText("");
            this.immatriculation1.setText("");
            if (this.nouvelleImma != null) this.nouvelleImma.setText("");
        } else
            Toast.makeText(this, "Champ incorrecte", Toast.LENGTH_SHORT).show();
    }

    public boolean verifChamp() {
        boolean champRemplie = true;
        if (this.nom.getText().toString().equals("")) {
            this.nom.setError("Veuillez remplir ce champ");
            champRemplie = false;
        }
        if (this.prenom.getText().toString().equals("")) {
            this.prenom.setError("Veuillez remplir ce champ");
            champRemplie = false;
        }
        if (this.immatriculation1.getText().toString().equals("")) {
            this.immatriculation1.setError("Veuillez remplir ce champ");
            champRemplie = false;
        }
        if (!this.immatriculation1.getText().toString().equals("")) {
            if (!Utilitaire.syntaxImmatriculation(this.immatriculation1)) {
                this.immatriculation1.setError("Syntaxe incorrecte");
                champRemplie = false;
            }
        }
        if (this.nouvelleImma != null) {
            if(!this.nouvelleImma.getText().toString().equals("")) {
                if (!Utilitaire.syntaxImmatriculation(this.nouvelleImma)) {
                    this.nouvelleImma.setError("Syntaxe incorrecte");
                    champRemplie = false;
                }
            }
        }
        return champRemplie;
    }

}
