package net.tassit.jobqueueselftest;

import android.app.Application;

import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;

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
public class JobApplication extends Application {

    private JobManager jobManager;
    private static JobApplication instance;

    public JobApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Configuration conf = new Configuration.Builder(this)
                .maxConsumerCount(3)
                .minConsumerCount(1)
                .loadFactor(3)
                .consumerKeepAlive(120)
                .id("Main Job manager")
                .build();
        jobManager = new JobManager(this, conf);
    }

    public JobManager getJobManager() {
        return jobManager;
    }

    public static JobApplication getInstance() {
        return instance;
    }
}
