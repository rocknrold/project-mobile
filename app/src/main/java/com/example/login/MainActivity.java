package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

//        sample calling of toast method , pass the activity , your message, and the drawable icon you want
        customToast(MainActivity.this,"Logged In! Remembered",R.drawable.ic_baseline_account_circle_24);

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
                editor.clear();
                editor.commit();

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



//   dito nag create tayo ng custom function na pwede naten tawagin
    /**
     * Custom Toast For Term Test <br>
     *
     * Dito nag create tayo ng layout niya pero isang drawable gumawa tayo ng
     * Shape na rectangle tapos ginawa nateng rounded yung corner niya see
     * custom_rounded_corner_toast.xml file pwede niyo customize yun palitan ng color
     * <br>
     *Tapos nag create tayo ng custom toast sa res/layout para i design yung toast
     *textview lang yun na may image sa left side niya.
     *
     * @param activity Lagay ang activity na tumatawag ex: gusto mo ng toast sa LoginActivity
     *                 lalagay mo lang loginActivity.this
     * @param toastMessage Message na gusto mo lagay sa toast
     * @param drawable_id  Yung drawable na id ng vector na gusto mong maging image sa leftside
     *                     ng message mo.
     *
     */
    public static void customToast(Activity activity, String toastMessage, int drawable_id){
//        kunin yung inflater service
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
//       create ng view na mag iinflate sa custom_toast naten
        View view = inflater.inflate(R.layout.custom_toast, (ViewGroup) activity.findViewById(R.id.customToast));
//      Initialize/ creating a toast object
        Toast newToast = new Toast(activity.getApplicationContext());

//       set yung message ng toast sa textview
        TextView toastText = view.findViewById(R.id.tvCustomToast);
        toastText.setText(toastMessage);
//      set ng image sa leftside ng textview
        toastText.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_id,0,0,0);
//      gaano katagal mag shoshow yung toast
        newToast.setDuration(Toast.LENGTH_SHORT);

//      adjust the gravity if you want to put it on top or other side
        newToast.setGravity(Gravity.BOTTOM, 0,25);
//      this is underlined because it is deprecated but still works
        newToast.setView(view);
//      finally show na yung toast
        newToast.show();
    }
}