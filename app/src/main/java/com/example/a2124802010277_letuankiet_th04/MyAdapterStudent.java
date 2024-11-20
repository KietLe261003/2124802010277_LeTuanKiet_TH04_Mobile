package com.example.a2124802010277_letuankiet_th04;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyAdapterStudent extends ArrayAdapter<Student> {
    ArrayList<Student> studentList = new ArrayList<>();

    public MyAdapterStudent(@NonNull Context context, int resource, ArrayList<Student> objects) {
        super(context, resource, objects);
        studentList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.my_student, null);
//        ImageView imgStudent = v.findViewById(R.id.imgStudent);
        TextView txtStudentClass = v.findViewById(R.id.txtStudentClass);
        TextView txtStudentName = v.findViewById(R.id.txtStudentName);
        TextView txtStudentBirthday = v.findViewById(R.id.txtStudentBirthday);
        TextView txtStudentGender = v.findViewById(R.id.txtStudentGender);
        TextView txtStudentAddress = v.findViewById(R.id.txtStudentAddress);

//        imgStudent.setImageResource();
        txtStudentClass.setText(studentList.get(position).getName_class());
        txtStudentName.setText(studentList.get(position).getName_student());
        txtStudentBirthday.setText(studentList.get(position).getBirthday_student());
        if(studentList.get(position).getGender_student().equals("1"))
            txtStudentGender.setText("Nam");
        else
            txtStudentGender.setText("Nữ");
        //txtStudentGender.setText(studentList.get(position).getGender_student());
        txtStudentAddress.setText(studentList.get(position).getAddress_student());
        return v;
    }
}
