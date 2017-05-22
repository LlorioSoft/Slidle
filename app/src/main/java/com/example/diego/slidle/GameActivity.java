package com.example.diego.slidle;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    // Variables que se van a usar
    private int piezas;
    private int movimientos;
    private GridView gvImagenes;
    private ImageAdapter adapter;
    private TextView lblMovimientos;
    Uri ruta;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layout=(LinearLayout)findViewById(R.id.content_game);
        // Inicializo las variables
        gvImagenes=(GridView)findViewById(R.id.gvImagenes);
        lblMovimientos=(TextView)findViewById(R.id.lblMovimientos);

        movimientos=0;

        // Recojo la uri de la imagen que se ha pasado al intent como dato
        ruta=getIntent().getData();

        // Recojo el numero de piezas que va a tener el puzzle
        // Por defecto sera un puzzle 3x3
        piezas=getIntent().getIntExtra("Piezas",3);

        gvImagenes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                moverPieza(position);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Controlamos las excepciones
        try
        {
            //A partir de la uri, creamos un bitmap de la imagen
            Bitmap imagen = MediaStore.Images.Media.getBitmap(this.getContentResolver(),ruta);

            //Creamos un adaptador, para mostrar en el grid view que le proporcione las imagenes a mostrar.
            //Recibe como parámetros una lista de objetos imagen
            adapter = new ImageAdapter(this, cortarImagen(imagen, piezas));

            //Le metemos el adaptador al gridview de imágenes y le fijamos el núemro de columnas
            //que será igual que el número de piezas
            gvImagenes.setAdapter(adapter);
            gvImagenes.setNumColumns(piezas);

            //Hacemos que el gridview imagenes, ocupe lo mismo que la imagen
            int width = imagen.getWidth();
            int height = imagen.getHeight();

            // Cogemos el layout que está definido en el xml, le cambiamos el alto el ancho
            //y se lo volvemos a meter
            LinearLayout.LayoutParams lp=(LinearLayout.LayoutParams)gvImagenes.getLayoutParams();
            lp.height=height;
            lp.width=width;
            gvImagenes.setLayoutParams(lp);

            //adjustGridSize();
        }

        //Si hay algún problema, mostramos un Toast avisando que no se ha podido cargar la imagen
        catch (IOException e)
        {
            Toast.makeText(this, "No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    // Metodo que recibe fila y columna y devuelve la posicion al respecto
    public int getPosicion(int fila, int columna)
    {
        return fila*piezas+columna;
    }


    // Metodo para obtener la imagen del adapter segun fila y columna
    public Imagen getImagen(int fila, int columna)
    {
        return adapter.getItem(getPosicion(fila,columna));
    }


    private void moverPieza(int position)
    {
        // Compruebo si las imagenes están ordenadas
        if(adapter.isCompletado())
        { // No permito el movimiento de más piezas
        }

        // Saco la imagen en la que el usuario hace click
        int fila=position/piezas;
        int columna=position%piezas;

        // Compruebo si la imagen seleccionada tiene el movimiento libre
        // tiene el hueco negro a uno de sus lados laterales o superiores
        if(fila>0 && getImagen(fila-1,columna).isOculta())
        {
            adapter.cambiar(position,getPosicion(fila-1,columna));
        }

        //Miramos si el hueco de abajo está vacio
        else if (fila < (piezas - 1) && getImagen(fila + 1, columna).isOculta())
        {
            adapter.cambiar(position, getPosicion(fila + 1, columna));
        }

        //Comprueba si el hueco de la izquierda está vacio
        else if (columna > 0 && getImagen(fila, columna - 1).isOculta())
        {
            adapter.cambiar(position, getPosicion(fila, columna - 1));
        }

        //Mira si el hueco de la derecha está vacío
        else if (columna < (piezas - 1) && getImagen(fila, columna + 1).isOculta())
        {
            adapter.cambiar(position, getPosicion(fila, columna + 1));
        }

        //Si no hay ningún hueco vacio por los lados, activamos el sonido
        //de fallo, si está activa  la opción de sonido
        else
        {
            //Si falla, salimos
            return;
        }

        //Le pasamos el número de movimientos que llevamos al edittext
        //y lo incrementamos
        movimientos++;
        lblMovimientos.setText("Movimientos: "+movimientos);

        //Pedimos al adpatador que compruebe si el juego está completado con este movimiento
        //Si lo está mostramos un toast, y guardamos el número de movimientos en la base de datos
        if (adapter.comprobarCompletado(piezas))
        {
            Toast.makeText(this, "Juego completado", Toast.LENGTH_LONG).show();
        }
    }


    // Menu Inflater
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.restart)
        {
            Toast.makeText(this,"El puzzle se va a reiniciar",Toast.LENGTH_SHORT).show();
            reiniciar();
        }

        return super.onOptionsItemSelected(item);
    }

    private void reiniciar()
    {
        adapter.DesordenarPiezas();
        movimientos=0;
        lblMovimientos.setText("Movimientos: "+movimientos);
    }

    private List<Imagen> cortarImagen(Bitmap bitmap,int piezas)
    {
        //Crea la lista de objetos imagen
        List<Imagen> bs = new ArrayList<>();

        //Calcula el alto y ancho de la imagen a trocear
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();

        //Calcula cuantos pixel van a ser necesarios
        final int pixelByCol = width / piezas;
        final int pixelByRow = height / piezas;

        //Creamos para cada celda, el trocito de la imagen que necesita o le corresponde
        for (int i = 0; i < piezas; i++)
        {
            for (int j = 0; j < piezas; j++)
            {
                //Calculamos la posición x e y donde empieza el trozo
                int startY = pixelByRow * i;
                int startX = pixelByCol * j;

                //Creo un nuevo bitmap con ella a partir de la imagen original
                //del trozo que me interesa
                Bitmap b = Bitmap.createBitmap(bitmap, startX, startY, pixelByCol, pixelByRow);

                //Añadimos a la lista de imagen un nuevo objeto imagen
                //que contiene la fila y la columna y la imagen para ese trozo
                bs.add(new Imagen(i, j, b));
            }
        }

        //Oculta la última de las imagenes añadidas a la lista
        bs.get(bs.size() - 1).borrar();

        //Barajamos la lista de imágenes
        //Collections.shuffle(bs);  //Con esta instrucción se baraja el puzle entero
        Collections.swap(bs, bs.size() - 1, bs.size() - 2);  //Con esta instrucción la utilizamos para
        //poder realizar las pruebas más fácilmente

        // Devolvemos la lista,  preparada para añadir al adpater
        return bs;
    }

}
