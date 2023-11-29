package com.ssf.homevisit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.databinding.ItemScheduledMeetingBinding
import com.ssf.homevisit.models.Meeting

class MeetingsAdapter(val isVHND: Boolean = false, val clickListener: (meeting: Meeting, event: MeetingClickEvent)->Unit) :
    ListAdapter<Meeting, MeetingsAdapter.ItemViewholder>(DiffCallbackMeeting()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            ItemScheduledMeetingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewholder(val view: ItemScheduledMeetingBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(item: Meeting) = with(view) {
            meeting = item
            if(isVHND) {
                tvTopics.visibility = View.INVISIBLE
            }
            tvReSchedule.setOnClickListener {
                clickListener.invoke(item, MeetingClickEvent.RE_SCHEDULE_MEETING)
            }
            tvCancel.setOnClickListener {
                clickListener.invoke(item, MeetingClickEvent.CANCEL_MEETING)
            }
            root.setOnClickListener {
                clickListener.invoke(item, MeetingClickEvent.VIEW_MEETING)
            }
        }
    }
}

class DiffCallbackMeeting : DiffUtil.ItemCallback<Meeting>() {
    override fun areItemsTheSame(
        oldItem: Meeting,
        newItem: Meeting
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Meeting,
        newItem: Meeting
    ): Boolean {
        return oldItem == newItem
    }
}

enum class MeetingClickEvent(){
    VIEW_MEETING, RE_SCHEDULE_MEETING, CANCEL_MEETING
}