package com.example.betaforall.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.betaforall.R;
import com.example.betaforall.model.Employee;
import com.example.betaforall.model.Equipment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BlankFragment extends Fragment {

    public BlankFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        // Инициализация Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Инициализация элементов интерфейса
        AutoCompleteTextView employeeNameField = view.findViewById(R.id.emploers);
        AutoCompleteTextView brigadeField = view.findViewById(R.id.autoCompleteTextView);
        AutoCompleteTextView equipmentNameField = view.findViewById(R.id.technic);

        Button saveEmployeeButton = view.findViewById(R.id.saveEmployeeButton);
        Button deleteEmployeeButton = view.findViewById(R.id.deleteEmployeeButton);

        Button saveEquipmentButton = view.findViewById(R.id.saveEquipmentButton);
        Button deleteEquipmentButton = view.findViewById(R.id.deleteEquipmentButton);



        // Получаем данные о сотрудниках из Firebase
        databaseReference.child("employees").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> employeesList = new ArrayList<>();
                for (DataSnapshot employeeSnapshot : dataSnapshot.getChildren()) {
                    Employee employee = employeeSnapshot.getValue(Employee.class);
                    if (employee != null) {
                        employeesList.add(employee.getFullName()); // Добавляем имя сотрудника в список
                    }
                }
                // Настройка адаптера для сотрудников с фильтрацией
                ArrayAdapter<String> employeeAdapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_dropdown_item_1line, employeesList);
                employeeNameField.setAdapter(employeeAdapter);
                employeeNameField.setThreshold(1); // Начинать показывать варианты после 1 символа
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Обработка ошибок
                Toast.makeText(getContext(), "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
            }
        });

        // Получаем данные о технике из Firebase
        databaseReference.child("equipment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> equipmentList = new ArrayList<>();
                for (DataSnapshot equipmentSnapshot : dataSnapshot.getChildren()) {
                    Equipment equipment = equipmentSnapshot.getValue(Equipment.class);
                    if (equipment != null) {
                        equipmentList.add(equipment.getEquipmentName()); // Добавляем название техники в список
                    }
                }
                // Настройка адаптера для техники с фильтрацией
                ArrayAdapter<String> equipmentAdapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_dropdown_item_1line, equipmentList);
                equipmentNameField.setAdapter(equipmentAdapter);
                equipmentNameField.setThreshold(1); // Начинать показывать варианты после 1 символа
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Обработка ошибок
                Toast.makeText(getContext(), "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
            }
        });

        // Получаем данные о бригадах из Firebase
        databaseReference.child("brigades").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> brigadesList = new ArrayList<>();
                for (DataSnapshot brigadeSnapshot : dataSnapshot.getChildren()) {
                    String brigadeName = brigadeSnapshot.getValue(String.class);
                    if (brigadeName != null) {
                        brigadesList.add(brigadeName); // Добавляем название бригады в список
                    }
                }
                // Настройка адаптера для бригад с фильтрацией
                ArrayAdapter<String> brigadeAdapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_dropdown_item_1line, brigadesList);
                brigadeField.setAdapter(brigadeAdapter);
                brigadeField.setThreshold(1); // Начинать показывать варианты после 1 символа
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Обработка ошибок
                Toast.makeText(getContext(), "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
            }
        });

        // Логика сохранения сотрудника
        saveEmployeeButton.setOnClickListener(v -> {
            String id = databaseReference.push().getKey();
            String fullName = employeeNameField.getText().toString();
            String brigade = brigadeField.getText().toString();

            Employee employee = new Employee(id, fullName, brigade);
            databaseReference.child("employees").child(id).setValue(employee);

            // Очистка полей
            employeeNameField.setText("");
            brigadeField.setText("");
            Toast.makeText(getContext(), "Сотрудник сохранен", Toast.LENGTH_SHORT).show();
        });

        // Логика удаления сотрудника
        deleteEmployeeButton.setOnClickListener(v -> {
            String fullName = employeeNameField.getText().toString();
            databaseReference.child("employees").orderByChild("fullName").equalTo(fullName)
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (DataSnapshot snapshot : task.getResult().getChildren()) {
                                snapshot.getRef().removeValue();  // Удаление записи сотрудника
                            }
                            // Очистка полей
                            employeeNameField.setText("");
                            brigadeField.setText("");
                            Toast.makeText(getContext(), "Сотрудник удален", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Ошибка удаления", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Логика сохранения техники
        saveEquipmentButton.setOnClickListener(v -> {
            String id = databaseReference.push().getKey();
            String equipmentName = equipmentNameField.getText().toString();

            Equipment equipment = new Equipment(id, equipmentName);
            databaseReference.child("equipment").child(id).setValue(equipment);

            // Очистка поля
            equipmentNameField.setText("");
            Toast.makeText(getContext(), "Техника сохранена", Toast.LENGTH_SHORT).show();
        });

        // Логика удаления техники
        deleteEquipmentButton.setOnClickListener(v -> {
            String equipmentName = equipmentNameField.getText().toString();
            databaseReference.child("equipment").orderByChild("equipmentName").equalTo(equipmentName)
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (DataSnapshot snapshot : task.getResult().getChildren()) {
                                snapshot.getRef().removeValue();  // Удаление записи о технике
                            }
                            // Очистка поля
                            equipmentNameField.setText("");
                            Toast.makeText(getContext(), "Техника удалена", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Ошибка удаления", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Скрытие клавиатуры при нажатии на пустое пространство
        FrameLayout frameLayout = view.findViewById(R.id.fragment_blank);  // Убедитесь, что у FrameLayout правильный ID
        frameLayout.setOnTouchListener((v, event) -> {
            hideKeyboard(v);
            return false; // Возвращаем false, чтобы обработка события продолжалась
        });

        return view;
    }

    // Метод для скрытия клавиатуры
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
