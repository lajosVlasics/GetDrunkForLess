package com.lalikum.getdrunkforless;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.lalikum.getdrunkforless.controller.BeverageController;
import com.lalikum.getdrunkforless.controller.OptionsController;
import com.lalikum.getdrunkforless.model.Beverage;

public class AddBeverageActivity extends AppCompatActivity {

    Beverage beverage;
    Beverage editBeverage;
    private OptionsController optionsController = new OptionsController();
    private BeverageController beverageController = new BeverageController();
    private TextView beverageTitleTextView;
    private TextView beverageSizeTextView;
    private TextView priceTextView;

    private EditText beverageNameEditText;
    private EditText beverageSizeEditText;
    private EditText alcoholByVolumeEditText;
    private EditText priceEditText;
    private EditText bottlesEditText;

    private String unit;
    private String currency;

    private String beverageName;
    private float beverageSize;
    private float alcoholByVolume;
    private float price;
    private int bottles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beverage);

        beverageTitleTextView = findViewById(R.id.beverageTitleTextView);
        beverageSizeTextView = findViewById(R.id.beverageSizeTextView);
        priceTextView = findViewById(R.id.priceTextView);

        beverageNameEditText = findViewById(R.id.beverageNameEditText);
        beverageSizeEditText = findViewById(R.id.beverageSizeEditText);
        alcoholByVolumeEditText = findViewById(R.id.alcoholByVolumeEditText);
        priceEditText = findViewById(R.id.priceEditText);
        bottlesEditText = findViewById(R.id.bottlesEditText);

        // Set unit and currency field from options
        unit = optionsController.getUnit();
        currency = optionsController.getCurrency();

        beverageSizeTextView.setText(String.format("Size (%s)", unit));
        priceTextView.setText(String.format("Price (%s)", currency));

        // Set focus on first input
        beverageNameEditText.requestFocus();

        // TODO fill inputs if edit mode
        Intent intent = getIntent();
        long beverageId = intent.getLongExtra("beverageId", -1);
        if (beverageId > -1) {
            editBeverage = beverageController.getById(beverageId);

            beverageTitleTextView.setText("Edit beverage");
            beverageNameEditText.setText(editBeverage.getName());
            beverageSizeEditText.setText(String.valueOf(editBeverage.getSize()));
            alcoholByVolumeEditText.setText(String.valueOf(editBeverage.getAlcoholByVolume()));
            priceEditText.setText(String.valueOf(editBeverage.getPrice()));
            bottlesEditText.setText(String.valueOf(editBeverage.getBottles()));
        }

    }

    private boolean checkIfInputsAreEmpty(EditText... editTextList) {
        // Show error message in input field if its empty
        boolean isEmptyInput = false;

        for (EditText editText : editTextList) {
            String inputText = editText.getText().toString();
            if (TextUtils.isEmpty(inputText)) {
                editText.setError("Please fill this out!");
                isEmptyInput = true;
            }
        }
        return isEmptyInput;
    }

    private boolean checkIfDigitInputsAreZero(EditText... editTextList) {
        // Show error message in input field if its zero
        boolean isZeroInput = false;

        for (EditText editText : editTextList) {
            String inputText = editText.getText().toString();
            if (TextUtils.isEmpty(inputText)) {
                continue;
            }
            // TODO cant parse null to float!!!
            if (Float.parseFloat(inputText) == 0) {
                editText.setError("This cannot be null! Do you want to get drunk or what?");
                isZeroInput = true;
            }
        }
        return isZeroInput;
    }


    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void toHomeActivity(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void calculateButtonEvent(View view) {
        if (!setInputs()) {
            return;
        }
        setBeverage();

        TextView pureAlcoholTextView = findViewById(R.id.pureAlcoholTextView);
        TextView pricePerAlcoholTextView = findViewById(R.id.alcoholValueTextView);

        // TODO show currency and unit after values
        pureAlcoholTextView.setText(String.format("There is %s pure alcohol in the Beverage.", beverageController.getAlcoholQuantityWithSuffix(beverage)));
        pricePerAlcoholTextView.setText(String.format("That's %s alcohol value!", beverageController.getAlcoholValueWithSuffix(beverage)));

        hideKeyboard();
    }

    public void saveButtonEvent(View view) {
        if (!setInputs()) {
            return;
        }
        setBeverage();

        beverageController.save(beverage);
        toHomeActivity(view);
    }

    private boolean setInputs() {
        try {
            boolean isEmptyInput = checkIfInputsAreEmpty(beverageNameEditText, beverageSizeEditText, alcoholByVolumeEditText, priceEditText, bottlesEditText);
            boolean isZeroInput = checkIfDigitInputsAreZero(beverageSizeEditText, alcoholByVolumeEditText, priceEditText, bottlesEditText);
            if (isEmptyInput || isZeroInput) {
                // TODO hide keyboard only if its over an error input field not visible
                return false;
            }
            beverageName = beverageNameEditText.getText().toString();
            // TODO set max length
            beverageSize = Float.parseFloat(beverageSizeEditText.getText().toString());
            // TODO set max value 100
            alcoholByVolume = Float.parseFloat(alcoholByVolumeEditText.getText().toString());
            // TODO set max length
            price = Float.parseFloat(priceEditText.getText().toString());
            // TODO set max length
            bottles = Integer.parseInt(bottlesEditText.getText().toString());
        } catch (NumberFormatException e) {
            System.out.println("Can't parse input to float or int!");
            return false;
        }
        return true;
    }

    private void setBeverage() {
        if (editBeverage == null) {
            beverage = beverageController.create(beverageName, beverageSize, alcoholByVolume, price, bottles);
        } else {
            beverage = editBeverage;
            beverage.setName(beverageName);
            beverage.setSize(beverageSize);
            beverage.setAlcoholByVolume(alcoholByVolume);
            beverage.setPrice(price);
            beverage.setBottles(bottles);
            beverageController.calculate(beverage);
        }
    }

}
