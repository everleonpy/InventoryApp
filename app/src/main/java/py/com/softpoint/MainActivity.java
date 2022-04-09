package py.com.softpoint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import py.com.softpoint.pojos.User;
import py.com.softpoint.services.UserAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private EditText userName, password;
    private TextView tvVersion;
    private User usuarioLogin = null;
    private String stgIdMovil;
    private String urlBase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.txtUser);
        password = findViewById(R.id.txtPassword);
        tvVersion = findViewById(R.id.tvVersionAPP);

        //Tomamos el identificador de dispositivo desde un properties
        stgIdMovil = getProperty("ID_DEVICE",getApplicationContext());
        urlBase = getProperty("URL_BASE",getApplicationContext());
        tvVersion.setText(getProperty("VERSION_APP",getApplicationContext()));
        Log.i("MOVIL_ID","Id : "+stgIdMovil + "  "+urlBase);

        // Setin time out httpclient
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();


        retrofit = new Retrofit.Builder().client(okHttpClient)
                        .baseUrl(urlBase)
                        .addConverterFactory(GsonConverterFactory.create()).build();


    }

    /**
     * Validamos acceso al systema via Usuario / Password
     */
    public void loginApp(View v){

        if( stgIdMovil != null ) {
            userLogin(userName.getText().toString());
        }else{
            Toast.makeText(MainActivity.this,"No se a definido el ID del Dispositivo..",Toast.LENGTH_LONG).show();
        }
    }


    /**
    *
    * @param userName
    */
    private void  userLogin( String userName){

        UserAPI userAPI = retrofit.create(UserAPI.class);
        Call<User> call = userAPI.find(userName);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                try{
                    if( response.isSuccessful() ) {

                        usuarioLogin = response.body();
                        Log.i("TEST", "Usr : "+usuarioLogin.getPassword());

                        if( usuarioLogin.getName() != null  ){

                           if(usuarioLogin.getPassword().equalsIgnoreCase(password.getText().toString())) {
                               Bundle tmp = new Bundle();
                               tmp.putSerializable("usr", usuarioLogin);
                               tmp.putLong("idMovil",Long.parseLong(stgIdMovil.trim()));
                               tmp.putString("urlBase",urlBase);

                               Intent intx = new Intent(MainActivity.this, InventoryDetail.class);
                               intx.putExtras(tmp);
                               startActivity(intx);
                           }else {
                               Toast.makeText(MainActivity.this,"El password es incorrecto para "+userName,Toast.LENGTH_LONG).show();
                           }

                        }else{
                            Toast.makeText(MainActivity.this,"No existe el usuario : "+userName,Toast.LENGTH_LONG).show();
                        }

                    }else{
                        Toast.makeText(MainActivity.this,"Sin repuesta del Servidor",Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {e.printStackTrace(); }
                return;
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "ERROR EN CONEXION CON EL SERVIDOR",
                                   Toast.LENGTH_LONG).show();
            }
        }) ;

    }



    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        this.finish();
    }

    /**
    * Utilitario para leer un archivo .properties
    * @param key
    * @param context
    * @return
    */
    private static String getProperty(String key, Context context) {
        try {
            Properties properties = new Properties();
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("config.properties");
            properties.load(inputStream);
            return properties.getProperty(key);
        }catch ( Exception e){
            e.printStackTrace();
        }
        return null;
    }
}