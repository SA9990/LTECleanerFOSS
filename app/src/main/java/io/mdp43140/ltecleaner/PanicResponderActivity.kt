/*
 * SPDX-FileCopyrightText: 2024-2025 MDP43140
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package io.mdp43140.ltecleaner
import android.app.Activity
import android.content.Intent
import android.os.Bundle
class PanicResponderActivity: Activity(){
	override fun onCreate(savedInstanceState:Bundle?){
		super.onCreate(savedInstanceState)
		applicationContext.startForegroundService(
			Intent(applicationContext,CleanupService::class.java)
		)
		finishAndRemoveTask()
	}
}
