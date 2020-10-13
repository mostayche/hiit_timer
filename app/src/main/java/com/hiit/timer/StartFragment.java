package com.hiit.timer;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StartFragment extends Fragment {
    private Button startButton;
    private Button pauseButton;
    private Button restartButton;
    private TextView counterView;
    private TextView textViewStatus;
    private TextView howManyExercises;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds, exerciseTime, pauseTime;
    private int exerciseCounter;
    private MediaPlayer player;

    private boolean isExercisePaused, isExerciseRestarted;
    int counter;

    private boolean exercise;
    private List<Integer> optionList;
    private OptionValuesWriteToJson optv;

    private Calendar calendar;
    private Context context;

    private RelativeLayout relLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_start, container, false);

        startButton = view.findViewById(R.id.start_button);
        pauseButton = view.findViewById(R.id.pause_button);
        restartButton = view.findViewById(R.id.replay_button);
        counterView = view.findViewById(R.id.counter_window);
        textViewStatus = view.findViewById(R.id.textView_status);
        howManyExercises = view.findViewById(R.id.exercise_counter);
        relLayout = view.findViewById(R.id.start_fragment);
        isExerciseRestarted = false;
        calendar = Calendar.getInstance();
        context = StartFragment.super.getContext();
        optv = new OptionValuesWriteToJson();

        if (isOptionFileExists()) {
            optionList = readOptions();
        } else {
            optionList = optv.createDefaultTimeValues();
        }

        setTime();
        start();
        pause();
        restart();

        return view;
    }

    private void start() {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExercisePaused = true) {
                    isExercisePaused = false;
                    player = MediaPlayer.create(getContext(), R.raw.cloche);
                    player.start();
                    startExercise();
                } else {
                    isExerciseRestarted = false;
                    startExercise();
                }
            }
        });
    }

    private void pause() {
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExercisePaused = true;
                setTextViewText(textViewStatus, "PAUSE");
            }
        });
    }

    private void restart() {
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExerciseRestarted = true;
            }
        });
    }


    private synchronized void startExercise() {
        counter = exerciseCounter;

        for (int i = 0; i < exerciseCounter; i++) {
            if (exercise) {
                startButton.setEnabled(false);
                timeLeftInMilliseconds = exerciseTime;
                startTimer();
                relLayout.setBackgroundResource(R.drawable.gradient_three);
                setTextViewText(howManyExercises, ("Il reste : " + exerciseCounter));
                setTextViewText(textViewStatus, "ACTION");
                exerciseCounter--;
                counter++;
                exercise = false;
                exerciseTime = optionList.get(0) * 1000;
                break;
            } else {
                timeLeftInMilliseconds = pauseTime;
                startTimer();
                relLayout.setBackgroundResource(R.drawable.gradient);
                setTextViewText(textViewStatus, "REPOS");
                exercise = true;
                pauseTime = optionList.get(1) * 1000;
                break;
            }
        }
        if (counter == 0) {
            setTextViewText(textViewStatus, "FIN");
            setTextViewText(howManyExercises, "");
            exerciseCounter = optionList.get(2);
            exercise = true;
            player = MediaPlayer.create(getContext(), R.raw.applause_2);
            player.start();
            Toast.makeText(StartFragment.super.getContext(), "Exercice terminé", Toast.LENGTH_SHORT).show();
            startButton.setEnabled(true);
            addExerciseToEventsDB();
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds + 100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (isExercisePaused) {
                    cancel();
                    setPauseStatus();
                } else {
                    timeLeftInMilliseconds = millisUntilFinished;
                    updateCounterView();
                }
                if (isExerciseRestarted) {
                    cancel();
                    setTime();
                    startExercise();
                    isExerciseRestarted = false;
                }
            }

            @Override
            public void onFinish() {
                timeLeftInMilliseconds = 0;
                updateCounterView();
                startExercise();
            }
        }.start();
    }


    private void setPauseStatus() {
        if (exercise) {
            exercise = false;
            pauseTime = timeLeftInMilliseconds;
        } else {
            exercise = true;
            exerciseCounter += 1;
            exerciseTime = timeLeftInMilliseconds;
        }
        startButton.setEnabled(true);
    }


    private void updateCounterView() {
        int minutes = (int) ((timeLeftInMilliseconds / 1000) / 60);
        int seconds = (int) ((timeLeftInMilliseconds / 1000) % 60);

        String timeFormated = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        setTextViewText(counterView, timeFormated);
    }

    private List<Integer> readOptions() {
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        return optv.optionsFromFile(sp.getString("OptionsJson", null));
    }

    private boolean isOptionFileExists() {
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        if (sp.contains("OptionsJson")) {
            return true;
        } else return false;
    }

    private void setTime() {
        exerciseTime = optionList.get(0) * 1000;
        pauseTime = optionList.get(1) * 1000;
        exerciseCounter = optionList.get(2);
        exercise = true;
    }

    private void setTextViewText(TextView textView, String text) {
        textView.setText(text);
    }

    private void addExerciseToEventsDB() {
        DBCalendarHelper db = new DBCalendarHelper(context);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf((calendar.get(Calendar.MONTH)));
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        Cursor cursor = db.showOneEventFromDB(day, month, year);
        if (!(cursor.moveToFirst()) || cursor.getCount() == 0) {
            db.SaveEventsToDB("Hiit", day, "1", month, year);
        } else {
            String id = cursor.getString(0);
            String eventsNumber = cursor.getString(3);
            int numberOfEventsInteger = Integer.parseInt(eventsNumber);
            numberOfEventsInteger += 1;
            db.updateEvent(id, String.valueOf(numberOfEventsInteger));
        }
        Toast.makeText(context, "Exercice terminé", Toast.LENGTH_SHORT).show();
        cursor.close();
        db.close();
    }
}
