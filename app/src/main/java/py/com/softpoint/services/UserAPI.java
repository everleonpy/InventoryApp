package py.com.softpoint.services;


import py.com.softpoint.pojos.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserAPI {

   @GET("api/user/{userName}")
   public Call<User> find(@Path("userName") String userName);

}
