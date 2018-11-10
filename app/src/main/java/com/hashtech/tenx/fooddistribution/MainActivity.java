package com.hashtech.tenx.fooddistribution;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String JSON_URL = "url";///////////////////////////////////////////////////////////////////////////////
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<CustomDataType> listItems;

    public static String username = "";
    private FirebaseAuth mAuth;
    Button btnLogut;
    TextView tvheader;
    FirebaseUser currentUser;




    @Override
    protected void onStart() {
        super.onStart();
         currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent i = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(i);
        }


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
        tvheader.setText(username);


        btnLogut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                currentUser = null;
                Intent i = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(i);
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

    private void loadData() {


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



}
