package com.hiit.timer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class OptionsFragment extends Fragment {

    private SeekBar seekBarExercise, seekBarPause, seekBarRepeating;
    private TextView txtViewExerciseTime, txtViewPauseTime, txtViewRepeating;
    private Button confirmButton, cancelButton;

    private int exerciseTimeVal, pauseTimeVal, repeatVal;

    private List<Integer> optionList;

    OptionValuesWriteToJson optv;

    private final int STEP_SIZE = 5;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View optionView = inflater.inflate(R.layout.fragment_options, container, false);

        seekBarExercise = optionView.findViewById(R.id.seekBar_exrecise_time_setup);
        txtViewExerciseTime = optionView.findViewById(R.id.text_view_exercise_time_screen);

        seekBarPause = optionView.findViewById(R.id.seekBar_pause_time_setup);
        txtViewPauseTime = optionView.findViewById(R.id.text_view_pause_time_screen);

        seekBarRepeating = optionView.findViewById(R.id.seekBar_repeating_setup);
        txtViewRepeating = optionView.findViewById(R.id.text_view_repeating_screen);

        confirmButton = optionView.findViewById(R.id.button_approve_setup);
        cancelButton = optionView.findViewById(R.id.button_cancel_setup);

        optv = new OptionValuesWriteToJson();

        if (isOptionFileExists()) {
            optionList = readOptions();
        } else {
            optionList = optv.createDefaultTimeValues();
        }

        setValuesOnSeekBars();
        confirmChanges();
        cancelChanges();

        return optionView;
    }

    //Check if file with option exist in sharedPrefs
    private boolean isOptionFileExists(){
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        if (sp.contains("OptionsJson")){
            return true;
        }
        else return false;
    }

    private void changeExerciseTimeSeekBar(int excerciseTime) {
        seekBarExercise.setProgress(excerciseTime);
        exerciseTimeVal = excerciseTime;
        txtViewExerciseTime.setText(excerciseTime + " secondes");
        seekBarExercise.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress <= 5) {
                    progress = 5;
                }
                progress = Math.round(progress / STEP_SIZE) * STEP_SIZE;
                exerciseTimeVal = progress;
                txtViewExerciseTime.setText(progress + " secondes");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void changePauseTimeSeekBar(int pauseTime) {
        seekBarPause.setProgress(pauseTime);
        pauseTimeVal = pauseTime;
        txtViewPauseTime.setText(pauseTime + " secondes");
        seekBarPause.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress <= 5) {
                    progress = 5;
                }
                progress = Math.round(progress / STEP_SIZE) * STEP_SIZE;
                pauseTimeVal = progress;
                txtViewPauseTime.setText(progress + " secondes");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void changeRepeatingSeekBar(int repeating) {
        seekBarRepeating.setProgress(repeating);
        repeatVal = repeating;
        txtViewRepeating.setText(repeating + " répétitions");
        seekBarRepeating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress <= 1) {
                    progress = 1;
                }
                repeatVal = progress;
                txtViewRepeating.setText(progress + " répétitions");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void confirmChanges() {
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges(optv.writeOptionsToJson(exerciseTimeVal, pauseTimeVal, repeatVal));
            }
        });
    }

    public void saveChanges(String gson) {
        SharedPreferences sharedPrefsTimeOptions = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefsTimeOptions.edit();
        editor.putString("OptionsJson", gson);
        editor.apply();
        Toast.makeText(OptionsFragment.super.getContext(), "Paramètres sauvegardés", Toast.LENGTH_SHORT).show();
        optionList = readOptions();
    }


    private List<Integer> readOptions() {
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        return optv.optionsFromFile(sp.getString("OptionsJson", null));
    }

    private void cancelChanges() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readOptions();
                setValuesOnSeekBars();
            }
        });
    }

    private void setValuesOnSeekBars(){
        changeExerciseTimeSeekBar(optionList.get(0));
        changePauseTimeSeekBar(optionList.get(1));
        changeRepeatingSeekBar(optionList.get(2));
    }


}
