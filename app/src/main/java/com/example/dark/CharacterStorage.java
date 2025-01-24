package com.example.dark;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CharacterStorage {
    private static final String PREFS_NAME = "character_prefs";
    private static final String KEY_CHARACTERS = "characters";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public CharacterStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveCharacters(List<Character> characters) {
        String json = gson.toJson(characters);
        sharedPreferences.edit().putString(KEY_CHARACTERS, json).apply();
    }

    public List<Character> loadCharacters() {
        String json = sharedPreferences.getString(KEY_CHARACTERS, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<Character>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void addCharacter(Character character) {
        List<Character> characters = loadCharacters();
        characters.add(character);
        saveCharacters(characters);
    }
}

