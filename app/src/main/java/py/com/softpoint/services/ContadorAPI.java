package py.com.softpoint.services;

import java.util.ArrayList;

import py.com.softpoint.pojos.Contador;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ContadorAPI {

    @GET("api/counter/getall/{siteId}/{inventoryID}/")
    public Call<ArrayList<Contador>> getContador(@Path("siteId") Long siteId,
                                                 @Path("inventoryID") Long inventoryId);

}
