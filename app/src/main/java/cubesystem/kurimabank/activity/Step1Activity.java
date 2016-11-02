package cubesystem.kurimabank.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import cubesystem.kurimabank.R;
import cubesystem.kurimabank.model.Action;
import cubesystem.kurimabank.model.ActionDelay;
import cubesystem.kurimabank.model.ActionVacation;
import cubesystem.kurimabank.model.Contact;
import cubesystem.kurimabank.model.Info;
import cubesystem.kurimabank.view.SetTime;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import cubesystem.kurimabank.db.MySQLiteOpenHelper;

public class Step1Activity extends AppCompatActivity {
    private RadioGroup mRadioGroupAction;
    private EditText mEditText1;
    private SetTime mSetTimeComingTime;
    private Button mButtonNext;

    private Action[] actionList = {
            new ActionDelay("遅刻",""),
            new ActionVacation("午前半休"),
            new ActionVacation("午後半休"),
            new ActionVacation("全休")
    };

    //★
    static SQLiteDatabase mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step1);


        Contact firstContact = Info.getInstance().allContact.get(0);
        if (Info.getInstance().myContact.introduce.isEmpty()
                || firstContact.name.isEmpty()
                || firstContact.email.isEmpty()){
            Intent i = new Intent(Step1Activity.this, Step3Activity.class);
            startActivity(i);
        }


        mRadioGroupAction = (RadioGroup)findViewById(R.id.radioGroupAction);
        mRadioGroupAction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton1) {
                    Info.getInstance().action = actionList[0];
                } else if (checkedId == R.id.radioButton2) {
                    Info.getInstance().action = actionList[1];
                } else if (checkedId == R.id.radioButton3) {
                    Info.getInstance().action = actionList[2];
                } else if (checkedId == R.id.radioButton4) {
                    Info.getInstance().action = actionList[3];
                }
                if (checkedId == R.id.radioButton1) {
                    mEditText1.setEnabled(true);
                    mSetTimeComingTime.show();
                }
                else {
                    mEditText1.setEnabled(false);
                }
            }
        });
        mEditText1 = (EditText)findViewById(R.id.editText1);
        ActionDelay delay = (ActionDelay)actionList[0];
        mEditText1.setText(delay.getmComingTime());
        mEditText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ActionDelay delay = (ActionDelay) actionList[0];
                delay.setmComingTime(s.toString());
            }
        });

        mEditText1.setEnabled(false);
        //time picker for editText
        mSetTimeComingTime = new SetTime(mEditText1, this);

        mButtonNext= (Button)findViewById(R.id.button);
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //error
                if (Info.getInstance().action == null) {
                    //show error alert
                    new AlertDialog.Builder(Step1Activity.this)
                            .setMessage(R.string.not_yet_select)
                            .setPositiveButton(android.R.string.yes, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return;
                }
                //move to Step2Activity
                Intent intent = new Intent(Step1Activity.this, Step2Activity.class);
                startActivity(intent);
            }
        });

        //★DBオープン
        MySQLiteOpenHelper hlpr = new MySQLiteOpenHelper(getApplicationContext());
        mydb = hlpr.getWritableDatabase();

        //★列の挿入(テスト)
        ContentValues values = new ContentValues();
        values.put("DETAILNO", "0000000001");
        values.put("DETAILYMD", "20160905");
        values.put("RECEIVEPAYKBN", "1");
        values.put("ITMCD", "ITM01");
        values.put("ABSTRACT", "摘要");
        values.put("RECEIVEPAYMETHODCD", "1");
        values.put("MONEY", 2000);
        mydb.insert("KURT_DETAIL", null, values);

    }
    @Override
     public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_step, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== R.id.action_setting){
            //move to SettingActivity(Step3Activity)
            Intent intent = new Intent(Step1Activity.this, Step3Activity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
