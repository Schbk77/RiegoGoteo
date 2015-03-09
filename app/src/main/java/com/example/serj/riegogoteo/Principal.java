package com.example.serj.riegogoteo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.AndroidSupport;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Predicate;
import com.example.serj.riegogoteo.clases.Lectura;
import com.example.serj.riegogoteo.clases.Sector;
import com.example.serj.riegogoteo.clases.Zona;
import com.example.serj.riegogoteo.util.DatosRiego;
import com.example.serj.riegogoteo.util.Dia;
import com.example.serj.riegogoteo.util.Fecha;
import com.example.serj.riegogoteo.util.Riego;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Principal extends ActionBarActivity {

    private ObjectContainer bd;
    private Spinner spSectores;
    private List<Sector> listaSectores;
    private Sector sectorActual;
    private Switch swEmpezar;
    private EditText etInicial, etFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        conexionBD();
        inicializar();
    }

    private void inicializar() {
        Fecha f = new Fecha();
        Log.v("FECHA", f.getDiaSemana()+"");
        listaSectores = getSectoresDiaSemana(f.getDiaSemana());
        spSectores = (Spinner)findViewById(R.id.spinnerZonas);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Principal.this,
                android.R.layout.simple_spinner_item, getSectoresDiaSemana(listaSectores));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSectores.setAdapter(adapter);
        spSectores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sectorActual = listaSectores.get(position);
                Log.v("SECTOR ACTUAL:", sectorActual.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        etInicial = (EditText)findViewById(R.id.etLecturaI);
        etFinal = (EditText)findViewById(R.id.etLecturaF);
        // ---Switch---
        // 1.- Iniciar riego solo si esta primera lectura y no esta ultima lectura
        // 2.- Buscar una lectura incompleta y presentar si no (1)
        // 3.- Guardar si hay ultima lectura
        swEmpezar = (Switch)findViewById(R.id.swEmpezar);
        swEmpezar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // No esta bien escrito mu feo
                if(isChecked && (etInicial.getText().toString().length() == 0 ||
                        etFinal.getText().toString().length() > 0)){
                    swEmpezar.setChecked(false);
                } else {
                    if(isChecked){
                        insertarBD();
                    }
                    if (!isChecked && etFinal.getText().toString().length() == 0) {
                        swEmpezar.setChecked(true);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        desconexionBD();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void conexionBD(){
        bd = Db4oEmbedded.openFile(getConfiguracionDb4o(), getExternalFilesDir(null) + "/bd.db4o");
        Log.v("conexionBD", "bd abierta");
        if(!isInicializadaBD()){
            inicializarBD();
            Log.v("conexionBD", "bd inicializada");
            listarBD();
        }
        // Comprobar que no hay lecturas incompletas
        getOpIncompletaBD();
    }

    private void getOpIncompletaBD() {
        List<Lectura> lecturas = bd.query(
                new Predicate<Lectura>() {
                    @Override
                    public boolean match(Lectura l) {
                        return l.getHf() == null;
                    }
                });
        if(lecturas.size()>0){

        }
    }

    private void desconexionBD(){
        bd.commit();
        bd.close();
        Log.v("conexionBD", "bd cerrada");
    }

    private EmbeddedConfiguration getConfiguracionDb4o() {
        EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
        configuration.common().add(new AndroidSupport());
        configuration.common().activationDepth(25); // Clases anidadas
        configuration.common().objectClass(GregorianCalendar.class).storeTransientFields(true);
        configuration.common().objectClass(GregorianCalendar.class).callConstructor(true);
        configuration.common().objectClass(Sector.class).cascadeOnUpdate(true);
        return configuration;
    }

    private boolean isInicializadaBD(){
        List<DatosRiego> datos = bd.query(DatosRiego.class);
        return datos.size() > 0;
    }

    private void inicializarBD(){

        Zona zona = new Zona("Solana");
        bd.store(zona);
        Sector sector;
        sector = new Sector("s1", "Estación/B.Palo bajo", 12725, Dia.miércoles, Riego.gravedad, zona);
        bd.store(sector);
        sector = new Sector("s2", "Ventilla", 16127, Dia.jueves, Riego.gravedad, zona);
        bd.store(sector);
        sector = new Sector("s3", "Vía",  10444, Dia.viernes, Riego.gravedad, zona);
        bd.store(sector);
        sector = new Sector("s4", "Mitagalán/Balsa/B.Quiebras",  10200, Dia.sábado, Riego.rebombeo, zona);
        bd.store(sector);
        sector = new Sector("s5", "L. Encinillas/Quiebras altas",  7695, Dia.domingo, Riego.rebombeo, zona);
        bd.store(sector);
        sector = new Sector("s6","B. Palo alto/Chapa",  12751, Dia.sábado, Riego.rebombeo, zona);
        bd.store(sector);
        sector = new Sector("s7","Loma del Galgo",  13224, Dia.lunes, Riego.gravedad, zona);
        bd.store(sector);

        zona = new Zona("Llano");
        bd.store(zona);
        sector = new Sector("l1", "Destiladero", 6170, Dia.miércoles, Riego.gravedad, zona);
        bd.store(sector);
        sector = new Sector("l2","Barrascales", 6100, Dia.jueves, Riego.gravedad, zona);
        bd.store(sector);
        sector = new Sector("l3","Viñas/Canteras",  15300, Dia.viernes, Riego.gravedad, zona);
        bd.store(sector);
        sector = new Sector("l4","Llano/Campo de Tiro",  18889, Dia.sábado, Riego.rebombeo, zona);
        bd.store(sector);
        sector = new Sector("l5","Pedriza",  15373, Dia.domingo, Riego.rebombeo, zona);
        bd.store(sector);

        zona = new Zona("Viñas");
        bd.store(zona);
        sector = new Sector("v1", "Olla del Pastor", 7917, Dia.sábado, Riego.gravedad, zona);
        bd.store(sector);
        sector = new Sector("v2", "Nacimiento", 4842, Dia.domingo, Riego.gravedad, zona);
        bd.store(sector);

        DatosRiego datosRiego = new DatosRiego();
        bd.store(datosRiego);

        bd.commit();
    }

    private void listarBD(){
        List<Sector> datos = bd.query(Sector.class);
        for(Sector s : datos){
            Log.v("SECTOR", s.toString());
        }
    }

    private void insertarBD(){
        Lectura l = new Lectura(Calendar.getInstance(),
                Calendar.getInstance(),
                null,
                Double.parseDouble(etInicial.getText().toString()),
                0,
                0,
                sectorActual.getZona().getNombre(),
                sectorActual.getId(),
                sectorActual.getNombre(),
                sectorActual.getOlivos());
        bd.store(l);
        bd.commit();
    }

    private List<Sector> getSectoresDiaSemana(final Dia dia){
        List<Sector> sectores = bd.query(
                new Predicate<Sector>() {
                    @Override
                    public boolean match(Sector s) {
                        return s.getDiaDeRiego() == dia;
                    }
                });
        return sectores;
    }

    private List<String> getSectoresDiaSemana(List<Sector> sectores){
        List<String> sectoresString = new ArrayList<>();
        for(Sector s : sectores){
            sectoresString.add(s.getId()+" "+ s.getNombre());
        }

        return sectoresString;
    }
}
