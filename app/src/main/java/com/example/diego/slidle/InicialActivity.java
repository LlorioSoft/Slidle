package com.example.diego.slidle;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InicialActivity extends AppCompatActivity {

    Button btnEmpezar;
    TextView lblCopy;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Pido al usuario permiso para usar la camara
        pedirPermisos();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        intent=new Intent(this,SettingsActivity.class);

        btnEmpezar=(Button)findViewById(R.id.btnPlay);
        btnEmpezar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(intent);
                finish();
            }
        });

        lblCopy=(TextView)findViewById(R.id.lblCopyright);
        lblCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"La aplicación ha sido creada por:\n\t\t\tDiego Gonzalez Díaz;\n\tAlumno del IES Nº 1 de Gijón",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inicial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.help) {
            Toast.makeText(this,"El juego finalizará una vez completada la imagen seleccionada",Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }


    // Metodo que pedirá permiso al usuario para usar la camara
    private void pedirPermisos()
    {
        final int REQUEST_STORAGE=111;
        final String[] PERMISOS_A_PEDIR = {
                Manifest.permission.CAMERA,Manifest.permission.MEDIA_CONTENT_CONTROL, Manifest.permission.READ_EXTERNAL_STORAGE};	//	Sustituir por el permiso-os necesarios

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(InicialActivity.this, PERMISOS_A_PEDIR, REQUEST_STORAGE);
        }

        else
        {
            // No hace falta explicación se piden directamente los permisos
            ActivityCompat.requestPermissions(this, PERMISOS_A_PEDIR, REQUEST_STORAGE);
        }

    }
}
