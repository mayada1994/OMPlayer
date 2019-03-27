package com.omplayer.app.dialogFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.omplayer.app.R

class LyricsDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val selectLanguageView = inflater.inflate(R.layout.lyrics_dialog, container, false)
        val lyrics: TextView = selectLanguageView.findViewById(R.id.tv_lyrics)
        lyrics.text = arguments?.getString("content")
        dialog.setCanceledOnTouchOutside(true)
        return selectLanguageView
    }

    companion object {

        fun newInstance(content: String): LyricsDialogFragment {
            val lyricsDialogFragment = LyricsDialogFragment()
            lyricsDialogFragment.setStyle(STYLE_NO_TITLE, 0)
            val args = Bundle()
            args.putString("content", content)
            lyricsDialogFragment.arguments = args
            return lyricsDialogFragment
        }
    }
}