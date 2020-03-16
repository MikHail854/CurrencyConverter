package ru.mikhail.converter.CurrencyChange;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

import java.util.ArrayList;

import ru.mikhail.converter.Converter.MainConverter;
import ru.mikhail.converter.CurrentCurrencyValue.MainActivity;
import ru.mikhail.converter.CurrentCurrencyValue.Collection;

/**
 * Created by Admin on 15.03.2020.
 */

public class MyCurrencyChange extends AppCompatDialogFragment {

    public ArrayList<Collection> collectionArrayList = new ArrayList<>();

    public static int item = 0;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final MainActivity mainActivity = new MainActivity();
        collectionArrayList = mainActivity.listCollection;

        final String[] nameCountry = new String[collectionArrayList.size()];
        final MainConverter mainConverter = new MainConverter();
        final String choice;

        choice = (String) mainConverter.textNameLeft.getText();

        for (int i = 0; i < collectionArrayList.size(); i++){
            nameCountry[i] = collectionArrayList.get(i).getCharCode();
            if (collectionArrayList.get(i).getCharCode()
                    .equalsIgnoreCase(String.valueOf(mainConverter.textNameLeft.getText()))){
                item = i;
            }

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Выберите валюту")
                // добавляем переключатели
                .setSingleChoiceItems(nameCountry, item,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int item) {
                                /*Toast.makeText(
                                        getActivity(),
                                        "Любимое имя кота: "
                                                + nameCountry[item],
                                        Toast.LENGTH_SHORT).show();*/
                                if (mainConverter.flag == false) {
                                    mainConverter.textNameLeft.setText(nameCountry[item]);
                                } else if (mainConverter.flag == true){
                                    mainConverter.textNameRight.setText(nameCountry[item]);
                                }
                            }
                        })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog


                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (mainConverter.flag == false){
                        mainConverter.textNameLeft.setText(choice);
                        } else mainConverter.textNameRight.setText(choice);
                    }
                });

        return builder.create();
    }
}
