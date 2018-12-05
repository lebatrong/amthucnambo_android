package lbt.com.amthuc.retrofit2;

import lbt.com.amthuc.models.objectClass.GoogleMap.ObjGeocoding;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface getData {

    @GET("json")
    Call<ObjGeocoding> getGeocoding(@Query("address") String address,  @Query("key") String key );
}
