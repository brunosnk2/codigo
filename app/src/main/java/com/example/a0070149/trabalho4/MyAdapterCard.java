package com.example.a0070149.trabalho4;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapterCard extends RecyclerView.Adapter<MyAdapterCard.MyViewHolderCard> {
        static Context contexto;
        private Sensor sensor;
        private final List<Sensor> listaSensores;
        private Activity activity = null;

        ImageView imageView;

        public MyAdapterCard(Activity atividade, Context ctx, List<Sensor> list){
            this.contexto = ctx;
            this.listaSensores = list;
            this.activity = atividade;

            Picasso.with(contexto)  //Here, this is context.
                    .load("http://www.gazetadopovo.com.br/servicos/tempo/img/icones/dia/2.png")  //Url of the image to load.
                    .into(imageView);

        }

        public MyAdapterCard(List<Sensor> list){
            this.listaSensores = list;
        }
        @Override
        public MyViewHolderCard onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lista_itens_recycler_card, viewGroup, false);
            return new MyViewHolderCard(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolderCard viewHolder, int position) {
            sensor = listaSensores.get(position);
            viewHolder.viewNome.setText(sensor.nome );
            viewHolder.viewValorDesc.setText(sensor.valor);
            viewHolder.delete.setOnClickListener(viewHolder);

         }

        @Override
        public int getItemCount() {
            return listaSensores != null ? listaSensores.size() : 0;

        }

    public interface ExcluirSensor {
        void excluir(Sensor sensor);
    }

        protected class MyViewHolderCard extends RecyclerView.ViewHolder implements View.OnClickListener{
            protected TextView viewNome;
            protected TextView viewValorDesc;
            protected ImageButton delete;
            protected ImageView imageView;

            public MyViewHolderCard(View itemView) {
                super(itemView);
                viewNome = (TextView) itemView.findViewById(R.id.card_title);
                viewValorDesc = (TextView) itemView.findViewById(R.id.card_desc);
                delete = (ImageButton) itemView.findViewById(R.id.card_delete);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);
            }

            @Override
            public void onClick(View v) {
                if(v.equals(delete)){
                    removeAt(getAdapterPosition());
                }
            }
            public void removeAt(int position) {
                if(activity!=null) {
                    ExcluirSensor listener = (ExcluirSensor) activity;
                    listener.excluir(listaSensores.get(position));
                }
                listaSensores.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, listaSensores.size());
            }
        }
    }
