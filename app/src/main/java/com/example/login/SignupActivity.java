package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    private Button btnSregister, btnbacklogin;
    private EditText etSname, etSemail, etSpass, etSconfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnbacklogin = findViewById(R.id.btnSbackLogin);
        btnSregister = findViewById(R.id.btnSregister);
        etSname = findViewById(R.id.etSname);
        etSemail = findViewById(R.id.etSemail);
        etSpass = findViewById(R.id.etSpass);
        etSconfirm = findViewById(R.id.etSconfirm);


        backlogin();


        btnSregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    private void backlogin() {
        btnbacklogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void register() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.API_URL)+"/register";

        JSONObject registerDetails = new JSONObject();
        try {
            registerDetails.put("name", etSname.getText().toString());
            registerDetails.put("email", etSemail.getText().toString());
            registerDetails.put("password", etSpass.getText().toString());
            registerDetails.put("password_confirmation", etSconfirm.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(registerDetails);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, registerDetails, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(request);

    }


}