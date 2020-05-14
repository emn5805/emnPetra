package com.example.lkuygulama;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.sql.BatchUpdateException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    private EditText text;
    private ProgressDialog diyalog;
    private static String URL;
    private ListView lv, lv2;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapter2;
    private ArrayList liste = new ArrayList();
    private ArrayList liste1 = new ArrayList();
    private Button urunEkle, arama, bildirim, urunList,bit;
    private FirebaseAuth mAuth;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.ana_menum, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.otKapat) {

            mAuth.signOut();
            Toast.makeText(getApplicationContext(), "Oturumdan Çıkılıyor....", Toast.LENGTH_SHORT).show();
            Intent inntent = new Intent(MainActivity.this, Login.class);
            startActivity(inntent);
        }

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bit=(Button)findViewById(R.id.bit);
        urunList = (Button) findViewById(R.id.urunListeButon);
        text = (EditText) findViewById(R.id.text);
        lv = (ListView) findViewById(R.id.list);
        lv2 = (ListView) findViewById(R.id.list2);
        mAuth = FirebaseAuth.getInstance();
        urunEkle = (Button) findViewById(R.id.urunEkleButon);
        arama = (Button) findViewById(R.id.aramaButon);
        bildirim = (Button) findViewById(R.id.bildirimButon);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, liste);
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, liste1);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Alınan Linkler");
        myRef.setValue(URL);// burasını alınan linkler olarak tanımlamıştım ama işte url almışım

        if (mAuth.getCurrentUser() == null) {

            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Lütfen Giriş Yapınız!", Toast.LENGTH_SHORT).show();
        }

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        urunEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gonder2();
            }
        });

        arama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gonder();
            }
        });

        bildirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gonder1();
            }
        });

        urunList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                URL = text.getText().toString();
                text.setText("");;
                new VeriGetir().execute();




            }
        });

        bit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL = text.getText().toString();
                text.setText("");
                new VeriGotur().execute();

            }
        });



    }

    private void gonder2() {
        Intent intent = new Intent(this, UrunEklemeActivity.class);
        startActivity(intent);
    }

    private void gonder1() {
        Intent intent = new Intent(this, BildirimActivity.class);
        startActivity(intent);
    }

    private void gonder() {
        Intent intent = new Intent(this, AramaActivity.class);
        startActivity(intent);
    }


    private class VeriGetir extends AsyncTask<Void, Void, Void> {

        Bitmap bitmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            diyalog = new ProgressDialog(MainActivity.this);
            diyalog.setTitle("Ürün Adı");
            diyalog.setMessage("Lütfen Bekleyin");
            diyalog.setIndeterminate(false);
            diyalog.show();
        }


        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Urun urun = new Urun();
                Document doc = Jsoup.connect(URL).get();
                Elements urunAd = doc.select("div.nameHolder");
                Elements urunAd1 = doc.select("div.newPrice ins");
                Elements img = doc.select("img[src]");
                String imgSrc = img.attr("src");
                InputStream input = new java.net.URL(imgSrc).openStream();
                bitmap = BitmapFactory.decodeStream(input);

                urun.setBaslik(urunAd.text());
                urun.setFiyat(urunAd1.text());
                urun.setKullanici(mAuth.getUid());//giriş yapan kullanıc



                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("urunler").child(mAuth.getUid());
                myRef.setValue(urun);


                for (int a = 0; a < urunAd.size(); a++) {

                    liste.add(urunAd.get(a).text());
                }

                for (int b = 0; b < urunAd1.size(); b++) {

                    liste.add(urunAd1.get(b).text());
                }


                // liste.add(urunAd1.text());
                System.out.println(urunAd.text());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            lv.setAdapter(adapter);
            ImageView logoimg = (ImageView) findViewById(R.id.image);
            logoimg.setImageBitmap(bitmap);
            diyalog.dismiss();
        }

    }

    private class VeriGotur extends AsyncTask<Void, Void, Void> {

        Bitmap bitmap2;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            diyalog = new ProgressDialog(MainActivity.this);
            diyalog.setTitle("Ürün Adı");
            diyalog.setMessage("Lütfen Bekleyin");
            diyalog.setIndeterminate(false);
            diyalog.show();
        }


        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Urun urun = new Urun();
                Document doc1 = Jsoup.connect(URL).get();
                Elements urunAd2 = doc1.select("div.h1-container");
                Elements urunAd3 = doc1.select("div#sp-price-container");
                /* Elements img = doc1.select("a.header-gg-logo.robot-header-logoContainer-logo");
                String imgSrc = img.attr("2");
                InputStream input = new java.net.URL(imgSrc).openStream();
                bitmap2 = BitmapFactory.decodeStream(input); */


                urun.setBaslik(urunAd2.text());
                urun.setFiyat(urunAd3.text());
                urun.setKullanici(mAuth.getUid());//giriş yapan kullanıc


                for (int c = 0; c < urunAd2.size(); c++) {

                    liste1.add(urunAd2.get(c).text());
                }

                for (int d = 0; d < urunAd3.size(); d++) {

                    liste1.add(urunAd3.get(d).text());
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            lv2.setAdapter(adapter2);
        /*    ImageView logoimg2 = (ImageView) findViewById(R.id.image2);
            logoimg2.setImageBitmap(bitmap2); */
            diyalog.dismiss();
        }


    }
}



