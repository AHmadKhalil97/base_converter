package com.scriptorient.baseconverter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean doubleBackToExitPressedOnce = false;
    Typeface typeface;

    private String textBefore;
    private boolean COMMON_LAYOUT_VISIBLE = true;
    private boolean ALL_BASES_LAYOUT_VISIBLE = false;
    private boolean CUSTOM_LAYOUT_VISIBLE = false;
    private MenuItem menuItem;
    private Runnable clearAll;
    private Spinner spinner1;
    private Spinner spinner2;
    private boolean cleared = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        typeface = Typeface.createFromAsset(getAssets(), "ubunto_regular.ttf");
        ((TextView) toolbar.findViewById(R.id.tvToolbarTitle)).setTypeface(typeface);

        final FloatingActionButton fab_copy = (FloatingActionButton) findViewById(R.id.fab_copy);
        final FloatingActionButton fab_refresh = (FloatingActionButton) findViewById(R.id.fab_refresh);

        spinner1 = findViewById(R.id.spinner_base1);
        spinner2 = findViewById(R.id.spinner_base2);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.common_bases);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.custom_view:
                        findViewById(R.id.all_bases_layout).setVisibility(View.GONE);
                        findViewById(R.id.common_layout).setVisibility(View.GONE);
                        findViewById(R.id.custom_layout).setVisibility(View.VISIBLE);

                        CUSTOM_LAYOUT_VISIBLE = true;
                        COMMON_LAYOUT_VISIBLE = false;
                        ALL_BASES_LAYOUT_VISIBLE = false;

                        menuItem.setVisible(false);
                        fab_refresh.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.common_bases:
                        findViewById(R.id.all_bases_layout).setVisibility(View.GONE);
                        findViewById(R.id.common_layout).setVisibility(View.VISIBLE);
                        findViewById(R.id.custom_layout).setVisibility(View.GONE);

                        CUSTOM_LAYOUT_VISIBLE = false;
                        COMMON_LAYOUT_VISIBLE = true;
                        ALL_BASES_LAYOUT_VISIBLE = false;

                        menuItem.setVisible(false);
                        fab_refresh.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.all_bases:
                        findViewById(R.id.common_layout).setVisibility(View.GONE);
                        findViewById(R.id.all_bases_layout).setVisibility(View.VISIBLE);
                        findViewById(R.id.custom_layout).setVisibility(View.GONE);

                        CUSTOM_LAYOUT_VISIBLE = false;
                        COMMON_LAYOUT_VISIBLE = false;
                        ALL_BASES_LAYOUT_VISIBLE = true;

                        menuItem.setVisible(true);
                        fab_refresh.setVisibility(View.GONE);
                        return true;
                }
                return false;
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toolbar.findViewById(R.id.imgRateUs).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.getWindow();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                final View view1 = getLayoutInflater().inflate(R.layout.dialog_layout, null);

                Typeface typeface = Typeface.createFromAsset(getAssets(), "ubunto_regular.ttf");
                ((TextView) view1.findViewById(R.id.dialogTitle)).setTypeface(typeface);
                ((TextView) view1.findViewById(R.id.dialogBodyText)).setTypeface(typeface);
                ((EditText) view1.findViewById(R.id.alertEditText)).setTypeface(typeface);
                ((TextView) view1.findViewById(R.id.dialogEditTextTextView)).setTypeface(typeface);
                ((Button) view1.findViewById(R.id.dialogPositiveButton)).setTypeface(typeface);
                ((Button) view1.findViewById(R.id.dialogNegativeButton)).setTypeface(typeface);

                ((TextView) view1.findViewById(R.id.dialogTitle)).setText("Rate This App");
                ((TextView) view1.findViewById(R.id.dialogBodyText)).setText("Dear user, " +
                        "If you enjoy using this Base Converter, " +
                        "would you mind taking a moment to rate it? " +
                        "It won't take more than a minute. " +
                        "Thank you for your support!");

                view1.findViewById(R.id.dialogEditTextRelative).setVisibility(View.GONE);

                ((Button) view1.findViewById(R.id.dialogPositiveButton)).setText("Rate Now");
                ((Button) view1.findViewById(R.id.dialogNegativeButton)).setText("later");

                view1.findViewById(R.id.crossImage).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                view1.findViewById(R.id.dialogPositiveButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        rateUs();
                    }
                });

                view1.findViewById(R.id.dialogNegativeButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.setContentView(view1);
                dialog.show();
            }
        });

        final EditText custom_base1 = findViewById(R.id.custom_base1);
        final EditText custom_base2 = findViewById(R.id.custom_base2);
        final EditText common_base_2 = findViewById(R.id.common_base2);
        final EditText common_base_8 = findViewById(R.id.common_base8);
        final EditText common_base_10 = findViewById(R.id.common_base10);
        final EditText common_base_16 = findViewById(R.id.common_base16);
        final EditText all_base_2 = findViewById(R.id.all_base2);
        final EditText all_base_3 = findViewById(R.id.all_base3);
        final EditText all_base_4 = findViewById(R.id.all_base4);
        final EditText all_base_5 = findViewById(R.id.all_base5);
        final EditText all_base_6 = findViewById(R.id.all_base6);
        final EditText all_base_7 = findViewById(R.id.all_base7);
        final EditText all_base_8 = findViewById(R.id.all_base8);
        final EditText all_base_9 = findViewById(R.id.all_base9);
        final EditText all_base_10 = findViewById(R.id.all_base10);
        final EditText all_base_11 = findViewById(R.id.all_base11);
        final EditText all_base_12 = findViewById(R.id.all_base12);
        final EditText all_base_13 = findViewById(R.id.all_base13);
        final EditText all_base_14 = findViewById(R.id.all_base14);
        final EditText all_base_15 = findViewById(R.id.all_base15);
        final EditText all_base_16 = findViewById(R.id.all_base16);

//        common_base_16.setFilters(new InputFilter[] {new InputFilter.AllCaps(), new InputFilter.LengthFilter(1)});
//        all_base_11.setFilters(new InputFilter[] {new InputFilter.AllCaps(), new InputFilter.LengthFilter(1)});
//        all_base_12.setFilters(new InputFilter[] {new InputFilter.AllCaps(), new InputFilter.LengthFilter(1)});
//        all_base_13.setFilters(new InputFilter[] {new InputFilter.AllCaps(), new InputFilter.LengthFilter(1)});
//        all_base_14.setFilters(new InputFilter[] {new InputFilter.AllCaps(), new InputFilter.LengthFilter(1)});
//        all_base_15.setFilters(new InputFilter[] {new InputFilter.AllCaps(), new InputFilter.LengthFilter(1)});
//        all_base_16.setFilters(new InputFilter[] {new InputFilter.AllCaps(), new InputFilter.LengthFilter(1)});

        InputFilter[] editFilters = common_base_16.getFilters();
        InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
        System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
        newFilters[editFilters.length] = new InputFilter.AllCaps();

        custom_base1.setFilters(newFilters);
        common_base_16.setFilters(newFilters);
        all_base_11.setFilters(newFilters);
        all_base_12.setFilters(newFilters);
        all_base_13.setFilters(newFilters);
        all_base_14.setFilters(newFilters);
        all_base_15.setFilters(newFilters);
        all_base_16.setFilters(newFilters);

        Button btn = findViewById(R.id.btn);
        btn.setTypeface(typeface);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int inputBase = getBase(spinner1);
                int outputBase = getBase(spinner2);
                Editable editable = custom_base1.getText();
                if (editable.toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fields are empty...", Toast.LENGTH_SHORT).show();
                    custom_base2.setText(null);
                }
                else {
                    if (inputBase == 2) {
                        if (!base2_validation(editable)) {
                            Toast.makeText(MainActivity.this, "Invalid Number According To Given Base!", Toast.LENGTH_SHORT).show();
                            custom_base2.setText(null);
                        }
                        else {
                            custom_base2.setText(fromDecimal(convertToDecimal(editable.toString(), inputBase), outputBase));
                        }
                    }
                    else if (inputBase == 3) {
                        if (!base3_validation(editable)) {
                            Toast.makeText(MainActivity.this, "Invalid Number According To Given Base!", Toast.LENGTH_SHORT).show();
                            custom_base2.setText(null);
                        }
                        else {
                            custom_base2.setText(fromDecimal(convertToDecimal(editable.toString(), inputBase), outputBase));
                        }
                    }
                    else if (inputBase == 4) {
                        if (!base4_validation(editable)) {
                            Toast.makeText(MainActivity.this, "Invalid Number According To Given Base!", Toast.LENGTH_SHORT).show();
                            custom_base2.setText(null);
                        }
                        else {
                            custom_base2.setText(fromDecimal(convertToDecimal(editable.toString(), inputBase), outputBase));
                        }
                    }
                    else if (inputBase == 5) {
                        if (!base5_validation(editable)) {
                            Toast.makeText(MainActivity.this, "Invalid Number According To Given Base!", Toast.LENGTH_SHORT).show();
                            custom_base2.setText(null);
                        }
                        else {
                            custom_base2.setText(fromDecimal(convertToDecimal(editable.toString(), inputBase), outputBase));
                        }
                    }
                    else if (inputBase == 6) {
                        if (!base6_validation(editable)) {
                            Toast.makeText(MainActivity.this, "Invalid Number According To Given Base!", Toast.LENGTH_SHORT).show();
                            custom_base2.setText(null);
                        }
                        else {
                            custom_base2.setText(fromDecimal(convertToDecimal(editable.toString(), inputBase), outputBase));
                        }
                    }
                    else if (inputBase == 7) {
                        if (!base7_validation(editable)) {
                            Toast.makeText(MainActivity.this, "Invalid Number According To Given Base!", Toast.LENGTH_SHORT).show();
                            custom_base2.setText(null);
                        }
                        else {
                            custom_base2.setText(fromDecimal(convertToDecimal(editable.toString(), inputBase), outputBase));
                        }
                    }
                    else if (inputBase == 8) {
                        if (!base8_validation(editable)) {
                            Toast.makeText(MainActivity.this, "Invalid Number According To Given Base!", Toast.LENGTH_SHORT).show();
                            custom_base2.setText(null);
                        }
                        else {
                            custom_base2.setText(fromDecimal(convertToDecimal(editable.toString(), inputBase), outputBase));
                        }
                    }
                    else if (inputBase == 9) {
                        if (!base9_validation(editable)) {
                            Toast.makeText(MainActivity.this, "Invalid Number According To Given Base!", Toast.LENGTH_SHORT).show();
                            custom_base2.setText(null);
                        }
                        else {
                            custom_base2.setText(fromDecimal(convertToDecimal(editable.toString(), inputBase), outputBase));
                        }
                    }
                    else if (inputBase == 10) {
                        if (!base10_validation(editable)) {
                            Toast.makeText(MainActivity.this, "Invalid Number According To Given Base!", Toast.LENGTH_SHORT).show();
                            custom_base2.setText(null);
                        }
                        else {
                            custom_base2.setText(fromDecimal(convertToDecimal(editable.toString(), inputBase), outputBase));
                        }
                    }
                    else if (inputBase == 11) {
                        if (!base11_validation(editable)) {
                            Toast.makeText(MainActivity.this, "Invalid Number According To Given Base!", Toast.LENGTH_SHORT).show();
                            custom_base2.setText(null);
                        }
                        else {
                            custom_base2.setText(fromDecimal(convertToDecimal(editable.toString(), inputBase), outputBase));
                        }
                    }
                    else if (inputBase == 12) {
                        if (!base12_validation(editable)) {
                            Toast.makeText(MainActivity.this, "Invalid Number According To Given Base!", Toast.LENGTH_SHORT).show();
                            custom_base2.setText(null);
                        }
                        else {
                            custom_base2.setText(fromDecimal(convertToDecimal(editable.toString(), inputBase), outputBase));
                        }
                    }
                    else if (inputBase == 13) {
                        if (!base13_validation(editable)) {
                            Toast.makeText(MainActivity.this, "Invalid Number According To Given Base!", Toast.LENGTH_SHORT).show();
                            custom_base2.setText(null);
                        }
                        else {
                            custom_base2.setText(fromDecimal(convertToDecimal(editable.toString(), inputBase), outputBase));
                        }
                    }
                    else if (inputBase == 14) {
                        if (!base14_validation(editable)) {
                            Toast.makeText(MainActivity.this, "Invalid Number According To Given Base!", Toast.LENGTH_SHORT).show();
                            custom_base2.setText(null);
                        }
                        else {
                            custom_base2.setText(fromDecimal(convertToDecimal(editable.toString(), inputBase), outputBase));
                        }
                    }
                    else if (inputBase == 15) {
                        if (!base15_validation(editable)) {
                            Toast.makeText(MainActivity.this, "Invalid Number According To Given Base!", Toast.LENGTH_SHORT).show();
                            custom_base2.setText(null);
                        }
                        else {
                            custom_base2.setText(fromDecimal(convertToDecimal(editable.toString(), inputBase), outputBase));
                        }
                    }
                    else if (inputBase == 16) {
                        if (!base16_validation(editable)) {
                            Toast.makeText(MainActivity.this, "Invalid Number According To Given Base!", Toast.LENGTH_SHORT).show();
                            custom_base2.setText(null);
                        }
                        else {
                            custom_base2.setText(fromDecimal(convertToDecimal(editable.toString(), inputBase), outputBase));
                        }
                    }
                }
            }
        });

        custom_base1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textBefore = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!base16_validation(editable)) {
                    int len = textBefore.length();
                    custom_base1.setText(textBefore);
                    custom_base1.setSelection(len);
                }
            }
        });

        common_base_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textBefore = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!base2_validation(editable)) {
                    int len = textBefore.length();
                    common_base_2.setText(textBefore);
                    common_base_2.setSelection(len);
                }
                else if (MainActivity.this.getCurrentFocus().getId() == common_base_2.getId()){
                    if (editable.toString().isEmpty()) {
                        common_base_8.setText(null);
                        common_base_10.setText(null);
                        common_base_16.setText(null);
                    }
                    else {
                        int decimal = convertToDecimal(editable.toString(), 2);
                        common_base_8.setText(fromDecimal(decimal, 8));
                        common_base_10.setText(fromDecimal(decimal, 10));
                        common_base_16.setText(fromDecimal(decimal, 16));
                    }
                }
            }
        });

        common_base_8.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textBefore = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!base8_validation(editable)) {
                    int len = textBefore.length();
                    common_base_8.setText(textBefore);
                    common_base_8.setSelection(len);
                }
                else if (MainActivity.this.getCurrentFocus().getId() == common_base_8.getId()) {
                    if (editable.toString().isEmpty()) {
                        common_base_2.setText(null);
                        common_base_10.setText(null);
                        common_base_16.setText(null);
                    }
                    else {
                        int decimal = convertToDecimal(editable.toString(), 8);
                        common_base_2.setText(fromDecimal(decimal, 2));
                        common_base_10.setText(fromDecimal(decimal, 10));
                        common_base_16.setText(fromDecimal(decimal, 16));
                    }
                }
            }
        });

        common_base_10.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textBefore = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!base10_validation(editable)) {
                    int len = textBefore.length();
                    common_base_10.setText(textBefore);
                    common_base_10.setSelection(len);
                }
                else if (MainActivity.this.getCurrentFocus().getId() == common_base_10.getId()){
                    if (editable.toString().isEmpty()) {
                        common_base_2.setText(null);
                        common_base_8.setText(null);
                        common_base_16.setText(null);
                    }
                    else {
                        int decimal = convertToDecimal(editable.toString(), 10);
                        common_base_2.setText(fromDecimal(decimal, 2));
                        common_base_8.setText(fromDecimal(decimal, 8));
                        common_base_16.setText(fromDecimal(decimal, 16));
                    }
                }
            }
        });

        common_base_16.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textBefore = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!base16_validation(editable)) {
                    int len = textBefore.length();
                    common_base_16.setText(textBefore);
                    common_base_16.setSelection(len);
                }
                else if (MainActivity.this.getCurrentFocus().getId() == common_base_16.getId()){
                    if (editable.toString().isEmpty()) {
                        common_base_2.setText(null);
                        common_base_8.setText(null);
                        common_base_10.setText(null);
                    }
                    else {
                        int decimal = convertToDecimal(editable.toString(), 16);
                        common_base_2.setText(fromDecimal(decimal, 2));
                        common_base_8.setText(fromDecimal(decimal, 8));
                        common_base_10.setText(fromDecimal(decimal, 10));
                    }
                }
            }
        });

        clearAll = new Runnable() {
            @Override
            public void run() {
                cleared = true;
                all_base_2.setText(null);
                all_base_3.setText(null);
                all_base_4.setText(null);
                all_base_5.setText(null);
                all_base_6.setText(null);
                all_base_7.setText(null);
                all_base_8.setText(null);
                all_base_9.setText(null);
                all_base_10.setText(null);
                all_base_11.setText(null);
                all_base_12.setText(null);
                all_base_13.setText(null);
                all_base_14.setText(null);
                all_base_15.setText(null);
                all_base_16.setText(null);
            }
        };

        all_base_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textBefore = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!base2_validation(editable)) {
                    int len = textBefore.length();
                    all_base_2.setText(textBefore);
                    all_base_2.setSelection(len);
                }
                else if (MainActivity.this.getCurrentFocus().getId() == all_base_2.getId()){
                    if (editable.toString().isEmpty()) {
                        if (cleared) {
                            cleared = false;
                        }
                        else {
                            clearAll.run();
                        }
                    }
                    else {
                        int decimal = convertToDecimal(editable.toString(), 2);
                        all_base_3.setText(fromDecimal(decimal, 3));
                        all_base_4.setText(fromDecimal(decimal, 4));
                        all_base_5.setText(fromDecimal(decimal, 5));
                        all_base_6.setText(fromDecimal(decimal, 6));
                        all_base_7.setText(fromDecimal(decimal, 7));
                        all_base_8.setText(fromDecimal(decimal, 8));
                        all_base_9.setText(fromDecimal(decimal, 9));
                        all_base_10.setText(fromDecimal(decimal, 10));
                        all_base_11.setText(fromDecimal(decimal, 11));
                        all_base_12.setText(fromDecimal(decimal, 12));
                        all_base_13.setText(fromDecimal(decimal, 13));
                        all_base_14.setText(fromDecimal(decimal, 14));
                        all_base_15.setText(fromDecimal(decimal, 15));
                        all_base_16.setText(fromDecimal(decimal, 16));
                    }
                }
            }
        });

        all_base_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textBefore = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!base3_validation(editable)) {
                    int len = textBefore.length();
                    all_base_3.setText(textBefore);
                    all_base_3.setSelection(len);
                }
                else if (MainActivity.this.getCurrentFocus().getId() == all_base_3.getId()){
                    if (editable.toString().isEmpty()) {
                        if (cleared) {
                            cleared = false;
                        }
                        else {
                            clearAll.run();
                        }
                    }
                    else {
                        int decimal = convertToDecimal(editable.toString(), 3);
                        all_base_2.setText(fromDecimal(decimal, 2));
                        all_base_4.setText(fromDecimal(decimal, 4));
                        all_base_5.setText(fromDecimal(decimal, 5));
                        all_base_6.setText(fromDecimal(decimal, 6));
                        all_base_7.setText(fromDecimal(decimal, 7));
                        all_base_8.setText(fromDecimal(decimal, 8));
                        all_base_9.setText(fromDecimal(decimal, 9));
                        all_base_10.setText(fromDecimal(decimal, 10));
                        all_base_11.setText(fromDecimal(decimal, 11));
                        all_base_12.setText(fromDecimal(decimal, 12));
                        all_base_13.setText(fromDecimal(decimal, 13));
                        all_base_14.setText(fromDecimal(decimal, 14));
                        all_base_15.setText(fromDecimal(decimal, 15));
                        all_base_16.setText(fromDecimal(decimal, 16));
                    }
                }
            }
        });

        all_base_4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textBefore = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!base4_validation(editable)) {
                    int len = textBefore.length();
                    all_base_4.setText(textBefore);
                    all_base_4.setSelection(len);
                }
                else if (MainActivity.this.getCurrentFocus().getId() == all_base_4.getId()){
                    if (editable.toString().isEmpty()) {
                        if (cleared) {
                            cleared = false;
                        }
                        else {
                            clearAll.run();
                        }
                    }
                    else {
                        int decimal = convertToDecimal(editable.toString(), 4);
                        all_base_2.setText(fromDecimal(decimal, 2));
                        all_base_3.setText(fromDecimal(decimal, 3));
                        all_base_5.setText(fromDecimal(decimal, 5));
                        all_base_6.setText(fromDecimal(decimal, 6));
                        all_base_7.setText(fromDecimal(decimal, 7));
                        all_base_8.setText(fromDecimal(decimal, 8));
                        all_base_9.setText(fromDecimal(decimal, 9));
                        all_base_10.setText(fromDecimal(decimal, 10));
                        all_base_11.setText(fromDecimal(decimal, 11));
                        all_base_12.setText(fromDecimal(decimal, 12));
                        all_base_13.setText(fromDecimal(decimal, 13));
                        all_base_14.setText(fromDecimal(decimal, 14));
                        all_base_15.setText(fromDecimal(decimal, 15));
                        all_base_16.setText(fromDecimal(decimal, 16));
                    }
                }
            }
        });

        all_base_5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textBefore = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!base5_validation(editable)) {
                    int len = textBefore.length();
                    all_base_5.setText(textBefore);
                    all_base_5.setSelection(len);
                }
                else if (MainActivity.this.getCurrentFocus().getId() == all_base_5.getId()){
                    if (editable.toString().isEmpty()) {
                        if (cleared) {
                            cleared = false;
                        }
                        else {
                            clearAll.run();
                        }
                    }
                    else {
                        int decimal = convertToDecimal(editable.toString(), 5);
                        all_base_2.setText(fromDecimal(decimal,2));
                        all_base_3.setText(fromDecimal(decimal, 3));
                        all_base_4.setText(fromDecimal(decimal,4));
                        all_base_6.setText(fromDecimal(decimal, 6));
                        all_base_7.setText(fromDecimal(decimal, 7));
                        all_base_8.setText(fromDecimal(decimal, 8));
                        all_base_9.setText(fromDecimal(decimal, 9));
                        all_base_10.setText(fromDecimal(decimal, 10));
                        all_base_11.setText(fromDecimal(decimal, 11));
                        all_base_12.setText(fromDecimal(decimal, 12));
                        all_base_13.setText(fromDecimal(decimal, 13));
                        all_base_14.setText(fromDecimal(decimal, 14));
                        all_base_15.setText(fromDecimal(decimal, 15));
                        all_base_16.setText(fromDecimal(decimal, 16));
                    }
                }
            }
        });

        all_base_6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textBefore = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!base6_validation(editable)) {
                    int len = textBefore.length();
                    all_base_6.setText(textBefore);
                    all_base_6.setSelection(len);
                }
                else if (MainActivity.this.getCurrentFocus().getId() == all_base_6.getId()){
                    if (editable.toString().isEmpty()) {
                        if (cleared) {
                            cleared = false;
                        }
                        else {
                            clearAll.run();
                        }
                    }
                    else {
                        int decimal = convertToDecimal(editable.toString(), 6);
                        all_base_2.setText(fromDecimal(decimal, 2));
                        all_base_3.setText(fromDecimal(decimal, 3));
                        all_base_4.setText(fromDecimal(decimal, 4));
                        all_base_5.setText(fromDecimal(decimal, 5));
                        all_base_7.setText(fromDecimal(decimal, 7));
                        all_base_8.setText(fromDecimal(decimal, 8));
                        all_base_9.setText(fromDecimal(decimal, 9));
                        all_base_10.setText(fromDecimal(decimal, 10));
                        all_base_11.setText(fromDecimal(decimal, 11));
                        all_base_12.setText(fromDecimal(decimal, 12));
                        all_base_13.setText(fromDecimal(decimal, 13));
                        all_base_14.setText(fromDecimal(decimal, 14));
                        all_base_15.setText(fromDecimal(decimal, 15));
                        all_base_16.setText(fromDecimal(decimal, 16));
                    }
                }
            }
        });

        all_base_7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textBefore = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!base7_validation(editable)) {
                    int len = textBefore.length();
                    all_base_7.setText(textBefore);
                    all_base_7.setSelection(len);
                }
                else if (MainActivity.this.getCurrentFocus().getId() == all_base_7.getId()){
                    if (editable.toString().isEmpty()) {
                        if (cleared) {
                            cleared = false;
                        }
                        else {
                            clearAll.run();
                        }
                    }
                    else {
                        int decimal = convertToDecimal(editable.toString(), 7);
                        all_base_2.setText(fromDecimal(decimal, 2));
                        all_base_3.setText(fromDecimal(decimal, 3));
                        all_base_4.setText(fromDecimal(decimal, 4));
                        all_base_5.setText(fromDecimal(decimal, 5));
                        all_base_6.setText(fromDecimal(decimal, 6));
                        all_base_8.setText(fromDecimal(decimal, 8));
                        all_base_9.setText(fromDecimal(decimal, 9));
                        all_base_10.setText(fromDecimal(decimal, 10));
                        all_base_11.setText(fromDecimal(decimal, 11));
                        all_base_12.setText(fromDecimal(decimal, 12));
                        all_base_13.setText(fromDecimal(decimal, 13));
                        all_base_14.setText(fromDecimal(decimal, 14));
                        all_base_15.setText(fromDecimal(decimal, 15));
                        all_base_16.setText(fromDecimal(decimal, 16));
                    }
                }
            }
        });

        all_base_8.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textBefore = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!base8_validation(editable)) {
                    int len = textBefore.length();
                    all_base_8.setText(textBefore);
                    all_base_8.setSelection(len);
                }
                else if (MainActivity.this.getCurrentFocus().getId() == all_base_8.getId()){
                    if (editable.toString().isEmpty()) {
                        if (cleared) {
                            cleared = false;
                        }
                        else {
                            clearAll.run();
                        }
                    }
                    else {
                        int decimal = convertToDecimal(editable.toString(), 8);
                        all_base_2.setText(fromDecimal(decimal,2));
                        all_base_3.setText(fromDecimal(decimal, 3));
                        all_base_4.setText(fromDecimal(decimal, 4));
                        all_base_5.setText(fromDecimal(decimal, 5));
                        all_base_6.setText(fromDecimal(decimal,6));
                        all_base_7.setText(fromDecimal(decimal, 7));
                        all_base_9.setText(fromDecimal(decimal, 9));
                        all_base_10.setText(fromDecimal(decimal, 10));
                        all_base_11.setText(fromDecimal(decimal, 11));
                        all_base_12.setText(fromDecimal(decimal, 12));
                        all_base_13.setText(fromDecimal(decimal, 13));
                        all_base_14.setText(fromDecimal(decimal, 14));
                        all_base_15.setText(fromDecimal(decimal, 15));
                        all_base_16.setText(fromDecimal(decimal, 16));
                    }
                }
            }
        });

        all_base_9.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textBefore = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!base9_validation(editable)) {
                    int len = textBefore.length();
                    all_base_9.setText(textBefore);
                    all_base_9.setSelection(len);
                }
                else if (MainActivity.this.getCurrentFocus().getId() == all_base_9.getId()){
                    if (editable.toString().isEmpty()) {
                        if (cleared) {
                            cleared = false;
                        }
                        else {
                            clearAll.run();
                        }
                    }
                    else {
                        int decimal = convertToDecimal(editable.toString(), 9);
                        all_base_2.setText(fromDecimal(decimal, 2));
                        all_base_3.setText(fromDecimal(decimal, 3));
                        all_base_4.setText(fromDecimal(decimal, 4));
                        all_base_5.setText(fromDecimal(decimal, 5));
                        all_base_6.setText(fromDecimal(decimal, 6));
                        all_base_7.setText(fromDecimal(decimal, 7));
                        all_base_8.setText(fromDecimal(decimal, 8));
                        all_base_10.setText(fromDecimal(decimal, 10));
                        all_base_11.setText(fromDecimal(decimal, 11));
                        all_base_12.setText(fromDecimal(decimal, 12));
                        all_base_13.setText(fromDecimal(decimal, 13));
                        all_base_14.setText(fromDecimal(decimal, 14));
                        all_base_15.setText(fromDecimal(decimal, 15));
                        all_base_16.setText(fromDecimal(decimal, 16));
                    }
                }
            }
        });

        all_base_10.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textBefore = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!base10_validation(editable)) {
                    int len = textBefore.length();
                    all_base_10.setText(textBefore);
                    all_base_10.setSelection(len);
                }
                else if (MainActivity.this.getCurrentFocus().getId() == all_base_10.getId()){
                    if (editable.toString().isEmpty()) {
                        if (cleared) {
                            cleared = false;
                        }
                        else {
                            clearAll.run();
                        }
                    }
                    else {
                        int decimal = convertToDecimal(editable.toString(), 10);
                        all_base_2.setText(fromDecimal(decimal, 2));
                        all_base_3.setText(fromDecimal(decimal, 3));
                        all_base_4.setText(fromDecimal(decimal, 4));
                        all_base_5.setText(fromDecimal(decimal, 5));
                        all_base_6.setText(fromDecimal(decimal, 6));
                        all_base_7.setText(fromDecimal(decimal, 7));
                        all_base_8.setText(fromDecimal(decimal, 8));
                        all_base_9.setText(fromDecimal(decimal, 9));
                        all_base_11.setText(fromDecimal(decimal, 11));
                        all_base_12.setText(fromDecimal(decimal, 12));
                        all_base_13.setText(fromDecimal(decimal, 13));
                        all_base_14.setText(fromDecimal(decimal, 14));
                        all_base_15.setText(fromDecimal(decimal, 15));
                        all_base_16.setText(fromDecimal(decimal, 16));
                    }
                }
            }
        });

        all_base_11.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textBefore = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!base11_validation(editable)) {
                    int len = textBefore.length();
                    all_base_11.setText(textBefore);
                    all_base_11.setSelection(len);
                }
                else if (MainActivity.this.getCurrentFocus().getId() == all_base_11.getId()){
                    if (editable.toString().isEmpty()) {
                        if (cleared) {
                            cleared = false;
                        }
                        else {
                            clearAll.run();
                        }
                    }
                    else {
                        int decimal = convertToDecimal(editable.toString(), 11);
                        all_base_2.setText(fromDecimal(decimal, 2));
                        all_base_3.setText(fromDecimal(decimal, 3));
                        all_base_4.setText(fromDecimal(decimal, 4));
                        all_base_5.setText(fromDecimal(decimal, 5));
                        all_base_6.setText(fromDecimal(decimal, 6));
                        all_base_7.setText(fromDecimal(decimal, 7));
                        all_base_8.setText(fromDecimal(decimal, 8));
                        all_base_9.setText(fromDecimal(decimal, 9));
                        all_base_10.setText(fromDecimal(decimal, 10));
                        all_base_12.setText(fromDecimal(decimal, 12));
                        all_base_13.setText(fromDecimal(decimal, 13));
                        all_base_14.setText(fromDecimal(decimal, 14));
                        all_base_15.setText(fromDecimal(decimal, 15));
                        all_base_16.setText(fromDecimal(decimal, 16));
                    }
                }
            }
        });

        all_base_12.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textBefore = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!base12_validation(editable)) {
                    int len = textBefore.length();
                    all_base_12.setText(textBefore);
                    all_base_12.setSelection(len);
                }
                else if (MainActivity.this.getCurrentFocus().getId() == all_base_12.getId()){
                    if (editable.toString().isEmpty()) {
                        if (cleared) {
                            cleared = false;
                        }
                        else {
                            clearAll.run();
                        }
                    }
                    else {
                        int decimal = convertToDecimal(editable.toString(), 12);
                        all_base_2.setText(fromDecimal(decimal, 2));
                        all_base_3.setText(fromDecimal(decimal, 3));
                        all_base_4.setText(fromDecimal(decimal, 4));
                        all_base_5.setText(fromDecimal(decimal, 5));
                        all_base_6.setText(fromDecimal(decimal, 6));
                        all_base_7.setText(fromDecimal(decimal, 7));
                        all_base_8.setText(fromDecimal(decimal, 8));
                        all_base_9.setText(fromDecimal(decimal, 9));
                        all_base_10.setText(fromDecimal(decimal, 10));
                        all_base_11.setText(fromDecimal(decimal, 11));
                        all_base_13.setText(fromDecimal(decimal, 13));
                        all_base_14.setText(fromDecimal(decimal, 14));
                        all_base_15.setText(fromDecimal(decimal, 15));
                        all_base_16.setText(fromDecimal(decimal, 16));
                    }
                }
            }
        });

        all_base_13.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textBefore = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!base13_validation(editable)) {
                    int len = textBefore.length();
                    all_base_13.setText(textBefore);
                    all_base_13.setSelection(len);
                }
                else if (MainActivity.this.getCurrentFocus().getId() == all_base_13.getId()){
                    if (editable.toString().isEmpty()) {
                        if (cleared) {
                            cleared = false;
                        }
                        else {
                            clearAll.run();
                        }
                    }
                    else {
                        int decimal = convertToDecimal(editable.toString(), 13);
                        all_base_2.setText(fromDecimal(decimal, 2));
                        all_base_3.setText(fromDecimal(decimal, 3));
                        all_base_4.setText(fromDecimal(decimal, 4));
                        all_base_5.setText(fromDecimal(decimal, 5));
                        all_base_6.setText(fromDecimal(decimal, 6));
                        all_base_7.setText(fromDecimal(decimal, 7));
                        all_base_8.setText(fromDecimal(decimal, 8));
                        all_base_9.setText(fromDecimal(decimal, 9));
                        all_base_10.setText(fromDecimal(decimal, 10));
                        all_base_11.setText(fromDecimal(decimal, 11));
                        all_base_12.setText(fromDecimal(decimal, 12));
                        all_base_14.setText(fromDecimal(decimal, 14));
                        all_base_15.setText(fromDecimal(decimal, 15));
                        all_base_16.setText(fromDecimal(decimal, 16));
                    }
                }
            }
        });

        all_base_14.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textBefore = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!base14_validation(editable)) {
                    int len = textBefore.length();
                    all_base_14.setText(textBefore);
                    all_base_14.setSelection(len);
                }
                else if (MainActivity.this.getCurrentFocus().getId() == all_base_14.getId()){
                    if (editable.toString().isEmpty()) {
                        if (cleared) {
                            cleared = false;
                        }
                        else {
                            clearAll.run();
                        }
                    }
                    else {
                        int decimal = convertToDecimal(editable.toString(), 14);
                        all_base_2.setText(fromDecimal(decimal, 2));
                        all_base_3.setText(fromDecimal(decimal, 3));
                        all_base_4.setText(fromDecimal(decimal, 4));
                        all_base_5.setText(fromDecimal(decimal, 5));
                        all_base_6.setText(fromDecimal(decimal, 6));
                        all_base_7.setText(fromDecimal(decimal, 7));
                        all_base_8.setText(fromDecimal(decimal, 8));
                        all_base_9.setText(fromDecimal(decimal, 9));
                        all_base_10.setText(fromDecimal(decimal, 10));
                        all_base_11.setText(fromDecimal(decimal, 11));
                        all_base_12.setText(fromDecimal(decimal, 12));
                        all_base_13.setText(fromDecimal(decimal, 13));
                        all_base_15.setText(fromDecimal(decimal, 15));
                        all_base_16.setText(fromDecimal(decimal, 16));
                    }
                }
            }
        });

        all_base_15.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textBefore = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!base15_validation(editable)) {
                    int len = textBefore.length();
                    all_base_15.setText(textBefore);
                    all_base_15.setSelection(len);
                }
                else if (MainActivity.this.getCurrentFocus().getId() == all_base_15.getId()){
                    if (editable.toString().isEmpty()) {
                        if (cleared) {
                            cleared = false;
                        }
                        else {
                            clearAll.run();
                        }
                    }
                    else {
                        int decimal = convertToDecimal(editable.toString(), 15);
                        all_base_2.setText(fromDecimal(decimal, 2));
                        all_base_3.setText(fromDecimal(decimal, 3));
                        all_base_4.setText(fromDecimal(decimal, 4));
                        all_base_5.setText(fromDecimal(decimal, 5));
                        all_base_6.setText(fromDecimal(decimal, 6));
                        all_base_7.setText(fromDecimal(decimal, 7));
                        all_base_8.setText(fromDecimal(decimal, 8));
                        all_base_9.setText(fromDecimal(decimal, 9));
                        all_base_10.setText(fromDecimal(decimal, 10));
                        all_base_11.setText(fromDecimal(decimal, 11));
                        all_base_12.setText(fromDecimal(decimal, 12));
                        all_base_13.setText(fromDecimal(decimal, 13));
                        all_base_14.setText(fromDecimal(decimal, 14));
                        all_base_16.setText(fromDecimal(decimal, 16));
                    }
                }
            }
        });

        all_base_16.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textBefore = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!base16_validation(editable)) {
                    int len = textBefore.length();
                    all_base_16.setText(textBefore);
                    all_base_16.setSelection(len);
                }
                else if (MainActivity.this.getCurrentFocus().getId() == all_base_15.getId()){
                    if (editable.toString().isEmpty()) {
                        if (cleared) {
                            cleared = false;
                        }
                        else {
                            clearAll.run();
                        }
                    }
                    else {
                        int decimal = convertToDecimal(editable.toString(), 16);
                        all_base_2.setText(fromDecimal(decimal, 2));
                        all_base_3.setText(fromDecimal(decimal, 3));
                        all_base_4.setText(fromDecimal(decimal, 4));
                        all_base_5.setText(fromDecimal(decimal, 5));
                        all_base_6.setText(fromDecimal(decimal, 6));
                        all_base_7.setText(fromDecimal(decimal, 7));
                        all_base_8.setText(fromDecimal(decimal, 8));
                        all_base_9.setText(fromDecimal(decimal, 9));
                        all_base_10.setText(fromDecimal(decimal, 10));
                        all_base_11.setText(fromDecimal(decimal, 11));
                        all_base_12.setText(fromDecimal(decimal, 12));
                        all_base_13.setText(fromDecimal(decimal, 13));
                        all_base_14.setText(fromDecimal(decimal, 14));
                        all_base_15.setText(fromDecimal(decimal, 15));
                    }
                }
            }
        });

        fab_copy.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        fab_refresh.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

        fab_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (COMMON_LAYOUT_VISIBLE) {
                    if (!common_base_2.getText().toString().isEmpty()) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("label",
                                "Binary: " + common_base_2.getText().toString() +
                                        "\nOctal: " + common_base_8.getText().toString() +
                                        "\nDecimal: " + common_base_10.getText().toString() +
                                        "\nHex Decimal: " + common_base_16.getText().toString()
                        );
                        if (clipboard != null) {
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(MainActivity.this, "Copied To Clipboard", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(MainActivity.this, "fields are empty", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (ALL_BASES_LAYOUT_VISIBLE) {
                    if (!all_base_2.getText().toString().isEmpty()) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("label",
                                "Base 2: " + all_base_2.getText().toString() +
                                        "\nBase 3: " + all_base_3.getText().toString() +
                                        "\nBase 4: " + all_base_4.getText().toString() +
                                        "\nBase 5: " + all_base_5.getText().toString() +
                                        "\nBase 6: " + all_base_6.getText().toString() +
                                        "\nBase 7: " + all_base_7.getText().toString() +
                                        "\nBase 8: " + all_base_8.getText().toString() +
                                        "\nBase 9: " + all_base_9.getText().toString() +
                                        "\nBase 10: " + all_base_10.getText().toString() +
                                        "\nBase 11: " + all_base_11.getText().toString() +
                                        "\nBase 12: " + all_base_12.getText().toString() +
                                        "\nBase 13: " + all_base_13.getText().toString() +
                                        "\nBase 14: " + all_base_14.getText().toString() +
                                        "\nBase 15: " + all_base_15.getText().toString() +
                                        "\nBase 16: " + all_base_16.getText().toString()
                        );
                        if (clipboard != null) {
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(MainActivity.this, "Copied To Clipboard", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(MainActivity.this, "fields are empty", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if (!custom_base1.getText().toString().isEmpty()) {

                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("label",
                                custom_base1.getText().toString() + "(" + spinner1.getSelectedItem().toString() + ")" +
                                        " = " +
                                        custom_base2.getText().toString() + "(" + spinner2.getSelectedItem().toString() + ")");
                        if (clipboard != null) {
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(MainActivity.this, "Copied To Clipboard", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(MainActivity.this, "fields are empty", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        fab_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (COMMON_LAYOUT_VISIBLE) {
                    common_base_2.setText("");
                    common_base_8.setText("");
                    common_base_10.setText("");
                    common_base_16.setText("");
                }
                else {
                    custom_base1.setText("");
                    custom_base2.setText("");
                }
            }
        });

    }

    private int getBase(Spinner spinner) {
        String base = spinner.getSelectedItem().toString();
        if (base.equals("Base 2")) {
            return 2;
        }
        else if (base.equals("Base 3")) {
            return 3;
        }
        else if (base.equals("Base 4")) {
            return 4;
        }
        else if (base.equals("Base 5")) {
            return 5;
        }
        else if (base.equals("Base 6")) {
            return 6;
        }
        else if (base.equals("Base 7")) {
            return 7;
        }
        else if (base.equals("Base 8")) {
            return 8;
        }
        else if (base.equals("Base 9")) {
            return 9;
        }
        else if (base.equals("Base 10")) {
            return 10;
        }
        else if (base.equals("Base 11")) {
            return 11;
        }
        else if (base.equals("Base 12")) {
            return 12;
        }
        else if (base.equals("Base 13")) {
            return 13;
        }
        else if (base.equals("Base 14")) {
            return 14;
        }
        else if (base.equals("Base 15")) {
            return 15;
        }
        else if (base.equals("Base 16")) {
            return 16;
        }
        else {
            return 0;
        }
    }

    private Boolean base2_validation (Editable editable) {
        for (int j = 0; j < editable.length(); j++) {
            char ch = editable.charAt(j);
            if (ch != '0' && ch != '1') {
                return false;
            }
        }
        return true;
    }

    private Boolean base3_validation (Editable editable) {
        for (int j = 0; j < editable.length(); j++) {
            char ch = editable.charAt(j);
            if (ch != '0' && ch != '1' && ch != '2') {
                return false;
            }
        }
        return true;
    }

    private Boolean base4_validation (Editable editable) {
        for (int j = 0; j < editable.length(); j++) {
            char ch = editable.charAt(j);
            if (ch != '0' && ch != '1' && ch != '2'
                    && ch != '3') {
                return false;
            }
        }
        return true;
    }

    private Boolean base5_validation (Editable editable) {
        for (int j = 0; j < editable.length(); j++) {
            char ch = editable.charAt(j);
            if (ch != '0' && ch != '1' && ch != '2'
                    && ch != '3' && ch != '4') {
                return false;
            }
        }
        return true;
    }

    private Boolean base6_validation (Editable editable) {
        for (int j = 0; j < editable.length(); j++) {
            char ch = editable.charAt(j);
            if (ch != '0' && ch != '1' && ch != '2'
                    && ch != '3' && ch != '4' && ch != '5') {
                return false;
            }
        }
        return true;
    }

    private Boolean base7_validation (Editable editable) {
        for (int j = 0; j < editable.length(); j++) {
            char ch = editable.charAt(j);
            if (ch != '0' && ch != '1' && ch != '2'
                    && ch != '3' && ch != '4' && ch != '5'
                    && ch != '6') {
                return false;
            }
        }
        return true;
    }

    private Boolean base8_validation (Editable editable) {
        for (int j = 0; j < editable.length(); j++) {
            char ch = editable.charAt(j);
            if (ch != '0' && ch != '1' && ch != '2'
                    && ch != '3' && ch != '4' && ch != '5'
                    && ch != '6' && ch != '7') {
                return false;
            }
        }
        return true;
    }

    private Boolean base9_validation (Editable editable) {
        for (int j = 0; j < editable.length(); j++) {
            char ch = editable.charAt(j);
            if (ch != '0' && ch != '1' && ch != '2'
                    && ch != '3' && ch != '4' && ch != '5'
                    && ch != '6' && ch != '7' && ch != '8') {
                return false;
            }
        }
        return true;
    }

    private Boolean base10_validation (Editable editable) {
        for (int j = 0; j < editable.length(); j++) {
            char ch = editable.charAt(j);
            if (ch != '0' && ch != '1' && ch != '2'
                    && ch != '3' && ch != '4' && ch != '5'
                    && ch != '6' && ch != '7' && ch != '8'
                    && ch != '9') {
                return false;
            }
        }
        return true;
    }

    private Boolean base11_validation (Editable editable) {
        for (int j = 0; j < editable.length(); j++) {
            char ch = editable.charAt(j);
            if (ch != '0' && ch != '1' && ch != '2'
                    && ch != '3' && ch != '4' && ch != '5'
                    && ch != '6' && ch != '7' && ch != '8'
                    && ch != '9' && ch != 'A') {
                return false;
            }
        }
        return true;
    }

    private Boolean base12_validation (Editable editable) {
        for (int j = 0; j < editable.length(); j++) {
            char ch = editable.charAt(j);
            if (ch != '0' && ch != '1' && ch != '2'
                    && ch != '3' && ch != '4' && ch != '5'
                    && ch != '6' && ch != '7' && ch != '8'
                    && ch != '9' && ch != 'A' && ch != 'B') {
                return false;
            }
        }
        return true;
    }

    private Boolean base13_validation (Editable editable) {
        for (int j = 0; j < editable.length(); j++) {
            char ch = editable.charAt(j);
            if (ch != '0' && ch != '1' && ch != '2'
                    && ch != '3' && ch != '4' && ch != '5'
                    && ch != '6' && ch != '7' && ch != '8'
                    && ch != '9' && ch != 'A' && ch != 'B'
                    && ch != 'C') {
                return false;
            }
        }
        return true;
    }

    private Boolean base14_validation (Editable editable) {
        for (int j = 0; j < editable.length(); j++) {
            char ch = editable.charAt(j);
            if (ch != '0' && ch != '1' && ch != '2'
                    && ch != '3' && ch != '4' && ch != '5'
                    && ch != '6' && ch != '7' && ch != '8'
                    && ch != '9' && ch != 'A' && ch != 'B'
                    && ch != 'C' && ch != 'D') {
                return false;
            }
        }
        return true;
    }

    private Boolean base15_validation (Editable editable) {
        for (int j = 0; j < editable.length(); j++) {
            char ch = editable.charAt(j);
            if (ch != '0' && ch != '1' && ch != '2'
                    && ch != '3' && ch != '4' && ch != '5'
                    && ch != '6' && ch != '7' && ch != '8'
                    && ch != '9' && ch != 'A' && ch != 'B'
                    && ch != 'C' && ch != 'D' && ch != 'E') {
                return false;
            }
        }
        return true;
    }

    private Boolean base16_validation (Editable editable) {
        for (int j = 0; j < editable.length(); j++) {
            char ch = editable.charAt(j);
            if (ch != '0' && ch != '1' && ch != '2'
                    && ch != '3' && ch != '4' && ch != '5'
                    && ch != '6' && ch != '7' && ch != '8'
                    && ch != '9' && ch != 'A' && ch != 'B'
                    && ch != 'C' && ch != 'D' && ch != 'E'
                    && ch != 'F') {
                return false;
            }
        }
        return true;
    }

    public static int convertToDecimal(String str, int base) {
        int v = 0;
        int total = 0;
        int pow = 0;
        str = str.toUpperCase();
        for (int i = str.length() - 1; i >= 0; i--) {
            char c = str.charAt(i);
            if (c >= '0' && c <= '9')
                v = c - '0';
            else if (c >= 'A' && c <= 'Z')
                v = 10 + (c - 'A');
            total += v * Math.pow(base, pow);
            pow++;
        }
        return total;
    }

    public static String fromDecimal(int number, int base)
    {
        int quotient = number / base;
        int remainder = number % base;

        if (quotient == 0) // base case
        {
            if (remainder == 10) {
                return "A";
            }
            else if (remainder == 11) {
                return "B";
            }
            else if (remainder == 12) {
                return "C";
            }
            else if (remainder == 13) {
                return "D";
            }
            else if (remainder == 14) {
                return "E";
            }
            else if (remainder == 15) {
                return "F";
            }
            else {
                return Integer.toString(remainder);
            }
        }
        else {

            if (remainder == 10) {
                return fromDecimal(quotient, base) + "A";
            }
            else if (remainder == 11) {
                return fromDecimal(quotient, base) + "B";
            }
            else if (remainder == 12) {
                return fromDecimal(quotient, base) + "C";
            }
            else if (remainder == 13) {
                return fromDecimal(quotient, base) + "D";
            }
            else if (remainder == 14) {
                return fromDecimal(quotient, base) + "E";
            }
            else if (remainder == 15) {
                return fromDecimal(quotient, base) + "F";
            }
            else {
                return fromDecimal(quotient, base) + Integer.toString(remainder);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press Again To Exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menuItem = menu.findItem(R.id.reset);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.whyAds) {
            showWhyAds();
        }
        else if (id == R.id.reset) {
            clearAll.run();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            showAbout();
        }
        else if (id == R.id.nav_rate_us) {
            rateUs();
        }
        else if (id == R.id.nav_share) {
            shareApp();
        }
        else if (id == R.id.nav_more_apps) {
            moreApps();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void moreApps() {
        try {
            //replace &quot;Unified+Apps&quot; with your developer name
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Script+Orient")));
        }
        catch (android.content.ActivityNotFoundException anfe) {
            //replace &quot;Unified+Apps&quot; with your developer name
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/search?q=pub:Script+Orient")));
        }
    }

    private void rateUs() {
        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
        }
    }

    @SuppressLint("SetTextI18n")
    private void showWhyAds() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final View view1 = getLayoutInflater().inflate(R.layout.dialog_layout, null);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "ubunto_regular.ttf");
        ((TextView) view1.findViewById(R.id.dialogTitle)).setTypeface(typeface);
        ((TextView) view1.findViewById(R.id.dialogBodyText)).setTypeface(typeface);
        ((EditText) view1.findViewById(R.id.alertEditText)).setTypeface(typeface);
        ((TextView) view1.findViewById(R.id.dialogEditTextTextView)).setTypeface(typeface);
        ((Button) view1.findViewById(R.id.dialogPositiveButton)).setTypeface(typeface);
        ((Button) view1.findViewById(R.id.dialogNegativeButton)).setTypeface(typeface);

        ((TextView) view1.findViewById(R.id.dialogTitle)).setText("Why Ads?");
        ((TextView) view1.findViewById(R.id.dialogBodyText)).setText("Dear user! We know, Ads can be a pain for you, " +
                "but they are our only way to maintain the production and other costs. " +
                "Your patience is highly appreciated and we hope our service can be worth it.\n" +
                "Thanks!" +
                "\n♥♥♥");

        view1.findViewById(R.id.dialogEditTextRelative).setVisibility(View.GONE);
        view1.findViewById(R.id.dialogNegativeButton).setVisibility(View.GONE);

        ((Button) view1.findViewById(R.id.dialogPositiveButton)).setText("Okay ☺");

        view1.findViewById(R.id.crossImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        view1.findViewById(R.id.dialogPositiveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view1);
        dialog.show();
    }

    private void shareApp() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "WhatsApp Status Downloader");
            String massage = "\nDownload WhatsApp Statuses Of Your Friends And Beloved Once's " +
                    "And Save It To Your Gallery, Very Easily By Using: \n\n" +
                    "http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName() + "\n\n";
            i.putExtra(Intent.EXTRA_TEXT, massage);
            startActivity(Intent.createChooser(i, "Share via"));
        } catch(Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void showAbout() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final View view1 = getLayoutInflater().inflate(R.layout.dialog_layout, null);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "ubunto_regular.ttf");
        ((TextView) view1.findViewById(R.id.dialogTitle)).setTypeface(typeface);
        ((TextView) view1.findViewById(R.id.dialogBodyText)).setTypeface(typeface);
        ((EditText) view1.findViewById(R.id.alertEditText)).setTypeface(typeface);
        ((TextView) view1.findViewById(R.id.dialogEditTextTextView)).setTypeface(typeface);
        ((Button) view1.findViewById(R.id.dialogPositiveButton)).setTypeface(typeface);
        ((Button) view1.findViewById(R.id.dialogNegativeButton)).setTypeface(typeface);

        ((TextView) view1.findViewById(R.id.dialogTitle)).setText("About App");
        ((TextView) view1.findViewById(R.id.dialogBodyText)).setText("You can convert " +
                "the number of any base to the number of your desired base, very easily by using this \"Base Converter\"." +
                "\nThanks for using!" +
                "\n♥♥♥");

        view1.findViewById(R.id.dialogEditTextRelative).setVisibility(View.GONE);
        view1.findViewById(R.id.dialogNegativeButton).setVisibility(View.GONE);

        ((Button) view1.findViewById(R.id.dialogPositiveButton)).setText("Okay ☺");

        view1.findViewById(R.id.crossImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        view1.findViewById(R.id.dialogPositiveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view1);
        dialog.show();
    }
}