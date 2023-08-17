/*
 * (C) 2020-2023 Hunter J Drum
 * (C) 2023 MDP43140
 */
package theredspy15.ltecleanerfoss.controllers
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.CheckBoxPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import theredspy15.ltecleanerfoss.ScheduledWorker.Companion.enqueueWork
import theredspy15.ltecleanerfoss.R
class SettingsActivity: AppCompatActivity(){
	override fun onCreate(savedInstanceState:Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_settings)
		supportFragmentManager.beginTransaction()
			.replace(R.id.layout, MyPreferenceFragment())
			.commit()
	}

	class MyPreferenceFragment: PreferenceFragmentCompat() {
		override fun onCreate(savedInstanceState: Bundle?) {
			super.onCreate(savedInstanceState)
			setHasOptionsMenu(true)
			findPreference<Preference>("aggressive")!!.onPreferenceChangeListener =
				Preference.OnPreferenceChangeListener { _:Preference, value:Any? ->
					val isChecked = (value as Boolean)
					if (isChecked){
						val filtersFiles = resources.getStringArray(R.array.aggressive_filter_folders)
						val alertDialog = AlertDialog.Builder(requireContext()).create()
						alertDialog.setTitle(getString(R.string.aggressive_filter_what_title))
						alertDialog.setMessage(
							getString(R.string.adds_the_following) + " " + filtersFiles.contentToString()
						)
						alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"OK"){ dialog:DialogInterface, _:Int -> dialog.dismiss() }
						alertDialog.show()
					}
					true
				}
			findPreference<Preference>("cleanevery")!!.onPreferenceChangeListener =
				Preference.OnPreferenceChangeListener { _:Preference, _:Any? ->
					enqueueWork(requireContext().applicationContext)
					true
				}
			findPreference<Preference>("theme")!!.onPreferenceChangeListener =
				Preference.OnPreferenceChangeListener { _:Preference, value:Any? ->
					val light = resources.getStringArray(R.array.themes)[1]
					val dark = resources.getStringArray(R.array.themes)[2]
					if (value == dark) { // dark
						AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
					} else if (value == light) { // light
						AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
					} else { // auto
						AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
					}
					true
				}
		}

		/**
		 * Inflate Preferences
		 */
		override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
			setPreferencesFromResource(R.xml.preferences,rootKey)
		}
	}
}