package com.ankan.android.hikerswatch;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Setting2 extends AppCompatActivity {

    Button Redbutton,Greenbutton,Yellowbutton;
    RelativeLayout background;
    LinearLayout LL;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting2);

        Redbutton = findViewById(R.id.Redbutton);
        Greenbutton = findViewById(R.id.Greenbutton);

        Yellowbutton = findViewById(R.id.Yellobutton);
        LL = findViewById(R.id.LL);




//
//        if(getColor() != getResources().getColor(R.color.colorPrimary)){
//            LL.setBackgroundColor(getColor());


        }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public void passColorCode(int color) {
        Intent intent = new Intent(this, MainActivity.class);
        String colorCode = Integer.toString(color);
        intent.putExtra("ColorChoice", colorCode); //pass message through intent
        startActivity(intent);
    }

    public void triggerColorPicker(View view) {
        final ColorPicker cp = new ColorPicker(Setting2.this, 255, 55, 50);

        cp.show(); //show color picker
        Button selectButton = cp.findViewById(R.id.okColorButton);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passColorCode(cp.getColor());
                cp.dismiss();
            }
        });
    }










//
//        Redbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LL.setBackgroundColor(Color.parseColor("RED"));
////            LL.setBackgroundColor(getResources().getColor(R.color.colorred));
//
////            background.setBackgroundColor(getResources().getColor(R.color.colorred));
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////
////                    getWindow().setColorMode(getResources().getColor(R.color.colorred));
////
////
////                }
//                storeColor(getResources().getColor(R.color.colorred));
//            }
//        });
//
//        Greenbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                background.setBackgroundColor(Color.GREEN);
//                LL.setBackgroundColor(Color.parseColor("GREEN"));
//
////              LL.setBackgroundColor(getResources().getColor(R.color.colorgreen));
////              background.setBackgroundColor(getResources().getColor(R.color.colorgreen));
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////
////                    getWindow().setStatusBarColor(getResources().getColor(R.color.colorgreen));
////
////
////                }
//             storeColor(getResources().getColor(R.color.colorgreen));
//            }
//        });
//
//        Yellowbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                background.setBackgroundColor(Color.YELLOW);
//                LL.setBackgroundColor(Color.parseColor("Yellow"));
//
//
////              LL.setBackgroundColor(getResources().getColor(R.color.coloryellow));
////              background.setBackgroundColor(getResources().getColor(R.color.coloryellow));
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////
////                    getWindow().setStatusBarColor(getResources().getColor(R.color.coloryellow));
////
////
////                }
//             storeColor(getResources().getColor(R.color.coloryellow));
//           }
//        });
//
//    }
//
//    private void storeColor(int color){
//        SharedPreferences msharedPreference = getSharedPreferences("LL", MODE_PRIVATE);
//        SharedPreferences.Editor mEditor = msharedPreference.edit();
//        mEditor.putInt("color", color);
//        mEditor.apply();
//    }
//    private int getColor(){
//        SharedPreferences msharedPreference = getSharedPreferences("LL", MODE_PRIVATE);
//        int selectedColor = msharedPreference.getInt("color",getResources().getColor(R.color.colorPrimary));
//
//        return selectedColor;
//    }
}

