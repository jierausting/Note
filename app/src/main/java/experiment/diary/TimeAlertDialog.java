package experiment.diary;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by Administrator on 2016/12/22 0022.
 */
public class TimeAlertDialog  {
    Context context;
    android.app.AlertDialog ad;
    TextView titleView;
    Switch aSwitch;
    public TimePicker timePicker;
    Button positiveButton;
    Button negativeButton;
    Animation mAnimation = null;
    public TimeAlertDialog(Context context) {
        this.context = context;
        ad = new android.app.AlertDialog.Builder(context).create();
        ad.show();
        Window window =ad.getWindow();
        window.setContentView(R.layout.time_dialog);
        titleView = (TextView)window.findViewById(R.id.time_dialog_title);
        aSwitch = (Switch)window.findViewById(R.id.aswitch);
        timePicker = (TimePicker)window.findViewById(R.id.timePicker);
        positiveButton = (Button) window.findViewById(R.id.okButton);
        negativeButton =(Button) window.findViewById(R.id.cancelButton);
        timePicker.setIs24HourView(true);
    }
    public void setTitle(int resId) {
        titleView.setText(resId);
    }
    public void setTitle(String title){
        titleView.setText(title);
    }
    public void setSwitch(final View.OnClickListener listener) {
        aSwitch.setOnClickListener(listener);
    }
    public void setPositiveButton( final View.OnClickListener listener) {
        positiveButton.setOnClickListener(listener);
    }
    public void setNegativeButton(final View.OnClickListener listener) {
        negativeButton.setOnClickListener(listener);
    }
    public void dismiss() {
        ad.dismiss();
    }
    public void show() {
        ad.show();
    }
    public int getHour() {
        return timePicker.getCurrentHour();
    }
    public int getMinute() {
        return timePicker.getCurrentMinute();
    }
    public void setHour(int hour) {
        timePicker.setCurrentHour(hour);
    }
    public void setMinute(int minute) {
        timePicker.setCurrentMinute(minute);
    }
    public void setPickerEnabled(Boolean enabled) {
        timePicker.setEnabled(enabled);
    }
    public void setAlpha(float alpha) {
        timePicker.setAlpha(alpha);
    }
    public void setChecked(boolean b) {
        aSwitch.setChecked(b);
    }
    public boolean getChecked() {
        return aSwitch.isChecked();
    }
}
