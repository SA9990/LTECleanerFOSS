/*
 * SPDX-FileCopyrightText: 2020-2023 Hunter J Drum
 * SPDX-FileCopyrightText: 2024-2025 MDP43140
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package io.mdp43140.ltecleaner
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
//import io.mdp43140.ltecleaner.App
//import io.mdp43140.ltecleaner.Constants
class BootReceiver: BroadcastReceiver() {
	override fun onReceive(ctx: Context, i: Intent) {
		val constraints = Constraints.Builder()
			.setRequiresBatteryNotLow(true)
			.setRequiresDeviceIdle(true)
			.build()

		// Schedule the work hourly
		ScheduledWorker.enqueueWork(ctx)

		// Schedule the work at boot completed
		if (App.prefs!!.bootCleanup){
			val myWork = OneTimeWorkRequestBuilder<ScheduledWorker>()
				.addTag(Constants.BGCLEAN_WORK_TAG)
				.setConstraints(constraints)
				.setInitialDelay(1, TimeUnit.MINUTES)
				.build()
			WorkManager.getInstance(ctx).enqueueUniqueWork(
				Constants.BGCLEAN_WORK_NAME,
				ExistingWorkPolicy.REPLACE,
				myWork
			)
		}
	}
}
