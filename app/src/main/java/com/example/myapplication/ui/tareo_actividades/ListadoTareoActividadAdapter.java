package com.example.myapplication.ui.tareo_actividades;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.model.clsTareoActividad;

import java.util.List;

public class ListadoTareoActividadAdapter extends BaseAdapter {

    Context contexto = null;
    List<clsTareoActividad> rowItems = null;


    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewProyecto;
        TextView textViewHoraInicio;
        TextView textViewNombreSolid;
        TextView textViewNombreTarea;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public ListadoTareoActividadAdapter(Context contexto, List<clsTareoActividad> rowItems) {
        this.contexto = contexto;
        this.rowItems = rowItems;
    }

    @Override
    public int getCount() {
        if(rowItems == null)
            return 0;
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

        ListadoTareoActividadAdapter.ViewHolder viewHolder;

        LayoutInflater mInflater = (LayoutInflater) contexto.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        ;
        String ruta_imagen = "";
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.tareo_actividad_fila_fragment, null,false);
        }else{
            viewHolder = (ListadoTareoActividadAdapter.ViewHolder)convertView.getTag();
        }
        viewHolder = new ListadoTareoActividadAdapter.ViewHolder(convertView);

        viewHolder.textViewProyecto = (TextView)convertView.findViewById(R.id.value_proyecto);
        viewHolder.textViewHoraInicio = (TextView)convertView.findViewById(R.id.value_horainicio);

        viewHolder.textViewNombreSolid = (TextView)convertView.findViewById(R.id.value_solidnombre);
        viewHolder.textViewNombreTarea = (TextView)convertView.findViewById(R.id.value_tareanombre);

        clsTareoActividad row_pos = rowItems.get(position);
        viewHolder.textViewProyecto.setText(row_pos.getProyecto_nombre());
        viewHolder.textViewHoraInicio.setText(row_pos.getHora_inicio());
        viewHolder.textViewNombreSolid.setText(row_pos.getSolid_nombre());
        viewHolder.textViewNombreTarea.setText(row_pos.getTarea_nombre()+"");

        convertView.setTag(viewHolder);
        return convertView;
    }
}
