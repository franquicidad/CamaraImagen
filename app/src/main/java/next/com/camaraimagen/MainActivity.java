package next.com.camaraimagen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button botonCamara;
    private ImageView imgCamara;

    private static final int REQUEST_CODE = 1;

    private static final String[] PERMISOS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int leer = ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(leer == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, PERMISOS, REQUEST_CODE);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botonCamara = (Button)findViewById(R.id.btn_camara);
        imgCamara = (ImageView)findViewById(R.id.img_camara);

        botonCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File imagenFolder = new File(Environment.getExternalStorageDirectory(),"CamaraFolder");

                imagenFolder.mkdirs();

                File imagen = new File(imagenFolder , "foto.jpg");

                Uri uriImagen = Uri.fromFile(imagen);

                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImagen);

                startActivityForResult(cameraIntent,REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            Toast.makeText(MainActivity.this, "Se ha guardado la imagen:\n" + Environment.getExternalStorageDirectory() + "/CamaraFolder/foto.jpg", Toast.LENGTH_LONG).show();

            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/CamaraFolder/foto.jpg");

            int height = bitmap.getHeight();
            int width = bitmap.getWidth();

            float scaleA = ((float)(width/2))/width;
            float scaleB = ((float)(height/2))/height;

            Matrix matrix = new Matrix();
            matrix.postScale(scaleA,scaleB);

            Bitmap nuevaImagen = Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);

            imgCamara.setImageBitmap(nuevaImagen);

        }else{
            Toast.makeText(MainActivity.this, "No se guardó correctamente la imagen en el dispositivo", Toast.LENGTH_LONG).show();
        }
    }
}
