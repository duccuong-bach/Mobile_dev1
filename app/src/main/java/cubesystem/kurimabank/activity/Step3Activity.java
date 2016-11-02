package cubesystem.kurimabank.activity;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cubesystem.kurimabank.R;
import cubesystem.kurimabank.model.Contact;
import cubesystem.kurimabank.model.Info;
import cubesystem.kurimabank.view.ContactView;

public class Step3Activity extends AppCompatActivity {

    private LinearLayout contactViewLayout;
    private Button mButtonSave;
    private EditText editTextIntroduce;
    private TextInputLayout textInputLayoutIntroduce;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step3);

        contactViewLayout = (LinearLayout) findViewById(R.id.contact_view_layout);
        for (int i = 0; i < contactViewLayout.getChildCount(); i++) {
            ContactView child = (ContactView) contactViewLayout.getChildAt(i);
            Contact contact = Info.getInstance().allContact.get(i);
            if (i == 0) {
                child.spinnerSendType.setEnabled(false);
                child.textViewTitle.setText(String.format("連絡先%d（必須入力）", i + 1));
                child.spinnerSendType.setSelection(0);
            } else {
                child.textViewTitle.setText(String.format("連絡先%d", i + 1));
                if (contact.sendType == Contact.SendType.TO) {
                    child.spinnerSendType.setSelection(0);
                } else if (contact.sendType == Contact.SendType.CC) {
                    child.spinnerSendType.setSelection(1);
                }
            }
            child.editTextName.setText(contact.name);
            child.editTextEmail.setText(contact.email);


        }
        editTextIntroduce = (EditText) findViewById(R.id.editText3);
        editTextIntroduce.setText(Info.getInstance().myContact.introduce);
        textInputLayoutIntroduce = (TextInputLayout) findViewById(R.id.textInputLayoutIntroduce);
        editTextIntroduce.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textInputLayoutIntroduce.setError((null));
                textInputLayoutIntroduce.setErrorEnabled(false);
            }
        });


        mButtonSave = (Button) findViewById(R.id.button);
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidate()) {
                    save();

                    //close activity
                    finish();
                }
            }
        });

        scrollView = (ScrollView) findViewById(R.id.scrollView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkValidate() {
        EditText firstErrorEditText = null;
        //Start clear error
        //clear introduce
        textInputLayoutIntroduce.setError(null);
        textInputLayoutIntroduce.setErrorEnabled(false);

        //clear all contact
        for (int i = 0; i < contactViewLayout.getChildCount(); i++) {
            ContactView contactView = (ContactView) contactViewLayout.getChildAt(i);
            contactView.clearError();
        }

        //End clear error

        //Start validate
        //check introduce
        // 必須チェック（連絡元情報）
        if (Info.isBlank(editTextIntroduce.getText().toString())){
            textInputLayoutIntroduce.setError("必須入力");
            textInputLayoutIntroduce.setErrorEnabled(true);

            if (firstErrorEditText == null){
                firstErrorEditText = editTextIntroduce;
            }
        }

        //check all contact
        for (int i = 0; i < contactViewLayout.getChildCount(); i++) {
            ContactView contactView = (ContactView) contactViewLayout.getChildAt(i);
            if (i==0){
                //Name 必須入力
                if (Info.isBlank(contactView.editTextName.getText().toString())) {
                    contactView.textInputLayoutName.setError("必須入力");
                    contactView.textInputLayoutName.setErrorEnabled(true);
                    if (firstErrorEditText == null) {
                        firstErrorEditText = contactView.editTextName;
                    }
                }
                //Email 必須入力
                if (contactView.editTextEmail.getText().toString().isEmpty()){
                    contactView.textInputLayoutEmail.setError("必須入力");
                    contactView.textInputLayoutEmail.setErrorEnabled(true);
                    if (firstErrorEditText == null){
                        firstErrorEditText = contactView.editTextEmail;
                    }
                }
                //Email check
                else if (!contactView.editTextEmail.getText().toString().isEmpty()&&
                        !isEmailValid(contactView.editTextEmail.getText().toString())){
                    contactView.textInputLayoutEmail.setError("メールが有効ではありません。");
                    contactView.textInputLayoutEmail.setErrorEnabled(true);
                    if (firstErrorEditText == null){
                        firstErrorEditText = contactView.editTextEmail;
                    }
                }
            }
            else {
                //name: not empty, email: empty
                if (!Info.isBlank(contactView.editTextName.getText().toString()) &&
                        contactView.editTextEmail.getText().toString().isEmpty()){
                    contactView.textInputLayoutEmail.setError("名前があるから、必須入力");
                    contactView.textInputLayoutEmail.setErrorEnabled(true);
                    if (firstErrorEditText == null){
                        firstErrorEditText = contactView.editTextEmail;}
                }
                //name: empty, email: not empty
                else if (Info.isBlank(contactView.editTextName.getText().toString()) &&
                        !contactView.editTextEmail.getText().toString().isEmpty()){
                    contactView.textInputLayoutName.setError("メールがあるから、必須入力");
                    contactView.textInputLayoutName.setErrorEnabled(true);
                    if (firstErrorEditText == null){
                        firstErrorEditText = contactView.editTextName;}
                }
                //Email check
                if (!contactView.editTextEmail.getText().toString().isEmpty()&&
                        !isEmailValid(contactView.editTextEmail.getText().toString())){
                    contactView.textInputLayoutEmail.setError("メールが有効ではありません。");
                    contactView.textInputLayoutEmail.setErrorEnabled(true);
                    if (firstErrorEditText == null){
                        firstErrorEditText = contactView.editTextEmail;
                    }
                }
            }
        }
        //End validate

        if (firstErrorEditText != null) {
            firstErrorEditText.clearFocus();
            firstErrorEditText.requestFocus();
            return false;
        }

        return true;
    }

    private void save() {
        //Save Introduce
        Info.getInstance().myContact.introduce = editTextIntroduce.getText().toString();
        Info.getInstance().myContact.saveToPreference();

        //Save all contact
        for (int i = 0; i < contactViewLayout.getChildCount(); i++) {
            ContactView contactView = (ContactView) contactViewLayout.getChildAt(i);
            Contact contact = Info.getInstance().allContact.get(i);
            if (contactView.spinnerSendType.getSelectedItemPosition() == 1) {
                contact.sendType = Contact.SendType.CC;
            } else {
                contact.sendType = Contact.SendType.TO;
            }
            contact.name = contactView.editTextName.getText().toString();
            contact.email = contactView.editTextEmail.getText().toString();
            contact.saveToPreference();
        }
    }

    private boolean isEmailValid(String email){
        boolean isValid = false;

        //*begin of text
        //$end of text
        //\\w a-z, A-Z, 0-9
        //\\ : -
        //-/-

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()){
            isValid = true;
        }
        return isValid;
    }
}
