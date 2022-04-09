package py.com.softpoint.services;

import py.com.softpoint.pojos.InventoryItems;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InventoryItemAPI {

    @POST("api/inventory/save")
    Call<InventoryItems> saveItem(@Body InventoryItems item);

}
