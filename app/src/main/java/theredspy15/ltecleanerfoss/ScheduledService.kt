/*
 * (C) 2020-2023 Hunter J Drum
 * (C) 2023 MDP43140
 */
package theredspy15.ltecleanerfoss
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.WorkManager
import theredspy15.ltecleanerfoss.controllers.MainActivity.Companion.convertSize
import java.util.concurrent.TimeUnit
class ScheduledWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {
	override fun doWork(): Result {
		try {
			val path = Environment.getExternalStorageDirectory()
			val prefs = PreferenceManager.getDefaultSharedPreferences(
				applicationContext
			)

			// scanner setup
			val fs = FileScanner(path, applicationContext)
				.setEmptyDir(prefs.getBoolean("empty", false))
				.setAutoWhite(prefs.getBoolean("auto_white", true))
				.setDelete(true)
				.setCorpse(prefs.getBoolean("corpse", false))
				.setGUI(null)
				.setContext(applicationContext)
				.setUpFilters(
					prefs.getBoolean("generic", true),
					prefs.getBoolean("aggressive", false),
					prefs.getBoolean("apk", false)
				)

			// kilobytes found/freed text
			val kilobytesTotal = fs.startScan()
			val title =
				applicationContext.getString(R.string.clean_notification) + " " + convertSize(
					kilobytesTotal
				)
			makeStatusNotification(title, applicationContext)
			return Result.success()
		} catch (e: Exception) {
			makeStatusNotification(e.toString(), applicationContext)
			return Result.failure()
		}
	}
	companion object {
		private const val UNIQUE_WORK_NAME = "scheduled_cleanup_work"
		private const val WORK_TAG = "cleanup_work_tag"
		@JvmStatic
		fun enqueueWork(context: Context) {
			val constraints = Constraints.Builder()
				.setRequiredNetworkType(NetworkType.UNMETERED)
				.build()
			val myWork = OneTimeWorkRequestBuilder<ScheduledWorker>()
				.setConstraints(constraints)
				.addTag(WORK_TAG)
				.setInitialDelay(1, TimeUnit.HOURS)
				.build()
			WorkManager.getInstance(context).enqueueUniqueWork(
				UNIQUE_WORK_NAME,
				ExistingWorkPolicy.REPLACE,
				myWork
			)
		}

		fun makeStatusNotification(message: String?, context: Context) {

			// Name of Notification Channel for verbose notifications of background work
			val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence =
				context.getString(R.string.settings_notification_name)
			val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
				context.getString(R.string.settings_notification_sum)
			val NOTIFICATION_TITLE: CharSequence = context.getString(R.string.notification_title)
			val CHANNEL_ID = "VERBOSE_NOTIFICATION"
			val NOTIFICATION_ID = 1

			// Make a channel if necessary
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				// Create the NotificationChannel, but only on API 26+ because
				// the NotificationChannel class is new and not in the support library
				val importance = NotificationManager.IMPORTANCE_DEFAULT
				val channel =
					NotificationChannel(CHANNEL_ID, VERBOSE_NOTIFICATION_CHANNEL_NAME, importance)
				channel.description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION

				// Add the channel
				val notificationManager =
					context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
				notificationManager.createNotificationChannel(channel)
			}

			// Create the notification
			val builder = NotificationCompat.Builder(context, CHANNEL_ID)
				.setSmallIcon(R.drawable.ic_baseline_cleaning_services_24)
				.setContentTitle(NOTIFICATION_TITLE)
				.setContentText(message)
				.setAutoCancel(true)
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
				.setVibrate(LongArray(0))

			// Show the notification
			NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
		}
	}
}