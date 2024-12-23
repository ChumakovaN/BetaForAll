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

public class regist extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        // Инициализация FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Инициализация элементов интерфейса
        emailEditText = findViewById(R.id.Mailreg);
        passwordEditText = findViewById(R.id.passwordreg);
        confirmPasswordEditText = findViewById(R.id.secondpasswordreg);
        registerButton = findViewById(R.id.button);
        ConstraintLayout constraintLayout = findViewById(R.id.reg); // замените на ваш ID
        hideKeyboardOnTouch(constraintLayout, this);


        TextView AuthTextView = findViewById(R.id.Auth);
        AuthTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(regist.this, MainActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            return;
        }

        // Регистрация пользователя через Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(regist.this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                        // Перенаправление на другую активность после успешной регистрации
                        Intent intent = new Intent(regist.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(regist.this, "Ошибка регистрации: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
