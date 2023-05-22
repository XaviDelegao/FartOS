package com.example.fartos;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private TextView textError, textRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        int color = getResources().getColor(R.color.colorPrimary);
        actionBar.setBackgroundDrawable(new ColorDrawable(color));


        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        textError = findViewById(R.id.text_error);
        textRegister = findViewById(R.id.text_register);

        Button buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    textError.setText("Por favor, complete todos los campos");
                    return;
                } else{
                    Intent i = new Intent(getApplicationContext(), Menu.class);
                    startActivity(i);
                }

                // Aquí puedes agregar tu lógica de inicio de sesión
                // Si la autenticación es exitosa, puedes iniciar la actividad principal

            }
        });

        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.fartos.Registro.class);
                startActivity(intent);
            }
        });
    }
}