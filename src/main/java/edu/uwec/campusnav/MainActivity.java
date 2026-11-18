package edu.uwec.campusnav;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import edu.uwec.campusnav.ui.CampusMapView;

/** when you guys build a screen, replace its notBuiltYet(...) call with e.g.
 *     startActivity(new Intent(this, RoomFinderActivity.class));
 * and register the new activity in AndroidManifest.xml.
 */
public class MainActivity extends BaseActivity {

    private static final String STATE_AVOID_STAIRS = "avoid_stairs";

    private ImageButton btnStairs;
    private boolean avoidStairs; // TODO: move to shared app state once routing exists

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            avoidStairs = savedInstanceState.getBoolean(STATE_AVOID_STAIRS, false);
        }

        CampusMapView mapView = findViewById(R.id.map_view);
        mapView.setUserLocation(CampusMapView.DEFAULT_START_X, CampusMapView.DEFAULT_START_Y); // TODO: real GPS
        btnStairs = findViewById(R.id.btn_accessibility);
        btnStairs.setSelected(avoidStairs);
        btnStairs.setOnClickListener(v -> toggleStairs());

        // TODO (hazard team): open the Hazard Report screen
        findViewById(R.id.btn_report_hazard).setOnClickListener(v -> notBuiltYet("Hazard Report"));

        // TODO (room finder team): open the Room Finder screen
        findViewById(R.id.btn_find_room).setOnClickListener(v -> notBuiltYet("Room Finder"));

        // TODO (schedule team): open the Schedule Mapper screen
        findViewById(R.id.btn_schedule).setOnClickListener(v -> notBuiltYet("Schedule Mapper"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_AVOID_STAIRS, avoidStairs);
    }

    private void toggleStairs() {
        avoidStairs = !avoidStairs;
        btnStairs.setSelected(avoidStairs); // filled button = on
        Toast.makeText(this,
                avoidStairs ? R.string.toast_accessibility_on : R.string.toast_accessibility_off,
                Toast.LENGTH_SHORT).show();
    }

    /** Placeholder until the screen exists. */
    private void notBuiltYet(String screenName) {
        Toast.makeText(this, screenName + " isn't built yet", Toast.LENGTH_SHORT).show();
    }
}