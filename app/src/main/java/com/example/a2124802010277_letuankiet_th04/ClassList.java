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

import java.util.ArrayList;

public class ClassList extends AppCompatActivity {
    ListView lvClass;
    Button btnOpenClass;
    ArrayList<Room> classList = new ArrayList<>();
    MyAdapterClass adapterClass;
    SQLiteDatabase db;
    int posselected = -1;
    public static final int OPEN_CLASS = 113;
    public static final int EDIT_CLASS = 114;
    public static final int SAVE_CLASS = 115;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_class_list);
        lvClass = findViewById(R.id.lstClass);
        btnOpenClass = findViewById(R.id.btnOpenClass);

        getClassList();
        registerForContextMenu(lvClass);
        btnOpenClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassList.this,
                        InsertClass.class);
                startActivityForResult(intent, OPEN_CLASS);
            }
        });
        lvClass.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                posselected = position;
                return false;
            }
        });
    }
    public void getClassList() {
        try {
            classList=new ArrayList<>();
            classList.add(new Room("Mã lớp", "Tên lớp", "Sĩ số"));
            db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
            Cursor c = db.query("tblclass", null, null, null,
                    null, null, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                classList.add(new Room(c.getInt(0) + "",
                        c.getString(1).toString(),
                        c.getString(2).toString(),
                        c.getInt(3) + ""));
                c.moveToNext();
            }
            adapterClass = new MyAdapterClass(this,
                    R.layout.my_class,
                    classList);
            lvClass.setAdapter(adapterClass);
        }
        catch (Exception ex) {
            Toast.makeText(getApplicationContext(),
                    "Loi " + ex.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
    public void confirmDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Thông báo");
        alertDialogBuilder.setIcon(R.drawable.question);
        alertDialogBuilder.setMessage("Xác nhận xoá lớp học");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db = openOrCreateDatabase(Login.DATABASE_NAME,
                        MODE_PRIVATE, null);
                String id_class = classList.get(posselected).getId_class();
                if (db.delete("tblclass", "id_class=?",
                        new String[] {id_class}) != -1) {
                    classList.remove(posselected);
                    adapterClass.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(),
                            "Xoá lớp học thành công",
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
        inflater.inflate(R.menu.mnuclass, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.mnueditclass)
        {
            Room room = classList.get(posselected);
            Bundle bundle = new Bundle();
            Intent intent = new Intent(ClassList.this,
                    EditClass.class);
            bundle.putSerializable("room", room);
            intent.putExtra("data", bundle);
            startActivityForResult(intent, EDIT_CLASS);
            return true;
        }
        else if(item.getItemId()==R.id.mnudeleteclass)
        {
            confirmDelete();
            return true;
        }
        else if(item.getItemId()==R.id.mnucloseclass)
        {
            Notify.exit(this);
            return true;
        }
        else
            return super.onContextItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==OPEN_CLASS)
        {
            if (resultCode == SAVE_CLASS) {
                Bundle bundle = data.getBundleExtra("data");
                Room room = (Room) bundle.getSerializable("room");
                classList.add(room);
                adapterClass.notifyDataSetChanged();
            }
        }
        else if(requestCode==EDIT_CLASS)
        {
            if (resultCode == SAVE_CLASS) {
                getClassList();
                adapterClass.notifyDataSetChanged();
            }
        }
    }
}