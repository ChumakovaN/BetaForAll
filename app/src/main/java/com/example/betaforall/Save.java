package com.example.betaforall;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.betaforall.model.Equipment;
import com.example.betaforall.model.Otvody;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Save extends AppCompatActivity {

    private Spinner lesnichestvoSpinner, delyankaSpinner, brigadaSpinner, equipmentSpinner;
    private FirebaseDatabase database;
    private DatabaseReference lesnichestvoRef, delyankaRef, brigadaRef, equipmentRef, otvodyRef;

    private String selectedLesnichestvoId = null;
    private String selectedDelyankaId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        // Инициализация всех Spinner
        lesnichestvoSpinner = findViewById(R.id.lesnichestvoSpinner);
        delyankaSpinner = findViewById(R.id.delyankaSpinner);
        brigadaSpinner = findViewById(R.id.brigadaSpinner);
        equipmentSpinner = findViewById(R.id.equipmentSpinner);

        // Инициализация Firebase Database
        database = FirebaseDatabase.getInstance();
        lesnichestvoRef = database.getReference("lesnichestvo");
        delyankaRef = database.getReference("delyanka");
        brigadaRef = database.getReference("employees");
        equipmentRef = database.getReference("equipment");
        otvodyRef = database.getReference("otvody");

        // Загрузка данных в Spinner
        loadData(lesnichestvoRef, lesnichestvoSpinner, "naimenovanie", "lesnichestvo");

        // Установка слушателя на изменение лесничества
        lesnichestvoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedLesnichestvoId = lesnichestvoSpinner.getSelectedItem().toString();
                loadDelyankaData(selectedLesnichestvoId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedLesnichestvoId = null;
            }
        });

        loadData(delyankaRef, delyankaSpinner, "naimenovanie", "delyanka");
        loadBrigadaData();
        loadEquipmentData();

        // Кнопка для сохранения DOCX
        Button saveDocxButton = findViewById(R.id.savePdfButton);
        saveDocxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lesnichestvo = lesnichestvoSpinner.getSelectedItem().toString();
                String delyanka = delyankaSpinner.getSelectedItem().toString();
                String brigada = brigadaSpinner.getSelectedItem().toString();
                String equipment = equipmentSpinner.getSelectedItem().toString();

                // Получаем текущую дату
                String currentDate = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());

                // Получаем последний отвод по делянке
                getLastOtvodyData(delyanka, lesnichestvo, brigada, equipment, currentDate);
            }
        });
    }

    private void loadData(DatabaseReference reference, Spinner spinner, String childKey, String errorMessage) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> names = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child(childKey).getValue(String.class);
                    if (name != null) {
                        names.add(name);
                    }
                }

                if (names.isEmpty()) {
                    names.add("Данные отсутствуют");
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Save.this, android.R.layout.simple_spinner_item, names);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("SaveActivity", "Ошибка при загрузке данных " + errorMessage + ": " + databaseError.getMessage());
                Toast.makeText(Save.this, "Ошибка при загрузке данных " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDelyankaData(String lesnichestvoId) {
        if (lesnichestvoId != null) {
            delyankaRef.orderByChild("lesnichestvoId").equalTo(lesnichestvoId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<String> delyankaNames = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String name = snapshot.child("naimenovanie").getValue(String.class);
                                if (name != null) {
                                    delyankaNames.add(name);
                                }
                            }

                            if (delyankaNames.isEmpty()) {
                                delyankaNames.add("Данные отсутствуют");
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(Save.this, android.R.layout.simple_spinner_item, delyankaNames);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            delyankaSpinner.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("SaveActivity", "Ошибка при загрузке данных делянки: " + databaseError.getMessage());
                        }
                    });
        }
    }

    private void loadBrigadaData() {
        brigadaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> brigadaNames = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("brigade").getValue(String.class);
                    if (name != null) {
                        brigadaNames.add(name);
                    }
                }

                if (brigadaNames.isEmpty()) {
                    brigadaNames.add("Данные отсутствуют");
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Save.this, android.R.layout.simple_spinner_item, brigadaNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                brigadaSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("SaveActivity", "Ошибка при загрузке данных бригады: " + databaseError.getMessage());
            }
        });
    }

    private void loadEquipmentData() {
        equipmentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> equipmentNames = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Equipment equipment = snapshot.getValue(Equipment.class);
                    if (equipment != null) {
                        equipmentNames.add(equipment.getEquipmentName());
                    }
                }

                if (equipmentNames.isEmpty()) {
                    equipmentNames.add("Данные отсутствуют");
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Save.this, android.R.layout.simple_spinner_item, equipmentNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                equipmentSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("SaveActivity", "Ошибка при загрузке данных техники: " + databaseError.getMessage());
                Toast.makeText(Save.this, "Ошибка при загрузке данных техники", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getLastOtvodyData(String delyanka, String lesnichestvo, String brigada, String equipment, String currentDate) {
        otvodyRef.orderByChild("delyankaId").equalTo(delyanka).limitToLast(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Логируем количество найденных данных
                            Log.d("SaveActivity", "Найдено " + dataSnapshot.getChildrenCount() + " данных для delyanka: " + delyanka);

                            // Извлекаем первый элемент из результата
                            DataSnapshot lastOtvody = dataSnapshot.getChildren().iterator().next();
                            Otvody otvody = lastOtvody.getValue(Otvody.class);

                            if (otvody != null) {
                                // Логируем полученные данные
                                Log.d("SaveActivity", "Получен отвод: " + otvody.toString());
                                generateDocx(lesnichestvo, delyanka, brigada, equipment, currentDate, otvody);
                            } else {
                                // Логируем, если отвод пустой
                                Log.d("SaveActivity", "Оtvody пустой, но данные найдены.");
                                generateDocx(lesnichestvo, delyanka, brigada, equipment, currentDate, null);
                            }
                        } else {
                            // Логируем, если данные не найдены
                            Log.d("SaveActivity", "Нет данных для delyanka: " + delyanka);
                            generateDocx(lesnichestvo, delyanka, brigada, equipment, currentDate, null);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("SaveActivity", "Ошибка при получении данных отвода: " + databaseError.getMessage());
                    }
                });
    }




    private void generateDocx(String lesnichestvo, String delyanka, String brigada, String equipment, String currentDate, Otvody otvody) {
        // Создаем новый документ
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();

        // Добавляем данные в документ
        run.setText("Лесничество: " + lesnichestvo);
        run.addBreak();
        run.setText("Делянка: " + delyanka);
        run.addBreak();
        if (otvody != null) {
            String result = (otvody.getPoroda() != null ? otvody.getPoroda() : "Нет данных") + " | " +
                    (otvody.getDelovaya() != null ? otvody.getDelovaya() : "Нет данных") + " | " +
                    (otvody.getDrovyannaya() != null ? otvody.getDrovyannaya() : "Нет данных") + " | " +
                    (otvody.getOtkhody() != null ? otvody.getOtkhody() : "Нет данных")  + " | " +
                    (otvody.getOtvetstvennyi() != null ? otvody.getOtvetstvennyi() : "Нет данных") + " | " +
                    (otvody.getKommentariy() != null ? otvody.getKommentariy() : "Нет данных");
            run.setText("Отводы: " + result);
            run.addBreak();
        } else {
            run.setText("Нет данных о выводах");
            run.addBreak();
        }
        run.setText("Бригада: " + brigada);
        run.addBreak();
        run.setText("Техника: " + equipment);
        run.addBreak();
        run.setText("Дата: " + currentDate);
        run.addBreak();
        // Создаем имя файла
        String fileName = "SaveDocument.docx";

        // Получаем путь к директории "Загрузки"
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

        // Вставляем файл в MediaStore
        Uri fileUri = getContentResolver().insert(MediaStore.Files.getContentUri("external"), contentValues);

        // Записываем данные в файл
        try (OutputStream outputStream = getContentResolver().openOutputStream(fileUri)) {
            if (outputStream != null) {
                document.write(outputStream);
                Toast.makeText(this, "Файл сохранен в Загрузки как " + fileName, Toast.LENGTH_LONG).show();
            } else {
                Log.e("SaveActivity", "Не удалось открыть поток для записи файла.");
            }
        } catch (IOException e) {
            Log.e("SaveActivity", "Ошибка при создании документа: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Ошибка при создании документа", Toast.LENGTH_SHORT).show();
        }
    }


}
