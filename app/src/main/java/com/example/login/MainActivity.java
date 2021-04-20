package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences("LOGIN_PREFERENCES", Activity.MODE_PRIVATE);

        Toast.makeText(MainActivity.this,pref.getString("access_token", "wala"), Toast.LENGTH_LONG).show();

        Button btnLogout = (Button) findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    public void logout() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = getString(R.string.API_URL)+"/logout";

//       string ang ginamit naten dito na request kase wala naman tayong isesend na data tapos kailangan lang naten
//        yungtoken sa prefrerence na isend kasama nung request kaya StringRequest lang
        StringRequest request = new StringRequest(Request.Method.POST,url,
                response ->{

//            lipat lang naten dito so once okay yung logout sa database ang gaagawin clear naten yung
//            prefrerences tapos launch naten yung intent papunta dun sa Loginactivity kase nga nag logout siya
                System.out.println(response );
                SharedPreferences.Editor editor = pref.edit();
                editor.clear().apply();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

        },error -> {
            error.printStackTrace();
        }){
//            etong override naa method get headers laginng gagamit kung magrerequest na sa database na protected ng api
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = pref.getString("access_token",null);
                headers.put("Authorization", "Bearer "+ token);
                System.out.println(headers);
                return headers;
            }
        };

        queue.add(request);
    }
}