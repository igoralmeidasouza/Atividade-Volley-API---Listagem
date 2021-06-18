package com.example.atividade_volley_api_listagem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button botaoCarregar;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    List<ListarItens> itensList;

    // Api escolhida foi dos veículos do Star Wars
    String url = "https://swapi.dev/api/vehicles/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.idRecyclerView);
        recyclerView.hasFixedSize();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itensList = new ArrayList<>();

        botaoCarregar = findViewById(R.id.btnParse);
        botaoCarregar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {carregaDadosRecyclerView();}
        });


    }

    private void carregaDadosRecyclerView() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Carregando Dados...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        botaoCarregar.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray jsonArray = jsonObject.getJSONArray("results");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                ListarItens itens = new ListarItens(
                                        object.getString("name"),
                                        object.getString("vehicle_class")
                                );
                                itensList.add(itens);
                            }

                            adapter = new MyAdapter(itensList, getApplicationContext());
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),
                        error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}