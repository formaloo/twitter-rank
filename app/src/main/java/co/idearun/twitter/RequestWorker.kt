package co.idearun.twitter

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import co.idearun.twitter.feature.viewmodel.TwitterViewModel
import org.koin.android.viewmodel.compat.ViewModelCompat.viewModel

class RequestWorker(val context: Context, val workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        TODO("Not yet implemented")
    }


}