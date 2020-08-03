package com.example.myapplication.ui.tareo_actividades;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.model.clsTareoActividad;

import java.util.ArrayList;

public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos>  {
    ArrayList<clsTareoActividad> listDatos;

    public AdapterDatos(ArrayList<clsTareoActividad> listDatos) {
        this.listDatos = listDatos;
    }

    /*@NonNull*/
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tareo_actividad_fila_fragment2,null,false);

        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.asignarDatos(listDatos.get(position));
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }


    public class ViewHolderDatos extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView textViewProyecto;
        TextView textViewHoraInicio;
        TextView textViewNombreSolid;
        TextView textViewNombreTarea;
        TextView textViewEmpleados;
        CardView cardView;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            textViewProyecto = (TextView) itemView.findViewById(R.id.value_proyecto);
            textViewHoraInicio = (TextView) itemView.findViewById(R.id.value_horainicio);
            textViewNombreSolid = (TextView) itemView.findViewById(R.id.value_solidnombre);
            textViewNombreTarea = (TextView) itemView.findViewById(R.id.value_tareanombre);
            textViewEmpleados = (TextView) itemView.findViewById(R.id.value_empleadonombre);

            cardView = itemView.findViewById(R.id.mCardView);
            cardView.setOnCreateContextMenuListener(this);
        }


        public void asignarDatos(clsTareoActividad clsTareoActividad) {
            textViewProyecto.setText(clsTareoActividad.getProyecto_nombre());
            textViewHoraInicio.setText(clsTareoActividad.getHora_inicio());
            textViewNombreSolid.setText(clsTareoActividad.getSolid_nombre());
            textViewNombreTarea.setText(clsTareoActividad.getTarea_nombre());
            textViewEmpleados.setText(clsTareoActividad.getEmpleados());
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Seleccione la acción");
            menu.add(this.getAdapterPosition(), 0, 0, "Modificar");
        }

    }
    /*public void eliminar(int position){
        listDatos.remove(position);
        notifyDataSetChanged();
    }*/
}
