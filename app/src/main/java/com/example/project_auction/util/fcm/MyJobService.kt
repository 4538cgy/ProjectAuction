package com.example.project_auction.util.fcm

import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService

class MyJobService : JobService() {
    override fun onStartJob(jobParameters: JobParameters): Boolean {
        //작업이 실행되기 시작했음을 나타 내기 위해 호출됩니다.
        // TODO(developer): add long running task here.
        return false
    }

    override fun onStopJob(jobParameters: JobParameters): Boolean {
        return false
        //이 메서드는 호출 기회가 있기 전에도 작업 실행을 중지해야한다고 시스템에서 결정한 경우 호출 jobFinished(JobParameters, boolean)됩니다.
    }

    companion object {

        private const val TAG = "MyJobService"
    }
}