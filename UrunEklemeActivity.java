package com.example.lkuygulama;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UrunEklemeActivity extends AppCompatActivity {
        FirebaseDatabase firebaseDatabase;
        DatabaseReference reference;
        private String userId;
        private FirebaseAuth.AuthStateListener mAuthlistener;
        public ArrayList<Urun> goster = new ArrayList<Urun>();
        List<Urun> list;
        ListView view;
        ArrayAdapter<Urun> arrayAdapter;
        public Button aramam,bildirim,liste;
        private FirebaseAuth mAuth;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_ekleme);

        Urun urun = new Urun();

        bildirim=(Button) findViewById(R.id.bildirimButon);
        liste = (Button) findViewById(R.id.urunListeButon);
        view=(ListView)findViewById(R.id.listele);
        arrayAdapter = new ArrayAdapter<Urun>(this,android.R.layout.simple_list_item_1,list);

            //view.setAdapter(arrayAdapter);

        mAuthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                userId = mAuth.getUid();
            }
        };


        tanimla();
        listele();

            bildirim.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                        gonderBildirim();
                   }
               });





    }

    private void tanimla() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
    }

    private void listele() {

        reference.child("urunler").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

              //  Log.i("urunler",dataSnapshot.getKey());

                  /*   Urun valeu =dataSnapshot.getValue(Urun.class);
                    goster.add(valeu);
                    arrayAdapter.notifyDataSetChanged(); */

                Log.i("urunler", dataSnapshot.getKey());

                 /* Urun u = dataSnapshot.getValue(Urun.class);
                  list.add(u);
                  arrayAdapter.notifyDataSetChanged(); */
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }


    private void gonderBildirim() {
        Intent intent = new Intent(this, BildirimActivity.class);
        startActivity(intent);
    }

    private void gonderAra() {
        Intent intent = new Intent(this,AramaActivity.class);
        startActivity(intent);
    }

    private void gonderListe() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
