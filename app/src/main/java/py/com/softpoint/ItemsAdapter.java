package py.com.softpoint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import py.com.softpoint.pojos.DetalleItemsVo;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolerItems> {

    ArrayList<DetalleItemsVo> listaDetalle;

    public ItemsAdapter(ArrayList<DetalleItemsVo> listaDetalle) {
        this.listaDetalle = listaDetalle;
    }

    @NonNull
    @Override
    public ViewHolerItems onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detalle,null,false);
        return new ViewHolerItems(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolerItems holder, int position) {

        holder.fBarras.setText(listaDetalle.get(position).getCodigoBarras());
        holder.fInterno.setText( listaDetalle.get(position).getCodigoInterno().toString() );
        holder.fDesc.setText(listaDetalle.get(position).getDescripcion());
        holder.fCantidad.setText(listaDetalle.get(position).getCantidad().toString());

    }

    @Override
    public int getItemCount() {
        return listaDetalle.size();
    }

    public class ViewHolerItems extends RecyclerView.ViewHolder {
        TextView fBarras, fInterno, fDesc, fCantidad;

        public ViewHolerItems(@NonNull View itemView) {
            super(itemView);
            fBarras = itemView.findViewById(R.id.fieldCodigoBarras);
            fInterno = itemView.findViewById(R.id.fieldCodigoInterno);
            fDesc = itemView.findViewById(R.id.fieldDescripcion);
            fCantidad = itemView.findViewById(R.id.fieldCantidad);
        }
    }

}
