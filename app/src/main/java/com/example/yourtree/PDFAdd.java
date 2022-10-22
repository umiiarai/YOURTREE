package com.example.yourtree;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tom_roush.pdfbox.android.PDFBoxResourceLoader;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;
import com.tom_roush.pdfbox.pdmodel.common.PDRectangle;
import com.tom_roush.pdfbox.pdmodel.font.PDFont;
import com.tom_roush.pdfbox.pdmodel.font.PDType0Font;
import com.tom_roush.pdfbox.pdmodel.graphics.image.LosslessFactory;
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PDFAdd extends AppCompatActivity {

    private static final String TAG = "webnautes" ;
    private EditText text;
    private File root;
    private AssetManager assetManager;
    private ImageView imageView;
    private PDFont font;
    private String Pdftitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfadd);

        imageView = (ImageView)findViewById(R.id.imageview);
        text = (EditText)findViewById(R.id.text);

        // 갤러리에서 사진 전달 받기
        String getbitmap = getIntent().getStringExtra("image");
        if (getbitmap != null && !getbitmap.isEmpty()){
            byte[] encodeByte = Base64.decode(getbitmap, Base64.DEFAULT);
            Bitmap setbitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            imageView.setImageBitmap(setbitmap);
        }
        //imageView.setImageResource(R.drawable.test);

        Button button_save = (Button) findViewById(R.id.button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final EditText edittext = new EditText(PDFAdd.this);
                AlertDialog.Builder dlg = new AlertDialog.Builder(PDFAdd.this);
                AlertDialog ad = dlg.create();
                ad.setTitle("PDF 제목");
                ad.setView(edittext);
                ad.setButton(DialogInterface.BUTTON_NEUTRAL, "입력", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Pdftitle = edittext.getText().toString();
                        if(Pdftitle == null) {
                            Toast.makeText(PDFAdd.this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), Pdftitle, Toast.LENGTH_LONG).show();
                            ad.dismiss();
                            final SaveTask saveTask = new SaveTask();
                            saveTask.execute();
                        }
                    }
                });
                ad.show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        setup();
    }


    private void setup() {

        PDFBoxResourceLoader.init(getApplicationContext());
        root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        assetManager = getAssets();

        if (ContextCompat.checkSelfPermission(PDFAdd.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(PDFAdd.this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }



    public String createPdf() {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);


        try{
            font = PDType0Font.load(document, assetManager.open("NanumBarunGothicLight.ttf"));
        }
        catch (IOException e){
            Log.e(TAG, "폰트를 읽을 수 없습니다.", e);
        }

        PDPageContentStream contentStream;

        try {
            contentStream = new PDPageContentStream( document, page, true, true);

            Drawable drawable = imageView.getDrawable();
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

            int image_width = bitmap.getWidth();
            int image_height = bitmap.getHeight();
            int A4_width = (int) PDRectangle.A4.getWidth();
            int A4_height = (int) PDRectangle.A4.getHeight();

            float scale = (float) (A4_width/(float)image_width*0.8);

            int image_w = (int) (bitmap.getWidth() * scale);
            int image_h = (int) (bitmap.getHeight() * scale);

            Bitmap resized = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
            PDImageXObject pdImage = LosslessFactory.createFromImage(document, resized);


            float x_pos = page.getCropBox().getWidth();
            float y_pos = page.getCropBox().getHeight();

            float x_adjusted = (float) (( x_pos - image_w ) * 0.5 + page.getCropBox().getLowerLeftX());
            float y_adjusted = (float) ((y_pos - image_h) * 0.9 + page.getCropBox().getLowerLeftY());

            contentStream.drawImage(pdImage, x_adjusted, y_adjusted, image_w, image_h);


            int text_width = 470;
            int text_left = 70;

            String textN = text.getText().toString();
            int fontSize = 17;
            float leading = 1.5f * fontSize;

            List<String> lines = new ArrayList<String>();
            int lastSpace = -1;
            for (String text : textN.split("\n"))
            {
                while (text.length() > 0) {
                    int spaceIndex = text.indexOf(' ', lastSpace + 1);
                    if (spaceIndex < 0)
                        spaceIndex = text.length();
                    String subString = text.substring(0, spaceIndex);
                    float size = fontSize * font.getStringWidth(subString) / 1000;
                    if (size > text_width) {
                        if (lastSpace < 0)
                            lastSpace = spaceIndex;
                        subString = text.substring(0, lastSpace);
                        lines.add(subString);
                        text = text.substring(lastSpace).trim();
                        lastSpace = -1;
                    } else if (spaceIndex == text.length()) {
                        lines.add(text);
                        text = "";
                    } else {
                        lastSpace = spaceIndex;
                    }
                }
            }

            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset(text_left, y_adjusted-20);

            for (String line: lines)
            {
                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -leading);
            }
            contentStream.endText();
            contentStream.close();

            String path = root.getAbsolutePath() + "/" + Pdftitle +".pdf";

            document.save(path);
            document.close();

            return path;

        } catch (IOException e) {
            Log.e(TAG, "Exception thrown while creating PDF", e);
        }

        return "error";
    }


    class SaveTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            String path = createPdf();
            return path;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Toast.makeText(PDFAdd.this, "잠시 기다리세요.", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);

            Toast.makeText(PDFAdd.this, path+"에 PDF 파일로 저장했습니다.", Toast.LENGTH_LONG).show();
        }

    }
}