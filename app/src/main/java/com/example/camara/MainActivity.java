package com.example.camara;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.takephoto.R;

import java.io.*;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Intent takePictureIntent;
    File dir = new File("data" + File.separator + "data" + File.separator + "com.example.tnb_20.takephoto" + File.separator + "files");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // En pirmier lugar, si el directorio al que se apunta no existe, se crea desde cero:
        if (!dir.exists()) {
            dir.mkdir();
        }
        // Instanacio un objeto de imagen y lo añado como archivo:
        ImageView img = findViewById(R.id.image);
        if (dir.listFiles().length > 0) {
            File image = new File(dir, dir.listFiles()[dir.listFiles().length - 1].getName());
            if (image.exists()) {
                Uri uri = Uri.fromFile(image);
                img.setImageURI(uri);
            }
        }

        Button b = findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView iv = findViewById(R.id.image);
            iv.setImageBitmap(imageBitmap);

            OutputStream os = null;
            // Esta funcion lo que hace es recorrer los archivos del directorio y
            // si no existe, mapea el archivo en una imagen ".PNG" y acaba de
            // recorrer el directorio.
            try {
                for (int i = 0; i <= dir.listFiles().length; i++) {
                    File file = new File(dir, "foto" + i + ".png");
                    if (!file.exists()) {
                        os = new FileOutputStream(file);
                        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                        i = dir.listFiles().length;
                    }
                }
            } catch (IOException e) {
                Log.e("ERROR","No se pudo guardar la imagen");
            }
        }
    }
}
