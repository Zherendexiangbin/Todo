package net.onest.time;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.mut_jaeryo.circletimer.CircleTimer;

import net.onest.time.entity.Item;
import net.onest.time.navigation.activity.NavigationActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                calendarView.scrollToCurrent();//回到“今天日期”
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, NavigationActivity.class);
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,btn,"fab").toBundle());
            }
        });


    }

}