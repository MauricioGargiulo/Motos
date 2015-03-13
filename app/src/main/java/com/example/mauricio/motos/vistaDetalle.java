package com.example.mauricio.motos;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;

/**
 * Created by Mauricio on 12/01/2015.
 */
public class vistaDetalle extends Activity{

    ImageView imagen;
    TextView marcaModelo;
    TextView precio;
    TextView motor;
    TextView kilometros;
    TextView color;
    TextView fecha;
    Uri imageUri;

    String mar,mod,pre,mot,km,col,fec;
    DBController controller = new DBController(this);
    HashMap<String, String> MotosList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_detalle);
        Intent objIntent = getIntent();
        String id = objIntent.getStringExtra("id");
        MotosList = controller.getMotoInfo(id);

        imagen= (ImageView)findViewById(R.id.imageViewPrincipal);
        marcaModelo = (TextView)findViewById(R.id.tvMarcaModelo);
        precio = (TextView)findViewById(R.id.tvPrecio);
        motor = (TextView)findViewById(R.id.tvMotor);
        kilometros = (TextView)findViewById(R.id.tvKilometros);
        color = (TextView)findViewById(R.id.tvColor);
        fecha = (TextView)findViewById(R.id.tvFecha);

    }

    public void onResume(){
        super.onResume();

        mar=MotosList.get("marca");
        mod=MotosList.get("modelo");
        pre=MotosList.get("precio");
        km=MotosList.get("kilometros");
        col=MotosList.get("color");
        fec=MotosList.get("fechaCompra");
        imageUri = Uri.parse(MotosList.get("idfoto"));
        mot=MotosList.get("motor");

        imagen.setImageURI(imageUri);
        marcaModelo.setText(mar+ " " + mod);
        precio.setText(pre + " €");
        motor.setText(mot);
        kilometros.setText(km + " km");
        color.setText(col);
        fecha.setText(fec);

    }



}
