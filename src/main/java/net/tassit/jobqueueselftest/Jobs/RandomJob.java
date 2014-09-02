package net.tassit.jobqueueselftest.Jobs;

import android.util.Log;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import net.tassit.jobqueueselftest.Events.JobDidFinishEvent;

import java.util.Random;

import de.greenrobot.event.EventBus;

/**
 * Copyright 2014 Cody Huzarski (chuzarski@gmail.com)
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class RandomJob extends Job {

    private static final String logtag = "RandomJob - ";
    private Random rand;
    private int sleepSeconds;
    private boolean isSleeping;
    private long startTime;

    public RandomJob(Params params) {
        super(params);
    }

    @Override
    public void onAdded() {
        rand = new Random();
        Log.d(logtag, "Got the sleep seconds");
        sleepSeconds = rand.nextInt(10);
        isSleeping = true;
    }

    @Override
    public void onRun() throws Throwable {
        startTime = System.currentTimeMillis();
        int timeElapsed = 0;
        while (isSleeping) {
            timeElapsed = (int) (System.currentTimeMillis() - startTime) / 1000;
            if(timeElapsed > sleepSeconds) {
                isSleeping = false;
            }
        }
        Log.d(logtag, String.format("Went to sleep for %d seconds", timeElapsed));
        EventBus.getDefault().post(new JobDidFinishEvent(timeElapsed));
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
