package com.lalikum.getdrunkforless;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lalikum.getdrunkforless.controller.BeverageController;
import com.lalikum.getdrunkforless.controller.OptionsController;
import com.lalikum.getdrunkforless.model.Beverage;
import com.lalikum.getdrunkforless.util.InputChecker;

public class AddBeverageActivity extends AppCompatActivity {

    TextView pureAlcoholTextView;
    TextView pricePerAlcoholTextView;
    private Beverage beverage;
    private Beverage editBeverage;
    private OptionsController optionsController = new OptionsController();
    private BeverageController beverageController = new BeverageController();
    private InputChecker inputChecker = new InputChecker();
    private TextView beverageTitleTextView;
    private TextView beverageSizeTextView;
    private TextView priceTextView;
    private EditText beverageNameEditText;
    private EditText beverageSizeEditText;
    private EditText alcoholByVolumeEditText;
    private EditText priceEditText;
    private EditText bottlesEditText;
    private EditText[] editTextList;

    private Button saveButton;

    private String unit;
    private String currency;

    private String beverageName;
    private float beverageSize;
    private float alcoholByVolume;
    private float price;
    private int bottles;

    private int maxAlcoholByVolume = 100;
    private int maxSize = 10000000;
    private int maxPrice = 1000000;
    private int maxBottles = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beverage);

        beverageTitleTextView = findViewById(R.id.beverageTitleTextView);
        beverageSizeTextView = findViewById(R.id.beverageSizeTextView);
        priceTextView = findViewById(R.id.priceTextView);
        pureAlcoholTextView = findViewById(R.id.pureAlcoholTextView);
        pricePerAlcoholTextView = findViewById(R.id.alcoholValueTextView);

        beverageNameEditText = findViewById(R.id.beverageNameEditText);
        beverageSizeEditText = findViewById(R.id.beverageSizeEditText);
        alcoholByVolumeEditText = findViewById(R.id.alcoholByVolumeEditText);
        priceEditText = findViewById(R.id.priceEditText);
        bottlesEditText = findViewById(R.id.bottlesEditText);
        editTextList = new EditText[]{beverageNameEditText, beverageSizeEditText, alcoholByVolumeEditText, priceEditText, bottlesEditText};

        saveButton = findViewById(R.id.saveButton);

        // Set unit and currency field from options
        unit = optionsController.getUnit();
        currency = optionsController.getCurrency();

        beverageSizeTextView.setText(String.format("Size (%s)", unit));
        priceTextView.setText(String.format("Price (%s)", currency));

        // Set focus on first input
        beverageNameEditText.requestFocus();

        // fill inputs if edit mode
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

            saveButton.setEnabled(true);
        }

        // set event listeners for edit texts
        beverageNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isBeverageNameInputError(true);
                calculateIfPossible();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        beverageNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    isBeverageNameInputError(true);
                    calculateIfPossible();
                }
            }
        });

        beverageSizeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isBeverageSizeInputError(true);
                calculateIfPossible();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        beverageSizeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    isBeverageSizeInputError(true);
                    calculateIfPossible();
                }
            }
        });

        alcoholByVolumeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isAlcoholByVolumeInputError(true);
                calculateIfPossible();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        alcoholByVolumeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    isAlcoholByVolumeInputError(true);
                    calculateIfPossible();
                }
            }
        });

        priceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isPriceInputError(true);
                calculateIfPossible();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        priceEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    isPriceInputError(true);
                    calculateIfPossible();
                }
            }
        });

        bottlesEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isBottlesInputError(true);
                calculateIfPossible();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bottlesEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    isBottlesInputError(true);
                    calculateIfPossible();
                }
            }
        });

    }

    private boolean isBeverageNameInputError(boolean setErrorText) {
        boolean isEmptyInput;
        if (setErrorText) {
            isEmptyInput = inputChecker.isEmptyInput("Please tell me the beverage name!", beverageNameEditText);
        } else {
            isEmptyInput = inputChecker.isEmptyInput(beverageNameEditText);
        }
        return isEmptyInput;
    }

    private boolean isBeverageSizeInputError(boolean setErrorText) {
        boolean isEmptyInput;
        boolean isZeroInput;
        boolean isHigherInput;
        if (setErrorText) {
            isEmptyInput = inputChecker.isEmptyInput("Please tell me the beverage size!", beverageSizeEditText);
            isZeroInput = inputChecker.isZeroInput("Is this water??? Are you an animal? Do you want to get drunk or what?", beverageSizeEditText);
            isHigherInput = inputChecker.isHigherInput("OMG! Do you want to drink that all alone? Write a lower number please.", maxSize, beverageSizeEditText);

        } else {
            isEmptyInput = inputChecker.isEmptyInput(beverageSizeEditText);
            isZeroInput = inputChecker.isZeroInput(beverageSizeEditText);
            isHigherInput = inputChecker.isHigherInput(maxSize, beverageSizeEditText);

        }
        return isEmptyInput || isZeroInput || isHigherInput;
    }

    private boolean isAlcoholByVolumeInputError(boolean setErrorText) {
        boolean isEmptyInput;
        boolean isZeroInput;
        boolean isHigherInput;
        if (setErrorText) {
            isEmptyInput = inputChecker.isEmptyInput("Please tell me the alcohol by volume!", alcoholByVolumeEditText);
            isZeroInput = inputChecker.isZeroInput("This cannot be null! Do you want to get drunk or what?", alcoholByVolumeEditText);
            isHigherInput = inputChecker.isHigherInput("The alcohol volume can't be higher than 100% (Or You know something that I don't!)", maxAlcoholByVolume, alcoholByVolumeEditText);
        } else {
            isEmptyInput = inputChecker.isEmptyInput(alcoholByVolumeEditText);
            isZeroInput = inputChecker.isZeroInput(alcoholByVolumeEditText);
            isHigherInput = inputChecker.isHigherInput(maxAlcoholByVolume, alcoholByVolumeEditText);
        }
        return isEmptyInput || isZeroInput || isHigherInput;
    }

    private boolean isPriceInputError(boolean setErrorText) {
        boolean isEmptyInput;
        boolean isZeroInput;
        boolean isHigherInput;
        if (setErrorText) {
            isEmptyInput = inputChecker.isEmptyInput("Please write the price of the beverage here!", priceEditText);
            isZeroInput = inputChecker.isZeroInput("Ok, that's totally free. That's is not the situation I was programmed for...", priceEditText);
            isHigherInput = inputChecker.isHigherInput("That's clearly not worth it. Write a lower number please.", maxPrice, priceEditText);
        } else {
            isEmptyInput = inputChecker.isEmptyInput(priceEditText);
            isZeroInput = inputChecker.isZeroInput(priceEditText);
            isHigherInput = inputChecker.isHigherInput(maxPrice, priceEditText);
        }
        return isEmptyInput || isZeroInput || isHigherInput;
    }

    private boolean isBottlesInputError(boolean setErrorText) {
        boolean isEmptyInput;
        boolean isZeroInput;
        boolean isHigherInput;
        if (setErrorText) {
            isEmptyInput = inputChecker.isEmptyInput("Please write how many bottles in the package!", bottlesEditText);
            isZeroInput = inputChecker.isZeroInput("This cannot be null! Do you want to get drunk or what?", bottlesEditText);
            isHigherInput = inputChecker.isHigherInput("I don't think that kind of package exits. Write a lower number please", maxBottles, bottlesEditText);
        } else {
            isEmptyInput = inputChecker.isEmptyInput(bottlesEditText);
            isZeroInput = inputChecker.isZeroInput(bottlesEditText);
            isHigherInput = inputChecker.isHigherInput(maxBottles, bottlesEditText);
        }
        return isEmptyInput || isZeroInput || isHigherInput;
    }

    private void calculateIfPossible() {
        boolean isAnyInputError = isAnyInputError();
        if (isAnyInputError) {
            pureAlcoholTextView.setText("default empty text here");
            pricePerAlcoholTextView.setText("default empty text here");
            saveButton.setEnabled(false);
        } else {
            calculate();
            saveButton.setEnabled(true);
        }
    }

    private boolean isAnyInputError() {
        boolean isNameInputError = isBeverageNameInputError(false);
        boolean isSizeInputError = isBeverageSizeInputError(false);
        boolean isAlcoholInputError = isAlcoholByVolumeInputError(false);
        boolean isPriceInputError = isPriceInputError(false);
        boolean isBottlesInputError = isBottlesInputError(false);
        return isNameInputError || isSizeInputError || isAlcoholInputError || isPriceInputError || isBottlesInputError;
    }

    private void toHomeActivity(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void calculate() {
        if (!setInputs()) {
            return;
        }
        setBeverage();

        // TODO show currency and unit after values
        pureAlcoholTextView.setText(String.format("There is %s pure alcohol in the Beverage.", beverageController.getAlcoholQuantityWithSuffix(beverage)));
        pricePerAlcoholTextView.setText(String.format("That's %s alcohol value!", beverageController.getAlcoholValueWithSuffix(beverage)));
    }

    public void saveButtonEvent(View view) {
        beverageController.save(beverage);
        toHomeActivity(view);
    }

    private boolean setInputs() {
        if (isAnyInputError()) {
            return false;
        }
        beverageName = beverageNameEditText.getText().toString();
        beverageSize = Float.parseFloat(beverageSizeEditText.getText().toString());
        alcoholByVolume = Float.parseFloat(alcoholByVolumeEditText.getText().toString());
        price = Float.parseFloat(priceEditText.getText().toString());
        bottles = Integer.parseInt(bottlesEditText.getText().toString());
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
