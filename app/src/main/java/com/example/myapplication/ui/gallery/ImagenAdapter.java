package com.example.myapplication.ui.gallery;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.example.myapplication.data.model.galleryRowItemImagen;
import com.example.myapplication.R;

import java.io.File;
import java.util.List;

public class ImagenAdapter extends BaseAdapter {
    Context contexto;
    List<galleryRowItemImagen> rowItems;


    static class ViewHolder{
        TextView textViewNombre;
        ImageView thumb_imagen;
    }


    public ImagenAdapter(Context contexto, List<galleryRowItemImagen> rowItems) {
        this.contexto = contexto;
        this.rowItems=rowItems;
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

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        LayoutInflater mInflater = (LayoutInflater) contexto.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
         ;
        String ruta_imagen = "";
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.fragment_gallery_fila_imagen, null);
            viewHolder = new ViewHolder();

            viewHolder.textViewNombre = (TextView)convertView.findViewById(R.id.textViewNombre);
            viewHolder.thumb_imagen = (ImageView)convertView.findViewById(R.id.thumb_imagen);
            Log.d("position",position+"");


        }else{
            Log.d("position2xd",position+"");
            viewHolder = (ViewHolder)convertView.getTag();
        }
        galleryRowItemImagen row_pos = rowItems.get(position);
        viewHolder.textViewNombre.setText(row_pos.getNombre());
        ruta_imagen = row_pos.getRuta_imagen();
        convertView.setTag(viewHolder);

        Boolean imageDefault= ruta_imagen.toString().contains(".mp4");
        Boolean audioDefault= ruta_imagen.toString().contains(".3gp");

        if (imageDefault || audioDefault ){
            int img = ruta_imagen.toString().contains(".mp4") ? R.drawable.ic_videocam_black_48 : R.drawable.ic_mic_black_48;
            viewHolder.thumb_imagen.setImageResource(img);
        }else{
            File imagenArchivo = new  File(ruta_imagen);
            if(imagenArchivo.exists()){
                Bitmap bitmap = BitmapFactory.decodeFile(imagenArchivo.getAbsolutePath());
                BitmapFactory.Options opciones = new BitmapFactory.Options();
                opciones.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(ruta_imagen, opciones);

                int scaleW = opciones.outWidth / 1000 + 1;
                int scaleH = opciones.outHeight / 626 + 1;
                int scale = Math.max(scaleW, scaleH);

                opciones.inJustDecodeBounds = false;
                opciones.inSampleSize = scale;
                opciones.inSampleSize = scale;
                bitmap = BitmapFactory.decodeFile(ruta_imagen, opciones);
                viewHolder.thumb_imagen.setImageBitmap(bitmap);
            }
        }

        return convertView;
    }
}
