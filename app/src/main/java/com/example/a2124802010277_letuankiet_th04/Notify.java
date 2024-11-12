package com.example.a2124802010277_letuankiet_th04;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Notify {
    public static void exit(Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("Xác nhận đã thoát...!!!");

        // Icon of Alert Dialog
        alertDialogBuilder.setIcon(R.drawable.question);

        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage("Bạn có muốn thoát?");

        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // Đóng Activity hiện tại
                System.exit(1);
            }
        });

        alertDialogBuilder.setNegativeButton("Không đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
