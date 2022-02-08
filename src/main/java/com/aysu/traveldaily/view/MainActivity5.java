package com.aysu.traveldaily.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Placeholder;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.aysu.traveldaily.R;
import com.aysu.traveldaily.adapter.PostAdapter;
import com.aysu.traveldaily.databinding.ActivityMain5Binding;
import com.aysu.traveldaily.model.Post;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity5 extends AppCompatActivity {
     private FirebaseFirestore firebaseFirestore;
     ArrayList<Post> arrayList;
     PostAdapter postAdapter;
     ActivityMain5Binding main5Binding;
     RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main5Binding=ActivityMain5Binding.inflate(getLayoutInflater());
        View view=main5Binding.getRoot();
        setContentView(view);

        arrayList= new ArrayList<>();
        postAdapter=new PostAdapter(arrayList);
        firebaseFirestore=FirebaseFirestore.getInstance();
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);

        getData();




    }






    public void getData(){

        firebaseFirestore.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(MainActivity5.this, error.getLocalizedMessage(),Toast.LENGTH_LONG);
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

                        //String recyclercommenttext=country+"/ "+city+"/ "+PlaceName;
                        Post post=new Post(PlaceName);
                        arrayList.add(post);

                        recyclerView.setAdapter(postAdapter);

                        recyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
                            @Override
                            public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
                                
                            }
                        });



                        main5Binding.textView2.setText(PlaceName.toString());
                        main5Binding.textView3.setText(acıklama.toString());



                        Picasso.get().load(url).into(main5Binding.imageView2);



                    }

                    postAdapter.notifyDataSetChanged();
                }

            }
        });

    }


}