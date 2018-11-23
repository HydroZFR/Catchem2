package catchem.catchem2;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;


public class Menu_ModifierDonnee extends TabActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_donnee);

        Resources res = getResources();
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec; // reusable tabspec for each tab
        Intent intent;

        intent = new Intent().setClass(this, Menu_MD_ajouterDonner.class);
        spec = tabHost.newTabSpec("buildings")
                .setIndicator("Ajouter")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Menu_MD_modifierDonnee.class);
        spec = tabHost.newTabSpec("research")
                .setIndicator("Modifier")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Menu_MD_suprimeDonnee.class);
        spec = tabHost.newTabSpec("research")
                .setIndicator("Suprimer")
                .setContent(intent);
        tabHost.addTab(spec);


        tabHost.setCurrentTab(0);
    }
}
