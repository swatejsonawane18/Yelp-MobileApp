package info.androidhive.assignment9;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

public class BusinessDetailFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    static String name,address, price, phone, status, categories, value="";
    static ArrayList<String> images;
    static ArrayList<Data> imageList=null;
    static Context context=null;

    EditText mEmail;
    EditText mDate;
    EditText mTime;

    public BusinessDetailFragment(String name1,String address1,String price1,String phone1,String status1,String categories1,String value1, ArrayList<String> imageArray,Context context1){
        name=name1;
        address=address1;
        price=price1;
        phone=phone1;
        status=status1;
        categories=categories1;
        value=value1;
        images=imageArray;
        imageList = new ArrayList<>();
        context=context1;

        imageList.add(new Data(images.get(0)));
        imageList.add(new Data(images.get(1)));
        imageList.add(new Data(images.get(2)));
        Log.d("in Constructor", images.toString());
    }

//    public static void setData(String name1,String address1,String price1,String phone1,String status1,String categories1,String value1, ArrayList<String> imageArray,Context context1){
//        name=name1;
//        address=address1;
//        price=price1;
//        phone=phone1;
//        status=status1;
//        categories=categories1;
//        value=value1;
//        images=imageArray;
//        imageList = new ArrayList<>();
//        context=context1;
//
//        imageList.add(new Data(images.get(0)));
//        imageList.add(new Data(images.get(1)));
//        imageList.add(new Data(images.get(2)));
//        Log.d("in Constructor", images.toString());
//    }

//    public static BusinessDetailFragment getInstance(){
//        BusinessDetailFragment businessDetailFragment = new BusinessDetailFragment();
//        return businessDetailFragment;
//    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate( R.layout.business_details_tab, container,false);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        TextView address_detail = (TextView) view.findViewById(R.id.detail_address);
        address_detail.setText(address);
        TextView price_detail = (TextView) view.findViewById(R.id.detail_price);
        price_detail.setText(price);
        TextView phone_detail = (TextView) view.findViewById(R.id.detail_phone_number);
        phone_detail.setText(phone);
        TextView status_detail = (TextView) view.findViewById(R.id.detail_status);
        if(status == "Open Now"){
            status_detail.setText(status);
            status_detail.setTextColor(Color.parseColor("#00FF00"));
        }else{
            status_detail.setText(status);
            status_detail.setTextColor(Color.parseColor("#FF0000"));
        }
        TextView category_detail = (TextView) view.findViewById(R.id.detail_category);
        category_detail.setText(categories);
        TextView yelp_detail = (TextView) view.findViewById(R.id.detail_yelp_link);
        String value2 = "<a href="+value+"> Business Link </a>";
        yelp_detail.setText(Html.fromHtml(value2));

        yelp_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(value));
                startActivity(i);
            }
        });


        RecyclerView viewPager2 = (RecyclerView) view.findViewById(R.id.viewPager3);

        viewPager2.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));

        if(imageList!=null) {
            Log.d("IN imageLIST", imageList.toString());
            Log.d("IN imageLIST", context.toString());
            ImageAdapter imageAdapter = new ImageAdapter(context, imageList);
            viewPager2.setAdapter(imageAdapter);
        }


        Button reserveButton = (Button) view.findViewById(R.id.reservebutton);
        reserveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("In Reserve Button",getContext().toString());
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.dialog_reservation,null);

                TextView name1 = (TextView) mView.findViewById(R.id.business_name);
                 mEmail = (EditText) mView.findViewById(R.id.editTextTextEmailAddress);
                 mDate = (EditText) mView.findViewById(R.id.editTextDate);
                 mTime = (EditText) mView.findViewById(R.id.editTextTime);

                 name1.setText(name);

                 TextView mClose = (TextView) mView.findViewById(R.id.canceltag);
                TextView mSubmit = (TextView) mView.findViewById(R.id.submittag);

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

                mDate.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        if (v == mDate) {
                            showDatePickerDialog();
                        }
                    }
                });

                mTime.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Calendar mcurrentTime = Calendar.getInstance();
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                mTime.setText( selectedHour + ":" + selectedMinute);
                            }
                        }, hour, minute, false);
                        mTimePicker.show();
                    }
                });


                mClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                mSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String email=mEmail.getText().toString();
                        String time = mTime.getText().toString();
                        String date = mDate.getText().toString();
                        String[] date1=date.split("/");
                        Log.d("Date",date1.toString());

                        date=date1[0]+"-"+date1[1]+"-"+date1[2];

                        String[] time1=time.split(":");

                        int hours=Integer.parseInt(time1[0]);
                        int minutes=Integer.parseInt(time1[1]);

                        if(! Pattern.matches("^(.+)@(.+)$",email )){
                            Toast.makeText(getContext(),"InValid Email ID", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                        else if(! ((hours>=10 && hours<17) || (hours==17 && minutes==0))){
                            Toast.makeText(getContext(),"Time should be between 10 AM AND 5 PM", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                        else if(!email.isEmpty() && !date.isEmpty() && !time.isEmpty()){

                            SharedPreferences pref = getContext().getSharedPreferences("Reservations", 0);
                            SharedPreferences.Editor editor = pref.edit();

                            Gson gson = new Gson();

                            if( pref.getString("reservation", null) != null){

                                String jsonText = pref.getString("reservation", null);
                                Log.d("Existing res", jsonText);
                                Type type = new TypeToken<ArrayList<ArrayList<String>>>() {}.getType();
                                ArrayList<ArrayList<String>> reservations = gson.fromJson(jsonText, type);
//                                List<String> reservations = Arrays.asList(jsonText);
                                for(int i=0;i<reservations.size();i++){
                                    if(reservations.get(i).get(0).equals(name)){
                                        reservations.remove(i);
                                    }
                                }

                                ArrayList<String> user_res = new ArrayList<String>();
                                user_res.add(name);
                                user_res.add(email);
                                user_res.add(date);
                                user_res.add(time);
                                reservations.add(user_res);
                                String updatedjsonText = gson.toJson(reservations);
                                editor.putString("reservation", updatedjsonText);
                                editor.apply();
                            }
                            else{
                                ArrayList<ArrayList<String>> reservations = new ArrayList<ArrayList<String>>();
                                ArrayList<String> user_res = new ArrayList<String>();
                                user_res.add(name);
                                user_res.add(email);
                                user_res.add(date);
                                user_res.add(time);
                                reservations.add(user_res);
                                String jsonText = gson.toJson(reservations);
                                editor.putString("reservation", jsonText);
                                editor.apply();
                                reservations.clear();
                            }


                            Toast.makeText(getContext(),"Reservation Complete", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    }
                });

            }
        });

    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String date = ""+month+"/"+dayOfMonth+"/"+year;
            mDate.setText(date);
    }
}


