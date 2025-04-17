package info.androidhive.assignment9;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, BusinessTableAdapter.OnItemClickListener {

    public static final String BUSINESS_ID = "businessID";
    public static final String BUSINESS_NAME = "businessName";

    private RecyclerView recyclerView;
    private BusinessTableAdapter rcvadapter;
    private ArrayList<BusinessItem> businessList;

    private JsonObjectRequest mJsonObjectRequest;
    String []categry = new String[]{"all","arts","health","hotelstravel","food","professional"};
    private String url_autocomplete = "https://webdev-assignment8.wl.r.appspot.com/autocomplete?keyword=";
    private String url_ipinfolocation= "https://webdev-assignment8.wl.r.appspot.com/iplocation";
    private String url_gcplocation= "https://webdev-assignment8.wl.r.appspot.com/googlelocation?user_location=";
    private String url_searchbusinesses= "https://webdev-assignment8.wl.r.appspot.com/business_search";
    private String latitude="";
    private String longitude="";
    private String category_api="none";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        businessList=new ArrayList<>();

        ImageView reservations_button = (ImageView) findViewById(R.id.tv_header_title);
        reservations_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Reservation.class);
                view.getContext().startActivity(intent);}
        });

        AutoCompleteTextView autocomplete = (AutoCompleteTextView) findViewById(R.id.autoComplete_keyword);


        autocomplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //retrieve data s
                Log.d("String", String.valueOf(s));
//                getData(s);

                RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());

                mJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_autocomplete+String.valueOf(s), null,

                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                Log.e("Response: " , response.toString());
                                try {
                                    List<String> keywordOptions = new ArrayList<String>();

                                    keywordOptions.clear();
                                    JSONArray categories=response.getJSONArray("categories");

                                    for (int i = 0; i < categories.length(); i++) {

                                        JSONObject category = categories.getJSONObject(i);

                                        String title = category.getString("title");
                                        keywordOptions.add(title);
                                    }

                                    categories=response.getJSONArray("terms");

                                    for (int i = 0; i < categories.length(); i++) {

                                        JSONObject category = categories.getJSONObject(i);

                                        String title = category.getString("text");
                                        keywordOptions.add(title);
                                    }
                                    Log.d("keyowrds in for",keywordOptions.toString());
                                    ArrayAdapter<String> autoAdapter;
                                    autoAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, keywordOptions);
                                    autocomplete.setAdapter(autoAdapter);
                                    autoAdapter.notifyDataSetChanged();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                                autocomplete.dismissDropDown();
                                Log.e("Error: " , error.toString());
                            }
                        });

                mRequestQueue.add(mJsonObjectRequest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Resources res = getResources();
        String[] categories = res.getStringArray(R.array.categories_dropdown);

        Spinner spin = findViewById(R.id.categories_dropdown);

        ArrayAdapter<CharSequence> ad = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                categories
                );

        ad.setDropDownViewResource(android.R.layout
                        .simple_spinner_item);

        spin.setAdapter(ad);
        spin.setOnItemSelectedListener(this);

        EditText distanceinput = (EditText) findViewById(R.id.input_distance);
        EditText location=(EditText)findViewById(R.id.inputLocation);
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);

        Button button_clear = (Button) findViewById(R.id.button_clear);
        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autocomplete.setText("");
                distanceinput.setText("");
                spin.setSelection(0);
                location.setText("");
                if(checkBox.isChecked()){
                    checkBox.toggle();
                    location.setVisibility(View.VISIBLE);
                }
                TextView nobusiness = (TextView) findViewById(R.id.nobusiness);
                nobusiness.setText("");
                recyclerView.setAdapter(new BusinessTableAdapter(getApplicationContext(),new ArrayList<BusinessItem>()));
            }
        });



        Button button_submit = (Button) findViewById(R.id.button_submit);
//
        button_submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                String keyword = autocomplete.getText().toString();
                String distance = distanceinput.getText().toString();
                String category = spin.getSelectedItem().toString();
                EditText location=(EditText)findViewById(R.id.inputLocation);
                String u_location = location.getText().toString();

                if(TextUtils.isEmpty(keyword)){
                    autocomplete.setError("This field is required!");
                    return;
                }

                if(TextUtils.isEmpty(distance)){
                    distanceinput.setError("This field is required!");
                    return;
                }


                CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
                if(TextUtils.isEmpty(u_location) && !checkBox.isChecked()){
                    location.setError("This field is required!");
                    return;
                }

                Log.e("Keyword: ",keyword);

                RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
                mRequestQueue.start();


                if (checkBox.isChecked()) {

                    mJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_ipinfolocation, null,

                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String location=response.getString("loc");
                                        String[] coordinates = location.split(",");
                                        latitude=coordinates[0];
                                        longitude=coordinates[1];

                                        businessData(keyword,distance,category_api,latitude,longitude);
                                        Log.d("IP JSON", latitude+" at "+ longitude);
//                                        businessData(keyword, distance, category, latitude, longitude);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error in IPinfo: " , error.toString());
                                }
                            });

                    mRequestQueue.add(mJsonObjectRequest);
                }
                else{

                    mJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_gcplocation+u_location, null,

                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Log.d("Google JSON ",response.toString());
                                           JSONArray results=response.getJSONArray("results");
                                           JSONObject loc_obj=results.getJSONObject(0);
                                           JSONObject geometry = loc_obj.getJSONObject("geometry");
                                            Log.d("Google JSON ",geometry.toString());
                                           JSONObject location = geometry.getJSONObject("location");
                                        Log.d("Google JSON ",location.toString());
                                           latitude=location.getString("lat");
                                           longitude=location.getString("lng");

                                        businessData(keyword,distance,category,latitude,longitude);
                                        Log.d("Google JSON", latitude+" at "+ longitude);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error in IPinfo: " , error.toString());
                                }
                            });

                    mRequestQueue.add(mJsonObjectRequest);

                }
            }
        });

    }

    public void businessData(String keyword, String distance, String category, String latitude, String longitude){

        businessList.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        Spinner spin = findViewById(R.id.categories_dropdown);
        int cat_pos= spin.getSelectedItemPosition();
        Log.d("form field",category);
        Log.d("Form Fields ", "?keyword="+keyword+"&distance="+distance+"&category="+categry[cat_pos]+"&latitude="+latitude+"&longitude="+longitude);

        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_searchbusinesses+"?keyword="+keyword+"&distance="+distance+"&category="+categry[cat_pos]+"&latitude="+latitude+"&longitude="+longitude, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String location=response.getString("businesses");
                            Log.d("Search Results", response.toString());

                            JSONArray businesses=response.getJSONArray("businesses");

                            for (int i = 0; i < businesses.length(); i++) {

                                JSONObject business = businesses.getJSONObject(i);

                                String id= business.getString("id");
                                String name = business.getString("name");
                                String image = business.getString("image_url");
                                String rating = business.getString("rating");
                                float distance_meters = Float.parseFloat(business.getString("distance"));
                                int distance_miles = (int)(distance_meters/1609.94);
                                String distance = ""+distance_miles;
                                String index=Integer.toString(i+1);
                                businessList.add(new BusinessItem(index,image,name,rating,distance, id));
                            }

                            System.out.println("Business Table"+businessList);

                            TextView nobusiness = (TextView) findViewById(R.id.nobusiness);

                            if(businessList.size()==0){
                                nobusiness.setText("No Results Found!");
                                recyclerView.setAdapter(new BusinessTableAdapter(getApplicationContext(),new ArrayList<BusinessItem>()));
                            }
                            else {
                                nobusiness.setText("");
                                rcvadapter = new BusinessTableAdapter(MainActivity.this, businessList);
                                recyclerView.setAdapter(rcvadapter);
                                rcvadapter.setOnItemClickListener(MainActivity.this);
                            }
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

    public void onCheckboxClicked(View view) {

        EditText location = (EditText) findViewById(R.id.inputLocation);
        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        if (checkBox.isChecked()) {
            Log.d("in is checked", "in here");
            location.setVisibility(View.INVISIBLE);
        }
        else{
            location.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text=parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(int position) {
        Intent cardIntent = new Intent(this, BusinessDetailActivity.class );
        BusinessItem clickedItem = businessList.get(position);
        cardIntent.putExtra(BUSINESS_ID, clickedItem.getBusiness_id());
        cardIntent.putExtra(BUSINESS_NAME, clickedItem.getBusiness_name());

        startActivity(cardIntent);
    }
}

