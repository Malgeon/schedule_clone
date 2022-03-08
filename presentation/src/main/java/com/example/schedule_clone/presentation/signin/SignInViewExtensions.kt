package com.example.schedule_clone.presentation.signin

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LifecycleOwner
import com.example.schedule_clone.presentation.MainActivityViewModel
import com.example.schedule_clone.presentation.R

fun Toolbar.setupProfileMenuIte(
    viewModel: MainActivityViewModel,
    lifecycleOwner: LifecycleOwner
) {
    inflateMenu(R.menu.profile)
    val profileItem = menu.findItem(R.id.action_profile) ?: return
    profileItem.setOnMenuItemClickListener {
        viewModel.onProfileClicked()
        true
    }

}