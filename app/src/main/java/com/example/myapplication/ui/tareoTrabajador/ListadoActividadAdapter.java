package com.example.myapplication.ui.tareoTrabajador;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.model.clsActividad;
import com.example.myapplication.ui.tareoTrabajador.ListadoActividadAdapter;

import java.io.File;
import java.util.List;

public class ListadoActividadAdapter extends BaseAdapter {

    Context contexto;
    List<clsActividad> rowItems;


    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewProyecto;
        TextView textViewEstado;
        TextView textViewAvance;
        TextView textViewAdjuntos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public ListadoActividadAdapter(Context contexto, List<clsActividad> rowItems) {
        this.contexto = contexto;
        this.rowItems = rowItems;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListadoActividadAdapter.ViewHolder viewHolder;

        LayoutInflater mInflater = (LayoutInflater) contexto.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        ;
        String ruta_imagen = "";
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.tareo_trabajador_fila_actividad_fragment, null);
        }else{
            viewHolder = (ListadoActividadAdapter.ViewHolder)convertView.getTag();
        }
        viewHolder = new ListadoActividadAdapter.ViewHolder(convertView);

        viewHolder.textViewProyecto = (TextView)convertView.findViewById(R.id.proyecto);
        viewHolder.textViewEstado = (TextView)convertView.findViewById(R.id.estado);

        viewHolder.textViewAvance = (TextView)convertView.findViewById(R.id.avance);
        viewHolder.textViewAdjuntos = (TextView)convertView.findViewById(R.id.numeroadjuntos);

        clsActividad row_pos = rowItems.get(position);
        viewHolder.textViewProyecto.setText(row_pos.getNombre_proyecto());
        viewHolder.textViewEstado.setText(row_pos.getEstado_proyecto());
        viewHolder.textViewAvance.setText(row_pos.getHito());
        viewHolder.textViewAdjuntos.setText(row_pos.getCantidad_adjuntos()+"");


        convertView.setTag(viewHolder);
        return convertView;
    }
}
