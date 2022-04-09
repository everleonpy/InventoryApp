package py.com.softpoint.services;

import py.com.softpoint.pojos.Producto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductoAPI {

    @GET("api/producto/get/{codigo}/{siteId}")
    public Call<Producto> getProducto(@Path("codigo") String codigo, @Path("siteId") Long siteId );

}
