package com.example.android.feedbackchitkara;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.android.feedbackchitkara.adapters.ReviewAdapter;
import com.example.android.feedbackchitkara.data.MainData;
import com.example.android.feedbackchitkara.data.Rating;
import com.example.android.feedbackchitkara.data.UserReview;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import javax.xml.namespace.NamespaceContext;

public class Item_Details extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbarLayout;
    android.support.v7.widget.Toolbar toolbar;
    String title_name;
    EditText editText;
    RecyclerView recyclerView;
    TextView textView;
    TextView reviewCount;
    FloatingActionButton floatingActionButton;
    ArrayList<UserReview> reviewArrayList=new ArrayList<>();
    DatabaseReference title_ref;
    int noOfUsers=0;
    Float sumOfRatings=0f;
    Boolean isRated;
    TextView dialogAction;
        RatingBar ratingBar;
    LinearLayout linearLayout;
    RatingBar activityRatingBar;
     DatabaseReference ratingRef;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item__details);
        editText=findViewById(R.id.user_review);
        reviewCount=findViewById(R.id.reviews_count_Details);
        textView=findViewById(R.id.add_review);
        dialog=new Dialog(Item_Details.this);
        activityRatingBar=findViewById(R.id.rating_Activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_ratings_popup);
        dialogAction=dialog.findViewById(R.id.action_text);
        ratingBar=dialog.findViewById(R.id.rating_values);
        floatingActionButton=findViewById(R.id.addRatings);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               checkIfRated();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

                String re=editText.getText().toString();
                if(!re.isEmpty()){
                    UserReview userReview;
                     if(firebaseUser.getPhotoUrl()!=null){
                         String url=firebaseUser.getPhotoUrl().toString();
                          userReview=new UserReview(url,firebaseUser.getDisplayName(),re);
                     }else{
                         userReview=new UserReview("",firebaseUser.getDisplayName(),re);
                     }



                    title_ref.child(getString(R.string.review_list)).push().setValue(userReview);
                    editText.setText("");
                }


            }
        });
        Intent intent=getIntent();
        collapsingToolbarLayout=findViewById(R.id.collapseToolbar);
        toolbar=findViewById(R.id.toolbar);
        title_name=intent.getStringExtra(getString(R.string.title_name_key));
        collapsingToolbarLayout.setTitle(title_name);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        readRatings();
        readData();
        readReviews();
    }
    private void readRatings(){
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(getString(R.string.topics))
                .child(title_name).child(getString(R.string.atingsOverall)).child(getString(R.string.overall_rating));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(Item_Details.this,"updated",Toast.LENGTH_SHORT).show();
                if(dataSnapshot.getValue(Float.class)!=null){
                   activityRatingBar.setRating(dataSnapshot.getValue(Float.class));
                }else {
                    activityRatingBar.setRating(0);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void checkIfRated(){
        ratingRef=title_ref.child(getString(R.string.atingsOverall)).child(getString(R.string.ratings_list));
        ratingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    dialogAction.setText(R.string.edit_ratings);
                    isRated=true;
                    dialog.show();
                    addRatings();
                    ratingRef.removeEventListener(this);

                }else{
                    dialogAction.setText(R.string.add_ratings);
                    isRated=false;
                    dialog.show();
                    addRatings();
                    ratingRef.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void addRatings(){
        if(isRated){
            displayRatings();
        }
        TextView submit=dialog.findViewById(R.id.submit_ratings);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ratingRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(ratingBar.getRating());
                if(isRated){
                    Toast.makeText(Item_Details.this,"Rating Edited",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Item_Details.this,"Rating Addded",Toast.LENGTH_SHORT).show();
                }
                updateOverall();
                dialog.dismiss();
            }
        });

    }
    private void updateOverall(){

        final DatabaseReference overallRating=title_ref.child(getString(R.string.atingsOverall)).child(getString(R.string.overall_rating));
        ratingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sumOfRatings=0f;
                noOfUsers=0;
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                          sumOfRatings=sumOfRatings+ds.getValue(Float.class);
                          noOfUsers++;
                }
                Toast.makeText(Item_Details.this,String.valueOf(sumOfRatings),Toast.LENGTH_SHORT).show();
                Toast.makeText(Item_Details.this,String.valueOf(noOfUsers),Toast.LENGTH_SHORT).show();
                Float overAll=sumOfRatings/noOfUsers;
                overallRating.setValue(overAll);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void displayRatings(){
        final DatabaseReference userRating=ratingRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRating.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ratingBar.setRating(dataSnapshot.getValue(Float.class));
                userRating.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void readReviews(){

        DatabaseReference reviewList=title_ref.child(getString(R.string.review_list));
        reviewList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                reviewArrayList.add(0,dataSnapshot.getValue(UserReview.class));
                reviewCount.setText("Reviews("+String.valueOf(reviewArrayList.size()+")"));
                ReviewAdapter reviewAdapter=new ReviewAdapter(reviewArrayList,Item_Details.this);
                recyclerView=findViewById(R.id.review_list_details_recycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(Item_Details.this,LinearLayoutManager.VERTICAL,false));
                recyclerView.setAdapter(reviewAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void readData(){
        title_ref= FirebaseDatabase.getInstance().getReference().child(getString(R.string.topics)).child(title_name);
      title_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MainData mainData=dataSnapshot.getValue(MainData.class);
                ImageView imageView=findViewById(R.id.title_image_detail);
                imageView.setImageResource(mainData.getImage_id());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
