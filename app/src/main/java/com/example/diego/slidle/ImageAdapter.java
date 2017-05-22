package com.example.diego.slidle;

import android.content.Context;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Diego on 19/05/2017.
 */

public class ImageAdapter extends ArrayAdapter<Imagen> {

    private List<Imagen> imagenes;
    private boolean completado;
    ImageView imageView;

    public ImageAdapter(Context context, List<Imagen> imagenes)
    {
        super(context,R.layout.item_imagen,imagenes);
        this.imagenes=imagenes;
        this.completado=false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // comprobamos que el view no esta vacio
        View vItem=convertView;
        if(vItem==null)
        {
            LayoutInflater li=LayoutInflater.from(getContext());
            vItem=li.inflate(R.layout.item_imagen,null);
        }

        // Paso la imagen al imageView correspondiente
        imageView=(ImageView)vItem.findViewById(R.id.ivItem);
        if(getItem(position).isOculta()&&!completado)
        {
            imageView.setImageResource(R.drawable.negro);
        }
        else {
            imageView.setImageBitmap(getItem(position).getBitmap());
        }

        // Defino el ancho y el alto que va a tener la imagen
        int ancho=getItem(position).getBitmap().getWidth();
        int alto=getItem(position).getBitmap().getHeight();

        // Le paso las medidas al contenedor ImageView
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ancho,alto));

        // devielvo el control view inflado
        return  vItem;
    }

    // Metodo para cambiar una imagen por otra
    public void cambiar(int pos1, int pos2)
    {
        Collections.swap(imagenes,pos1,pos2);

        notifyDataSetChanged();
    }

    // Metodo para desordenar las piezas de forma aleatoria
    public void DesordenarPiezas()
    {
        Collections.shuffle(imagenes);
        notifyDataSetChanged();
        completado=false; // El Puzzle volvera a estar incompleto
    }

    // Metodo para comprobar si el Puzzle se ha completado con exito
    public boolean comprobarCompletado(int piezas)
    {
        // filas y columnas
        int fila,columna;
        fila=columna=0;

        // para cada imagen dentro de la lista
        for(Imagen imagen : imagenes) {
            if (imagen.getFila() != fila || imagen.getColumna() != columna)
            {
                return false;
            }

            columna++;

            if (columna == piezas) {
                fila++;
                columna = 0;
            }
        }

        completado=true;
        notifyDataSetChanged();

        return true;
    }

    // metodo para obtener el valor de complete
    public boolean isCompletado(){return completado;}
}
