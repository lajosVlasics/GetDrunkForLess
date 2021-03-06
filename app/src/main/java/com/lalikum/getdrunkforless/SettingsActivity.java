package com.lalikum.getdrunkforless;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lalikum.getdrunkforless.controller.SettingsController;
import com.lalikum.getdrunkforless.model.MeasurementSystem;
import com.lalikum.getdrunkforless.model.Settings;
import com.lalikum.getdrunkforless.util.InputChecker;

public class SettingsActivity extends AppCompatActivity {

    private static final String[] CURRENCIES = new String[]{
            "JPY", "CNY", "SDG", "RON", "MKD", "MXN", "CAD",
            "ZAR", "AUD", "NOK", "ILS", "ISK", "SYP", "LYD", "UYU", "YER", "CSD",
            "EEK", "THB", "IDR", "LBP", "AED", "BOB", "QAR", "BHD", "HNL", "HRK",
            "COP", "ALL", "DKK", "MYR", "SEK", "RSD", "BGN", "DOP", "KRW", "LVL",
            "VEF", "CZK", "TND", "KWD", "VND", "JOD", "NZD", "PAB", "CLP", "PEN",
            "GBP", "DZD", "CHF", "RUB", "UAH", "ARS", "SAR", "EGP", "INR", "PYG",
            "TWD", "TRY", "BAM", "OMR", "SGD", "MAD", "BYR", "NIO", "HKD", "LTL",
            "SKK", "GTQ", "BRL", "EUR", "HUF", "IQD", "CRC", "PHP", "SVC", "PLN",
            "USD"};

    private static MenuItem saveMenuItem;
    private TextInputLayout userNameTextInputLayout;
    private TextInputLayout currencyTextInputLayout;
    private EditText userNameEditText;
    private AutoCompleteTextView currencyAutoCompleteTextView;
    private RadioButton metricRadioButton;
    private RadioButton imperialRadioButton;
    private RadioGroup unitRadioGroup;
    private ImageButton tutorialButton;
    private SettingsController settingsController = new SettingsController();
    private InputChecker inputChecker = new InputChecker();
    private String userName;
    private MeasurementSystem measurementSystem = MeasurementSystem.METRIC;
    private String currency;

    @Override
    protected void onResume() {
        super.onResume();
        // add TextWatcher listeners here to prevent error messages after orientation changes
        userNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setUserNameInputError();
                setSaveButtonStatus();
            }
        });

        currencyAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setCurrencyInputError();
                setSaveButtonStatus();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        userNameTextInputLayout = findViewById(R.id.tilSettingsUserName);
        currencyTextInputLayout = findViewById(R.id.tilSettingsCurrency);
        // TODO check max length on small screen
        userNameEditText = findViewById(R.id.etSettingsUserName);

        // set autocomplete for currency edit text
        currencyAutoCompleteTextView = findViewById(R.id.acetSettingsCurrency);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, CURRENCIES);
        currencyAutoCompleteTextView.setAdapter(adapter);

        metricRadioButton = findViewById(R.id.rbSettingsMetric);
        imperialRadioButton = findViewById(R.id.ebSettingsImperial);
        unitRadioGroup = findViewById(R.id.rbgSettingsUnit);
        tutorialButton = findViewById(R.id.btnSettingsTutorial);
        // init
        tutorialButton.setVisibility(View.GONE);
        // set unit fields from ENUM
        metricRadioButton.setText(String.format("%s (%s)", getString(R.string.settings_metric), MeasurementSystem.METRIC.getUnit()));
        imperialRadioButton.setText(String.format("%s (%s)", getString(R.string.settings_imperial), MeasurementSystem.IMPERIAL.getUnit()));

        // If settings DB exists (from home menu button) fill from DB and enable Save button
        if (settingsController.isSettingsExists()) {
            Settings settings = settingsController.getInstance();
            // show things
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            tutorialButton.setVisibility(View.VISIBLE);

            // disable floating label animations
            userNameTextInputLayout.setHintAnimationEnabled(false);
            currencyTextInputLayout.setHintAnimationEnabled(false);

            // populate fields
            userNameEditText.setText(settings.getUserName());

            measurementSystem = settings.getMeasurementSystem();
            unitRadioGroup.clearCheck();
            switch (measurementSystem) {
                // TODO convert beverage units too...
                case METRIC:
                    metricRadioButton.toggle();
                    break;
                case IMPERIAL:
                    imperialRadioButton.toggle();
                    break;
            }

            currencyAutoCompleteTextView.setText(settings.getCurrency());

            // enable floating label animations
            userNameTextInputLayout.setHintAnimationEnabled(true);
            currencyTextInputLayout.setHintAnimationEnabled(true);
        }

        userNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    setUserNameInputError();
                    setSaveButtonStatus();
                }
            }
        });

        currencyAutoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    setCurrencyInputError();
                    setSaveButtonStatus();
                }
            }
        });

        currencyAutoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                saveSettings();
                return false;
            }
        });

        tutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTutorialActivity(v);
            }
        });

    }

    // Set action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);

        saveMenuItem = menu.findItem(R.id.itemSettingsMenuSave);
        // enable save button if settings edited
        if (settingsController.isSettingsExists()) {
            setSaveButtonActive();
        } else {
            setSaveButtonInactive();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemSettingsMenuSave:
                saveSettings();
                toHomeActivity();
                return true;
            case android.R.id.home:
                toHomeActivity();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveSettings() {
        if (setUserNameInputError() || setCurrencyInputError()) {
            return;
        }
        userName = userNameEditText.getText().toString();
        currency = currencyAutoCompleteTextView.getText().toString();

        settingsController.saveInstance(userName, measurementSystem, currency);
        toHomeActivity();
    }

    public void toHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void toTutorialActivity(View view) {
        Intent intent = new Intent(this, TutorialActivity.class);
        startActivity(intent);
    }

    public void unitTypeRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.rbSettingsMetric:
                if (checked)
                    measurementSystem = MeasurementSystem.METRIC;
                break;
            case R.id.ebSettingsImperial:
                if (checked)
                    measurementSystem = MeasurementSystem.IMPERIAL;
                break;
        }
    }

    private void setSaveButtonStatus() {
        boolean isInputError = inputChecker.isEmptyInput(userNameEditText, currencyAutoCompleteTextView);
        if (isInputError) {
            setSaveButtonInactive();
        } else {
            setSaveButtonActive();
        }
    }

    public void setSaveButtonActive() {
        saveMenuItem.setIcon(R.drawable.ic_save_active);
        saveMenuItem.setEnabled(true);
    }

    public void setSaveButtonInactive() {
        saveMenuItem.setIcon(R.drawable.ic_save_inactive);
        saveMenuItem.setEnabled(false);
    }

    private boolean setUserNameInputError() {
        return inputChecker.isEmptyInput(getString(R.string.settings_error_empty_name), userNameEditText);
    }

    private boolean setCurrencyInputError() {
        return inputChecker.isEmptyInput(getString(R.string.settings_error_empty_currency), currencyAutoCompleteTextView);
    }

}
