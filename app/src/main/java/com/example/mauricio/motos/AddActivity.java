package com.example.mauricio.motos;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.HashMap;


public class AddActivity extends Activity implements OnItemSelectedListener {

    final String[] datos=new String[]{"Motor", "Gasolina","Eléctrico"};
    ImageView contactImageImgView;
    private Spinner spMarcas;
    private Spinner spModelos;
    private Spinner Motor;
    private String opnSpinner, marcaSpinner, modeloSpinner;
    private EditText Kilometros;
    private EditText Precio;
    private EditText Color;
    private DatePicker FechaCompra;
    Uri imageUri = Uri.parse("android.resource://com.example.mauricio.motos/drawable/no_moto");
    DBController controller = new DBController(this);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_motos);
        Color=(EditText)findViewById(R.id.entradaColor);
        Kilometros=(EditText)findViewById(R.id.entradaKilometros);
        Precio=(EditText)findViewById(R.id.entradaPrecio);
        Motor=(Spinner)findViewById(R.id.spnMotorizacion);
        FechaCompra=(DatePicker)findViewById(R.id.datePicker);
        ArrayAdapter<String> adap=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,datos);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Motor.setAdapter(adap);
        Motor.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View v, int position, long id) {

                        opnSpinner = datos[position].toString();//paso el elemento mediante objeto bundle.
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        this.spMarcas = (Spinner) findViewById(R.id.sp_marca);
        this.spModelos = (Spinner) findViewById(R.id.sp_modelo);

        loadSpinnerMarcas();

        contactImageImgView = (ImageView) findViewById(R.id.imgViewContactImage);

        contactImageImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), 1);

            }

        });
    }

    public void callHomeActivity(View view) {
        Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(objIntent);
    }

    public void onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode == RESULT_OK) {
            if (reqCode == 1) {
                imageUri = data.getData();
                contactImageImgView.setImageURI(data.getData());

            }
        }
    }


    private void loadSpinnerMarcas() {

        // Create an ArrayAdapter using the string array and a default spinner
        // layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.marcas, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.spMarcas.setAdapter(adapter);

        // This activity implements the AdapterView.OnItemSelectedListener
        this.spMarcas.setOnItemSelectedListener(this);
        this.spModelos.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos,
                               long id) {

        switch (parent.getId()) {
            case R.id.sp_marca:

                // Retrieves an array
                TypedArray arrayLocalidades = getResources().obtainTypedArray(R.array.array_motos_a_modelos);
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
                this.spModelos.setAdapter(adapter);
                marcaSpinner=spMarcas.getSelectedItem().toString();
                break;

            case R.id.sp_modelo:
                modeloSpinner=spModelos.getSelectedItem().toString();
                break;
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Callback method to be invoked when the selection disappears from this
        // view. The selection can disappear for instance when touch is
        // activated or when the adapter becomes empty.
    }

    public void onClick(View v){
        Motor=(Spinner)findViewById(R.id.spnMotorizacion);
        Precio=(EditText)findViewById(R.id.entradaPrecio);
        Kilometros=(EditText)findViewById(R.id.entradaKilometros);
        Color=(EditText)findViewById(R.id.entradaColor);

        int dia,mes,anno;
        dia=FechaCompra.getDayOfMonth();
        mes=FechaCompra.getMonth()+1;
        anno=FechaCompra.getYear();
        String fecha=dia+"/"+mes+"/"+anno;

        HashMap<String, String> queryValues =  new HashMap<String, String>();
        queryValues.put("idfoto", imageUri.toString());
        queryValues.put("marca", marcaSpinner);
        queryValues.put("modelo", modeloSpinner);
        queryValues.put("precio", Precio.getText().toString());
        queryValues.put("motor", opnSpinner);
        queryValues.put("kilometros", Kilometros.getText().toString());
        queryValues.put("color", Color.getText().toString());
        queryValues.put("fechaCompra", fecha.toString());
        controller.insertMoto(queryValues);
        this.callHomeActivity(v);
    }
    public void onClickCancelar(View v){
        finish();
    }

    public void onClickReset(View v){
        contactImageImgView.setImageResource(R.drawable.no_moto);
        Precio.setText("");
        Kilometros.setText("");
        Color.setText("");
        spMarcas.setSelection(0);
        spModelos.setSelection(0);
        Motor.setSelection(0);
        FechaCompra.updateDate(FechaCompra.getYear(),FechaCompra.getMonth(),FechaCompra.getDayOfMonth());
    }

}
