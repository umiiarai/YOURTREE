package com.example.yourtree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class TEXTViewer extends AppCompatActivity {

    private static final String TAG = "webnautes";
    EditText mMemoEdit;
    Button b_save;
    ImageView imageView, imageview;
    private String Texttitle;
    private RelativeLayout main;
    private File root;
    private AssetManager assetManager;
    private PDFont font;
    private String gettext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textviewer);
        mMemoEdit = (EditText) findViewById(R.id.memo_edit);
        b_save = (Button) findViewById(R.id.save_btn);
        imageView = (ImageView) findViewById(R.id.imageView);
        main = (RelativeLayout) findViewById(R.id.main);

        main.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                hideKeyboard();
                return false;
            }
        });

        // OCR 전달 받기
        gettext = getIntent().getStringExtra("string");
        if (gettext != null && !gettext.isEmpty()) {
            mMemoEdit.setText(gettext);
        }

        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 텍스트 비었는지 확인 후 저장
                if (mMemoEdit.toString() != null) {
                    final EditText edittext = new EditText(TEXTViewer.this);
                    AlertDialog.Builder dlg = new AlertDialog.Builder(TEXTViewer.this);
                    AlertDialog ad = dlg.create();
                    ad.setTitle("TEXT 제목");
                    ad.setView(edittext);
                    ad.setButton(DialogInterface.BUTTON_NEUTRAL, "입력", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Texttitle = edittext.getText().toString();
                            if(Texttitle == null) {
                                Toast.makeText(TEXTViewer.this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), Texttitle, Toast.LENGTH_LONG).show();
                                ad.dismiss();
                                final SaveTask saveTask = new SaveTask();
                                saveTask.execute();
                            }
                        }
                    });
                    ad.show();

                }
                else {
                    Toast.makeText(TEXTViewer.this, "저장할 텍스트가 없습니다.", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    void hideKeyboard()
    {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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

        if (ContextCompat.checkSelfPermission(TEXTViewer.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(TEXTViewer.this,
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

            int text_width = 470;
            int text_left = 70;

            String textN = mMemoEdit.getText().toString();
            int fontSize = 20;
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
            contentStream.newLineAtOffset(text_left, 700);

            for (String line: lines)
            {
                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -leading);
            }
            contentStream.endText();
            contentStream.close();

            String path = root.getAbsolutePath() + "/" + Texttitle +".pdf";

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

            Toast.makeText(TEXTViewer.this, "잠시 기다리세요.", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);

            Toast.makeText(TEXTViewer.this, path+"에 PDF 파일로 저장했습니다.", Toast.LENGTH_LONG).show();
        }

    }

}