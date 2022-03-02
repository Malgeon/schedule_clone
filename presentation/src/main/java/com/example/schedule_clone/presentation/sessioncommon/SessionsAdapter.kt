package com.example.schedule_clone.presentation.sessioncommon

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.schedule_clone.model.userdata.UserSession
import com.example.schedule_clone.presentation.databinding.ItemSessionBinding
import kotlinx.coroutines.flow.StateFlow
import java.time.ZoneId

class SessionsAdapter(
    private val tagViewPool: RecycledViewPool,
    private val showReservations: StateFlow<Boolean>,
    private val timeZoneId: StateFlow<ZoneId>,
    private val lifecycleOwner: LifecycleOwner,
    private val onSessionClickListener: OnSessionClickListener,
    private val onSessionStartClickListener: OnSessionStarClickListener
) : ListAdapter<UserSession, SessionViewHolder>(SessionDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val binding = ItemSessionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).apply {
            tags.apply {
                setRecycledViewPool(tagViewPool)
                layoutManager = FlexboxLayoutManager
            }
        }
    }
}

class SessionViewHolder(
    internal val binding: ItemSessionBinding
) : ViewHolder(binding.root)

object SessionDiff : DiffUtil.ItemCallback<UserSession>() {
    override fun areItemsTheSame(oldItem: UserSession, newItem: UserSession): Boolean {
        // We don't have to compare the userEvent id because it matches the session id.
        return oldItem.session.id == newItem.session.id
    }

    override fun areContentsTheSame(oldItem: UserSession, newItem: UserSession): Boolean {
        return oldItem == newItem
    }
}