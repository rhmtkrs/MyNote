package com.mynote.app.activity;
// Rahmat - 10120150 - IF4

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mynote.app.R;
import com.mynote.app.model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etPassword, etConfirmPassword;
    private Button btnRegister, btnBack;
    private FirebaseAuth auth;
    private DatabaseReference userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inisialisasi objek FirebaseAuth
        auth = FirebaseAuth.getInstance();
        userDatabase = FirebaseDatabase.getInstance().getReference("users"); // Menginisialisasi referensi ke node "users" di Firebase Database

        // Menghubungkan komponen View dengan elemen di layout
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);

        // Mengatur OnClickListener untuk tombol Register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Memanggil metode untuk melakukan pendaftaran pengguna
                registerUser();
            }
        });

        // Mengatur OnClickListener untuk tombol Kembali
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Menutup halaman saat tombol Kembali diklik
                finish();
            }
        });
    }

    private void registerUser() {
        // Mendapatkan nilai input dari komponen EditText
        String fullName = etFullName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        // Validasi input, memastikan password dan konfirmasi password sesuai
        if (!password.equals(confirmPassword)) {
            // Menampilkan pesan kesalahan jika password tidak sesuai
            Toast.makeText(this, "Password dan konfirmasi password tidak sesuai.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Melakukan pendaftaran pengguna dengan Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Pendaftaran berhasil
                            // Simpan data pengguna ke Firebase Database
                            String userId = auth.getCurrentUser().getUid();
                            User newUser = new User(userId, fullName, email);
                            userDatabase.child(userId).setValue(newUser);

                            // Arahkan pengguna ke MainActivity
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();

                        } else {
                            // Pendaftaran gagal, tampilkan pesan kesalahan
                            Toast.makeText(RegisterActivity.this, "Pendaftaran gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
