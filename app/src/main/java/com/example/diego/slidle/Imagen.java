package com.example.diego.slidle;

import android.graphics.Bitmap;

/**
 * Created by Diego on 19/05/2017.
 */

public class Imagen
{
    // Variables que se van a usar
    private int fila,columna;
    private Bitmap bitmap;
    private boolean ocultada;

    public Imagen(int fila, int columna,Bitmap bitmap)
    {
        this.fila=fila;
        this.columna=columna;
        this.bitmap=bitmap;
    }

    public void borrar(){this.ocultada=true;}

    public  int getFila(){return fila;}

    public  int getColumna(){return columna;}

    public Bitmap getBitmap(){return bitmap;}

    public  boolean isOculta(){return ocultada;}

}
