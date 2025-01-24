package com.example.dark;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class LightsOut extends Fragment {

    private Button[] buttons = new Button[9];  // Массив для кнопок
    private boolean[] buttonStates = new boolean[9]; // Состояния кнопок (true - ON, false - OFF)
    private Random random = new Random();
    TextView textView;

    public LightsOut() {
        // Пустой конструктор
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate фрагмента
        View view = inflater.inflate(R.layout.lights_out, container, false);

        // Инициализация элементов UI
        GridLayout gridLayout = view.findViewById(R.id.gridLayout);
        for (int i = 0; i < 9; i++) {
            buttons[i] = new Button(getContext());
            buttons[i].setText("OFF");
            buttons[i].setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            buttons[i].setLayoutParams(new GridLayout.LayoutParams());
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClicked((Button) v);
                }
            });
            gridLayout.addView(buttons[i]);
        }

        refreshScore(view);



        // Кнопки "Играть снова" и "Выйти"
        Button btnPlayAgain = view.findViewById(R.id.btn_play_again);
        Button btnExit = view.findViewById(R.id.btn_exit);


        btnPlayAgain.setOnClickListener(v -> onPlayAgainClicked(view));
        btnExit.setOnClickListener(v -> onExitClicked(view));

        // Скрытие кнопок "Играть снова" и "Выйти" по умолчанию
        btnPlayAgain.setVisibility(View.GONE);
        btnExit.setVisibility(View.GONE);

        // Инициализация игры (рандомное состояние кнопок)
        randomizeButtonStates();



        return view;
    }

    private void refreshScore(View view) {
        textView = view.findViewById(R.id.balance);
        String fileContent = null;
        try {
            fileContent = getStringFromFile(requireContext());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        textView.setText(fileContent);
    }

    private String getStringFromFile(Context context) throws Exception {
        File file = new File(context.getFilesDir(),"money.txt");
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

    // Рандомизация начальных состояний кнопок
    private void randomizeButtonStates() {
        for (int i = 0; i < 9; i++) {
            buttonStates[i] = random.nextBoolean(); // Случайное значение true или false
            if (buttonStates[i]) {
                buttons[i].setText("ON");
                buttons[i].setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
            } else {
                buttons[i].setText("OFF");
                buttons[i].setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            }
        }
    }

    // Обработка нажатия на кнопку игры
    private void onButtonClicked(Button button) {
        int index = -1;

        // Найти индекс кнопки
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == button) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            toggleButtonState(index);  // Переключить состояние этой кнопки
            // Переключить соседние кнопки
            if (index > 2) toggleButtonState(index - 3); // Верхняя кнопка
            if (index < 6) toggleButtonState(index + 3); // Нижняя кнопка
            if (index % 3 != 0) toggleButtonState(index - 1); // Левая кнопка
            if (index % 3 != 2) toggleButtonState(index + 1); // Правая кнопка
        }

        // Проверить, не победил ли игрок
        if (checkVictory()) {
            // Показать кнопки для игры заново и выхода
            getView().findViewById(R.id.btn_play_again).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.btn_exit).setVisibility(View.VISIBLE);

            // Начислить 100 монет
            giveReward(requireContext());

            // Скрыть все кнопки игры
            for (Button b : buttons) {
                b.setVisibility(View.GONE);
            }
        }
    }

    // Переключить состояние кнопки
    private void toggleButtonState(int index) {
        if (buttonStates[index]) {
            buttonStates[index] = false;
            buttons[index].setText("OFF");
            buttons[index].setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        } else {
            buttonStates[index] = true;
            buttons[index].setText("ON");
            buttons[index].setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
        }
    }

    // Проверка на победу (все кнопки OFF)
    private boolean checkVictory() {
        for (boolean state : buttonStates) {
            if (state) return false;  // Если хотя бы одна кнопка включена
        }
            return true;
    }

    public void giveReward(Context context){
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            // Получаем файл
            //File file = new File(context.getFilesDir(),"money.txt");
            File file = new File(context.getFilesDir(),"money.txt");
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
            String number = getStringFromFile(requireContext());
            if (number != "") {
                int a = Integer.parseInt(number) + 100;
                number = String.valueOf(a);
                fos = new FileOutputStream(file);
                fos.write(number.getBytes());
            }else {
                fos = new FileOutputStream(file);
                fos.write("100".getBytes());
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
    }

    // Событие кнопки "Играть снова"
    public void onPlayAgainClicked(View view) {
        refreshScore(view);
        resetGame();  // Сбросить игру
    }


    public void onExitClicked(View view) {
        refreshScore(view);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Закрываем фрагмент (удаляем его из контейнера)
        transaction.remove(this);
        transaction.commit();
    }

    // Сбросить игру
    private void resetGame() {
        // Сбросить состояние кнопок
        randomizeButtonStates();

        // Скрыть кнопки "Играть снова" и "Выйти"
        getView().findViewById(R.id.btn_play_again).setVisibility(View.GONE);
        getView().findViewById(R.id.btn_exit).setVisibility(View.GONE);

        // Сделать кнопки снова видимыми
        for (Button b : buttons) {
            b.setVisibility(View.VISIBLE);
        }
    }
}
