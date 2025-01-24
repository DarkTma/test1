package com.example.dark;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class fiveteen extends Fragment {

    private Button[] buttons = new Button[16];
    private int[] buttonValues = new int[16];
    private Button isEmpty;
    private TextView textView;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fiveteen_game, container, false);

        // Инициализация кнопок
        buttons[0] = view.findViewById(R.id.button1);
        buttons[1] = view.findViewById(R.id.button2);
        buttons[2] = view.findViewById(R.id.button3);
        buttons[3] = view.findViewById(R.id.button4);
        buttons[4] = view.findViewById(R.id.button5);
        buttons[5] = view.findViewById(R.id.button6);
        buttons[6] = view.findViewById(R.id.button7);
        buttons[7] = view.findViewById(R.id.button8);
        buttons[8] = view.findViewById(R.id.button9);
        buttons[9] = view.findViewById(R.id.button10);
        buttons[10] = view.findViewById(R.id.button11);
        buttons[11] = view.findViewById(R.id.button12);
        buttons[12] = view.findViewById(R.id.button13);
        buttons[13] = view.findViewById(R.id.button14);
        buttons[14] = view.findViewById(R.id.button15);
        buttons[15] = view.findViewById(R.id.button16);

        // Генерация значений для кнопок
        List<Integer> values = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            values.add(i);
        }
        values.add(0); // Пустая ячейка

        // Перемешиваем значения
        Collections.shuffle(values);

        for (int i = 0; i < 16; i++) {
            buttonValues[i] = values.get(i);
            if (buttonValues[i] == 0) {
                buttons[i].setText("");
                buttons[i].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), android.R.color.holo_red_light)));
                isEmpty = buttons[i];
            } else {
                buttons[i].setText(String.valueOf(buttonValues[i]));
                buttons[i].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), android.R.color.holo_green_light)));
            }
        }

        // Устанавливаем обработчики перетаскивания
        for (int i = 0; i < 16; i++) {
            final int index = i;
            buttons[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        handleDrag(index);
                    }
                    return true;
                }
            });
        }

        try {
            refreshScore(view);
        } catch ( Exception e){
            System.out.println(e);
        }


        Button btncheat = view.findViewById(R.id.button23);
        btncheat.setOnClickListener(v -> cheat());

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

    private void handleDrag(int index) {
        if (isEmpty == buttons[index]) {
            Toast.makeText(getActivity(), "Это пустая ячейка", Toast.LENGTH_SHORT).show();
            return;
        }

        int emptyIndex = getIndexForButton(isEmpty);

        if (checkNear(index, emptyIndex)) {
            // Меняем значения местами
            buttonValues[emptyIndex] = buttonValues[index];
            buttonValues[index] = 0;

            // Обновляем кнопки
            buttons[emptyIndex].setText(String.valueOf(buttonValues[emptyIndex]));
            buttons[emptyIndex].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), android.R.color.holo_green_light)));

            buttons[index].setText("");
            buttons[index].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), android.R.color.holo_red_light)));

            isEmpty = buttons[index];

            if (checkWin()) {
                Toast.makeText(getActivity(), "Поздравляем, вы выиграли!", Toast.LENGTH_SHORT).show();
                giveReward(requireContext());
            }
        } else {
            Toast.makeText(getActivity(), "Можно двигать только соседние ячейки", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkNear(int thisIndex, int secondIndex) {
        boolean okay = false;
        if (thisIndex > 3 && secondIndex == thisIndex - 4) okay = true; // Верхняя ячейка
        if (thisIndex < 12 && secondIndex == thisIndex + 4) okay = true; // Нижняя ячейка
        if (thisIndex % 4 != 0 && secondIndex == thisIndex - 1) okay = true; // Левая ячейка
        if (thisIndex % 4 != 3 && secondIndex == thisIndex + 1) okay = true; // Правая ячейка
        return okay;
    }

    private int getIndexForButton(Button button) {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == button) {
                return i;
            }
        }
        return -1;
    }

    private boolean checkWin() {
        for (int i = 0; i < 15; i++) {
            if (buttonValues[i] != i + 1) {
                return false;
            }
        }
        return true;
    }

    private void giveReward(Context context) {
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
            Toast.makeText(getActivity(), "Поздровляю! вы победили", Toast.LENGTH_SHORT).show();
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

    public void cheat(){
        for (int i = 0; i < 15; i++) {
            buttons[i].setText(String.valueOf(i+1));
            buttonValues[i] = i+1;
        }
        buttons[15].setText("");
        buttonValues[15] = 0;
        if (checkWin()){
            giveReward(requireContext());
        }
    }

}
