package py.com.softpoint;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import py.com.softpoint.pojos.Contador;
import py.com.softpoint.pojos.DetalleItemsVo;
import py.com.softpoint.pojos.Inventory;
import py.com.softpoint.pojos.InventoryItems;
import py.com.softpoint.pojos.Producto;
import py.com.softpoint.pojos.User;
import py.com.softpoint.services.ContadorAPI;
import py.com.softpoint.services.InventoryAPI;
import py.com.softpoint.services.InventoryItemAPI;
import py.com.softpoint.services.ProductoAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InventoryDetail extends AppCompatActivity implements View.OnKeyListener, AdapterView.OnItemSelectedListener {

    private Retrofit retrofit;
    private EditText codigo, cantidad;
    private TextView txtDescripcion;
    private Button btnAceptar, btnDetalle;
    private Spinner spContador, spInventario;
    private ArrayList<Contador> countLst;
    private ArrayList<Inventory> invLst;
    private ArrayList<String>  conterSP;
    private ArrayList<String>  invSP;

    //Variables de Datos
    private User usr;
    private Long idMovil;
    private Contador contdorSelected;
    private Inventory inventorySelected;
    private Producto producto;

    //private ArrayList<InventoryItems> itemsDetail;
    private ArrayList<DetalleItemsVo> itemsVo;
    private DetalleItemsVo rowVo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_detail);

        // Enlazamos el Spiner
        spContador = findViewById(R.id.spContador);
        spContador.setOnItemSelectedListener(this);
        spInventario = findViewById(R.id.spNroInventario);
        spInventario.setOnItemSelectedListener(this);

        // Botones
        btnAceptar = findViewById(R.id.btnAceptar);
        btnDetalle = findViewById(R.id.btnDetalle);
        btnDetalle.setEnabled(false);

        // Los EditText
        codigo = findViewById(R.id.txtCodigo);
        codigo.addTextChangedListener(validWatcher);
        codigo.setOnKeyListener(this);

        cantidad = findViewById(R.id.txtCantidad);
        cantidad.addTextChangedListener(validWatcher);
        txtDescripcion = findViewById(R.id.txtDescripcion);

        // Inicialiamos el Array que guarda la lista de los items colectados
        // itemsDetail = new ArrayList<InventoryItems>();
        itemsVo = new ArrayList<DetalleItemsVo>();

        Bundle  tmp = getIntent().getExtras();

            if( tmp != null ) {
                usr = new User();
                usr = (User) tmp.getSerializable("usr");
                idMovil = tmp.getLong("idMovil");
                Log.i("USER_LOGIN", "Session de  : "+ usr.getFullName()+
                        "  unitID : "+usr.getUnitId() +"  siteId : "+usr.getSiteId());

                retrofit = new Retrofit.Builder().baseUrl(tmp.getString("urlBase"))
                        .addConverterFactory(GsonConverterFactory.create()).build();

                //Cargamos la lista de Inventarios
                InventoryAPI invApi = retrofit.create(InventoryAPI.class);
                Call<ArrayList<Inventory>> callInv = invApi.getAllOpened(usr.getSiteId());
                callInv.enqueue(new Callback<ArrayList<Inventory>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Inventory>> callInv, Response<ArrayList<Inventory>> response) {

                        if( response.isSuccessful() ){

                            invLst = response.body();
                            invSP = new ArrayList<>();

                                for (Inventory inv : invLst)
                                {
                                    int i = 0;
                                    invSP.add(inv.getId()+" - "+inv.getName());

                                        if(i == 0 ){
                                           //---- cargamos la lista de Contadores apartir de id de inventario
                                            cargarSpeenerContador(inv.getId(), inv.getUnitId());
                                        }

                                    Log.i("INV","Inv : "+inv.getName()+" Cant : "+invLst.size());
                                }

                            spInventario.setAdapter(new ArrayAdapter<String>(InventoryDetail.this, android.R.layout.simple_spinner_dropdown_item, invSP));

                        }else{
                            Toast.makeText(InventoryDetail.this,"No hay INVENTARIO abierto",Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onFailure(Call<ArrayList<Inventory>> call, Throwable t) {
                        Toast.makeText(InventoryDetail.this,"ERROR DE CONEXION CON EL SERVIDOR",Toast.LENGTH_SHORT).show();
                    }
                });
            }

    }


    /**
     * controla la salida de la app
     * @param view
     */
    public void salirAPP(View view) {
        //finish();
        endActivityContol();;
    }


    /**
     * Boton ACEPTAR
     * @param view
     */
    public void btnAceptar(View view) {

        InventoryItems item = new InventoryItems();
        item.setDeviceId(idMovil);
        item.setInventoryId(inventorySelected.getId());
        item.setItemStatus("OPTIMO");
        item.setProductId(producto.getIdentifier());
        item.setQuantity(Double.parseDouble(cantidad.getText().toString().trim()));
        item.setUsr(usr.getName());
        item.setWorkerId(contdorSelected.getId());

        // Grabamos el item
        InventoryItemAPI itemAPI = retrofit.create(InventoryItemAPI.class);
        Call<InventoryItems>  call = itemAPI.saveItem(item);
        call.enqueue(new Callback<InventoryItems>() {
            @Override
            public void onResponse(Call<InventoryItems> call, Response<InventoryItems> response) {

                if( response.isSuccessful() ){

                    //itemsDetail.add(item);
                    //Log.i("ITEMS","Items  : "+itemsDetail.size());
                    Toast.makeText(InventoryDetail.this,"Item Guardado",Toast.LENGTH_SHORT).show();
                    rowVo.setCantidad(Double.parseDouble(cantidad.getText().toString().trim()));
                    itemsVo.add(rowVo);

                    // Clear data
                    codigo.setText("");
                    txtDescripcion.setText("");
                    cantidad.setText("");
                    codigo.requestFocus();

                    if( itemsVo.size() > 0 ){
                        btnDetalle.setEnabled(true);
                        btnDetalle.refreshDrawableState();
                    }

                }else{
                    Toast.makeText(InventoryDetail.this,"Error al guardar el item..",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<InventoryItems> call, Throwable t) {
                Toast.makeText(InventoryDetail.this,"ERROR DE CONEXION CON EL SERVIDOR AL GUARDAR ITEMS",Toast.LENGTH_SHORT).show();
            }
        });

        //Log.i("ITEMS", "Cantidad : "+ itemsDetail.size());

    }

    @Override
    public void onBackPressed() {
        endActivityContol();
    }


    /**
    *
    */
    private void endActivityContol()
    {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(InventoryDetail.this);
        dialogo.setMessage("Desea salir de la pantalla de registro ?").setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();;
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
        });

        AlertDialog alert = dialogo.create();
        alert.setTitle("  Atencion  ");
        alert.show();

    }


    /**
     * Consultar detalle de Items  de Iventario
     * @param view
     */
    public void consultarDetalle(View view) {

        Intent detalleIntent = new Intent(InventoryDetail.this,  DetalleActivity.class);
        detalleIntent.putExtra("Det", itemsVo);
        startActivity(detalleIntent);

    }


    /**
    * Al pulsar ENTER en el campo codigo
    * @param v
    * @param keyCode
    * @param event
    * @return
    */
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

            if( event.getAction() == KeyEvent.ACTION_DOWN &&
                event.getKeyCode() == KeyEvent.KEYCODE_ENTER ){
                Log.i("DATA","Se Pulso ENTER");

                //Buscamos si el producto existe
                ProductoAPI prodApi = retrofit.create(ProductoAPI.class);
                Call<Producto>  callProd = prodApi.getProducto(codigo.getText().toString().trim(), usr.getUnitId());

                callProd.enqueue(new Callback<Producto>() {
                    @Override
                    public void onResponse(Call<Producto> call, Response<Producto> response) {

                        if(response.isSuccessful()){
                            producto = new Producto();
                            producto = response.body();

                            if( producto != null ) {
                                rowVo = new DetalleItemsVo();
                                rowVo.setCodigoBarras(producto.getCodigoBarras());
                                rowVo.setCodigoInterno(Long.parseLong(producto.getCodigoInterno()));
                                rowVo.setDescripcion(producto.getDescripcion());
                                txtDescripcion.setText(producto.getDescripcion());
                                cantidad.setEnabled(true);

                            }else{
                                Log.i("ERROR_PRODUCTO","Not Product.....");
                                codigo.setError("Producto no registrado...");
                                codigo.requestFocus();
                                cantidad.setEnabled(false);
                            }

                        }else{

                            Toast.makeText(InventoryDetail.this,"Erro de Conexion con el Servidor",Toast.LENGTH_LONG).show();;

                        }

                    }

                    @Override
                    public void onFailure(Call<Producto> call, Throwable t) {

                    }
                });

                return true;
            }
        return false;
    }



    //Validador de Boton Aceptar
    private TextWatcher  validWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

                String codigoProd = codigo.getText().toString();
                String cantProd = cantidad.getText().toString();

                btnAceptar.setEnabled( !codigoProd.isEmpty() && !cantProd.isEmpty() );

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    //Items Selected Spiner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()){
            case R.id.spContador:

                String contSelected = (String) parent.getItemAtPosition(position);
                for (Contador cont : countLst){
                        if( cont.getId() == Long.parseLong(contSelected.substring(0,contSelected.indexOf("-")).trim())){
                            contdorSelected = cont;
                            Log.i("SELECTED","Contador Seleccionado  : "+contdorSelected.getUserName());
                        }
                }
                break;


            case R.id.spNroInventario:
                String invSelected = (String) parent.getItemAtPosition(position);
                for( Inventory inv : invLst ){
                    if( inv.getId() == Long.parseLong( invSelected.substring(0,invSelected.indexOf("-")).trim()) ){
                        inventorySelected = inv;
                        Log.i("SELECTED","Inv : "+inventorySelected.getName());
                        cargarSpeenerContador(inventorySelected.getId(), inventorySelected.getUnitId());
                    }
                }
                break;

            default:
                break;

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }




    private void cargarSpeenerContador( Long invId, Long unitId){

        // Cargamos la lista de Contadores
        ContadorAPI contApi = retrofit.create(ContadorAPI.class);
        Log.i("COUNTER"," UnitIid      : "+unitId);
        Log.i("COUNTER"," Inventory Id : "+invId);

        //Call<ArrayList<Contador>> call = contApi.getContador(usr.getSiteId(), invId);
        Call<ArrayList<Contador>> call = contApi.getContador(unitId, invId);
        call.enqueue(new Callback<ArrayList<Contador>>() {
            @Override
            public void onResponse(Call<ArrayList<Contador>> call, Response<ArrayList<Contador>> response) {
                if( response.isSuccessful()){
                    countLst = response.body();
                    conterSP = new ArrayList<>();

                        if( countLst != null ) {
                            for (Contador cont : countLst) {
                                conterSP.add(cont.getId() + " - " + cont.getNombreApellido());
                            }
                        }

                    spContador.setAdapter(new ArrayAdapter<String>(InventoryDetail.this, android.R.layout.simple_spinner_dropdown_item,conterSP));

                }else{
                    Toast.makeText(InventoryDetail.this,"No hay CONTADORES asignados",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Contador>> call, Throwable t) {
                Toast.makeText(InventoryDetail.this,"ERROR DE CONEXION CON EL SERVIDOR",Toast.LENGTH_SHORT).show();
            }
        });

    }

}