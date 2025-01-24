package com.example.dark;

// CharacterListActivity.java
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CharacterListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCharacters;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_list);

        // Получаем переданный список персонажей
        List<Character> characterList = (List<Character>) getIntent().getSerializableExtra("characterList");

        // Инициализация RecyclerView
        recyclerViewCharacters = findViewById(R.id.recyclerViewCharacters);
        recyclerViewCharacters.setLayoutManager(new LinearLayoutManager(this));

        // Устанавливаем адаптер для RecyclerView
        if (characterList != null && !characterList.isEmpty()) {
            CharacterAdapter adapter = new CharacterAdapter(characterList);
            recyclerViewCharacters.setAdapter(adapter);
        }

        // Кнопка для возврата в MainActivity
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());  // Закрыть эту активность и вернуться назад
    }
}

