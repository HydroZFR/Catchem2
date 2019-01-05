package catchem.catchem2.menu;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TabHost;

import catchem.catchem2.MainActivity;
import catchem.catchem2.R;


public class Menu_ModifierDonnee extends TabActivity implements GestureDetector.OnGestureListener {

    private GestureDetectorCompat gestureDetector;
    private TabHost tabHost;
    private TabHost.TabSpec spec;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_donnee);

        gestureDetector = new GestureDetectorCompat(this, this);
        tabHost = getTabHost();
        Intent intent;

        intent = new Intent().setClass(this, Menu_MD_ajouterDonner.class);
        spec = tabHost.newTabSpec("ajouter")
                .setIndicator("Ajouter")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Menu_MD_modifierDonnee.class);
        spec = tabHost.newTabSpec("modifier")
                .setIndicator("Modifier")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Menu_MD_suprimeDonnee.class);
        spec = tabHost.newTabSpec("supprimer")
                .setIndicator("Suprimer")
                .setContent(intent);
        tabHost.addTab(spec);


        tabHost.setCurrentTab(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() < e2.getX()) {
            this.swipeLeft();
        }
        else this.swipeRight();
        return false;
    }


    public void swipeLeft(){
        int currentTab = tabHost.getCurrentTab();
        Log.e("Swipe", currentTab + "");
        if (currentTab == 1) {
            tabHost.setCurrentTabByTag("ajouter");
        }
        if (currentTab == 2) {
            tabHost.setCurrentTabByTag("modifier");
        }
    }

    public void swipeRight(){
        int currentTab = tabHost.getCurrentTab();
        if (currentTab == 0) {
            tabHost.setCurrentTabByTag("modifier");
        }
        if (currentTab == 1) {
            tabHost.setCurrentTabByTag("supprimer");
        }
    }
}
