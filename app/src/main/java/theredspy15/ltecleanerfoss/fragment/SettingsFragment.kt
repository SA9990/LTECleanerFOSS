/*
 * SPDX-FileCopyrightText: 2020-2023 Hunter J Drum
 * SPDX-FileCopyrightText: 2024-2025 MDP43140
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package theredspy15.ltecleanerfoss.fragment
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis
import io.mdp43140.ael.ErrorLogger
import io.mdp43140.ltecleanerfoss.util.putData // SharedPreferencesExtension.kt
import org.json.JSONArray
import org.json.JSONObject
import theredspy15.ltecleanerfoss.App
import theredspy15.ltecleanerfoss.CleanupService
import theredspy15.ltecleanerfoss.CommonFunctions
import theredspy15.ltecleanerfoss.MainActivity
import theredspy15.ltecleanerfoss.ScheduledWorker.Companion.enqueueWork
import theredspy15.ltecleanerfoss.R
class SettingsFragment: PreferenceFragmentCompat(){
	private val importFileLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
		if (uri != null){
			val jsonObject = JSONObject(
				requireContext().contentResolver.openInputStream(uri)
					?.use { it.readBytes() }
					!!.toString(Charsets.UTF_8)
			)
			val prefsEditor = App.prefs?.edit()
			val buffer = StringBuilder();
			for (key in jsonObject.keys()){
				val value = jsonObject.get(key)
				if (prefsEditor?.putData(key,value) == false)
					buffer.append("\n- $key: $value")
			}
			prefsEditor?.apply()
			val text = buffer.toString()
			Snackbar.make(
				(requireActivity() as MainActivity).binding.root,
				if (text == "") "Settings imported!" else "Unsupported data type:${text}",
				Snackbar.LENGTH_SHORT
			).let {
				it.setAction(getString(android.R.string.ok)){ _: View ->
					it.dismiss()
				}
				it.show()
			}
			(requireActivity() as MainActivity).startFragment(SettingsFragment())
		}
	}
	private val exportFileLauncher = registerForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
		if (uri != null){
			// TODO - warning - Type mismatch: inferred type is (Mutable)Map<String!, *>? but (MutableMap<Any?, Any?>..Map<*, *>) was expected
			val jsonData: String = JSONObject(App.prefs?.all).toString(2)
			CommonFunctions.writeContentToUri(requireContext(), uri, jsonData);
			Snackbar.make(
				(requireActivity() as MainActivity).binding.root,
				"Settings exported!",
				Snackbar.LENGTH_SHORT
			).let {
				it.setAction(getString(android.R.string.ok)){ _: View ->
					it.dismiss()
				}
				it.show()
			}
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true) // deprecated, newer api dont need boolean, but compile error
		enterTransition   = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true )
		returnTransition  = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
		exitTransition    = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true )
		reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
		findPreference<Preference>("blacklist")!!.setOnPreferenceClickListener {
			(requireActivity() as MainActivity).startFragment(BlacklistFragment())
			false
		}
		findPreference<Preference>("whitelist")!!.setOnPreferenceClickListener {
			(requireActivity() as MainActivity).startFragment(WhitelistFragment())
			false
		}
		findPreference<Preference>("cleanevery")!!.setOnPreferenceChangeListener { _:Preference, _:Any? ->
			enqueueWork(requireContext().applicationContext)
			true
		}
		findPreference<Preference>("theme")!!.setOnPreferenceChangeListener { _:Preference, value:Any? ->
			val themeStr = resources.getStringArray(R.array.themes_key)
			AppCompatDelegate.setDefaultNightMode(when (value){
				themeStr[1] -> AppCompatDelegate.MODE_NIGHT_NO
				themeStr[2] -> AppCompatDelegate.MODE_NIGHT_YES
				else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
			})
			true
		}
		findPreference<Preference>("dataImport")!!.setOnPreferenceClickListener {
			importFileLauncher.launch(arrayOf("application/json"))
			false
		}
		findPreference<Preference>("dataExport")!!.setOnPreferenceClickListener {
			exportFileLauncher.launch("LTECleaner_settings.json")
			false
		}
	}
	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.preferences,rootKey)
	}
}