package com.example.fartos;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

public class Opciones extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    private SeekBar volumeSeekBar;
    private CheckBox muteCheckBox;
    private RadioGroup languageRadioGroup;
    private RadioButton englishRadioButton;
    private RadioButton spanishRadioButton;
    private RadioButton frenchRadioButton;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);

        // Initialize the shared preferences object
        sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);

        // Get references to the UI components
        volumeSeekBar = findViewById(R.id.seekbar_volume);
        muteCheckBox = findViewById(R.id.mute_checkbox);
        languageRadioGroup = findViewById(R.id.language_radio_group);
        englishRadioButton = findViewById(R.id.english_radio_button);
        spanishRadioButton = findViewById(R.id.spanish_radio_button);
        frenchRadioButton = findViewById(R.id.french_radio_button);
        saveButton = findViewById(R.id.save_button);

        // Restore the previous state of the UI components
        volumeSeekBar.setProgress(sharedPreferences.getInt("volume", 50));
        muteCheckBox.setChecked(sharedPreferences.getBoolean("mute", false));
        int selectedLanguageId = sharedPreferences.getInt("language", R.id.english_radio_button);
        languageRadioGroup.check(selectedLanguageId);

        // Attach listeners to the UI components
        volumeSeekBar.setOnSeekBarChangeListener(new VolumeSeekBarChangeListener());
        muteCheckBox.setOnCheckedChangeListener(new MuteCheckBoxChangeListener());
        languageRadioGroup.setOnCheckedChangeListener(new LanguageRadioGroupChangeListener());
        saveButton.setOnClickListener(new SaveButtonClickListener());
    }

    private class VolumeSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // Save the current volume setting
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("volume", progress);
            editor.apply();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // Do nothing
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // Do nothing
        }
    }

    private class MuteCheckBoxChangeListener implements CheckBox.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // Save the current mute setting
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("mute", isChecked);
            editor.apply();
        }


    }

    private class LanguageRadioGroupChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // Save the current language selection
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("language", checkedId);
            editor.apply();
        }
    }

    private class SaveButtonClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }
}