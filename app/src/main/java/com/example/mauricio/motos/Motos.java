package com.example.mauricio.motos;

import android.net.Uri;

/**
 * Created by Hector on 28/12/2014.
 */
public class Motos {
    private int id;
    private String Marca;
    private String Modelo;
    private String Precio;
    private String Motor;
    private String Kilometros;
    private String Colo;
    private String FechaCompra;
    private Uri ImageURI;

    public Motos() {

    }

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String marca) {
        Marca = marca;
    }

    public String getModelo() {
        return Modelo;
    }

    public void setModelo(String modelo) {
        Modelo = modelo;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    public String getMotor() {
        return Motor;
    }

    public void setMotor(String motor) {
        Motor = motor;
    }

    public String getKilometros() {
        return Kilometros;
    }

    public void setKilometros(String kilometros) {
        Kilometros = kilometros;
    }

    public String getColo () {return Colo;}

    public void setColo(String color){ Colo=color;}

    public String getFechaCompra() {
        return FechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
        FechaCompra = fechaCompra;
    }

    public Uri getImageURI() { return ImageURI; }

    public void setImageURI(Uri imageURI) {
        ImageURI = imageURI;
    }

    public void setIdentificador(int id) {
        this.id = id;
    }

    public int getIdentificador(){
        return id;
    }


    public Motos(int id, String marca, String modelo, String precio, String motor, String kilometros, String color, String fechaCompra, Uri imageURI) {
        this.id=id;
        Marca = marca;
        Modelo = modelo;
        Precio = precio;
        Motor =motor;

        Kilometros = kilometros;
        Colo=color;
        FechaCompra = fechaCompra;
        ImageURI = imageURI;
    }

    public String toString() {
        return (getMarca() + "\n" + getModelo() + "\n" + getPrecio() + "\n"+ getMotor()+  "\n"+  getKilometros() + "\n"+ getColo()+ "\n" + getFechaCompra()+ "\n" + getImageURI());
    }


}
