package com.example.dark;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private Button btnLights;
    private Button btn15;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // GAME LIGHTS_OUT
        btnLights = findViewById(R.id.btn_lights);

        btnLights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LightsOut lightsOutFragment = new LightsOut();

                // Заменяем текущий фрагмент
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, lightsOutFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        //GAME !15
        btn15 = findViewById(R.id.game);

        btn15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fiveteen game = new fiveteen();

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, game);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        //Summon
        Button btnSummon = findViewById(R.id.btnSummon);
        btnSummon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Summon summon = new Summon();

                // Заменяем текущий фрагмент
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, summon);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}