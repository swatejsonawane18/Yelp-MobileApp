package info.androidhive.assignment9;

import static info.androidhive.assignment9.MainActivity.BUSINESS_ID;
import static info.androidhive.assignment9.MainActivity.BUSINESS_NAME;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BusinessDetailActivity extends AppCompatActivity {
    String []tabNames = new String[]{"BUSINESS DETAILS","MAP LOCATION","REVIEWS"};
    TabLayout tabLayout;
    ViewPager2 viewPager;
    private String url_businessdetail= "https://webdev-assignment8.wl.r.appspot.com/card_business?id=";
    private String url_businessreviews= "https://webdev-assignment8.wl.r.appspot.com/card_reviews?id=";
    ArrayList<String> reviews_list = new ArrayList<>();
    ArrayList<Data> imageList;
    String businessURL="";
    String name, price, phone, status,value, address, categories ;
    double lat,lng;
    ArrayList<String>imageArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent =getIntent();
        String businessID= intent.getStringExtra(BUSINESS_ID);
        String businessName= intent.getStringExtra(BUSINESS_NAME);

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewPager);

        TextView name = (TextView) findViewById(R.id.busines_name);
        name.setText(businessName);

        businessDetails(businessID);
        getReviews(businessID);
//        getTabs();

        ImageView twitter = (ImageView) findViewById(R.id.twitter_header);
        twitter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String detail = "Check "+ businessName + " on Yelp." + System.getProperty("line.separator") + businessURL;
                String url = "https://twitter.com/intent/tweet?text="+detail;

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        ImageView facebook = (ImageView) findViewById(R.id.facebook_header);
        facebook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = "https://www.facebook.com/sharer/sharer.php?u="+businessURL;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


    }



    public void businessDetails(String id){

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_businessdetail+id, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.d("Business Result - ", response.toString());

                            name = response.getString("name");

                            price ="";
                            if((response.has("price") && response.getString("price").equals("") )|| !response.has("price")){
                                price="N/A";
                            }
                            else{
                                price=response.getString("price");
                            }

                            Log.d("in phone", response.has("display_phone")+"");
                            if((response.has("display_phone") && response.getString("display_phone").equals("") )|| !response.has("display_phone") ){
                                phone="N/A";
                                Log.d("in phone", phone);
                            }
                            else{
                                phone=response.getString("display_phone");
                            }

                            JSONArray hours=response.getJSONArray("hours");
                            status =hours.getJSONObject(0).getString("is_open_now");

                            if(status=="true"){
                                status="Open Now";
                            }
                            else{
                                status="Closed";
                            }
                            String url = response.getString("url");
                            businessURL=url;

                            JSONObject location=response.getJSONObject("location");
                            address = "";
                            if(location.has("display_address") && !response.getString("display_phone").equals("") ) {
                                JSONArray address_list = location.getJSONArray("display_address");

                                for (int i = 0; i < address_list.length(); i++) {
                                    if (i != address_list.length() - 1) {
                                        address += address_list.getString(i) + " | ";
                                    } else {
                                        address += address_list.getString(i);
                                    }
                                }
                            }
                            else{
                                address="N/A";
                            }

                            JSONObject coordinates = response.getJSONObject("coordinates");
                            String lat1 = coordinates.getString("latitude");
                            String lng1 = coordinates.getString("longitude");

                             lat = Double.parseDouble(lat1);
                             lng = Double.parseDouble(lng1);


                            categories="";

                            JSONArray category=response.getJSONArray("categories");

                            for (int i = 0; i < category.length(); i++) {

                                JSONObject category2 = category.getJSONObject(i);
                                if(i!=category.length()-1)
                                    categories += category2.getString("title")+" | ";
                                else
                                    categories+=  category2.getString("title");

                            }

//                            ArrayList<String> imageArray = new ArrayList<>();
                            JSONArray photos = response.getJSONArray("photos");
                            for(int i=0;i<photos.length();i++){
                                String photo = photos.getString(i);
                                imageArray.add(photo);
                            }

//                            value = "<a href="+url+"> Business Link </a>";
                            value=url;
                            Log.e("Card data", price+" "+phone+" "+status+" "+value+" "+categories);

                            viewPager.setAdapter(
                                    new TabAdapter(BusinessDetailActivity.this)
                            );

                            new TabLayoutMediator(
                                    tabLayout,
                                    viewPager,
                                    new TabLayoutMediator.TabConfigurationStrategy() {
                                        @Override
                                        public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                                            tab.setText(tabNames[position]);
                                        }
                                    }
                            ).attach();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error in Business " , error.toString());
                    }
                });

        requestQueue.add(mJsonObjectRequest);


    }

    public void getReviews(String id){

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_businessreviews+id, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            reviews_list.clear();
                            Log.d("Reviews Result - ", response.toString());

                            JSONArray reviews= response.getJSONArray("reviews");
                            for (int i = 0; i < reviews.length(); i++) {

                                JSONObject user_review = reviews.getJSONObject(i);
                                JSONObject user = user_review.getJSONObject("user");
                                reviews_list.add(user.getString("name"));
                                reviews_list.add(user_review.getString("rating"));
                                reviews_list.add(user_review.getString("text"));
                                reviews_list.add(user_review.getString("time_created").split(" ")[0]);
                            }
                            ReviewFragment.setReviews(reviews_list);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error in Business " , error.toString());
                    }
                });

        requestQueue.add(mJsonObjectRequest);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class TabAdapter extends FragmentStateAdapter {

        public TabAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        public TabAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        public TabAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case 0:
                    return new BusinessDetailFragment(name,address, price, phone, status, categories, value,imageArray,getApplicationContext());
                case 1:
                    return new MapLocationFragment(lat,lng);
                case 2:
                    return ReviewFragment.getInstance();
            }
            return new MapLocationFragment(lat,lng);
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}