package info.androidhive.assignment9;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BusinessTableAdapter extends RecyclerView.Adapter<BusinessTableAdapter.BusinessTableViewHolder> {

    private ArrayList<BusinessItem> businessList;
    private Context mContext;
    private OnItemClickListener mListener;


    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public BusinessTableAdapter(Context context, ArrayList<BusinessItem> businessList){
        mContext=context;
        this.businessList=businessList;
    }

    @NonNull
    @Override
    public BusinessTableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.business_row, parent, false);
        return  new BusinessTableViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessTableViewHolder holder, int position) {
        BusinessItem currentItem = businessList.get(position);

        String index= currentItem.getIndex();
        String imageURL= currentItem.getImageurl();
        String business_name= currentItem.getBusiness_name();
        String business_rating=currentItem.getBusiness_rating();
        String business_distance=currentItem.getBusiness_distance();
        Log.d("IN LOOP", ""+index);
        holder.mbusiness_id.setText(index);
        Glide.with(mContext).load(imageURL).fitCenter().centerInside().into(holder.mbusiness_image);
        holder.mbusiness_name.setText(business_name);
        holder.mbusiness_rating.setText(business_rating);
        holder.mbusiness_distance.setText(business_distance);
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }

    public class BusinessTableViewHolder extends RecyclerView.ViewHolder{
        public TextView mbusiness_id;
        public ImageView mbusiness_image;
        public TextView mbusiness_name;
        public TextView mbusiness_rating;
        public TextView mbusiness_distance;


        public BusinessTableViewHolder(@NonNull View itemView) {
            super(itemView);
            mbusiness_id = (TextView) itemView.findViewById(R.id.busines_id);
            mbusiness_image= (ImageView) itemView.findViewById(R.id.busines_image);
            mbusiness_name= (TextView) itemView.findViewById(R.id.busines_name);
            mbusiness_rating= (TextView) itemView.findViewById(R.id.busines_rating);
            mbusiness_distance= (TextView) itemView.findViewById(R.id.busines_distance);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}
