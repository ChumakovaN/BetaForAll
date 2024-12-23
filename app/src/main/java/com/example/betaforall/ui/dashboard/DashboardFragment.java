package com.example.betaforall.ui.dashboard;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import com.example.betaforall.R;
import com.example.betaforall.model.Delyanka;
import com.example.betaforall.model.Lesnichestvo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private EditText naimenovanieEditText;
    private EditText khozyajstvoEditText;
    private EditText technologiyaZagotovkiEditText;
    private EditText preobladayushchayaPorodaEditText;
    private Spinner lesnichestvoSpinner;
    private Spinner sposobRubkiSpinner;
    private Button saveButton;

    private DatabaseReference databaseReference;
    private DatabaseReference lesnichestvoReference;
    private ConstraintLayout constraintLayout;
    private List<String> lesnichestvoNames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Инициализация Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("delyanka");
        lesnichestvoReference = FirebaseDatabase.getInstance().getReference("lesnichestvo");

        // Инициализация элементов интерфейса
        naimenovanieEditText = root.findViewById(R.id.naimenovanieEditText);
        khozyajstvoEditText = root.findViewById(R.id.khozyajstvoEditText);
        technologiyaZagotovkiEditText = root.findViewById(R.id.tehnologiyaZagotovkiEditText);
        preobladayushchayaPorodaEditText = root.findViewById(R.id.preobladayushchayaPorodaEditText);
        lesnichestvoSpinner = root.findViewById(R.id.LesnichestvoSpiner);
        sposobRubkiSpinner = root.findViewById(R.id.sposobRubkiSpinner);
        saveButton = root.findViewById(R.id.saveButton);
        constraintLayout = root.findViewById(R.id.fragment_dashboard);

        // Логика сохранения данных
        saveButton.setOnClickListener(v -> saveDelyankaData());

        // Загрузка данных о лесничествах в Spinner
        loadLesnichestvoData();

        // Настройка Spinner для способа рубки
        setupSposobRubkiSpinner();

        // Скрытие клавиатуры при нажатии на экран
        setupKeyboardHiding();

        return root;
    }

    private void saveDelyankaData() {
        // Получение данных из полей ввода
        String id = databaseReference.push().getKey();
        String naimenovanie = naimenovanieEditText.getText().toString();
        String khozyajstvo = khozyajstvoEditText.getText().toString();
        String technologiya = technologiyaZagotovkiEditText.getText().toString();
        String preobladayushchaya = preobladayushchayaPorodaEditText.getText().toString();

        // Проверка на null для Spinner
        String lesnichestvoId = lesnichestvoSpinner.getSelectedItem() != null ? lesnichestvoSpinner.getSelectedItem().toString() : null;
        String sposob = sposobRubkiSpinner.getSelectedItem() != null ? sposobRubkiSpinner.getSelectedItem().toString() : null;

        // Проверка на null для обязательных полей
        if (lesnichestvoId == null || sposob == null) {
            Toast.makeText(getContext(), "Пожалуйста, выберите все необходимые поля", Toast.LENGTH_SHORT).show();
            return;
        }

        if (naimenovanie.isEmpty() || khozyajstvo.isEmpty() || technologiya.isEmpty() || preobladayushchaya.isEmpty()) {
            Toast.makeText(getContext(), "Пожалуйста, заполните все необходимые поля", Toast.LENGTH_SHORT).show();
            return;
        }

        // Создание объекта модели Delyanka
        Delyanka delyanka = new Delyanka(id, lesnichestvoId, naimenovanie, khozyajstvo, sposob, technologiya, preobladayushchaya);

        // Сохранение в Firebase
        if (id != null) {
            databaseReference.child(id).setValue(delyanka)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Данные сохранены", Toast.LENGTH_SHORT).show();
                            clearFields(); // Очистка полей после сохранения
                        } else {
                            Toast.makeText(getContext(), "Ошибка сохранения", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void loadLesnichestvoData() {
        lesnichestvoNames = new ArrayList<>();
        lesnichestvoReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Lesnichestvo lesnichestvo = snapshot.getValue(Lesnichestvo.class);
                    if (lesnichestvo != null) {
                        lesnichestvoNames.add(lesnichestvo.getNaimenovanie());
                    }
                }

                // Установка адаптера в Spinner после загрузки данных
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lesnichestvoNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                lesnichestvoSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Ошибка при загрузке данных", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSposobRubkiSpinner() {
        // Список для Spinner способов рубки
        String[] sposobRubkiOptions = {"Выборочная", "Сплошная"};

        // Установка адаптера для Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, sposobRubkiOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sposobRubkiSpinner.setAdapter(adapter);
    }

    private void clearFields() {
        naimenovanieEditText.setText("");
        khozyajstvoEditText.setText("");
        technologiyaZagotovkiEditText.setText("");
        preobladayushchayaPorodaEditText.setText("");
        lesnichestvoSpinner.setSelection(0);  // Сброс Spinner
        sposobRubkiSpinner.setSelection(0);  // Сброс Spinner
    }

    private void setupKeyboardHiding() {
        // Скрытие клавиатуры при нажатии на экран
        constraintLayout.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                hideKeyboard();
            }
            return false;
        });
    }

    private void hideKeyboard() {
        // Получаем текущую активность и контекст
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && getActivity().getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }
}
