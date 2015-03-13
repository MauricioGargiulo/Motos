package com.example.mauricio.motos;

/**
 * Created by Mauricio on 08/03/2015.
 */

import android.net.Uri;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class RssParserDomApache
{
    private URL rssUrl;
    HashMap<String, String> queryValues =  new  HashMap<String, String>();

    public RssParserDomApache(String url)
    {
        try
        {
            this.rssUrl = new URL(url);
        }
        catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Motos> parse() {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        ArrayList<Motos> motos = new ArrayList<Motos>();

        try
        {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(this.getInputStream());
            Element root = dom.getDocumentElement();

            NodeList items = root.getElementsByTagName("Moto");

            for (int i=0; i<items.getLength(); i++)
            {

                Motos moto = new Motos();

                Node item = items.item(i);
                NodeList datosMotos = item.getChildNodes();

                for (int j=0; j<datosMotos.getLength(); j++)
                {
                    Node dato = datosMotos.item(j);
                    String etiqueta = dato.getNodeName();

                    if (etiqueta.equals("idfoto"))
                    {
                        moto.setImageURI(Uri.parse(dato.getFirstChild().getNodeValue()));
                        queryValues.put("idfoto", moto.getImageURI().toString());
                        Log.e("idfoto", moto.getImageURI().toString());
                    }
                    else if (etiqueta.equals("marca"))
                    {
                        String texto = obtenerTexto(dato);

                        moto.setMarca(texto);
                        queryValues.put("marca",moto.getMarca());
                        Log.e("marca", moto.getMarca().toString());
                    }
                    else if (etiqueta.equals("modelo"))
                    {
                        moto.setModelo(dato.getFirstChild().getNodeValue());
                        queryValues.put("modelo", moto.getModelo());
                        Log.e("modelo", moto.getModelo().toString());
                    }
                    else if (etiqueta.equals("precio"))
                    {
                        moto.setPrecio(dato.getFirstChild().getNodeValue());
                        queryValues.put("precio", moto.getPrecio());
                        Log.e("precio", moto.getPrecio().toString());
                    }
                    else if (etiqueta.equals("motor"))
                    {
                        moto.setMotor(dato.getFirstChild().getNodeValue());
                        queryValues.put("motor", moto.getMotor());
                        Log.e("motor", moto.getMotor().toString());
                    }
                    else if (etiqueta.equals("kilometros"))
                    {
                        moto.setKilometros(dato.getFirstChild().getNodeValue());
                        queryValues.put("kilometros", moto.getKilometros());
                        Log.e("kilometros", moto.getKilometros().toString());
                    }
                    else if (etiqueta.equals("color"))
                    {
                        moto.setColo(dato.getFirstChild().getNodeValue());
                        queryValues.put("color", moto.getColo());
                        Log.e("color", moto.getColo().toString());
                    }
                    else if (etiqueta.equals("fechaCompra"))
                    {
                        moto.setFechaCompra(dato.getFirstChild().getNodeValue());
                        queryValues.put("fechaCompra",moto.getFechaCompra());
                        Log.e("fechaCompra", moto.getFechaCompra().toString());
                    }

                }

                motos.add(moto);
            }
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }

        return motos;

    }

    private String obtenerTexto(Node dato)
    {
        StringBuilder texto = new StringBuilder();
        NodeList fragmentos = dato.getChildNodes();

        for (int k=0;k<fragmentos.getLength();k++)
        {
            texto.append(fragmentos.item(k).getNodeValue());
        }

        return texto.toString();
    }

    private InputStream getInputStream()
    {
        try
        {
            return rssUrl.openConnection().getInputStream();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}