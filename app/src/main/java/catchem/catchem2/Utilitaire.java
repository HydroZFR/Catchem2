package catchem.catchem2;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilitaire {

    public static boolean syntaxImmatriculation(EditText immatriculation) {
        boolean syntaxCorrect = true;
        Pattern p = Pattern.compile("([A-Z]{2}[- ]+[0-9]{3}[- ]+[A-Z]{2})|([0-9]{3}[- ]+[A-Z]{3}[- ]+[0-9]{2})");
        Matcher m = p.matcher(immatriculation.getText().toString());
        if (!m.matches())
            syntaxCorrect = false;
        return syntaxCorrect;
    }
}
