package com.example.betaforall.ui.notifications;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.betaforall.R;
import com.example.betaforall.model.Delyanka;
import com.example.betaforall.model.Otvody;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private EditText porodaEditText, delovayaEditText, drovyannayaEditText, otkhodyEditText, otvetstvennyEditText, kommentariyEditText;
    private Spinner delyankaSpinner;
    private TextView lesnichestvoLabel;
    private Button saveButton;

    private FirebaseDatabase database;
    private DatabaseReference otvodyRef;
    private DatabaseReference delyankaRef;

    private List<Delyanka> delyankaList = new ArrayList<>(); // Список для хранения всех делянок

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);

        // Инициализация UI компонентов
        porodaEditText = rootView.findViewById(R.id.porodaEditText);
        delovayaEditText = rootView.findViewById(R.id.delovayaEditText);
        drovyannayaEditText = rootView.findViewById(R.id.drovyannayaEditText);
        otkhodyEditText = rootView.findViewById(R.id.otkhodyEditText);
        otvetstvennyEditText = rootView.findViewById(R.id.otvetstvennyEditText);
        kommentariyEditText = rootView.findViewById(R.id.kommentariyEditText);
        delyankaSpinner = rootView.findViewById(R.id.delyankaSpinner);
        lesnichestvoLabel = rootView.findViewById(R.id.lesnichestvoLabel); // Инициализация TextView
        saveButton = rootView.findViewById(R.id.saveButton);


        // Инициализация Firebase Database
        database = FirebaseDatabase.getInstance();
        otvodyRef = database.getReference("otvody");
        delyankaRef = database.getReference("delyanka");

        // Загружаем данные делянок в Spinner
        loadDelyankaData();

        // Обработчик нажатия на кнопку "Сохранить"
        saveButton.setOnClickListener(v -> {
            String poroda = porodaEditText.getText().toString();
            String delovaya = delovayaEditText.getText().toString();
            String drovyannaya = (drovyannayaEditText.getText().toString());
            String otkhody = (otkhodyEditText.getText().toString());
            String otvetstvennyi = (otvetstvennyEditText.getText().toString());
            String kommentariy = kommentariyEditText.getText().toString();
            String delyankaId = delyankaSpinner.getSelectedItem().toString();  // Получаем выбранный элемент из Spinner
            String lesnichestvoId = lesnichestvoLabel.getText().toString();
            String id = otvodyRef.push().getKey();
            saveOtvodyData(id, poroda, delovaya, drovyannaya, otkhody, otvetstvennyi, kommentariy, delyankaId, lesnichestvoId);
            clearFields();
        });

        // Обработчик выбора делянки в Spinner
        delyankaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Обновляем лейбл с лесничеством
                updateLesnichestvoLabel(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Это можно оставить пустым или обновить лейбл, если ничего не выбрано
                lesnichestvoLabel.setText("Лесничество: Не выбрано");
            }

        });

        // Добавляем TextWatcher для каждого поля

        // Для delovayaEditText
        delovayaEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString();
                if (!input.isEmpty() && !input.endsWith(" м³")) {
                    delovayaEditText.setText(input + " м³");
                    delovayaEditText.setSelection(delovayaEditText.getText().length() - 3); // Устанавливаем курсор перед "м³"
                }
            }
        });

        // Для drovyannayaEditText
        drovyannayaEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString();
                if (!input.isEmpty() && !input.endsWith(" м³")) {
                    drovyannayaEditText.setText(input + " м³");
                    drovyannayaEditText.setSelection(drovyannayaEditText.getText().length() - 3); // Устанавливаем курсор перед "м³"
                }
            }
        });

        // Для otkhodyEditText
        otkhodyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString();
                if (!input.isEmpty() && !input.endsWith(" м³")) {
                    otkhodyEditText.setText(input + " м³");
                    otkhodyEditText.setSelection(otkhodyEditText.getText().length() - 3); // Устанавливаем курсор перед "м³"
                }
            }
        });
        rootView.setOnTouchListener((v, event) -> {
            hideKeyboard(v);
            return false;
        });

        return rootView;
    }

    // Метод для скрытия клавиатуры
    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && getActivity() != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    // Метод для загрузки данных делянок в Spinner
    private void loadDelyankaData() {
        delyankaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                delyankaList.clear(); // Очищаем список перед загрузкой
                List<String> delyankaNames = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Delyanka delyanka = snapshot.getValue(Delyanka.class);
                    if (delyanka != null) {
                        delyankaList.add(delyanka); // Добавляем делянку в список
                        delyankaNames.add(delyanka.getNaimenovanie());  // Добавляем только наименование
                    }
                }

                // Проверка на наличие данных
                if (!delyankaNames.isEmpty()) {
                    // Установка данных в Spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, delyankaNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    delyankaSpinner.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Нет доступных делянок", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Обработка ошибки
                Toast.makeText(getContext(), "Ошибка загрузки данных делянок", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Метод для обновления текста лейбла с лесничеством
    private void updateLesnichestvoLabel(int position) {
        if (position >= 0 && position < delyankaList.size()) {
            Delyanka selectedDelyanka = delyankaList.get(position);
            String lesnichestvo = "Лесничество: " + selectedDelyanka.getLesnichestvoId(); // Используем ID лесничества
            lesnichestvoLabel.setText(lesnichestvo); // Обновляем текст лейбла
        }
    }

    // Метод для сохранения данных в Firebase
    private void saveOtvodyData(String poroda, String delovaya, String drovyannaya, String otkhody, String otvetstvennyi, String kommentariy, String delyankaId, String lesnichestvoId, String id) {


        // Проверка на пустые поля
        if (poroda.isEmpty() || delovaya.isEmpty() || drovyannaya.isEmpty() || otkhody.isEmpty()|| otvetstvennyi.isEmpty() || kommentariy.isEmpty() ) {
            Toast.makeText(getContext(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }
        // Сохранение данных в Firebase
        Otvody otvody = new Otvody(id, poroda, delovaya, drovyannaya, otkhody, otvetstvennyi, kommentariy, delyankaId, lesnichestvoId);
        otvodyRef.push().setValue(otvody).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Данные успешно сохранены", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Ошибка сохранения данных", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void clearFields() {
        porodaEditText.setText("");
        delovayaEditText.setText("");
        drovyannayaEditText.setText("");
        otkhodyEditText.setText("");
        otvetstvennyEditText.setText("");
        kommentariyEditText.setText("");
        delyankaSpinner.setSelection(0);  // Сброс Spinner
    }

}
