package com.example.a2124802010277_letuankiet_th04;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.Serializable;
import java.util.ArrayList;

public class StudentList extends AppCompatActivity {
    ListView lvStudent;
    Button btnOpenStudent;
    ArrayList<Student> studentList = new ArrayList<>();
    MyAdapterStudent adapterStudent;
    SQLiteDatabase db;
    int posselected = -1;
    public static final int OPEN_STUDENT = 113;
    public static final int EDIT_STUDENT = 114;
    public static final int SAVE_STUDENT = 115;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_list);
        lvStudent = findViewById(R.id.lvStudent);
        btnOpenStudent = findViewById(R.id.btnOpenStudent);

        getStudentList();
        registerForContextMenu(lvStudent);
        btnOpenStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentList.this,
                        InsertStudent.class);
                startActivityForResult(intent, OPEN_STUDENT);
            }
        });
        lvStudent.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                posselected = position;
                return false;
            }
        });
    }
    private void getStudentList() {
        studentList=new ArrayList<>();
        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor c = db.rawQuery("select tblclass.id_class, tblclass.name_class, tblstudent.id_student," +
                "tblstudent.code_student, tblstudent.name_student, tblstudent.gender_student," +
                "tblstudent.birthday_student, tblstudent.address_student from tblclass," +
                "tblstudent where tblclass.id_class=tblstudent.id_class", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            studentList.add(new Student(c.getString(0).toString(),
                    c.getString(1).toString(),
                    c.getString(2).toString(),
                    c.getString(3).toString(),
                    c.getString(4).toString(),
                    c.getString(5).toString(),
                    c.getString(6).toString(),
                    c.getString(7).toString()));
            c.moveToNext();
        }
        adapterStudent = new MyAdapterStudent(this,
                R.layout.my_student,
                studentList);
        lvStudent.setAdapter(adapterStudent);
    }
    public void confirmDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Thông báo");
        alertDialogBuilder.setIcon(R.drawable.question);
        alertDialogBuilder.setMessage("Xác nhận xoá sinh viên");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db = openOrCreateDatabase(Login.DATABASE_NAME,
                        MODE_PRIVATE, null);
                String id_student = studentList.get(posselected).getId_student();
                if (db.delete("tblstudent", "id_student=?",
                        new String[] {id_student}) != -1) {
                    studentList.remove(posselected);
                    adapterStudent.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(),
                            "Xoá sinh viên thành công",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        alertDialogBuilder.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnustudent, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mnueditstudent) {
            Student student = studentList.get(posselected);
            Bundle bundle = new Bundle();
            Intent intent = new Intent(StudentList.this, EditStudent.class);
            bundle.putSerializable("student",student);
            intent.putExtra("data", bundle);
            startActivityForResult(intent, EDIT_STUDENT);
            return true;
        } else if(item.getItemId() == R.id.mnudeletestudent) {
            confirmDelete();
            return true;
        } else if(item.getItemId() == R.id.mnuclosestudent) {
            Notify.exit(this);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OPEN_STUDENT) {
            if (resultCode == SAVE_STUDENT && data != null) {
                Bundle bundle = data.getBundleExtra("data");
                Student student = (Student) bundle.getSerializable("student");
                studentList.add(student);
                adapterStudent.notifyDataSetChanged();
            }
        } else if (requestCode == EDIT_STUDENT) {
            if (resultCode == SAVE_STUDENT && data != null) {
                getStudentList();
                adapterStudent.notifyDataSetChanged();
            }
        }
    }


}