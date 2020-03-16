package ru.mikhail.converter.Converter;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.util.ArrayList;

import ru.mikhail.converter.CurrencyChange.MyCurrencyChange;
import ru.mikhail.converter.CurrentCurrencyValue.Collection;
import ru.mikhail.converter.CurrentCurrencyValue.MainActivity;
import ru.mikhail.converter.R;

/**
 * Created by Admin on 13.03.2020.
 */

public class MainConverter extends AppCompatActivity {
    public static TextView textNameLeft;
    EditText editTextLeft;

    public static TextView textNameRight;
    EditText editTextRight;

    public static boolean flag;


    public ArrayList<Collection> collectionArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_converter);

        final MainActivity mainActivity = new MainActivity();
        collectionArrayList = mainActivity.listCollection;


        //добавление рубля в коллекцию
        Collection valueRUB = new Collection();
        valueRUB.setCharCode("RUB");
        valueRUB.setValue("1");
        collectionArrayList.add(0, valueRUB);

        editTextLeft = (EditText) findViewById(R.id.editText_left_ID);
        editTextRight = (EditText) findViewById(R.id.editText_Right_ID);

        textNameLeft = (TextView) findViewById(R.id.textView_left_ID);
        textNameRight = (TextView) findViewById(R.id.textView_Right_ID);


        /**
         * замена запятой на точку в коллекции значений;
         *
         * установка первоначальных значений на экран
         */
        for (Collection collection: collectionArrayList) {

            //замена запятой на точку в коллекции значений
            String string = "";
            for (int i = 0; i < collection.getValue().length(); i++) {
                if (collection.getValue().charAt(i) == ',') {
                    string = string + ".";
                } else {
                    string = string + collection.getValue().charAt(i);
                }
            }
            collection.setValue(string);

            //установка первоначальных значений на экран
            if (collection.getCharCode().equalsIgnoreCase("rub")) {
                textNameLeft.setText(collection.getCharCode());
                editTextRight.setText(collection.getValue());
            }
            if (collection.getCharCode().equalsIgnoreCase("usd")) {
                textNameRight.setText(collection.getCharCode());
                editTextLeft.setText(collection.getValue());
            }
        }

        MyListener myListener = new MyListener();
        editTextLeft.addTextChangedListener(myListener.valueWatcher);
        editTextRight.addTextChangedListener(myListener.valueWatcher);

        textNameLeft.addTextChangedListener(myListener.countryWatcher);
        textNameRight.addTextChangedListener(myListener.countryWatcher);

        Button button_left = (Button) findViewById(R.id.button_left);
        Button button_right = (Button) findViewById(R.id.button_right);


        button_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = false;
                FragmentManager manager = getSupportFragmentManager();
                MyCurrencyChange myCurrencyChange = new MyCurrencyChange();
                myCurrencyChange.show(manager, "button_left");
            }
        });

        button_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                FragmentManager manager = getSupportFragmentManager();
                MyCurrencyChange myCurrencyChange = new MyCurrencyChange();
                myCurrencyChange.show(manager, "button_right");
            }
        });
    }

    private class MyListener {


        TextWatcher countryWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                double total;
                String value_left = "";
                String value_right = "";
                for (Collection collection : collectionArrayList) {
                    if (collection.getCharCode().equalsIgnoreCase(
                            textNameRight.getText().toString())) {
                        value_right = collection.getValue();
                    }
                    if (collection.getCharCode().equalsIgnoreCase(
                            textNameLeft.getText().toString())) {
                        value_left = collection.getValue();
                    }
                }
                if (textNameLeft.getText().hashCode() == editable.hashCode()) {
                    total = Double.parseDouble(value_left) / Double.parseDouble(value_right)
                            * Double.parseDouble(String.valueOf(editTextLeft.getText()));
                    editTextRight.setText(total + "");
                } else if (textNameRight.getText().hashCode() == editable.hashCode()) {
                    total = Double.parseDouble(value_left) * Double.parseDouble(value_right)
                            / Double.parseDouble(String.valueOf(editTextLeft.getText()));
                    editTextLeft.setText(total + "");
                }

            }
        };


        TextWatcher valueWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String valueLeft = "";
                String valueRight = "";
                if (editable != null && editable.length() != 0) {
                    double total = 0;
                    for (Collection collection : collectionArrayList) {
                        if (collection.getCharCode().equalsIgnoreCase(
                                textNameLeft.getText().toString())) {
                            valueLeft = collection.getValue();
                        }
                        if (collection.getCharCode().equalsIgnoreCase(
                                textNameRight.getText().toString())) {
                            valueRight = collection.getValue();
                        }
                    }
                    if (editTextLeft.getText().hashCode() == editable.hashCode()) {
                        total = Double.parseDouble(editTextLeft.getText().toString()) /
                                Double.parseDouble(valueRight) * Double.parseDouble(valueLeft);
                        editTextLeft.removeTextChangedListener(valueWatcher);
                        editTextRight.setText(total + "");
                        editTextLeft.addTextChangedListener(valueWatcher);
                        editTextLeft.setSelection(editTextLeft.getText().length());
                    } else if (editTextRight.getText().hashCode() == editable.hashCode()) {
                        total = Double.parseDouble(editTextRight.getText().toString()) *
                                Double.parseDouble(valueRight) / Double.parseDouble(valueLeft);
                        editTextRight.removeTextChangedListener(valueWatcher);
                        editTextLeft.setText(total + "");
                        editTextRight.addTextChangedListener(valueWatcher);
                        editTextRight.setSelection(editTextRight.getText().length());
                    }
                }
            }
        };
    }
}

