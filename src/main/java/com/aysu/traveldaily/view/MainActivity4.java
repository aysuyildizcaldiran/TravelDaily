package com.aysu.traveldaily.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aysu.traveldaily.R;
import com.aysu.traveldaily.adapter.PostAdapter;
import com.aysu.traveldaily.databinding.ActivityMain4Binding;
import com.aysu.traveldaily.databinding.RecyclerRowBinding;
import com.aysu.traveldaily.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity4 extends AppCompatActivity {
     private FirebaseAuth auth;
     private FirebaseFirestore firebaseFirestore;
     ArrayList<Post> arrayList;
     private ActivityMain4Binding activityMain4Binding;
     PostAdapter postAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMain4Binding=ActivityMain4Binding.inflate(getLayoutInflater());
        View view=activityMain4Binding.getRoot();
        setContentView(view);
        arrayList=new ArrayList<>();
        auth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        getData();

        activityMain4Binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter= new PostAdapter(arrayList);
        activityMain4Binding.recyclerView.setAdapter(postAdapter);

    }

    public void getData(){

        firebaseFirestore.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(MainActivity4.this, error.getLocalizedMessage(),Toast.LENGTH_LONG);
                }
                if(value!=null){

                    for(DocumentSnapshot snapshot :value.getDocuments()){
                        Map<String,Object> map=snapshot.getData();

                        //Casting
                        String PlaceName=(String) map.get("PlaceName");
                        String country=(String) map.get("country");
                        String city=(String) map.get("city");
                        String acıklama=(String) map.get("comment");
                        String email=(String) map.get("email");
                        String url=(String) map.get("url");

                        String recyclercommenttext=country+"/ "+city+"/ "+PlaceName;
                        Post post=new Post(PlaceName);
                        arrayList.add(post);


                    }
                  postAdapter.notifyDataSetChanged();
                }

            }
        });

    }
    public void post(View view){

        Intent intent=new Intent(MainActivity4.this,MainActivity5.class);
        startActivity(intent);
    }
    public void addplace(View view){
        Intent intent=new Intent(this, MainActivity3.class);
        startActivity(intent);
    }
    public void home(View view){
        Intent intent=new Intent(this,MainActivity4.class);
        startActivity(intent);
    }
    public void settings(View view){
        Intent intent=new Intent(this,MainActivity4.class);
        startActivity(intent);
    }

}