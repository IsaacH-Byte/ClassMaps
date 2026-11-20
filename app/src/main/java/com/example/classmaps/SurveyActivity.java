package com.example.classmaps;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// SurveyActivity manages both the Survey screen and the Hazard Report screen
public class SurveyActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean isOnHazardScreen = false;

    // Survey screen views and state
    private Button[] starButtons;
    private Switch obstacleSwitch;
    private Switch roomSwitch;
    private int selectedRating = 0;

    // Hazard report screen views and state
    private TextView selectedHazardTypeText;
    private Button[] hazardOptionButtons;
    private String selectedHazardType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showSurveyScreen();
    }

    // Loads the survey layout and binds click listeners
    private void showSurveyScreen() {
        isOnHazardScreen = false;
        setContentView(R.layout.activity_survey);
        selectedRating = 0;

        starButtons = new Button[] {
                (Button) findViewById(R.id.star_1),
                (Button) findViewById(R.id.star_2),
                (Button) findViewById(R.id.star_3),
                (Button) findViewById(R.id.star_4),
                (Button) findViewById(R.id.star_5)
        };

        for (int i = 0; i < starButtons.length; i++) {
            starButtons[i].setOnClickListener(this);
        }

        // Store Switch references so we can read .isChecked() directly on Submit (works for tap & drag)
        obstacleSwitch = (Switch) findViewById(R.id.obstacle_switch);
        roomSwitch = (Switch) findViewById(R.id.room_switch);

        Button submitButton = (Button) findViewById(R.id.submit_button);
        Button maybeLaterButton = (Button) findViewById(R.id.maybe_later_button);
        submitButton.setOnClickListener(this);
        maybeLaterButton.setOnClickListener(this);
    }

    // Loads hazard report layout and binds click listeners
    private void showHazardReportScreen() {
        isOnHazardScreen = true;
        setContentView(R.layout.activity_hazard_report);

        selectedHazardType = "";
        selectedHazardTypeText = (TextView) findViewById(R.id.selected_hazard_type_text);

        Button backButton = (Button) findViewById(R.id.back_button);
        Button selectLocationButton = (Button) findViewById(R.id.select_location_button);
        backButton.setOnClickListener(this);
        selectLocationButton.setOnClickListener(this);

        hazardOptionButtons = new Button[] {
                (Button) findViewById(R.id.hazard_ice),
                (Button) findViewById(R.id.hazard_construction),
                (Button) findViewById(R.id.hazard_closure),
                (Button) findViewById(R.id.hazard_obstacle),
                (Button) findViewById(R.id.hazard_wet),
                (Button) findViewById(R.id.hazard_car)
        };

        for (int i = 0; i < hazardOptionButtons.length; i++) {
            hazardOptionButtons[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // --- Survey Screen Events ---
        if (id == R.id.star_1) {
            updateRating(1);
        } else if (id == R.id.star_2) {
            updateRating(2);
        } else if (id == R.id.star_3) {
            updateRating(3);
        } else if (id == R.id.star_4) {
            updateRating(4);
        } else if (id == R.id.star_5) {
            updateRating(5);
        } else if (id == R.id.submit_button) {
            // OFF (left) = No (false), ON (right) = Yes (true)
            boolean encounteredObstacle = obstacleSwitch.isChecked();
            boolean foundRoomEasily = roomSwitch.isChecked();

            String yesText = getString(R.string.yes_label);
            String noText = getString(R.string.no_label);
            String summary = "Rating: " + selectedRating + "/5"
                    + " | Obstacle: " + (encounteredObstacle ? yesText : noText)
                    + " | Found room: " + (foundRoomEasily ? yesText : noText);
            Toast.makeText(this, summary, Toast.LENGTH_SHORT).show();

            // Open Hazard Report screen if the user switched "Encounter obstacle?" to Yes
            if (encounteredObstacle) {
                showHazardReportScreen();
            } else {
                finish();
            }
        } else if (id == R.id.maybe_later_button) {
            Toast.makeText(this, getString(R.string.survey_dismissed_msg), Toast.LENGTH_SHORT).show();
            finish();

            // --- Hazard Report Screen Events ---
        } else if (id == R.id.back_button) {
            showSurveyScreen();
        } else if (id == R.id.hazard_ice) {
            selectHazardType(R.string.hazard_ice, (Button) v);
        } else if (id == R.id.hazard_construction) {
            selectHazardType(R.string.hazard_construction, (Button) v);
        } else if (id == R.id.hazard_closure) {
            selectHazardType(R.string.hazard_closure, (Button) v);
        } else if (id == R.id.hazard_obstacle) {
            selectHazardType(R.string.hazard_obstacle, (Button) v);
        } else if (id == R.id.hazard_wet) {
            selectHazardType(R.string.hazard_wet, (Button) v);
        } else if (id == R.id.hazard_car) {
            selectHazardType(R.string.hazard_car, (Button) v);
        } else if (id == R.id.select_location_button) {
            if (selectedHazardType.equals("")) {
                Toast.makeText(this, getString(R.string.please_select_hazard_msg), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.select_location_msg_prefix) + selectedHazardType, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Pressing the phone's system Back button on Hazard Report returns to the Survey screen
    @SuppressWarnings("deprecation")
    @Override
    public void onBackPressed() {
        if (isOnHazardScreen) {
            showSurveyScreen();
        } else {
            super.onBackPressed();
        }
    }

    // Updates star buttons between "★" (selected) and "☆" (unselected)
    private void updateRating(int rating) {
        selectedRating = rating;
        for (int i = 0; i < starButtons.length; i++) {
            if (i < rating) {
                starButtons[i].setText(getString(R.string.star_selected));
            } else {
                starButtons[i].setText(getString(R.string.star_unselected));
            }
        }
    }

    // Updates the "..." text box and highlights the chosen hazard button
    private void selectHazardType(int stringResId, Button selectedButton) {
        selectedHazardType = getString(stringResId);
        selectedHazardTypeText.setText(selectedHazardType);

        for (int i = 0; i < hazardOptionButtons.length; i++) {
            hazardOptionButtons[i].setTextColor(Color.BLACK);
            hazardOptionButtons[i].setBackgroundColor(Color.TRANSPARENT);
        }
        selectedButton.setTextColor(Color.parseColor("#3F51B5"));
        selectedButton.setBackgroundColor(Color.parseColor("#E0E0E0"));
    }
}