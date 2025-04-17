package info.androidhive.assignment9;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Reservation extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ModelClass> arrayList = new ArrayList<>();
    ReserveAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.reservation_recycler_view);
        ArrayList<ModelClass> res = new ArrayList<>();
        res = getData();
        TextView nores = (TextView) findViewById(R.id.nobookings);
        if(res.size()==0){
            nores.setText("No Bookings found!");
        }
        else {
            nores.setText("");
            myAdapter = new ReserveAdapter((Reservation) Reservation.this, res);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(this,
                    LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(myAdapter);
            setSwipeToDelete();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setSwipeToDelete(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                ModelClass modelClass = arrayList.get(viewHolder.getAdapterPosition());
                int position = viewHolder.getAdapterPosition();

                ArrayList<ArrayList<String>> reservations = new ArrayList<>();

                SharedPreferences pref = getSharedPreferences("Reservations", 0);
                SharedPreferences.Editor editor = pref.edit();

                Gson gson = new Gson();

                if( pref.getString("reservation", null) != null){

                    String jsonText = pref.getString("reservation", null);
                    Log.d("Existing res", jsonText);
                    Type type = new TypeToken<ArrayList<ArrayList<String>>>() {}.getType();
                    reservations = gson.fromJson(jsonText, type);
                    reservations.remove(viewHolder.getAdapterPosition());
                    Log.d("Del res", reservations.toString());
                    String updatedjsonText = gson.toJson(reservations);
                    editor.putString("reservation", updatedjsonText);
                    editor.apply();
                }

                arrayList.remove(viewHolder.getAdapterPosition());
                TextView nores = (TextView) findViewById(R.id.nobookings);
                if(arrayList.size()==0){
                    nores.setText("No Bookings found!");
                }
                else{
                    nores.setText("");
                }

                myAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                Snackbar.make(recyclerView, "Removing Existing Reservation", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder){
                return 1f;
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
                setDeleteIcon(c,recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                super.onChildDraw(c,recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void setDeleteIcon(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
        Paint mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        ColorDrawable mBackground = new ColorDrawable();
        int backgroundColor = Color.parseColor("#b80f0a");
        Drawable deleteDrawable = ContextCompat.getDrawable(this, R.drawable.ic_delete);
        int intrinsicWidth = deleteDrawable.getIntrinsicWidth();
        int intrinsicHeight = deleteDrawable.getIntrinsicHeight();

        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getHeight();

        boolean isCancelled = dX == 0 && !isCurrentlyActive;

        if(isCancelled){
            c.drawRect(itemView.getRight() + dX, (float) itemView.getTop(),
                    (float) itemView.getRight(), (float) itemView.getBottom(), mClearPaint);
            return;
        }

        mBackground.setColor(backgroundColor);
        mBackground.setBounds(itemView.getRight()+ (int)dX, itemView.getTop(),
                 itemView.getRight(),  itemView.getBottom());
        mBackground.draw(c);

        int deleteIconTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
        int deleteIconMargin = (itemHeight - intrinsicHeight)/2;
        int deleteIconLeft = itemView.getRight() - deleteIconMargin - intrinsicWidth;
        int deleteIconRight = itemView.getRight() - deleteIconMargin;
        int deleteIconBottom = deleteIconTop + intrinsicHeight;

        deleteDrawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
        deleteDrawable.draw(c);


    }

    private ArrayList<ModelClass> getData(){
        ArrayList<ArrayList<String>> reservations = new ArrayList<>();

        SharedPreferences pref = this.getSharedPreferences("Reservations", 0);
        SharedPreferences.Editor editor = pref.edit();

        Gson gson = new Gson();

        if( pref.getString("reservation", null) != null){

            String jsonText = pref.getString("reservation", null);
            Log.d("Existing res", jsonText);
            Type type = new TypeToken<ArrayList<ArrayList<String>>>() {}.getType();
            reservations = gson.fromJson(jsonText, type);
        }


        for(int i=0;i<reservations.size();i++){
                arrayList.add(new ModelClass(reservations.get(i).get(0),reservations.get(i).get(1),reservations.get(i).get(2),reservations.get(i).get(3)));
        }
        return arrayList;
    }
}