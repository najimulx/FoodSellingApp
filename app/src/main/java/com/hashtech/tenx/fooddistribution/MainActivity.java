package com.hashtech.tenx.fooddistribution;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public static final String JSON_URL = "url";///////////////////////////////////////////////////////////////////////////////
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<CustomDataType> listItems;

    public static String username = "";
    private FirebaseAuth mAuth;
    Button btnLogut,btnSell;
    TextView tvheader;
    FirebaseUser currentUser;
    FirebaseFirestore db;
    DocumentReference userDoc;
    CollectionReference collRef ;
    CollectionReference postRef ;

    private Map<String, String> list;
    DrawerLayout drawer;



    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent i = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(i);
        }else {
            tvheader.setText(currentUser.getEmail());
            userDoc = db.collection("users").document(currentUser.getEmail());
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity);
        db = FirebaseFirestore.getInstance();
        collRef = db.collection("users");
        mAuth = FirebaseAuth.getInstance();

        //nav
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_nav);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);



        list = new HashMap<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItems = new ArrayList<>();

        username = getIntent().getStringExtra("username");
        btnLogut = findViewById(R.id.btn_signout);

        tvheader = findViewById(R.id.tv_header);

        postRef = db.collection("posts");
        postRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                    List<CustomDataType> list = new ArrayList<>();
                    for(DocumentSnapshot s : docs){
                        String name = s.getString("name");
                        String addr = s.getString("address");
                        String contact = s.getString("contact");
                        String days = s.getString("days");
                        String details = s.getString("details");
                        String time = s.getString("time");
                        String timestamp = s.getString("timestamp");
                        String email = s.getString("email");
                        String id = s.getId();


                        CustomDataType data = new CustomDataType(name, email, contact, addr, days, time, details, timestamp, id);
                        list.add(data);
                    }
                    adapter = new RecyclerViewAdapter( list,MainActivity.this);
                    recyclerView.setAdapter(adapter);



            }
        });

        btnLogut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                currentUser = null;
                Intent i = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(i);
            }
        });
        btnSell = findViewById(R.id.btn_sell);
        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sellDialog(MainActivity.this);
            }
        });



    }

    public void sellDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View  v = LayoutInflater.from(context).inflate(R.layout.dialog_seller, null, false);
        builder.setView(v);
        final AlertDialog dialog = builder.create();
        dialog.show();
        final EditText etname, etaddress, etcontact, etdetails, etTime, etDays;
        Button btnPost;
        etaddress = v.findViewById(R.id.et_s_address);
        etname = v.findViewById(R.id.et_s_name);
        etcontact = v.findViewById(R.id.et_s_contact);
        etdetails = v.findViewById(R.id.et_s_details);
        etTime = v.findViewById(R.id.et_s_time);
        etDays = v.findViewById(R.id.et_s_days);
        btnPost = v.findViewById(R.id.btn_post);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etname.getText().toString();
                String address = etaddress.getText().toString();
                String contact = etcontact.getText().toString();
                String details = etdetails.getText().toString();
                String time = etTime.getText().toString();
                String days = etDays.getText().toString();
                final Map<String, String> data = new HashMap<>();
                data.put("name", name);
                data.put("email", currentUser.getEmail());
                data.put("address", address);
                data.put("contact", contact);
                data.put("details", details);
                data.put("time", time);
                data.put("days", days);
                data.put("timestamp", ""+System.currentTimeMillis());
                postRef.document().set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed, Try again!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });






    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_share:
                Toast.makeText(this, "Sharing", Toast.LENGTH_SHORT).show();
        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
