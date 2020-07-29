package com.example.welnfo.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.welnfo.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialCalendarView mCalendarView;
    private Button mConfirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initView();
    }

    private void initView() {
        mCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        mConfirmBtn = (Button) findViewById(R.id.btn_confirm);
        mConfirmBtn.setOnClickListener(this);

        //动态的设置可选择的最新日期,应该是今天
        Calendar calendar = Calendar.getInstance();
        //注意java中日历使用的是 格里高利历,月份是从0开始的(0-11)
        mCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)//设置一周的第一天是周几
                .setMinimumDate(CalendarDay.from(2019, 4, 3))//设置最早的日期
                .setMaximumDate(CalendarDay.from(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)))//设置最新日期,需要动态设置
                .setCalendarDisplayMode(CalendarMode.MONTHS)//设置日历的模式
                .commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                CalendarDay date = mCalendarView.getSelectedDate();
                if (date != null){
                    EventBus.getDefault().post(date);
                    finish();
                }
                break;
            default:
                break;
        }
    }
}
