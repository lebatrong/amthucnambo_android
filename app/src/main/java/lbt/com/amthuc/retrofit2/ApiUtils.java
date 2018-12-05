package lbt.com.amthuc.retrofit2;

public class ApiUtils {

    public static getData getApiUtils(String BASE_URL) {
        return RetrofitClient.getClient(BASE_URL).create(getData.class);
    }
}
