package cubesystem.kurimabank.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import cubesystem.kurimabank.R;
import cubesystem.kurimabank.model.Contact;
import cubesystem.kurimabank.model.Info;

public class Step2Activity extends AppCompatActivity {
    public static final int SETTING_REQUEST_CD = 111;
    public static final int SENDEMAIL_REQUEST_CODE = 100;
    private Spinner spinnerReason;
    private EditText editTextDetail;
    private TextInputLayout textInputLayoutDetail;
    private Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step2);

        spinnerReason = (Spinner)findViewById(R.id.spinner);
        final String[] reason_values = getResources().getStringArray(R.array.reasons);
        for (int i = 0; i < reason_values.length; i++){
            if (reason_values[i].equalsIgnoreCase(Info.getInstance().reason)){
                spinnerReason.setSelection(i);
                break;
            }
        }

        spinnerReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Info.getInstance().reason = reason_values[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        editTextDetail = (EditText)findViewById(R.id.editText2);
        editTextDetail.setText(Info.getInstance().detail);
        editTextDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Info.getInstance().detail = s.toString();
                textInputLayoutDetail.setError(null);
                textInputLayoutDetail.setErrorEnabled(false);
            }
        });

        textInputLayoutDetail = (TextInputLayout)findViewById(R.id.textInputLayoutDetail);

        buttonSend = (Button)findViewById(R.id.button);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //その他の場合、詳細必須入力チェック
                if (Info.getInstance().reason.equalsIgnoreCase("その他") &&
                        Info.isBlank(Info.getInstance().detail)) {
                    textInputLayoutDetail.setError("必須入力");
                    textInputLayoutDetail.setErrorEnabled(true);
                    editTextDetail.requestFocus();
                    return;
                }

                //contact is empty
                Contact firstContact = Info.getInstance().allContact.get(0);
                if (Info.getInstance().myContact.introduce.isEmpty() ||
                        firstContact.name.isEmpty() ||
                        firstContact.email.isEmpty()) {
                    Intent i = new Intent(Step2Activity.this, Step3Activity.class);
                    startActivityForResult(i, SETTING_REQUEST_CD);
                    return;
                }

                sendEmail();

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getMenuInflater().inflate(R.menu.menu_step, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_setting:
                Intent i = new Intent(Step2Activity.this, Step3Activity.class);
                startActivity(i);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SENDEMAIL_REQUEST_CODE){
            new AlertDialog.Builder(Step2Activity.this)
                    .setMessage(R.string.remind_call)
                    .setPositiveButton(android.R.string.yes, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    private void sendEmail() {
        Location currentLocation = getLastBestLocation();
        ArrayList<String> toEmails = new ArrayList<String>();
        ArrayList<String> ccEmails = new ArrayList<String>();
        ArrayList<String> contactNames = new ArrayList<String>();

        for (Contact contact : Info.getInstance().allContact) {

            if (!contact.email.isEmpty() && !contact.name.isEmpty()) {
                contactNames.add(contact.name);
                if (contact.sendType == Contact.SendType.TO) {
                    toEmails.add(contact.email);
                } else if (contact.sendType == Contact.SendType.CC) {
                    ccEmails.add(contact.email);
                }
            }

        }
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, toEmails.toArray(new String[toEmails.size()]));
        i.putExtra(Intent.EXTRA_CC, ccEmails.toArray(new String[ccEmails.size()]));
        i.putExtra(Intent.EXTRA_SUBJECT, "遅延・休暇連絡");
        String body = "";
        body += TextUtils.join("\n", contactNames);
        body += "\n";
        body += "\n";
        body += String.format("お疲れ様です。\n%sです。",Info.getInstance().myContact.introduce);
        body += "\n";
        body += "\n";
        if(Info.getInstance().reason.equalsIgnoreCase("その他")){
            body += String.format("下記事情のため、\n");
        }
        else {
            body += String.format("%sのため、\n", Info.getInstance().reason);
        }

        body += Info.getInstance().action.getSummary();
        body += "\n";
        body += Info.getInstance().detail.isEmpty() ? "" : "\n" + Info.getInstance().detail + "\n";
        body += "\n";
        body += "【現在位置】";
        body += "\n";
        body += currentLocation != null ?
                String.format("https://www.google.co.jp/maps/place/%f,%f", currentLocation.getLatitude(), currentLocation.getLongitude()) :
                "現在位置情報を取得できません。";
        body += "\n";
        body += "\n";
        body += "以上、よろしくお願いいたします。";

        i.putExtra(Intent.EXTRA_TEXT, body);
        try {
            //startActivity(Intent.createChooser(i, "メールを送信"));
            startActivityForResult(Intent.createChooser(i, "メールを送信"), SENDEMAIL_REQUEST_CODE);

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Step2Activity.this, "インストールされたメールクライアントはありません。", Toast.LENGTH_SHORT).show();
        }
    }

    private Location getLastBestLocation() {
        LocationManager mLocationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
        }

        try {
            Location locationGPS =
                    mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNet =
                    mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            long GPSLocationTime = 0;
            if (null != locationGPS) {
                GPSLocationTime = locationGPS.getTime();
            }

            long NetLocationTime = 0;

            if (null != locationNet) {
                NetLocationTime = locationNet.getTime();
            }

            if (0 < GPSLocationTime - NetLocationTime) {
                return locationGPS;
            } else {
                return locationNet;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


}
