package com.example.mauricio.motos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.HashMap;


public class editActivity extends Activity implements OnItemSelectedListener {
    final String[] datos=new String[]{"Motor", "Gasolina","Eléctrico"};
    private Spinner Marca;
    private Spinner Modelo;
    private Spinner Motor;
    private EditText Precio;
    private EditText Kilometros;
    private DatePicker FechaCompra;
    private EditText Color;
    Uri imageUri = Uri.parse("android.resource://com.example.mauricio.motos/drawable/no_moto");
    ImageView contactImageImgView;
    int posMoto;
    boolean entroGaleria=false;
    int position;
    DBController controller = new DBController(this);
    HashMap<String, String> MotosList;

    private String opnSpinner,marcaSpinner,modeloSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_motos);

        contactImageImgView = (ImageView) findViewById(R.id.imgViewContactImage);
        Intent objIntent = getIntent();
        String id = objIntent.getStringExtra("id");
        MotosList = controller.getMotoInfo(id);
    }

    public int setSpinText(Spinner spin, String text)
    {
        int pos=0;
        for(int i= 0; i < spin.getAdapter().getCount(); i++)
        {

            if(spin.getAdapter().getItem(i).toString().contains(text))
            {
                pos=i;
                spin.setSelection(i);
            }
        }
    return pos;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Callback method to be invoked when the selection disappears from this
        // view. The selection can disappear for instance when touch is
        // activated or when the adapter becomes empty.
    }

    /**
     * Populate the Spinner.
     */
    private void loadSpinnerMarcas() {

        // Create an ArrayAdapter using the string array and a default spinner
        // layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.marcas, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.Marca.setAdapter(adapter);

        // This activity implements the AdapterView.OnItemSelectedListener
        this.Marca.setOnItemSelectedListener(this);
        this.Modelo.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {

        switch (parent.getId()) {
            case R.id.sp_marca:

                // Retrieves an array
                TypedArray arrayLocalidades = getResources().obtainTypedArray(
                        R.array.array_motos_a_modelos);
                CharSequence[] localidades = arrayLocalidades.getTextArray(pos);
                arrayLocalidades.recycle();

                // Create an ArrayAdapter using the string array and a default
                // spinner layout
                ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                        this, android.R.layout.simple_spinner_item,
                        android.R.id.text1, localidades);

                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Apply the adapter to the spinner
                this.Modelo.setAdapter(adapter);
                marcaSpinner=Marca.getSelectedItem().toString();
                String modelo;
                //modelo=getIntent().getStringExtra("Modelo");
                modelo=MotosList.get("modelo");
                setSpinText(Modelo, modelo);
                break;

            case R.id.sp_modelo:

                modeloSpinner=Modelo.getSelectedItem().toString();
                break;
        }
    }


    public void onClick(final View v){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("¿Está seguro que desea modificar la moto? ");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Intent data = new Intent();
                        HashMap<String, String> queryValues =  new  HashMap<String, String>();

                        Precio=(EditText)findViewById(R.id.entradaPrecio);
                        Kilometros=(EditText)findViewById(R.id.entradaKilometros);
                        FechaCompra=(DatePicker)findViewById(R.id.datePicker);
                        Color=(EditText)findViewById(R.id.entradaColor);
                        int dia,mes,anno;
                        dia=FechaCompra.getDayOfMonth();
                        mes=FechaCompra.getMonth()+1;
                        anno=FechaCompra.getYear();
                        String fecha=dia+"/"+mes+"/"+anno;

                        position=getIntent().getIntExtra("Position",0);
                        //Bundle b= new Bundle();

                        Intent objIntent = getIntent();
                        String MotoId = objIntent.getStringExtra("id");
                        queryValues.put("id", MotoId);

                        queryValues.put("idfoto",imageUri.toString());
                        queryValues.put("marca",marcaSpinner );
                        queryValues.put("modelo",modeloSpinner);
                        queryValues.put("precio", Precio.getText().toString());
                        queryValues.put("motor", opnSpinner);
                        queryValues.put("kilometros", Kilometros.getText().toString());
                        queryValues.put("color", Color.getText().toString());
                        queryValues.put("fechaCompra",fecha.toString());
                        controller.updateMoto(queryValues);
                        callHomeActivity(v);
                    }
                });
        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }
    public void onClickCancelar(View v){
        finish();
    }

    public void callHomeActivity(View view) {
        Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(objIntent);
    }

    public void onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode == RESULT_OK) {
            if (reqCode == 1) {
                imageUri = data.getData();
                entroGaleria=true;
                contactImageImgView.setImageURI(data.getData());
            }
        }

    }


    public void onResume(){
        super.onResume();

        String marca, modelo, precio,motor,kilometros,color,fechaCadena,imagen;

        Marca = (Spinner) findViewById(R.id.sp_marca);
        Modelo = (Spinner) findViewById(R.id.sp_modelo);
        Precio=(EditText)findViewById(R.id.entradaPrecio);
        Kilometros = (EditText)findViewById(R.id.entradaKilometros);
        Color= (EditText)findViewById(R.id.entradaColor);
        FechaCompra=(DatePicker)findViewById(R.id.datePicker);
        contactImageImgView=(ImageView)findViewById(R.id.imgViewContactImage);

        /////////////
        Motor=(Spinner)findViewById(R.id.spnMotorizacion);
        /////////////

        marca=MotosList.get("marca");
        modelo=MotosList.get("modelo");
        precio=MotosList.get("precio");
        kilometros=MotosList.get("kilometros");
        color=MotosList.get("color");
        fechaCadena=MotosList.get("fechaCompra");
        motor=MotosList.get("motor");

        switch (motor){
            case "Motor":
                posMoto=0;
                break;
            case "Gasolina":
                posMoto=1;
                break;
            case "Eléctrico":
                posMoto=2;
                break;

        }


       if(!entroGaleria) {
           imageUri = Uri.parse(MotosList.get("idfoto"));
           entroGaleria=false;
       }


        ArrayAdapter<String> adap=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,datos);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Motor.setAdapter(adap);
        Motor.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {
                        opnSpinner=datos[position].toString();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


        this.Marca = (Spinner) findViewById(R.id.sp_marca);
        this.Modelo = (Spinner) findViewById(R.id.sp_modelo);


        String str[] = fechaCadena.split("/");
        int day = Integer.parseInt(str[0]);
        int month = Integer.parseInt(str[1]);
        int year = Integer.parseInt(str[2]);

        Log.e("DIAAAA", String.valueOf(day));
        Log.e("MEEES", String.valueOf(month));
        Log.e("AÑOOOOO", String.valueOf(year));

        //definir valor spinner.
        FechaCompra.updateDate(year,month-1,day);//definir valor del datepicker con los valores del main.


        loadSpinnerMarcas();


        setSpinText(Marca, marca);
        int pos = setSpinText(Marca, marca);

        // Retrieves an array
        TypedArray arrayLocalidades = getResources().obtainTypedArray(
                R.array.array_motos_a_modelos);
        CharSequence[] localidades = arrayLocalidades.getTextArray(pos);
        arrayLocalidades.recycle();

        // Create an ArrayAdapter using the string array and a default
        // spinner layout
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                this, android.R.layout.simple_spinner_item,
                android.R.id.text1, localidades);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        this.Modelo.setAdapter(adapter);

        setSpinText(Modelo, modelo);


        Precio.setText(precio);
        Kilometros.setText(kilometros);
        Color.setText(color);

        Motor.setSelection(posMoto);

        contactImageImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), 1);

            }
        });
        contactImageImgView.setImageURI(imageUri);
        marcaSpinner=Marca.getSelectedItem().toString();
        modeloSpinner=Modelo.getSelectedItem().toString();
    }

}
