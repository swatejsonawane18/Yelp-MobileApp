package info.androidhive.assignment9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReserveAdapter extends RecyclerView.Adapter<ReserveAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ModelClass> data;

    public ReserveAdapter(Reservation context, ArrayList<ModelClass> data){
        this.context=context;
        this.data=data;
    }


    @NonNull
    @Override
    public ReserveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.reservation_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReserveAdapter.ViewHolder holder, int position) {
        ModelClass modelClass= data.get(position);
        holder.res_index.setText(position+1+"");
        holder.res_busines_name.setText(modelClass.getName());
        holder.res_date.setText(modelClass.getDate());
        holder.res_time.setText(modelClass.getTime());
        holder.res_email.setText(modelClass.getEmail());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView res_index;
        private TextView res_busines_name;
        private TextView res_date;
        private TextView res_time;
        private TextView res_email;
        private ConstraintLayout rowItem;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            this.res_index= itemView.findViewById(R.id.res_id);
            this.res_busines_name= itemView.findViewById(R.id.res_busines_name);
            this.res_date= itemView.findViewById(R.id.res_date);
            this.res_time= itemView.findViewById(R.id.res_time);
            this.res_email= itemView.findViewById(R.id.res_email);

            this.rowItem = itemView.findViewById(R.id.rowitem);

            rowItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, String.valueOf(data.get(getAdapterPosition())),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
