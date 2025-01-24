package com.example.dark;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateCharacter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_character);

        EditText etName = findViewById(R.id.et_name);
        EditText etStrength = findViewById(R.id.et_strength);
        EditText etHealth = findViewById(R.id.et_health);
        EditText etDefense = findViewById(R.id.et_defense);
        Button btnSaveCharacter = findViewById(R.id.btn_save_character);

        CharacterManager characterManager = new CharacterManager(this);

        btnSaveCharacter.setOnClickListener(v -> {
            // Считываем данные из формы
            String name = etName.getText().toString().trim();
            String strengthStr = etStrength.getText().toString().trim();
            String healthStr = etHealth.getText().toString().trim();
            String defenseStr = etDefense.getText().toString().trim();

            if (name.isEmpty() || strengthStr.isEmpty() || healthStr.isEmpty() || defenseStr.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля!", Toast.LENGTH_SHORT).show();
                return;
            }

            int strength = Integer.parseInt(strengthStr);
            int health = Integer.parseInt(healthStr);
            int defense = Integer.parseInt(defenseStr);

            // Создаем персонажа
            Character character = new Character(name, strength, health, defense);

            // Сохраняем персонажа
            characterManager.createCharacter(character);

            // Уведомляем пользователя
            Toast.makeText(this, "Персонаж успешно создан!", Toast.LENGTH_SHORT).show();

            // Закрываем экран
            finish();
        });
    }
}
