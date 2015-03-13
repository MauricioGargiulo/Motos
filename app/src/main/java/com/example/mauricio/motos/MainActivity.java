package com.example.mauricio.motos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity implements SearchView.OnQueryTextListener{
    private SearchView mSearchView;
    private ListView LstOpciones;
    private TextView ID;
    int posi,x;
    DBController controller = new DBController(this);
    SimpleAdapter adaptador;
    ArrayList<HashMap<String, String>> motosList;
    private ArrayList<Motos> motos= new ArrayList<Motos>();
    HashMap<String, String> queryValues =  new  HashMap<String, String>();
    public final static int SOCKET_PORT = 6000;
    public final static String SERVER = "192.168.1.89";// localhost|10.0.2.2
    Socket sock = null;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    String file="/data/data/com.example.mauricio.motos/files/Motos.xml";
    public final static int FILE_SIZE = 6022386;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try
        {
            OutputStreamWriter fout=
                    new OutputStreamWriter(
                            openFileOutput("prueba_int.txt", Context.MODE_PRIVATE));
            fout.write("Texto de prueba.");
            fout.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }


        motosList =  controller.getAllMotos();
        LstOpciones = (ListView) findViewById(R.id.LstOpciones);
        LstOpciones.setAdapter(adaptador);

        registerForContextMenu(LstOpciones);
        mSearchView = (SearchView) findViewById(R.id.searchView1);
        LstOpciones.setTextFilterEnabled(true);
        setupSearchView();
        ID=(TextView) findViewById(R.id.ID);

        if(motosList.size()!=0) {
            LstOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    ID = (TextView) v.findViewById(R.id.ID);
                    Intent data = new Intent(MainActivity.this, vistaDetalle.class);//Intent explicito a editActivity
                    TextView marca = (TextView) v.findViewById(R.id.lblMarcaModelo);//Obtenemos la referencia al listView TextView
                    String m = marca.getText().toString();//Almacenamos el texto
                    Log.e("m ", m);
                    for (int k = 0; k < motosList.size(); k++) {//Recorremos el ArrayList<Motos> datos
                        if (motosList.get(k).get("marca").toString().equalsIgnoreCase(m)) {//Para cada elemento comparamos cada matricula
                            x = k;//Guardamos aquella posicion cuyo elemento coincida.

                        }
                    }
                /*
                Pasamos todos los datos del elemento al vistaDetalle
                 */
                    data.putExtra("id", motosList.get(x).get("id"));
                    Log.e("id= " ,  motosList.get(x).get("id"));
                    startActivity(data);
                }
            });
            adaptador = new SimpleAdapter(MainActivity.this, motosList, R.layout.mi_layout, new String[]{"id", "idfoto", "marca", "modelo", "precio", "kilometros"}, new int[]{R.id.ID, R.id.ivContactImage, R.id.lblMarcaModelo, R.id.lblModelo, R.id.lblPrecio, R.id.lblKilometros});
            LstOpciones.setAdapter(adaptador);
        }
    }


    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Introduzca marca....");
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (TextUtils.isEmpty(newText)) {
            LstOpciones.clearTextFilter();
        } else {
            LstOpciones.setFilterText(newText);
        }
        return true;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();

        if (v.getId() == R.id.LstOpciones) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            inflater.inflate(R.menu.opciones_elementos, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final TextView ID=(TextView)info.targetView.findViewById(R.id.ID);
        switch (item.getItemId()) {
            case R.id.EliminarSeleccionada:
                posi = info.position;
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("¿Está seguro que desea borrar la moto? ");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Sí",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                HashMap<String, String> co;
                                co=controller.getMotoInfo(ID.getText().toString());
                                controller.deleteMoto(ID.getText().toString());
                                motosList.remove(posi);
                                adaptador.notifyDataSetChanged();//Refresca adaptador.
                                String dataCarDelete="¡MOTO ELIMINADA!\n";
                                dataCarDelete=dataCarDelete+"MARCA: "+co.get("marca")+"\nMODELO: "+co.get("modelo")+"\nPRECIO: "+co.get("precio");
                                Toast.makeText(getBaseContext(),dataCarDelete, Toast.LENGTH_LONG).show();
                                Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(objIntent);
                           }
                        });
                builder1.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

                return true;

            case R.id.EditarSeleccionada:
                posi = info.position;
                Intent data = new Intent(MainActivity.this, editActivity.class);//Intent explicito a editActivity
                data.putExtra("id", ID.getText().toString());
                startActivity(data);
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.AnadirCocheOverflow:
                Intent i = new Intent(this, AddActivity.class);
                startActivity(i);
                return true;
            case R.id.AnadirCoche:
                Intent in = new Intent(this, AddActivity.class);
                startActivity(in);
                return true;
            case R.id.BorrarTodas:

                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("¿Está seguro que desea borrar todo? ");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Sí",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                    controller.deleteAllMotos();
                                    Intent objIntent1 = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(objIntent1);
                                    adaptador.notifyDataSetChanged();//Refresca adaptador.
                                    Toast.makeText(getBaseContext(),"Se han borrado todos los datos", Toast.LENGTH_LONG).show();
                            }
                        });
                builder1.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
                return true;
            case R.id.BDaXML:
                controller.GenerateXMl(controller.getAllMotos());
                Toast.makeText(getBaseContext(),"XML generado correctamente", Toast.LENGTH_LONG).show();
                return true;
            case R.id.Cargarxml:
                //motos.clear();
                CargarXmlTask tarea = new CargarXmlTask();
                tarea.execute();
                return true;
            case R.id.CargarXmlApache:
                //motos.clear();
                CargarXmlApacheTask tarea1 = new CargarXmlApacheTask();
                tarea1.execute("http://10.0.2.2/Motos.xml");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onResume() {
        super.onResume();
        mSearchView.setQuery("", false);
        mSearchView.clearFocus();
        motosList =  controller.getAllMotos();
        adaptador = new SimpleAdapter(MainActivity.this, motosList, R.layout.mi_layout, new String[]{"id", "idfoto", "marca", "modelo", "precio", "kilometros"}, new int[]{R.id.ID, R.id.ivContactImage, R.id.lblMarcaModelo, R.id.lblModelo, R.id.lblPrecio, R.id.lblKilometros});
        LstOpciones.setAdapter(adaptador);
    }


    private class CargarXmlTask extends AsyncTask<Void,Integer,Boolean> {
        @Override
        protected Boolean doInBackground(Void... params)  {
            try {
                Log.e("PreConnecting...", "");
                sock = new Socket(SERVER, SOCKET_PORT);
                Log.e("Conectando.........", sock.toString());
                obtenerfichero();
                RssParserDom saxparser = new RssParserDom(file);
                motos = saxparser.parse();
                return true;
            }
            catch(IOException e) {
                 /*if (fos != null) fos.close();
                 if (bos != null) bos.close();
                 if (sock != null) sock.close();*/
            }
            return true;

        }
        protected void onPostExecute(Boolean result) {

            for(int i=0; i<motos.size(); i++) {
                String marca = motos.get(i).getMarca();
                String modelo = motos.get(i).getModelo();
                String fechaCompra = motos.get(i).getFechaCompra();
                if (!controller.existeMoto(marca, modelo, fechaCompra)) {
                    HashMap<String, String> queryValues = new HashMap<String, String>();
                    queryValues.put("idfoto", String.valueOf(motos.get(i).getImageURI()));
                    queryValues.put("marca", motos.get(i).getMarca());
                    queryValues.put("modelo", motos.get(i).getModelo());
                    queryValues.put("precio", motos.get(i).getPrecio());
                    queryValues.put("motor", motos.get(i).getMotor());
                    queryValues.put("kilometros", motos.get(i).getKilometros());
                    queryValues.put("color", motos.get(i).getColo());
                    queryValues.put("fechaCompra", motos.get(i).getFechaCompra());
                    controller.insertMoto(queryValues);

                    Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(objIntent);
                }
            }
            Toast.makeText(getBaseContext(),"Sincronización finalizada", Toast.LENGTH_LONG).show();
        }
    }

    public void obtenerfichero () throws IOException {
        int bytesRead;
        int current = 0;
        try {
            // receive file
            Log.e("Mensaje","entro en obtener fichero");
            byte[] mybytearray = new byte[FILE_SIZE];
            InputStream is = sock.getInputStream();
            //fos = new OutputStreamWriter(openFileOutput(file, Context.MODE_PRIVATE));
            Log.e("prueba",file.toString());
            Log.e("Mensaje","obtengo inputstream");
            File f = new File(file);
            Log.e("Mensaje","creo file");
            fos = new FileOutputStream(f);
            Log.e("Mensaje","creo fos");
            bos = new BufferedOutputStream(fos);
            Log.e("Mensaje","creo bos");
            bytesRead = is.read(mybytearray, 0, mybytearray.length);
            current = bytesRead;

            do {
                bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
                if (bytesRead >= 0) current += bytesRead;
            } while (bytesRead > -1);
            bos.write(mybytearray,0,current);
            bos.flush();
            Log.e("pruebaFIN",file.toString());
        }
        finally {
            if (fos != null) fos.close();
            if (bos != null) bos.close();
            if (sock != null) sock.close();
        }
    }

    private class CargarXmlApacheTask extends AsyncTask<String,Integer,Boolean> {
        @Override
        protected Boolean doInBackground(String... params)  {

                RssParserDomApache saxparser = new RssParserDomApache(params[0]);
                motos = saxparser.parse();
                return true;

        }
        protected void onPostExecute(Boolean result) {

            for(int i=0; i<motos.size(); i++) {
                String marca = motos.get(i).getMarca();
                String modelo = motos.get(i).getModelo();
                String fechaCompra = motos.get(i).getFechaCompra();
                if (!controller.existeMoto(marca, modelo, fechaCompra)) {
                    HashMap<String, String> queryValues = new HashMap<String, String>();
                    queryValues.put("idfoto", String.valueOf(motos.get(i).getImageURI()));
                    queryValues.put("marca", motos.get(i).getMarca());
                    queryValues.put("modelo", motos.get(i).getModelo());
                    queryValues.put("precio", motos.get(i).getPrecio());
                    queryValues.put("motor", motos.get(i).getMotor());
                    queryValues.put("kilometros", motos.get(i).getKilometros());
                    queryValues.put("color", motos.get(i).getColo());
                    queryValues.put("fechaCompra", motos.get(i).getFechaCompra());
                    controller.insertMoto(queryValues);

                    Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(objIntent);
                }
            }
            Toast.makeText(getBaseContext(),"Sincronización finalizada", Toast.LENGTH_LONG).show();
        }
    }
}
