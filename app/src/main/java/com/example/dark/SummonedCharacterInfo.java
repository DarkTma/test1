package com.example.dark;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class SummonedCharacterInfo extends AppCompatActivity {

    private final Handler handler = new Handler();
    private Runnable closeRunnable;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summoned_info);
        String[] info = (String[]) getIntent().getSerializableExtra("GainedCharacter");

        TextView name =  findViewById(R.id.name);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView value =  findViewById(R.id.value);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView strength =  findViewById(R.id.strength);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView defense =  findViewById(R.id.defens);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView health =  findViewById(R.id.health);

        name.setText(info[0]);
        value.setText("ценность: " + info[1]);
        strength.setText("атака: " + info[2]);
        defense.setText("зашита: " + info[3]);
        health.setText("здоровье: " + info[4]);


        View rootView = findViewById(android.R.id.content);

        // Устанавливаем слушатель нажатий
        rootView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                startCloseTimer();
            }
            return true;
        });
    }

    private void startCloseTimer() {
        // Если уже был запущен таймер, отменяем его
        if (closeRunnable != null) {
            handler.removeCallbacks(closeRunnable);
        }

        // Создаем задачу для закрытия активности
        closeRunnable = this::finish;

        // Запускаем задачу с задержкой 2 секунды
        handler.postDelayed(closeRunnable, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Убираем отложенные задачи при уничтожении активности
        if (closeRunnable != null) {
            handler.removeCallbacks(closeRunnable);
        }
    }
}



