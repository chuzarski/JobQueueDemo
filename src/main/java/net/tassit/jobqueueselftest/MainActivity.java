package net.tassit.jobqueueselftest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.Params;

import net.tassit.jobqueueselftest.Events.JobDidFinishEvent;
import net.tassit.jobqueueselftest.Jobs.RandomJob;

import de.greenrobot.event.EventBus;

import static android.view.View.OnClickListener;


public class MainActivity extends Activity {

    public static final String logtag = "MainActivity - ";

    private Button mainButton;
    private ProgressBar progress;
    private TextView indicator;

    private EventHandler eHandler;
    private EventBus events;
    private JobManager jobs;

    private boolean uiIsDirty = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eHandler = new EventHandler();

        //wire the UI
        mainButton = (Button) findViewById(R.id.main_button);
        mainButton.setOnClickListener(eHandler);

        indicator = (TextView) findViewById(R.id.finished_indicator);
        progress = (ProgressBar) findViewById(R.id.progress_bar);

        //fetch the EventBus and JobManager
        events = EventBus.getDefault();
        jobs = JobApplication.getInstance().getJobManager();
        events.register(this);

        //that should be it
        Log.d(logtag, "Application Ready");
    }


    //--------------------------------
    //UI Methods
    //--------------------------------

    /**
     * Method to update the UI when a job is started
     */
    private void uiUpdateForJobStart() {
        mainButton.setClickable(false);
        mainButton.setText(getResources().getString(R.string.button_inactive));

        progress.setVisibility(View.VISIBLE);
        progress.setMax(10);
        progress.setProgress(8);

        if(uiIsDirty) {
            indicator.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * Method to update the UI for when a job is completed.
     * @param sleepSeconds number of seconds the job slept. Display purposes :)
     */
    private void uiUpdateForJobComplete(int sleepSeconds) {
        mainButton.setClickable(true);
        mainButton.setText(getResources().getString(R.string.button_string));

        progress.setVisibility(View.INVISIBLE);

        //display the indicator
        indicator.setText(getResources().getString(R.string.completed_string, sleepSeconds));
        indicator.setVisibility(View.VISIBLE);
        uiIsDirty = true;
    }

    private void uiShowAboutMessage() {
        uiIsDirty = true;

        //set our message
        indicator.setText(getResources().getString(R.string.about_string));
        indicator.setVisibility(View.VISIBLE);

    }

    //--------------------------------
    //ActionBar
    //--------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_about) {
            uiShowAboutMessage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //--------------------------------
    //Jobqueue Methods
    //--------------------------------

    /**
     * Method that adds a Random(Sleep) job to JobQueue
     */
    private void createRandomJob() {
        jobs.addJob(new RandomJob(new Params(1)));
    }

    //--------------------------------
    //EventBus
    //--------------------------------

    /**
     * EventBus will call this method when the RandomJob posts a JobDidFinishEvent
     * @param event The event object that was posted/caught
     */
    public void onEventMainThread(JobDidFinishEvent event) {
        Log.d(logtag, "JobDidFinishEvent caught");

        //cool beans, let the UI in on the secret
        uiUpdateForJobComplete(event.getTime());
    }

    //--------------------------------
    //EventHandler
    //--------------------------------
    private class EventHandler implements OnClickListener {
        @Override
        public void onClick(View v) {
            //update the UI and create the job
            uiUpdateForJobStart();
            createRandomJob();
        }
    }
}
