package com.mynote.app.activity;
// Rahmat - 10120150 - IF4

import static android.app.PendingIntent.getActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.mynote.app.R;
import com.mynote.app.databinding.ActivityMainBinding;
import com.mynote.app.ui.info.InfoFragment;
import com.mynote.app.ui.note.NoteFragment;
import com.mynote.app.ui.profile.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Inisialisasi FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getFragmentPage(new InfoFragment());

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                int itemId = item.getItemId();
                if (itemId == R.id.info){
                    fragment = new InfoFragment();
                } else if (itemId == R.id.note) {
                    fragment = new NoteFragment();
                } else if (itemId == R.id.profile ) {
                    fragment = new ProfileFragment();
                } else if (itemId == R.id.logout) {
                    showLogoutConfirmationDialog();
                }
                return getFragmentPage(fragment);
            }
        });
    }

    private void performLogout() {
        // Cek apakah mAuth telah diinisialisasi
        if (mAuth != null) {
            // Lakukan logout
            mAuth.signOut();

            // Tampilkan pesan toast berhasil logout
            Toast.makeText(MainActivity.this, "Berhasil Logout", Toast.LENGTH_SHORT).show();
        }

        // Setelah logout berhasil, arahkan pengguna kembali ke halaman login
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Selesaikan MainActivity saat logout
    }

    private void showLogoutConfirmationDialog() {
        // Buat AlertDialog untuk konfirmasi logout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakah Anda yakin ingin keluar aplikasi?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Jika pengguna yakin ingin keluar, lakukan logout
                        performLogout();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Jika pengguna membatalkan, tutup dialog
                        dialog.dismiss();
                    }
                });

        // Tampilkan dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean getFragmentPage(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.page_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}