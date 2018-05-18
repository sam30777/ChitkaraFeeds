package com.example.android.feedbackchitkara;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.feedbackchitkara.adapters.MainAdapter;
import com.example.android.feedbackchitkara.data.MainData;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements MainAdapter.ListItemInterface{
   ArrayList<MainData> arrayList=new ArrayList<>();
   DatabaseReference topicRef;
   RecyclerView recyclerView;
   private Integer RC_SIGN_IN=100;
   MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.feedback_recycler);
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                if(firebaseUser==null){
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                   .setLogo(R.drawable.chitkara)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }else{
                    check();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_item,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.sign_out){
            FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            Toast.makeText(MainActivity.this,"Signed Out",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==RC_SIGN_IN){
            if(resultCode==RESULT_OK){
                Toast.makeText(MainActivity.this,"Signed in",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this,"Sign in Failed",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void check(){
       topicRef=FirebaseDatabase.getInstance().getReference().child(getString(R.string.topics));
       final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
       databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               if(dataSnapshot.hasChild(getString(R.string.topics))){
                   readData();
                   databaseReference.removeEventListener(this);
               }else{
                   setData();
               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

   }
    private void readData(){
        arrayList=new ArrayList<>();
        topicRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                arrayList.add(dataSnapshot.getValue(MainData.class));
                MainAdapter mainAdapter =new MainAdapter(MainActivity.this,arrayList,MainActivity.this);
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
                recyclerView.setAdapter(mainAdapter);
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

   private void setData(){
        arrayList.add(new MainData("Infrastructure",R.drawable.infra,0));
       arrayList.add(new MainData("Teachers",R.drawable.teacher,0));
       arrayList.add(new MainData("Placements",R.drawable.placements,0));
       arrayList.add(new MainData("College Fests",R.drawable.fest,0));

      
        for(int i=0;i<arrayList.size();i++){
            topicRef.child(arrayList.get(i).getTitle_name()).setValue(arrayList.get(i));
        }
        readData();
   }

    @Override
    public void onItemClicked(int position) {
       Intent intent=new Intent(MainActivity.this,Item_Details.class);
       intent.putExtra(getString(R.string.title_name_key),arrayList.get(position).getTitle_name());
       startActivity(intent);

    }
}
