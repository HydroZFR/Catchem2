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

    public static String clearSyntax(String syntaxAClear) {
        String syntaxClear = syntaxAClear.toLowerCase();
        syntaxClear = syntaxClear.replaceAll("à","a");
        syntaxClear = syntaxClear.replaceAll("â","a");
        syntaxClear = syntaxClear.replaceAll("ä","a");
        syntaxClear = syntaxClear.replaceAll("ç","c");
        syntaxClear = syntaxClear.replaceAll("é","e");
        syntaxClear = syntaxClear.replaceAll("è","e");
        syntaxClear = syntaxClear.replaceAll("ê","e");
        syntaxClear = syntaxClear.replaceAll("ë","e");
        syntaxClear = syntaxClear.replaceAll("î","i");
        syntaxClear = syntaxClear.replaceAll("ï","i");
        syntaxClear = syntaxClear.replaceAll("ô","o");
        syntaxClear = syntaxClear.replaceAll("ö","o");
        syntaxClear = syntaxClear.replaceAll("ù","u");
        syntaxClear = syntaxClear.replaceAll("û","u");
        syntaxClear = syntaxClear.replaceAll("ü","u");
        syntaxClear = syntaxClear.replaceAll("ÿ","y");
        syntaxClear = syntaxClear.replaceAll("ñ","n");
        return syntaxClear;
    }
}
