package lbt.com.amthuc.Views.ChiTietBaiViet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.kongzue.dialog.v2.WaitDialog;

import lbt.com.amthuc.R;
import lbt.com.amthuc.models.objectClass.GoogleMap.ObjGeocoding;
import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;
import lbt.com.amthuc.retrofit2.ApiUtils;
import lbt.com.amthuc.retrofit2.getData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;

    private Toolbar toolbar;

    private getData getDataRetrofit2;

    private float zoom = 15.0f;

    objbaiviet_app mBaiViet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        initView();
        WaitDialog.show(this,"Loading");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    private void getDataRetrofit2(final objbaiviet_app mBaiViet) {
        String baseURL = "https://maps.googleapis.com/maps/api/geocode/";
        getDataRetrofit2 = ApiUtils.getApiUtils(baseURL);

        Call<ObjGeocoding> resultsGeocoding = getDataRetrofit2.getGeocoding(mBaiViet.getChitiet().getDiachi(),getText(R.string.API_MAP).toString());
        resultsGeocoding.enqueue(new Callback<ObjGeocoding>() {
            @Override
            public void onResponse(Call<ObjGeocoding> call, Response<ObjGeocoding> response) {

                WaitDialog.dismiss();
                ObjGeocoding obj = response.body();
                if(obj.getStatus().matches("OK")) {
                    LatLng position = new LatLng(
                            obj.getResults().get(0).getGeometry().getLocation().getLat(),
                            obj.getResults().get(0).getGeometry().getLocation().getLng());

                    mMap.addMarker(new MarkerOptions().position(position).title(obj.getResults().get(0).getFormattedAddress()));

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
                }else {
                    Toast.makeText(GoogleMapActivity.this, getText(R.string.diachikhonghople).toString(), Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

            @Override
            public void onFailure(Call<ObjGeocoding> call, Throwable t) {
                Toast.makeText(GoogleMapActivity.this, getText(R.string.khongcodulieu).toString(), Toast.LENGTH_SHORT).show();
                Log.e("MAP",t.toString());
                finish();
            }
        });

    }

    private void getData() {
        mBaiViet = new objbaiviet_app();
        Bundle bundle = getIntent().getBundleExtra("data");
        if(bundle!=null){
           // Log.e("kiemtra", new Gson().toJson((objbaiviet_app) bundle.getSerializable("baiviet")));
            mBaiViet = (objbaiviet_app) bundle.getSerializable("baiviet");
            getDataRetrofit2(mBaiViet);

        }else {
            WaitDialog.dismiss();
            finish();
        }
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbarBanDo);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getData();
    }
}
