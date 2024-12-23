package com.example.betaforall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация FirebaseAuth
        mAuth = FirebaseAuth.getInstance();


        // Находим элементы интерфейса
        emailEditText = findViewById(R.id.Mail);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.button);
        ConstraintLayout constraintLayout = findViewById(R.id.main); // замените на ваш ID
        hideKeyboardOnTouch(constraintLayout, this);


        // Обработчик кнопки входа
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        // Обработчик перехода на регистрацию
        TextView registTextView = findViewById(R.id.regist);
        registTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, regist.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        // Аутентификация пользователя через Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Успешная авторизация
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(MainActivity.this, "Авторизация успешна!", Toast.LENGTH_SHORT).show();
                        // Переход на другую активность, например, главную
                        startActivity(new Intent(MainActivity.this, Program.class));
                        finish(); // Завершаем текущую активность
                    } else {
                        // Ошибка авторизации
                        Toast.makeText(MainActivity.this, "Ошибка: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void hideKeyboardOnTouch(View view, Activity activity) {
        // Слушатель для скрытия клавиатуры при касании фрейма
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Получаем InputMethodManager
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        // Скрываем клавиатуру
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
                return false; // Чтобы событие продолжало передаваться дальше
            }
        });
    }

}
