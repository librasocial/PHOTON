package com.ssf.homevisit.fragment.vhsnc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ssf.homevisit.databinding.FragmentMeetingListBinding


/**
 * A simple [Fragment] subclass.
 * Use the [MeetingListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MeetingListFragment : Fragment() {

    private lateinit var binding: FragmentMeetingListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMeetingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNewMeetingUI()
    }

    private fun initNewMeetingUI() {

    }

    companion object {
        @JvmStatic
        fun newInstance() = MeetingListFragment()
    }
}