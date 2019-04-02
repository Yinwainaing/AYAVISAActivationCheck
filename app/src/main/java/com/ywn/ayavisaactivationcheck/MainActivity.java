package com.ywn.ayavisaactivationcheck;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ArrayList<String> card_list = new ArrayList<String>();
    private PopupWindow mPopupWindow;
    @BindView(R.id.mlinearlayout)
    LinearLayout linearLayout;
    @BindView(R.id.et_cardno)
    EditText et_cardno;
    @BindView(R.id.img_search)
    ImageView imgsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        importCSV();
        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_cardno.getText().toString().equalsIgnoreCase("")) {
                    findCard(et_cardno.getText().toString());
                }
            }
        });

    }

    private void importCSV() {
        List<String[]> list = new ArrayList<String[]>();
        String next[] = {};
        try {
            InputStreamReader csvStreamReader = new InputStreamReader(
                    MainActivity.this.getAssets().open(
                            "test.csv"));
            CSVReader reader = new CSVReader(csvStreamReader);
            for (; ; ) {
                next = reader.readNext();
                if (next != null) {
                    list.add(next);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < list.size(); i++) {
            card_list.add(list.get(i)[1]);
        }
    }

    private void findCard(String cardno) {
        boolean flag = false;
        for (int j = 0; j < card_list.size(); j++) {
            if (cardno.equalsIgnoreCase(card_list.get(j))) {
                flag = true;
                break;
            }
        }
        showPopup(flag);
    }

    private void showPopup(boolean flag) {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.popup_layout, null);
        mPopupWindow = new PopupWindow(
                customView,
                800,
                500
        );
        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(5.0f);
        }
        Button closeButton = customView.findViewById(R.id.btn_back);
        ImageView img_result = customView.findViewById(R.id.img_result);
        TextView textresult =  customView.findViewById(R.id.text_result);
        if (flag) {
            img_result.setImageResource(R.drawable.checked_50_green);
            textresult.setText(getResources().getString(R.string.found));

        }
        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                mPopupWindow.dismiss();
                et_cardno.setText("");
            }
        });
        mPopupWindow.showAtLocation(linearLayout, Gravity.CENTER, 0, 0);
    }
}
