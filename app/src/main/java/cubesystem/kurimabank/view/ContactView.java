package cubesystem.kurimabank.view;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import cubesystem.kurimabank.R;

/**
 * Created by kurimabank-kurima on 2016/03/22.
 */

public class ContactView extends LinearLayout {

    public TextView textViewTitle;
    public EditText editTextName;
    public EditText editTextEmail;
    public Spinner spinnerSendType;
    public TextInputLayout textInputLayoutName;
    public TextInputLayout textInputLayoutEmail;

    public ContactView(Context context) {
        super(context);
        init();
    }

    public ContactView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContactView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.contact_view, this);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        spinnerSendType = (Spinner)findViewById(R.id.spinner2);
        editTextName = (EditText)findViewById(R.id.editText4);
        editTextEmail = (EditText)findViewById(R.id.editText5);
        textInputLayoutName = (TextInputLayout)findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = (TextInputLayout)findViewById(R.id.textInputLayoutEmail);

        this.editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ContactView.this.textInputLayoutName.setError(null);
                ContactView.this.textInputLayoutName.setEnabled(false);

            }
        });

        this.editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ContactView.this.textInputLayoutEmail.setError(null);
                ContactView.this.textInputLayoutEmail.setErrorEnabled(false);

            }
        });
    }


    public void clearError(){
        textInputLayoutName.setError(null);
        textInputLayoutName.setErrorEnabled(false);

        textInputLayoutEmail.setError(null);
        textInputLayoutEmail.setErrorEnabled(false);

    }

}
