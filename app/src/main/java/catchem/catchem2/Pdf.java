package catchem.catchem2;

import android.app.Activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;


public class Pdf extends Activity {


    boolean repDejaExistant = false;
    BaseColor couleurIUT = new BaseColor(226, 0, 21); // or red, green, blue, alpha
    BaseColor black = new BaseColor(0, 0, 0); // or red, green, blue, alpha
    BaseColor grey = new BaseColor(127, 143, 166, 100); // or red, green, blue, alpha


    private static String FILE = "c:/temp/FirstPdf.pdf";
    private static Font bigBold = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static Font blackFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK);
    private static Font blueFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLUE);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.RED);

    MainActivity context;

    public Pdf(String nom, String prenom, String plaque, String infractions, int nbSautLigne, MainActivity context) throws IOException, DocumentException {


        this.context = context;

        String outpath1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM";
        File sdcard = new File(outpath1);
        String[] contenuSdCard = sdcard.list();
        int i = 0;
        while (i < contenuSdCard.length && !repDejaExistant) {
            if (contenuSdCard[i].compareTo("PDF") == 0)
                repDejaExistant = true;
            i++;
        }
        File rep = new File(outpath1 + "/PDF");
        if (!repDejaExistant)
            rep.mkdirs();

        Document doc = new Document(PageSize.A4);

        String outpathPDF = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/PDF/" + plaque + ".pdf";

        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(outpathPDF));

        doc.open();



        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logoentete);

        //Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 3, outputStream);
        Image logo = Image.getInstance(outputStream.toByteArray());
        logo.scalePercent(30);
        logo.setAbsolutePosition(0, 750);
        doc.add(logo);

        PdfContentByte contentByte = writer.getDirectContent();
        Rectangle rectangle = new Rectangle(0, doc.getPageSize().getHeight() - 100, doc.getPageSize().getWidth(), doc.getPageSize().getHeight() - 102);
        rectangle.setBorder(Rectangle.BOX);
        contentByte.setColorStroke(grey);
        rectangle.setBorderWidth(2);
        contentByte.rectangle(rectangle);


        doc.add(new Paragraph("\n\n\n\nObjet : Stationnement encombrant.", bigBold));
        doc.add(new Paragraph("\n\nBonjour, \n\n Je vous informe qu'une voiture a été signalée comme mal garée ou dérangeante. Voici les informations relatives à ce véhicule :", blackFont));
        doc.add(new Paragraph(("\n         - nom du propriétaire             : " + nom), blackFont));
        doc.add(new Paragraph(("\n         - prénom du propriétaire        : " + prenom), blackFont));
        doc.add(new Paragraph(("\n         - Plaque d'immatriculation     : " + plaque), blackFont));
        doc.add(new Paragraph("\n         - Date de l'infraction               : " + recupererDate(), blackFont));
        doc.add(new Paragraph("\n         - Liste de ou des infractions  : " + infractions, blackFont));
        doc.add(new Paragraph("\nMerci de faire le nécessaire afin de rétablir l'ordre au sein du parking.", blackFont));
        doc.add(new Paragraph("Cordialement.", blackFont));

            Paragraph paragraph1 = new Paragraph("");
            paragraph1 = new Paragraph("\n\n\n\n\n\n\n\n\n  \n\n\n Ce message est un message automatique généré par l'application Catch'em.\n Merci de ne pas y répondre.", smallBold);
            paragraph1.setAlignment(Element.ALIGN_CENTER);
            doc.add(paragraph1);





        String outpathPhoto = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/PDF/" + plaque + ".png";

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap photoDeLaVoiture = BitmapFactory.decodeFile(outpathPhoto, options);
        //selected_photo.setImageBitmap(bitmap);
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        photoDeLaVoiture.compress(Bitmap.CompressFormat.PNG, 100, outputStream2);
        Image photoVoiture = Image.getInstance(outputStream2.toByteArray());
        //photoVoiture.setAbsolutePosition(doc.getPageSize().getWidth() / 2, 200);
        photoVoiture.setAbsolutePosition(230, 120);
        photoVoiture.setAlignment(Element.ALIGN_CENTER);
        photoVoiture.scalePercent(4);
        doc.add(photoVoiture);


        Rectangle rectangle3 = new Rectangle(0, 25, doc.getPageSize().getWidth(), 102);
        rectangle3.setBackgroundColor(grey);
        contentByte.rectangle(rectangle3);

        doc.close();


    }

    public String recupererDate() {
        Date d = new Date();
        return " " + d.getDate() + "/" + (d.getMonth() + 1) + "/" + (d.getYear() + 1900);

    }


}