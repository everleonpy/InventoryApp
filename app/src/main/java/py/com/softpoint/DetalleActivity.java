package py.com.softpoint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import py.com.softpoint.pojos.DetalleItemsVo;
import py.com.softpoint.pojos.InventoryItems;

public class DetalleActivity extends AppCompatActivity {

    //private ArrayList<InventoryItems> itemsDetail;
    private ArrayList<DetalleItemsVo> itemRecicle;
    RecyclerView recyclerItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        /* Recepcionamos la lista desde el activity anterior
        itemsDetail = (ArrayList<InventoryItems>) getIntent().getSerializableExtra("Det");
        if(itemsDetail.size() > 0 ){
            Log.i("DET", "Items : "+itemsDetail.size());
        }*/
        itemRecicle = (ArrayList<DetalleItemsVo>) getIntent().getSerializableExtra("Det");
        if( itemRecicle.size() > 0 ){
            Log.i("DET", "Items : "+itemRecicle.size());
        }


        // Refernciamos los componentes
        recyclerItems = findViewById(R.id.recicleDetalle);
        recyclerItems.setLayoutManager(new LinearLayoutManager(this));

        //cargarDetalle(itemsDetail);
        ItemsAdapter adapter = new ItemsAdapter(itemRecicle);
        recyclerItems.setAdapter(adapter);

    }

    /*private void cargarDetalle(ArrayList<InventoryItems> itemsDetail) {

        itemRecicle = new ArrayList<>();
        for (InventoryItems i : itemsDetail){
            itemRecicle.add(new DetalleItemsVo(String.valueOf(i.getProductId()),i.getProductId(),i.getUsr(),i.getQuantity()));
        }
    }*/


    /**
    * Cerrar la ventana de consulta
    * @param view
    */
    public void btnCerrar(View view) {
        finish();
    }
}