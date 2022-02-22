package com.example.schedule_clone.presentation.schedule

sealed class ScheduleNavigationAction {
    object NavigationToSignInDialogAction : ScheduleNavigationAction()
    object NavigationToSignOutDialogAction : ScheduleNavigationAction( )
    object ShowScheduleUiHints : ScheduleNavigationAction()
}