package com.example.apps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ContactActivity extends AppCompatActivity {
    private EditText editTextNom, editTextPhone, editTextAdresse, editTextRemarque, editTextDatejr;
    private RadioButton editTextDecision;
    //String id_user;

    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        editTextNom = findViewById(R.id.EditTextNom);
        editTextAdresse = findViewById(R.id.EditTextAadresse);
        editTextPhone = findViewById(R.id.EditTextPhone);

        RadioGroup radioGroup = findViewById(R.id.radioDecision);
        radioGroup.setOnCheckedChangeListener((group, i) -> {
            editTextDecision = findViewById(i);
            Toast.makeText(getApplicationContext(), "ggg " + editTextDecision.getText().toString(), Toast.LENGTH_SHORT).show();
        });
        editTextRemarque = findViewById(R.id.EditTextSuggestion);


        dataManager = new DataManager(getApplicationContext());

        Button buttonResgist = findViewById(R.id.BtnInscrip);

        buttonResgist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_contact();
            }
        });

    }

    public void save_contact(){
        String nom = editTextNom.getText().toString().trim();
        String adresse = editTextAdresse.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String decision = editTextDecision.getText().toString().trim();
        String remarque = editTextRemarque.getText().toString().trim();
        String id_user = "1";

        String url = "https://api.mascodeproduct.com/devApp/actions/registercontact.php";
        Map<String, String> params = new HashMap<>();

        params.put("nom", nom);
        params.put("adresse", adresse);
        params.put("phone", phone);
        params.put("decision", decision);
        params.put("remarque", remarque);
        params.put("id_user", id_user);

        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(getApplicationContext(), "Enregistrement réussi", Toast.LENGTH_SHORT).show();
                            editTextNom.setText("");
                            editTextAdresse.setText("");
                            editTextPhone.setText("");
                            editTextDecision.setText("");
                            editTextRemarque.setText("");

                        } else {
                            String errorMessage = response.getString("error");
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Erreur de traitement des données", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    if (error instanceof NetworkError || error instanceof ServerError) {
                        Toast.makeText(getApplicationContext(), "Problème réseau ou serveur b", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Ajouter la requête à la file d'attente de Volley
        dataManager.queue.add(jsonObjectRequest);
    }
}
