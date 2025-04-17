package info.androidhive.assignment9;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ReviewFragment extends Fragment {

    static ArrayList<String> reviews_list = new ArrayList<>();

    public static ReviewFragment getInstance(){
        ReviewFragment reviewFragment= new ReviewFragment();
        return reviewFragment;
    }

    public static void setReviews(ArrayList<String> review_list) {
        reviews_list=review_list;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate( R.layout.reviews_tab, container,false);
        TextView user1 = (TextView) view.findViewById(R.id.user1);
        user1.setText(reviews_list.get(0));
        TextView rating1 = (TextView) view.findViewById(R.id.rating1);
        rating1.setText("Rating :"+reviews_list.get(1)+"/5");
        TextView review1 = (TextView) view.findViewById(R.id.review1);
        review1.setText(reviews_list.get(2));
        TextView date1 = (TextView) view.findViewById(R.id.date1);
        date1.setText(reviews_list.get(3));

        TextView user2 = (TextView) view.findViewById(R.id.user2);
        user2.setText(reviews_list.get(4));
        TextView rating2 = (TextView) view.findViewById(R.id.rating2);
        rating2.setText("Rating :"+reviews_list.get(5)+"/5");
        TextView review2 = (TextView) view.findViewById(R.id.review2);
        review2.setText(reviews_list.get(6));
        TextView date2 = (TextView) view.findViewById(R.id.date2);
        date2.setText(reviews_list.get(7));

        TextView user3 = (TextView) view.findViewById(R.id.user3);
        user3.setText(reviews_list.get(8));
        TextView rating3 = (TextView) view.findViewById(R.id.rating3);
        rating3.setText("Rating :"+reviews_list.get(9)+"/5");
        TextView review3 = (TextView) view.findViewById(R.id.review3);
        review3.setText(reviews_list.get(10));
        TextView date3 = (TextView) view.findViewById(R.id.date3);
        date3.setText(reviews_list.get(11));
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

//        TextView user1 = (TextView) view.findViewById(R.id.user1);
//        user1.setText(reviews_list.get(0));
//        TextView rating1 = (TextView) view.findViewById(R.id.rating1);
//        rating1.setText(reviews_list.get(1)+"/5");
//        TextView review1 = (TextView) view.findViewById(R.id.review1);
//        review1.setText(reviews_list.get(2));
//        TextView date1 = (TextView) view.findViewById(R.id.date1);
//        date1.setText(reviews_list.get(3));
//
//        TextView user2 = (TextView) view.findViewById(R.id.user2);
//        user2.setText(reviews_list.get(4));
//        TextView rating2 = (TextView) view.findViewById(R.id.rating2);
//        rating2.setText(reviews_list.get(5)+"/5");
//        TextView review2 = (TextView) view.findViewById(R.id.review2);
//        review2.setText(reviews_list.get(6));
//        TextView date2 = (TextView) view.findViewById(R.id.date2);
//        date2.setText(reviews_list.get(7));
//
//        TextView user3 = (TextView) view.findViewById(R.id.user3);
//        user3.setText(reviews_list.get(8));
//        TextView rating3 = (TextView) view.findViewById(R.id.rating3);
//        rating3.setText(reviews_list.get(9)+"/5");
//        TextView review3 = (TextView) view.findViewById(R.id.review3);
//        review3.setText(reviews_list.get(10));
//        TextView date3 = (TextView) view.findViewById(R.id.date3);
//        date3.setText(reviews_list.get(11));

    }
}
