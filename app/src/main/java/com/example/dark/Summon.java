package com.example.dark;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class Summon extends Fragment {

    private boolean isSwitched = false;

    private RecyclerView recyclerViewCharacters;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Загружаем макет summon_char
        View view = inflater.inflate(R.layout.summon_char, container, false);

        Button btnCreate = view.findViewById(R.id.create);
        Button btnList = view.findViewById(R.id.list);
        Button btnDelete = view.findViewById(R.id.delete);
        Button btnSummon = view.findViewById(R.id.summon);

        recyclerViewCharacters = view.findViewById(R.id.recyclerViewCharacters);

        btnCreate.setOnClickListener(v -> newCharacter());
        btnList.setOnClickListener(v -> showCharacters());
        btnDelete.setOnClickListener(v -> delete());
        btnSummon.setOnClickListener(v -> summonNewCharacter());

        Switch switchOnOff = view.findViewById(R.id.switchOnOff);

        // Установить слушатель изменения состояния
        switchOnOff.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Выполняется, когда переключатель включен
                switchOnOff.setText("все персанажи");
                functionOn();
            } else {
                // Выполняется, когда переключатель выключен
                switchOnOff.setText("только ваши");
                functionOff();
            }
        });



        return view;
    }

    private void functionOff() {
        isSwitched = false;
    }

    private void functionOn() {
        isSwitched = true;
    }

    private void summonNewCharacter() {
        CharacterManager characterManager = new CharacterManager(requireContext());
        List<Character> characterList = characterManager.loadCharacters();

        if (!characterList.isEmpty()) {
            FileOutputStream fos = null;
            File file = new File(requireContext().getFilesDir(),"chars.txt");
            String[] info = {"","","","",""};
            Random random = new Random();
            int N = random.nextInt(characterList.size());
            Character newGaintCharacter = characterList.get(N);
            String spisok = "";
            for(Character i : characterList){
                if (i.getName() != newGaintCharacter.getName()){
                    int[] stenght = i.getStats();
                    spisok += (i.getName() + "-" + "false" + "-" + stenght[0] + "-" + stenght[1] +"-" + stenght[2] +"-" + stenght[3] +"!");
                }else {
                    int[] stenght = newGaintCharacter.getStats();
                    info[0] = String.valueOf(newGaintCharacter.getName());
                    info[1] = String.valueOf(stenght[3]);
                    info[2] = String.valueOf(stenght[0]);
                    info[3] = String.valueOf(stenght[1]);
                    info[4] = String.valueOf(stenght[2]);

                    spisok += (newGaintCharacter.getName() + "-" + "true" + "-" + stenght[0] + "-" + stenght[1] +"-" + stenght[2] +"-" + stenght[3] +"!");
                }
            }
            try {
                fos = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            try {
                fos.write(spisok.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Intent intent = new Intent(requireContext(), SummonedCharacterInfo.class);
            intent.putExtra("GainedCharacter", (Serializable) info);  // Преобразуем список в Serializable
            startActivity(intent);
        } else {
            Toast.makeText(requireContext(), "Список персонажей пуст", Toast.LENGTH_SHORT).show();
        }
    }

    private void  delete(){
        CharacterManager characterManager = new CharacterManager(requireContext());
        try {
            characterManager.deleteAllCharacters();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void newCharacter() {
        // Переход на экран создания персонажа
        Intent intent = new Intent(requireContext(), CreateCharacter.class);
        startActivity(intent);
    }

    private void showCharacters() {
        if (isSwitched) {
            // Загрузка списка персонажей
            CharacterManager characterManager = new CharacterManager(requireContext());
            List<Character> characterList = characterManager.loadCharacters();

            Log.d("CharacterDebug", "Loaded characters: " + characterList.size());

            if (!characterList.isEmpty()) {
                // Передаем список персонажей в новую активность
                Intent intent = new Intent(requireContext(), CharacterListActivity.class);
                intent.putExtra("characterList", (Serializable) characterList);  // Преобразуем список в Serializable
                startActivity(intent);
            } else {
                Toast.makeText(requireContext(), "Список персонажей пуст", Toast.LENGTH_SHORT).show();
            }
        }else {
            // Загрузка списка персонажей
            CharacterManager characterManager = new CharacterManager(requireContext());
            List<Character> characterList = characterManager.loadOurCharacters();

            Log.d("CharacterDebug", "Loaded characters: " + characterList.size());

            if (!characterList.isEmpty()) {
                // Передаем список персонажей в новую активность
                Intent intent = new Intent(requireContext(), CharacterListActivity.class);
                intent.putExtra("characterList", (Serializable) characterList);  // Преобразуем список в Serializable
                startActivity(intent);
            } else {
                Toast.makeText(requireContext(), "Список персонажей пуст", Toast.LENGTH_SHORT).show();
            }
        }

        // Логирование

    }

}

