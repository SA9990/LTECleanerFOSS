package io.mdp43140.baselineprofile
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
/**
 * This test class generates a basic startup baseline profile for the target package.
 *
 * We recommend you start with this but add important user flows to the profile to improve their performance.
 * Refer to https://d.android.com/topic/performance/baselineprofiles
 * for more info.
 *
 * You can run the generator with the "Generate Baseline Profile" run configuration in Android Studio or
 * the equivalent gradle task:
 * gradle :app:generateReleaseBaselineProfile
 *
 * Check https://d.android.com/topic/performance/benchmarking/macrobenchmark-instrumentation-args
 * for more information about available instrumentation arguments.
 *
 * After you run the generator, you can verify the improvements running the [StartupBenchmarks] benchmark.
 *
 * When using this class to generate a baseline profile, only API 33+ or rooted API 28+ are supported.
 *
 * The minimum required version of androidx.benchmark to generate a baseline profile is 1.2.0.
 **/
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

	@get:Rule
	val rule = BaselineProfileRule()

	@Test
	fun generate() {
		// The application id for the running build variant is read from the instrumentation arguments.
		rule.collect(
			packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
				?: throw Exception("targetAppId not passed as instrumentation runner arg"),

			// See: https://d.android.com/topic/performance/baselineprofiles/dex-layout-optimizations
			includeInStartupProfile = true
		) {
			// This block defines the app's critical user journey. Here we are interested in
			// optimizing for app startup. But you can also navigate and scroll through your most important UI.

			// Start default activity for your app
			startActivityAndWait()
			// Check UiAutomator documentation for more information how to interact with the app.
			// https://d.android.com/training/testing/other-components/ui-automator
		}
	}
}
