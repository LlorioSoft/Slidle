package com.example.diego.slidle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    // Variables
    int Piezas;
    RadioButton rb3Piezas, rb6Piezas, rb9Piezas;
    Button btImagen,btJugar;
    Intent intent;
    Uri selectedImage=null;
    TextView lblCopy;
    // Variables para usar al elegir la imagen
    private int SELECT_IMAGE = 1;
    private int TAKE_PICTURE = 2;
    ImageView igvImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        igvImagen=(ImageView)findViewById(R.id.imvImagen);

        intent=new Intent(getApplicationContext(),GameActivity.class);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        lblCopy=(TextView)findViewById(R.id.lblCopy);
        lblCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"La aplicación ha sido creada por:\n\t\t\tDiego Gonzalez Díaz;\n\tAlumno del IES Nº 1 de Gijón",Toast.LENGTH_LONG).show();
            }
        });

        // Obtengo los controles que voy a usar
        rb3Piezas=(RadioButton)findViewById(R.id.rbt8tiles);
        rb6Piezas=(RadioButton)findViewById(R.id.rbt36tiles);
        rb9Piezas=(RadioButton)findViewById(R.id.rbt80tiles);
        btImagen=(Button)findViewById(R.id.btnSelImage);
        btJugar=(Button)findViewById(R.id.btnJugar);


        // CLick en el botón para elegir imagen
        btImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                // Llamo al método que se encargará de seleccionar una imagen
                selImage();
            }
        });

        // Click en el bbotón de jugar
        btJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Compruebo el numero de piezas seleccionadas
                if(rb3Piezas.isChecked())
                {
                    Piezas=3;
                }
                else
                {
                    if(rb6Piezas.isChecked())
                    {
                        Piezas=6;
                    }
                    else
                    {
                        Piezas=9;
                    }
                }

                // Compruebo que se haya seleccionado una imagen
                if(selectedImage!=null)
                {
                    // Paso la uri al intent como dato
                    intent.setData(selectedImage);

                    // Además añado al intent el numero de piezas que el usuario ha elegido
                    intent.putExtra("Piezas",Piezas);

                    // Lanzo la Activity
                    startActivity(intent);

                }
                else
                {
                    // Muestro un mensaje de error
                    Toast.makeText(getApplicationContext(),"Hubo un problema con la imagen",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //metodo que crea un dialogo de alerta con dos opciones
    //segun la accion seleccionada abrira la galeria o seleccionara la imagen
    private void selImage()
    {
        try{
            final CharSequence[] items = {"Seleccionar de la galería", "Hacer una foto"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Seleccionar una foto");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    switch(item){
                        case 0:
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent, SELECT_IMAGE);
                            break;
                        case 1:
                            startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), TAKE_PICTURE);
                            break;
                    }

                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } catch(Exception e){}
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try{
            if (requestCode == SELECT_IMAGE)    // Abrir la gaeria y seleccionar una imagen
                if (resultCode == Activity.RESULT_OK) {
                    selectedImage = data.getData();
                    igvImagen.setImageURI(selectedImage);
                }
            if(requestCode == TAKE_PICTURE)     // Reaiizar una nueva imagen a través de la cámara
                if(resultCode == Activity.RESULT_OK){
                    selectedImage = data.getData();
                    igvImagen.setImageURI(selectedImage);
                }
        } catch(Exception e){
            Toast.makeText(this,"Hubo un error inesperado",Toast.LENGTH_SHORT).show();
        }
    }


//    // Metodo que obtiene la ruta de la imagen
//    private String getPath(Uri uri) {
//        String[] projection = { android.provider.MediaStore.Images.Media.DATA };
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
//        int column_index = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }

}
