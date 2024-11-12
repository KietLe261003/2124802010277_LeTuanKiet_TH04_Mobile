package com.example.a2124802010277_letuankiet_th04;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    public static final String DATABASE_NAME="student.db";
    SQLiteDatabase db;
    EditText edtUsername, edtPassword;
    Button btnLogin, btnCloseLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnCloseLogin = (Button) findViewById(R.id.btnCloseLogin);

        initDB(); // Initialize the database

        // Set onClickListener for the login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve username and password from input fields
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                // Check if username is empty
                if (username.isEmpty()) {
                    Toast.makeText(getApplication(), "Xin vui lòng nhập tài khoản", Toast.LENGTH_LONG).show();
                    edtUsername.requestFocus(); // Move focus to the username field
                    return;
                }

                // Check if password is empty
                if (password.isEmpty()) {
                    Toast.makeText(getApplication(), "Xin vui lòng nhập mật khẩu", Toast.LENGTH_LONG).show();
                    edtPassword.requestFocus(); // Move focus to the password field
                    return;
                }

                // Check if username and password are correct
                if (isUser(edtUsername.getText().toString(), edtPassword.getText().toString())) {
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplication(), "Tài khoản hoặc mật khẩu không tồn tại!!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Set onClickListener for the close button
        btnCloseLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the login activity
                finish();
            }
        });

    }
    private void initDB() {
        db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        String sql = "";
        try {
            if (!isTableExists(db, "tbluser")) {
                sql += "CREATE TABLE tbluser (id_user INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,";
                sql += "username TEXT NOT NULL,";
                sql += "password TEXT NOT NULL)";
                db.execSQL(sql);
                sql = "insert into tbluser(username, password) values('admin', 'admin')";
                db.execSQL(sql);
            }
            if (!isTableExists(db, "tblclass")) {
                sql += "CREATE TABLE tblclass (";
                sql += "id_class INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,";
                sql += "code_class TEXT,";
                sql += "name_class TEXT,";
                sql += "number_student INTEGER);";
                db.execSQL(sql);
            }
            if (!isTableExists(db, "tblstudent")) {
                sql += "CREATE TABLE tblstudent (";
                sql += "id_student INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,";
                sql += "id_class INTEGER NOT NULL,";
                sql += "code_student TEXT NOT NULL,";
                sql += "name_student TEXT,";
                sql += "gender_student NUMBERIC,";
                sql += "birthday_student TEXT,";
                sql += "address_student TEXT);";
                db.execSQL(sql);
            }
        }
        catch (Exception ex) {
            Toast.makeText(this,
                    "Khởi tạo CSDL không thành công",
                    Toast.LENGTH_LONG).show();
        }
    }
    // Kiểm tra xem bảng có tồn tại chưa?
    private boolean isTableExists(SQLiteDatabase database, String tableName) {
        Cursor cursor = database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }
    // Check username and password
    private boolean isUser(String username, String password) {
        try {
            db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            Cursor c = db.rawQuery("select * from tbluser where username=? and password=?",
                    new String[] {username, password});
            c.moveToFirst();
            if (c.getCount() > 0)
                return true;
        } catch (Exception ex) {
            Toast.makeText(this, "Lỗi hệ thống", Toast.LENGTH_LONG).show();
        }
        return false;
    }



}