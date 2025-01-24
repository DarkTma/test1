package com.example.dark;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CharacterManager {

    private static final String FILE_NAME = "characters.json";
    private Context context;

    public CharacterManager(Context context) {
        this.context = context;
    }

    public void createCharacter(Character character) {
        try{
            FileInputStream fis = null;
            FileOutputStream fos = null;

            try {
                // Получаем файл
                File file = new File(context.getFilesDir(),"chars.txt");
                // Проверяем существование файла
                if (!file.exists()) {
                    boolean created = file.createNewFile();  // Создаем новый файл, если его нет
                    if (created) {
                        System.out.println("Файл был успешно создан!");
                    } else {
                        System.out.println("Не удалось создать файл!");
                    }
                }

                // Чтение содержимого файла
                String chars = getStringFromFile(context);
                if (chars != "") {
                    int[] stenght = character.getStats();
                    String name = character.getName();
                    String owned = String.valueOf(character.getOwned());
                    String result = "";

                    result = (name + "-" + owned + "-" + stenght[0] + "-" + stenght[1] +"-" + stenght[2] +"-" + stenght[3] +"!");

                    chars +=  result;

                    fos = new FileOutputStream(file);
                    fos.write(chars.getBytes());
                    System.out.println(chars);
                }else {
                    int[] stenght = character.getStats();
                    String name = character.getName();
                    String owned = String.valueOf(character.getOwned());
                    String result = "";

                    result = (name + "-" + owned + "-" + stenght[0] + "-" + stenght[1] +"-" + stenght[2] +"-" + stenght[3] +"!");
                    fos = new FileOutputStream(file);

                    fos.write(result.getBytes());
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null) fis.close();
                    if (fos != null) fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void deleteAllCharacters() throws IOException {
        FileOutputStream fos = null;
        File file = new File(context.getFilesDir(),"chars.txt");
        fos = new FileOutputStream(file);
        fos.write("".getBytes());
    }

    public List<Character> loadCharacters() {
        try {
            String chars = getStringFromFile(context);
            List<Character> characters = new ArrayList<>();
            if (chars == ""){
                Toast.makeText(context, "список персанажей пуст", Toast.LENGTH_SHORT).show();
            }else {
                String[] data = chars.split("!");
                for (String character : data) {
                    String[] characterInfo = character.split("-");
                        characters.add(new Character(characterInfo[0] , Integer.parseInt(characterInfo[2]),Integer.parseInt(characterInfo[3]),Integer.parseInt(characterInfo[4])));
                    }
                }
            return characters;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Character> loadOurCharacters() {
        try {
            String chars = getStringFromFile(context);
            List<Character> characters = new ArrayList<>();
            if (chars == ""){
                Toast.makeText(context, "список персанажей пуст", Toast.LENGTH_SHORT).show();
            }else {
                String[] data = chars.split("!");
                for (String character : data) {
                    String[] characterInfo = character.split("-");
                    if (characterInfo[1].equals("true")) {
                        characters.add(new Character(characterInfo[0], Integer.parseInt(characterInfo[2]), Integer.parseInt(characterInfo[3]), Integer.parseInt(characterInfo[4])));
                    }
                }
            }
            return characters;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getStringFromFile(Context context) throws Exception {
        File file = new File(context.getFilesDir(),"chars.txt");
        FileInputStream fin = new FileInputStream(file);
        String ret = convertStreamToString(fin);
        fin.close();
        return ret;
    }
    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }
}