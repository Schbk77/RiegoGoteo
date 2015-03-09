package com.example.serj.riegogoteo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.AndroidSupport;
import com.db4o.config.EmbeddedConfiguration;
import com.example.serj.riegogoteo.clases.Sector;
import com.example.serj.riegogoteo.clases.Zona;
import com.example.serj.riegogoteo.util.DatosRiego;
import com.example.serj.riegogoteo.util.Dia;
import com.example.serj.riegogoteo.util.Riego;

import java.util.GregorianCalendar;
import java.util.List;

public class Principal extends ActionBarActivity {

    private ObjectContainer bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        conexionBD();
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
        if(!isInicializadaBD()){
            inicializarBD();
        }
    }

    private void desconexionBD(){
        bd.commit();
        bd.close();
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
        sector = new Sector("s4", "Mitagalán/Balsa/B.Quiebras",  10200, Dia.sabado, Riego.rebombeo, zona);
        bd.store(sector);
        sector = new Sector("s5", "L. Encinillas/Quiebras altas",  7695, Dia.domingo, Riego.rebombeo, zona);
        bd.store(sector);
        sector = new Sector("s6","B. Palo alto/Chapa",  12751, Dia.sabado, Riego.rebombeo, zona);
        bd.store(sector);
        sector = new Sector("s7","Loma del Galgo",  13224, Dia.lunes, Riego.gravedad, zona);
        bd.store(sector);

        zona = new Zona("Llano");
        bd.store(zona);
        sector = new Sector("s8", "Destiladero", 6170, Dia.miércoles, Riego.gravedad, zona);
        bd.store(sector);
        sector = new Sector("s9","Barrascales", 6100, Dia.jueves, Riego.gravedad, zona);
        bd.store(sector);
        sector = new Sector("s10","Viñas/Canteras",  15300, Dia.viernes, Riego.gravedad, zona);
        bd.store(sector);
        sector = new Sector("s11","Llano/Campo de Tiro",  18889, Dia.sabado, Riego.rebombeo, zona);
        bd.store(sector);
        sector = new Sector("s12","Pedriza",  15373, Dia.domingo, Riego.rebombeo, zona);
        bd.store(sector);

        zona = new Zona("Viñas");
        bd.store(zona);
        sector = new Sector("s13", "Olla del Pastor", 7917, Dia.sabado, Riego.gravedad, zona);
        bd.store(sector);
        sector = new Sector("s14", "Nacimiento", 4842, Dia.domingo, Riego.gravedad, zona);
        bd.store(sector);

        DatosRiego datosRiego = new DatosRiego();
        bd.store(datosRiego);

        bd.commit();
    }
}
