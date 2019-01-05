package catchem.catchem2.PDF;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import catchem.catchem2.R;

public class Pdf extends Activity {
    boolean repDejaExistant = false;
    BaseColor couleurIUT = new BaseColor(226, 0, 21); // or red, green, blue, alpha
    BaseColor black = new BaseColor(0, 0, 0); // or red, green, blue, alpha
    BaseColor grey = new BaseColor(127, 143, 166,100); // or red, green, blue, alpha

    int numero = 0;

    private static String FILE = "c:/temp/FirstPdf.pdf";
    private static Font bigBold = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static Font blackFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK);
    private static Font blueFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLUE);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.RED);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String outpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM";
        File sdcard = new File(outpath);
        String[] contenuSdCard = sdcard.list();
        int i = 0;
        while (i < contenuSdCard.length && !repDejaExistant) {
            if (contenuSdCard[i].compareTo("PDF") == 0)
                repDejaExistant = true;
            i++;
        }
        File rep = new File(outpath + "/PDF");
        if (!repDejaExistant)
            rep.mkdirs();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    public void lancementPDF(View view) throws IOException, DocumentException {
        /*EditText nomView = (EditText) findViewById(R.id.name_input);
        EditText prenomView = (EditText) findViewById(R.id.prenom_input);
        EditText plaqueView = (EditText) findViewById(R.id.plaque_input);
        String nom = nomView.getText().toString();
        String prenom = prenomView.getText().toString();
        String plaque = plaqueView.getText().toString();*/

        InputStream inputStream = getAssets().open("vehicule.jpg");
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        Image imageVehicule = Image.getInstance(outputStream.toByteArray());


        InputStream inputStream2 = getAssets().open("maps.jpg");
        Bitmap maps = BitmapFactory.decodeStream(inputStream2);
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        maps.compress(Bitmap.CompressFormat.PNG, 100, outputStream2);
        Image localisationMaps = Image.getInstance(outputStream2.toByteArray());


        //createPDF(nom, prenom, plaque, imageVehicule, localisationMaps);
        createPDF("le nom ici", "le prenom ici", "la plaque ici", imageVehicule, localisationMaps);
    }

    public void createPDF(String nom, String prenom, String plaque, Image imageVehicule, Image localisationMaps) throws IOException, DocumentException {
        Document doc = new Document();

        String outpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/PDF/" + plaque+recupererDate() + ".pdf";
       // String outpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/PDF/" + numero + ".pdf";
        numero++;

        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(outpath));
        doc.open();


        InputStream inputStream = getAssets().open("deuxLogos.jpg");
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        Image deuxLogos = Image.getInstance(outputStream.toByteArray());
        deuxLogos.setAbsolutePosition(0, 750);
        doc.add(deuxLogos);

        PdfContentByte contentByte = writer.getDirectContent();
        Rectangle rectangle = new Rectangle(0, doc.getPageSize().getHeight() - 100, doc.getPageSize().getWidth(), doc.getPageSize().getHeight() - 102);
        rectangle.setBorder(Rectangle.BOX);
        contentByte.setColorStroke(grey);
        rectangle.setBorderWidth(2);
        contentByte.rectangle(rectangle);





        doc.add(new Paragraph("\n\n\n\nObjet : Stationnement encombrant.", bigBold));
        doc.add(new Paragraph("\n\nBonjour, \n\n Je vous informe qu'une voiture à été signalée comme mal garée ou dérangeante. Voici les informations relative à ce véhicule :", blackFont));
        doc.add(new Paragraph(("\n         - nom du propriétaire             : " + nom), blackFont));
        doc.add(new Paragraph(("\n         - prénom du propriétaire        : " + prenom), blackFont));
        doc.add(new Paragraph(("\n         - Plaque d'immatriculation     : " + plaque), blackFont));
        doc.add(new Paragraph("\n         - Date de l'infraction               : " + recupererDate(), blackFont));
        doc.add(new Paragraph("\n\nMerci de faire le nécéssaire afin de rétablir l'ordre au sein du parking.", blackFont));
        doc.add(new Paragraph("Cordialement.", blackFont));
        Paragraph paragraph1 = new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n Ce message est un message automatique généré par l'application Catch'em.\n Merci de ne pas y répondre.", smallBold);
        paragraph1.setAlignment(Element.ALIGN_CENTER );
        doc.add(paragraph1);
        imageVehicule.setAbsolutePosition(30, 180);
        imageVehicule.scalePercent(70);
        doc.add(imageVehicule);

        localisationMaps.setAbsolutePosition(doc.getPageSize().getWidth() / 2, 180);
        localisationMaps.setAlignment(Element.ALIGN_RIGHT);
        localisationMaps.scalePercent(70);
        doc.add(localisationMaps);


        Rectangle rectangle3 = new Rectangle(0, 25, doc.getPageSize().getWidth(), 82);
        rectangle3.setBackgroundColor(grey);
        contentByte.rectangle(rectangle3);

        doc.close();


    }

    public String recupererDate() {
        Date d = new Date();
        return "_"+d.getDate() + "_" + (d.getMonth() + 1) + "_" + (d.getYear() + 1900);

    }

}