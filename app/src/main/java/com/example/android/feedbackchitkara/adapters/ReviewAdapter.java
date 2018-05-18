package com.example.android.feedbackchitkara.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.feedbackchitkara.R;
import com.example.android.feedbackchitkara.data.UserReview;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
     ArrayList<UserReview> arrayList;
     Context context;
     public ReviewAdapter(ArrayList<UserReview> arrayList,Context context){
         this.arrayList=arrayList;
         this.context=context;
     }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserReview userReview=arrayList.get(position);
        holder.user_name.setText(userReview.getUser_name());
        holder.user_review.setText(userReview.getUser_review());
        Uri uri=Uri.parse(userReview.getUser_image_url());
        Picasso.with(context).load(uri).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
         if(arrayList!=null){
             return arrayList.size();
         }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView user_review;
        TextView user_name;
        public ViewHolder(View itemView) {
            super(itemView);
            user_name=itemView.findViewById(R.id.user_name_item);
            imageView=itemView.findViewById(R.id.user_image_item);
            user_review=itemView.findViewById(R.id.user_review_item);

        }
    }
}
