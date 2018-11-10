package com.hashtech.tenx.fooddistribution;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

public class MainActivity extends AppCompatActivity {

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

        collRef = db.collection("users");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItems = new ArrayList<>();

        username = getIntent().getStringExtra("username");
        btnLogut = findViewById(R.id.btn_signout);

        tvheader = findViewById(R.id.tv_header);
        db = FirebaseFirestore.getInstance();

        collRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

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




        /*for(int i=0;i<10;i++){
            CustomDataType sampledata = new CustomDataType("supplier"+i,0000+i,"Address"+i,"monday"+i, i,i);
            listItems.add(sampledata);
        }

        adapter = new RecyclerViewAdapter(listItems,this);
        recyclerView.setAdapter(adapter);
        */



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
                Map<String, String> data = new HashMap<>();
                data.put("name", name);
                data.put("address", address);
                data.put("contact", contact);
                data.put("details", details);
                data.put("time", time);
                data.put("days", days);
                data.put("timestamp", ""+System.currentTimeMillis());
                userDoc.set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
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

    /*private void loadData() {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("supplier");
                            for(int j = 0;j<array.length();j++){
                                JSONObject o = array.getJSONObject(j);
                                CustomDataType data = new CustomDataType(
                                        o.getString("name"),
                                        o.getInt("phone"),
                                        o.getString("address"),
                                        o.getString("day"),
                                        o.getInt("time"),
                                        o.getInt("surplus")
                                );
                                listItems.add(data);
                            }
                            adapter = new RecyclerViewAdapter(listItems,getApplicationContext());
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    public void switchToLogIn(){
        Intent intentToLogIn = new Intent(getApplicationContext(),LogInActivity.class);
        startActivity(intentToLogIn);
    }

    public void checkLogInStatus(){


    }
*/


}
