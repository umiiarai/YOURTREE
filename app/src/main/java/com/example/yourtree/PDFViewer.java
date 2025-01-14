package com.example.yourtree;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PDFViewer extends AppCompatActivity {

    private final int READ_REQUEST_CODE = 200;
    private final int ADD_PDF = 100;
    public  String result = null;
    Button Viewbtn;
    Button Addbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);

        //openPdf("%ED%85%8C%EC%8A%A4%ED%8A%B8.pdf");
        Addbtn = findViewById(R.id.Addbtn);
        Viewbtn = findViewById(R.id.Viewbtn);
        Viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("application/pdf");
                startActivityForResult(intent, READ_REQUEST_CODE);
            }
        });

        Addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, ADD_PDF);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (data != null) {
                Uri uri = data.getData();
                String fileName = getFileName(uri);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/pdf/" + fileName);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(getBaseContext(), "com.example.yourtree", file);
                intent.setDataAndType(contentUri, "application/pdf");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivity(intent);
                //getFileName(uri);
                //openPdf(result);
            }
        }
        else if(requestCode == ADD_PDF && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri image = data.getData();
            Intent intent = new Intent(PDFViewer.this, PDFAdd.class);
            intent.putExtra("image", image.toString());
            startActivity(intent);
            finish();
        }
    }

    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    void openPdf(String fileName) {
        //copyFileFromAssets(fileName);

        /** PDF reader code */
        File file = new File(getFilesDir() + "/" + fileName);

        Uri uri = null;
        if(!fileName.startsWith("http")) {
            uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
        } else {
            uri = Uri.parse(fileName);
        }
        Log.e("1", uri.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    private void copyFileFromAssets(String fileName) {
        AssetManager assetManager = this.getAssets();

        //앱 내의 파일폴더에 저장
        File cacheFile = new File( getFilesDir() + "/" + fileName );
        InputStream in = null;
        OutputStream out = null;
        try {
            if ( cacheFile.exists() ) {
                return;
            } else {
                in = assetManager.open(fileName);
                out = new FileOutputStream(cacheFile);
                copyFile(in, out);

                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

     */

}