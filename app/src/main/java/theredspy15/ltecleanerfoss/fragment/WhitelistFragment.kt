/*
 * SPDX-FileCopyrightText: 2020-2023 Hunter J Drum
 * SPDX-FileCopyrightText: 2024-2025 MDP43140
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package theredspy15.ltecleanerfoss.fragment
import android.content.DialogInterface
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts.OpenDocumentTree
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import theredspy15.ltecleanerfoss.R
import theredspy15.ltecleanerfoss.App
import theredspy15.ltecleanerfoss.Constants
import theredspy15.ltecleanerfoss.MainActivity
import theredspy15.ltecleanerfoss.databinding.FragmentWhitelistBinding
class WhitelistFragment: BaseFragment(){
	private lateinit var binding: FragmentWhitelistBinding
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentWhitelistBinding.inflate(inflater, container, false)
		binding.addBtn.setOnClickListener {
			// Creates a dialog asking for a file/folder name to add to the whitelist
			mGetContent.launch(Uri.fromFile(Environment.getDataDirectory()))
		}
		binding.resetBtn.setOnClickListener {
			getWhiteList(App.prefs)
			getWhitelistOn(App.prefs)
			for (i in Constants.whitelistDefault){
				if (!whiteList.contains(i))
					whiteList.add(i)
			}
			for (i in Constants.whitelistOnDefault){
				if (!whiteListOn.contains(i))
					whiteListOn.add(i)
			}
			App.prefs!!
				.edit()
				.putStringSet("whitelist", HashSet(whiteList))
				.putStringSet("whitelistOn", HashSet(whiteListOn))
				.apply()
			loadViews()
		}
		getWhiteList(App.prefs)
		getWhitelistOn(App.prefs)
		binding.pathsLayout.setItemViewCacheSize(1)
		val adapter = ListItemAdapter(requireActivity(), false)
		adapter.list = whiteList
		adapter.onItemClick = ::removePath
		binding.pathsLayout.adapter = adapter
		binding.pathsLayout.addItemDecoration(ListItemAdapter.VerticalSpaceItemDecoration())
		binding.pathsLayout.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
		loadViews()
		return binding.root
	}
	private fun loadViews() {
		requireActivity().runOnUiThread {
			binding.pathsLayout.adapter!!.notifyDataSetChanged()
		}
	}
	private fun removePath(path: String) {
		MaterialAlertDialogBuilder(requireContext())
			.setTitle(getString(R.string.remove_from_whitelist))
			.setMessage(path)
			.setPositiveButton(getString(R.string.delete)){ dialog:DialogInterface, _:Int ->
				rmWhiteList(App.prefs,path)
				dialog.dismiss()
				loadViews()
			}
			.setNegativeButton(getString(android.R.string.cancel)) { dialog:DialogInterface, _:Int ->
				dialog.dismiss()
			}
			.show()
	}

	/**
	 * Prepare a dialog that asks for a file/folder name to add to the whitelist
	 */
	private var mGetContent = registerForActivityResult(OpenDocumentTree()) { uri: Uri? ->
		if (uri != null){
			val path: String = uri.path!!.substring(uri.path!!.indexOf(":") + 1)
			addWhiteList(App.prefs, "/storage/emulated/0/" + path)
			loadViews()
		}
	}
	companion object {
		var whiteList: ArrayList<String> = ArrayList()
		var whiteListOn: ArrayList<String> = ArrayList()
		fun getWhiteList(prefs: SharedPreferences?): List<String?> {
			if (whiteList.isNullOrEmpty() && prefs != null) {
				// Java type mismatch: inferred type is
				// kotlin.collections.(Mutable)Set<kotlin.String!>?, but
				// kotlin.collections.(Mutable)Collection<out kotlin.String!> was expected
				whiteList = ArrayList(prefs.getStringSet("whitelist",Constants.whitelistDefault))
				whiteList.remove("[")
				whiteList.remove("]")
			}
			return whiteList
		}
		fun addWhiteList(prefs: SharedPreferences?, path: String) {
			if (whiteList.isNullOrEmpty()) getWhiteList(prefs)
			if (whiteListOn.isNullOrEmpty()) getWhitelistOn(prefs)
			whiteList.apply {
				add(path)
				distinct()
				sort()
			}
			whiteListOn.apply {
				add(path)
				distinct()
				sort()
			}
			prefs!!
				.edit()
				.putStringSet("whitelist", HashSet(whiteList))
				.putStringSet("whitelistOn", HashSet(whiteListOn))
				.apply()
		}
		fun rmWhiteList(prefs: SharedPreferences?, path: String) {
			if (whiteList.isNullOrEmpty()) getWhiteList(prefs)
			if (whiteListOn.isNullOrEmpty()) getWhitelistOn(prefs)
			whiteList.remove(path)
			whiteListOn.remove(path)
			prefs!!
				.edit()
				.putStringSet("whitelist", HashSet(whiteList))
				.putStringSet("whitelistOn", HashSet(whiteListOn))
				.apply()
		}
		fun getWhitelistOn(prefs: SharedPreferences?): List<String?> {
			if (whiteListOn.isNullOrEmpty() && prefs != null) {
				whiteListOn = ArrayList(prefs.getStringSet("whitelistOn",Constants.whitelistOnDefault))
			}
			return whiteListOn
		}
		fun setWhitelistOn(prefs: SharedPreferences?, path: String, checked: Boolean) {
			whiteListOn.apply {
				if (isNullOrEmpty()) getWhitelistOn(prefs)
				if (checked){
					if (!contains(path)) add(path)
					distinct()
					sort()
				} else {
					remove(path)
				}
			}
			prefs!!
				.edit()
				.putStringSet("whitelistOn",HashSet(whiteListOn))
				.apply()
		}
	}
}
