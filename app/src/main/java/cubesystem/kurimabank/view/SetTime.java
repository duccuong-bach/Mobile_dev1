package cubesystem.kurimabank.view;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by kurimabank-kurima on 2016/03/24.
 */
public class SetTime implements TimePickerDialog.OnTimeSetListener, View.OnClickListener, View.OnTouchListener, View.OnFocusChangeListener {
    private EditText editText;
    private Calendar myCalendar;
    private Context ctx;
    private boolean isTimePickerShow;

    public SetTime(EditText editText, Context ctx) {
        this.editText = editText;
        this.myCalendar = Calendar.getInstance();
        this.ctx = ctx;
        this.isTimePickerShow = false;

        this.editText.setOnClickListener(this);
        this.editText.setOnFocusChangeListener(this);
        this.editText.setOnTouchListener(this);
    }

    public void show() {
        if (!isTimePickerShow) {
            int hour;
            int minute;
            try {
                String text = this.editText.getText().toString().trim();
                String[] places = text.split(":");
                hour = Integer.parseInt(places[0]);
                minute = Integer.parseInt(places[1]);
            } catch (Exception a) {
                hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                minute = myCalendar.get(Calendar.MINUTE);
                this.editText.setText(String.format("%02d:%02d", hour, minute));
            }

            TimePickerDialog timePickerDialog = new TimePickerDialog(ctx, this, hour, minute, true);

            timePickerDialog.setOnCancelListener(new TimePickerDialog.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    SetTime.this.isTimePickerShow = false;
                }
            });

            timePickerDialog.setOnDismissListener(new TimePickerDialog.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    SetTime.this.isTimePickerShow = false;
                }
            });

            timePickerDialog.show();
            SetTime.this.isTimePickerShow = true;
            SetTime.this.editText.requestFocus(View.FOCUS_DOWN);

        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.editText.setText(String.format("%02d:%02d", hourOfDay, minute));
    }

    @Override
    public void onClick(View v) {
        show();
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        show();
        return true;
    }
}