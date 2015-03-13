package com.example.mauricio.motos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Mauricio on 07/03/2015.
 */
public class DBController extends SQLiteOpenHelper {

    private static final String LOGCAT = null;

    public DBController(Context applicationcontext) {
        super(applicationcontext, "MotosDB.db", null, 1);
        Log.d(LOGCAT, "MotosDB.db creada");
    }
    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE Motos (id integer PRIMARY KEY AUTOINCREMENT,idfoto TEXT, marca TEXT,modelo TEXT, precio INTEGER,motor TEXT, kilometros INTEGER,color TEXT,fechaCompra TEXT)";
        database.execSQL(query);
        Log.d(LOGCAT,"Motos Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS Motos";
        database.execSQL(query);
        onCreate(database);
    }

    public void insertMoto(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idfoto", queryValues.get("idfoto"));
        values.put("marca", queryValues.get("marca"));
        values.put("modelo", queryValues.get("modelo"));
        values.put("precio", queryValues.get("precio"));
        values.put("motor", queryValues.get("motor"));
        values.put("kilometros", queryValues.get("kilometros"));
        values.put("color", queryValues.get("color"));
        values.put("fechaCompra", queryValues.get("fechaCompra"));
        database.insert("Motos", null, values);
        database.close();
    }


    public int updateMoto(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idfoto", queryValues.get("idfoto"));
        values.put("marca", queryValues.get("marca"));
        values.put("modelo", queryValues.get("modelo"));
        values.put("precio", queryValues.get("precio"));
        values.put("motor", queryValues.get("motor"));
        values.put("kilometros", queryValues.get("kilometros"));
        values.put("color", queryValues.get("color"));
        values.put("fechaCompra", queryValues.get("fechaCompra"));
        return database.update("Motos", values, "id" + " = ?", new String[] { queryValues.get("id") });
    }

    public void deleteAllMotos() {
        Log.d(LOGCAT,"deleteAll");
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM Motos";
        Log.d("query",deleteQuery);
        database.execSQL(deleteQuery);
    }

    public void deleteMoto(String id) {
        Log.d(LOGCAT,"delete");
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM Motos where id='"+ id +"'";
        Log.d("query",deleteQuery);
        database.execSQL(deleteQuery);
    }

    public ArrayList<HashMap<String, String>> getAllMotos() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM Motos";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", cursor.getString(0));
                map.put("idfoto", cursor.getString(1));
                map.put("marca", cursor.getString(2));
                map.put("modelo", cursor.getString(3));
                map.put("precio", cursor.getString(4));
                map.put("motor", cursor.getString(5));
                map.put("kilometros", cursor.getString(6));
                map.put("color", cursor.getString(7));
                map.put("fechaCompra", cursor.getString(8));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        return wordList;
    }
    public HashMap<String, String> getMotoInfo(String id) {
        HashMap<String, String> wordList = new HashMap<String, String>();
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM Motos where id='"+id+"'";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                wordList.put("idfoto", cursor.getString(1));
                wordList.put("marca", cursor.getString(2));
                wordList.put("modelo", cursor.getString(3));
                wordList.put("precio", cursor.getString(4));
                wordList.put("motor", cursor.getString(5));
                wordList.put("kilometros", cursor.getString(6));
                wordList.put("color", cursor.getString(7));
                wordList.put("fechaCompra", cursor.getString(8));
            } while (cursor.moveToNext());
        }
        return wordList;
    }

    public boolean existeMoto (String marca, String modelo,String fechaCompra){
        boolean existe=false;
        String selectQuery = "SELECT * FROM Motos where fechaCompra='" + fechaCompra+"'"+"and marca='" +marca+"'"+"and modelo='" +modelo+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            existe=true;
        }
        else {
            existe = false;
        }
        return existe;
    }

    public void GenerateXMl(ArrayList<HashMap<String, String>> map){
        int i=0;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            doc.setXmlVersion("1.0");
            Element raiz = doc.createElement("Motos");
            doc.appendChild(raiz);

            while(i<map.size()) {
                Element moto = doc.createElement("Moto");

                Element idfoto = doc.createElement("idfoto");
                Text textidfoto = doc.createTextNode(map.get(i).get("idfoto"));
                idfoto.appendChild(textidfoto);
                moto.appendChild(idfoto);

                Element marca = doc.createElement("marca");
                Text textmarca = doc.createTextNode(map.get(i).get("marca"));
                marca.appendChild(textmarca);
                moto.appendChild(marca);

                Element modelo = doc.createElement("modelo");
                Text textmodelo = doc.createTextNode(map.get(i).get("modelo"));
                modelo.appendChild(textmodelo);
                moto.appendChild(modelo);

                Element precio = doc.createElement("precio");
                Text textprecio = doc.createTextNode(map.get(i).get("precio"));
                precio.appendChild(textprecio);
                moto.appendChild(precio);


                Element motor = doc.createElement("motor");
                Text textmotor = doc.createTextNode(map.get(i).get("motor"));
                motor.appendChild(textmotor);
                moto.appendChild(motor);

                Element kilometros = doc.createElement("kilometros");
                Text textkilometros = doc.createTextNode(map.get(i).get("kilometros"));
                kilometros.appendChild(textkilometros);
                moto.appendChild(kilometros);

                Element color = doc.createElement("color");
                Text textcolor = doc.createTextNode(map.get(i).get("color"));
                color.appendChild(textcolor);
                moto.appendChild(color);

                Element fechaCompra = doc.createElement("fechaCompra");
                Text textfechaCompra = doc.createTextNode(map.get(i).get("fechaCompra"));
                fechaCompra.appendChild(textfechaCompra);
                moto.appendChild(fechaCompra);

                doc.getDocumentElement().appendChild(moto);
                i++;
            }
            Source source = new DOMSource(doc);
            Result result = new StreamResult(new File("/data/data/com.example.mauricio.motos/files/MotosG.xml"));

            // Transformación del Document al fichero
            Transformer trans = TransformerFactory.newInstance().newTransformer();
            trans.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
