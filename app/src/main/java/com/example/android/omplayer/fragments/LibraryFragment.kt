package com.example.android.omplayer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.android.omplayer.R
import com.example.android.omplayer.activities.MainActivity
import com.google.android.material.tabs.TabLayout
import com.example.android.omplayer.adapters.LibraryAdapter


class LibraryFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity)
            .setActionBarTitle("Library")
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        val view = inflater.inflate(R.layout.fragment_library, container, false)

        // Setting ViewPager for each Tabs
        val viewPager = view.findViewById(R.id.viewpager) as ViewPager
        setupViewPager(viewPager)
        // Set Tabs inside Toolbar
        val tabs = view.findViewById(R.id.result_tabs) as TabLayout
        tabs.setupWithViewPager(viewPager)
        return view

    }

    private fun setupViewPager(viewPager: ViewPager) {

        val adapter = LibraryAdapter(childFragmentManager)
        adapter.addFragment(ArtistFragment(), getString(R.string.artists))
        adapter.addFragment(AlbumFragment(), getString(R.string.albums))
        adapter.addFragment(TrackFragment(), getString(R.string.tracklist))
        adapter.addFragment(GenreFragment(), getString(R.string.genres))

        viewPager.adapter = adapter

    }
}