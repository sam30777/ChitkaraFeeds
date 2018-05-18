package com.example.android.feedbackchitkara.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.feedbackchitkara.R;
import com.example.android.feedbackchitkara.data.MainData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{
   private ArrayList<MainData> arrayList;
   private Context context;
   private ListItemInterface listItemInterface;
   private Float finalRating1;
    private Float finalRating2;
    private Float finalRating3;
    private Float finalRating4;

   public MainAdapter(Context context, ArrayList<MainData> arrayList, ListItemInterface listItemInterface){
       this.arrayList=arrayList;
       this.context=context;
       this.listItemInterface=listItemInterface;
   }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        MainData mainData=arrayList.get(position);
       holder.imageView.setImageResource(mainData.getImage_id());
       holder.feedPackTopic.setText(mainData.getTitle_name());
      final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(context.getString(R.string.topics))
              .child(mainData.getTitle_name()).child(context.getString(R.string.atingsOverall)).child(context.getString(R.string.overall_rating));
         databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
              if(dataSnapshot.getValue(Float.class)!=null){
                  holder.ratingbar.setRating(dataSnapshot.getValue(Float.class));
              }else {
                  holder.ratingbar.setRating(0);
              }

             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });

    }

    @Override
    public int getItemCount() {
        if(arrayList!=null)
        return arrayList.size();
        else
            return 0;
    }
    public interface ListItemInterface{
       void onItemClicked(int position);
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView feedPackTopic;
        RatingBar ratingbar;
        TextView reviewsCount;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.topic_image);
            feedPackTopic=itemView.findViewById(R.id.topic_name);
            ratingbar=itemView.findViewById(R.id.rating_indicator);

           // reviewsCount=itemView.findViewById(R.id.reviews_count);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos=getAdapterPosition();
            listItemInterface.onItemClicked(pos);
        }
    }
}
