package com.example.betaforall.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.betaforall.R;
import com.example.betaforall.model.Lesnichestvo;
import com.example.betaforall.databinding.FragmentHomeBinding;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private EditText naimenovanieEditText, obemEditText;
    private Button saveButton;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Инициализация элементов UI
        naimenovanieEditText = root.findViewById(R.id.naimenovanieEditText);
        obemEditText = root.findViewById(R.id.obemEditText);
        saveButton = root.findViewById(R.id.saveButton);

        // Добавление "м³" в поле ввода объема
        obemEditText.addTextChangedListener(new TextWatcher() {
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
                    obemEditText.setText(input + " м³");
                    obemEditText.setSelection(obemEditText.getText().length() - 3); // Устанавливаем курсор перед "м³"
                }
            }
        });

        // Обработчик кнопки сохранения
        saveButton.setOnClickListener(view -> {
            String naimenovanie = naimenovanieEditText.getText().toString();
            String obemString = obemEditText.getText().toString();
            if (naimenovanie.isEmpty() || obemString.isEmpty()) {
                Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }
            // Убираем "м³" для сохранения в базу данных
            obemString = obemString.replace(" м³", "");
            int obem = Integer.parseInt(obemString);

            // Создаем объект Lesnichestvo и сохраняем в Firebase
            String id = databaseReference.push().getKey();
            saveLesnichestvoToFirebase(id, naimenovanie, obem);

            // Очистка полей после сохранения
            naimenovanieEditText.setText("");
            obemEditText.setText("");
            Toast.makeText(getContext(), "Данные сохранены", Toast.LENGTH_SHORT).show();
        });

        // Скрытие клавиатуры при нажатии на экран
        root.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                View currentFocus = getActivity().getCurrentFocus();
                if (currentFocus != null) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                }
            }
            return false;
        });

        return root;
    }

    // Метод для сохранения данных в Firebase
    private void saveLesnichestvoToFirebase(String id, String naimenovanie, int obem) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("lesnichestvo");

        Lesnichestvo lesnichestvo = new Lesnichestvo(id, naimenovanie, obem);
        myRef.child(id).setValue(lesnichestvo);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
