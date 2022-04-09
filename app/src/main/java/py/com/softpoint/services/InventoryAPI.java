package py.com.softpoint.services;

import java.util.ArrayList;

import py.com.softpoint.pojos.Inventory;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface InventoryAPI {

    @GET("api/inventory/opened/{unitId}")
    public Call<ArrayList<Inventory>> getAllOpened(@Path("unitId") Long unitId);
}
