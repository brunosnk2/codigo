package com.example.a0070149.trabalho4;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 0070149 on 04/12/2017.
 */

public class ListaRecyclerCard extends AppCompatActivity implements SensorDialogFragment.SalvarSensor, MyAdapterCard.ExcluirSensor{
    private RecyclerView recyclerView;
    MyAdapterCard adapter;
    private List<Sensor> listaSensores = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_recycler);






        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager layoutManager =  new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        new SensoresTask("GET", null).execute();
        adapter = new MyAdapterCard(this, ListaRecyclerCard.this, listaSensores);
        recyclerView.setAdapter(adapter);
    }




    @Override
    public void salvar(Sensor sensor) {
        listaSensores.add(sensor);
        adapter.notifyItemInserted(listaSensores.size());
        adapter.notifyDataSetChanged();
        new SensoresTask("POST", sensor).execute();
    }

    public void excluir(Sensor sensor){
        Log.d("MAC", sensor.toString());
        if(sensor!=null){
            new SensoresTask("DELETE", sensor).execute();
        }
    }


    public class SensoresTask extends AsyncTask<Void, Void, Void> {
        private String metodo;
        private Sensor sensor;


        public SensoresTask(String metodo, Sensor sensor) {
            this.metodo = metodo;
            this.sensor = sensor;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ListaRecyclerCard.this,"Carregando dados do serviço", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            if (metodo.equalsIgnoreCase("GET")) {
                listaSensores.clear();
                listaSensores = new SensorService().carregarSensores();
            }
            if (metodo.equalsIgnoreCase("POST")) {
                new SensorService().inserir(sensor);
            }
            if (metodo.equalsIgnoreCase("DELETE")) {
                new SensorService().excluir(sensor);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(listaSensores == null)
                Toast.makeText(getApplicationContext(),"Banco sem dados cadastrados ", Toast.LENGTH_LONG).show();
            adapter.notifyDataSetChanged();
            adapter = new MyAdapterCard(listaSensores);
            recyclerView.setAdapter(adapter);
        }
    }





}
