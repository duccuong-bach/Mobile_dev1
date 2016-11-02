package cubesystem.kurimabank.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;

import cubesystem.kurimabank.R;
import cubesystem.kurimabank.db.MySQLiteOpenHelper;
import cubesystem.kurimabank.model.Info;

/**
 * Created by kurimabank-kurima on 2016/08/31.
 */
public class Login extends AppCompatActivity {
    private Calendar mRadioGroupAction;
    private Button mButtonNext;
    //private EditText mEditText1;
    //private SetTime mSetTimeComingTime;
    //private Button mButtonNext;

    //private Action[] actionList = {
    //new ActionDelay("遅刻",""),
    //new ActionVacation("午前半休"),
    //new ActionVacation("午後半休"),
    //new ActionVacation("全休")

    mButtonNext= (Button)findViewById(R.id.email_sign_in_button);
    mButtonNext.setOnClickListener(new View.OnClickListener() {
        @Override
                //★ここにパスワードチェック入れる
        public void onClick(View v) {
            //error
            //if (Info.getInstance().action == null) {
                //show error alert
                //new AlertDialog.Builder(Login.this)
                       // .setMessage(R.string.not_yet_select)
                        //.setPositiveButton(android.R.string.yes, null)
                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        //.show();
                //return;
            }
            //move to Step2Activity
            Intent intent = new Intent(Login.this, Summary.class);
            startActivity(intent);
        }


    //★
    static SQLiteDatabase mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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



}