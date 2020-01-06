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
        TextView textViewtrabajador;
        TextView textViewhorainicio;
        TextView textViewhorafin;
        TextView textViewhoradiurna;
        TextView textViewrendDiurno;
        TextView textViewhorasNocturnas;
        TextView textViewhorasrendNocturno;

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
            viewHolder = new ListadoActividadAdapter.ViewHolder(convertView);

            viewHolder.textViewtrabajador = (TextView)convertView.findViewById(R.id.trabajador);
            viewHolder.textViewhorainicio = (TextView)convertView.findViewById(R.id.horainicio);
            viewHolder.textViewhorafin = (TextView)convertView.findViewById(R.id.horafin);
            viewHolder.textViewhoradiurna = (TextView)convertView.findViewById(R.id.horadiurna);
            viewHolder.textViewrendDiurno = (TextView)convertView.findViewById(R.id.rendDiurno);
            viewHolder.textViewhorasNocturnas = (TextView)convertView.findViewById(R.id.horasNocturnas);
            viewHolder.textViewhorasrendNocturno = (TextView)convertView.findViewById(R.id.rendNocturno);

            clsActividad row_pos = rowItems.get(position);
            viewHolder.textViewtrabajador.setText(row_pos.getNombreTrabajador());
            viewHolder.textViewhorainicio.setText(row_pos.getHorainicio());
            viewHolder.textViewhorafin.setText(row_pos.getHorafin());
            viewHolder.textViewhoradiurna.setText(row_pos.getHorasdiurnas());
            viewHolder.textViewrendDiurno.setText(row_pos.getRenddiurno());
            viewHolder.textViewhorasNocturnas.setText(row_pos.getHorasnocturas());
            viewHolder.textViewhorasrendNocturno.setText(row_pos.getRendnocturno());

            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ListadoActividadAdapter.ViewHolder)convertView.getTag();
        }

        return convertView;
    }
}
