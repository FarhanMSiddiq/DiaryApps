package com.tugas.zaky.diaryapps.Fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tugas.zaky.diaryapps.CreateNoteActivity
import com.tugas.zaky.diaryapps.R
import com.tugas.zaky.diaryapps.adapter.NoteAdapter
import com.tugas.zaky.diaryapps.database.NoteDatabase
import com.tugas.zaky.diaryapps.model.ModelNote
import com.tugas.zaky.diaryapps.utils.onClickItemListener
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() , onClickItemListener {

    private val modelNoteList: MutableList<ModelNote> = ArrayList()
    private var noteAdapter: NoteAdapter? = null
    private var onClickPosition = -1
    var rvListNote : RecyclerView? = null
    var layout_not_found : LinearLayout? = null
    var stringLayout = ""
    companion object {
        @JvmStatic
        fun newInstance(stringLayout: String) = HomeFragment().apply {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString("stringLayout", stringLayout)
            fragment.setArguments(args)
            return fragment
        }

        private const val REQUEST_ADD = 1
        private const val REQUEST_UPDATE = 2
        private const val REQUEST_SHOW = 3
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_home, container, false)
        rvListNote = view.findViewById(R.id.rvListNote!!)
        layout_not_found = view.findViewById(R.id.layout_notfound)

        arguments?.getString("stringLayout")?.let {
            stringLayout = it
        }

        view.fabCreateNote.setOnClickListener {
            startActivityForResult(Intent(view.context, CreateNoteActivity::class.java), REQUEST_ADD)
        }



        noteAdapter = NoteAdapter(modelNoteList, this , stringLayout)
        rvListNote!!.setAdapter(noteAdapter)

        Log.e("Status",stringLayout)

        if (stringLayout.equals("grid")){
            modeGrid()
        }else{
            modeList()
        }



        //get Data Catatan
        getNote(REQUEST_SHOW, false)



        return view
    }

    private fun modeGrid() {
        rvListNote!!.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun modeList() {
        rvListNote!!.layoutManager = LinearLayoutManager(context)
    }

    private fun getNote(requestCode: Int, deleteNote: Boolean ) {

        @Suppress("UNCHECKED_CAST")
        class GetNoteAsyncTask : AsyncTask<Void?, Void?, List<ModelNote>>() {

            override fun onPostExecute(notes: List<ModelNote>) {
                super.onPostExecute(notes)
                if (requestCode == REQUEST_SHOW) {
                    modelNoteList.addAll(notes)
                    noteAdapter?.notifyDataSetChanged()
                    if (notes.size>0){
                        layout_not_found!!.visibility = View.GONE
                        rvListNote!!.visibility = View.VISIBLE
                    }else{
                        layout_not_found!!.visibility = View.VISIBLE
                        rvListNote!!.visibility = View.GONE
                    }
                } else if (requestCode == REQUEST_ADD) {
                    modelNoteList.add(0, notes[0])
                    noteAdapter?.notifyItemInserted(0)
                    rvListNote!!.smoothScrollToPosition(0)
                    if (notes.size>0){
                        layout_not_found!!.visibility = View.GONE
                        rvListNote!!.visibility = View.VISIBLE
                    }else{
                        layout_not_found!!.visibility = View.VISIBLE
                        rvListNote!!.visibility = View.GONE
                    }
                } else if (requestCode == REQUEST_UPDATE) {
                    modelNoteList.removeAt(onClickPosition)
                    if (deleteNote) {
                        noteAdapter?.notifyItemRemoved(onClickPosition)
                    } else {
                        modelNoteList.add(onClickPosition, notes[onClickPosition])
                        noteAdapter?.notifyItemChanged(onClickPosition)
                    }
                    if (notes.size>0){
                        layout_not_found!!.visibility = View.GONE
                        rvListNote!!.visibility = View.VISIBLE
                    }else{
                        layout_not_found!!.visibility = View.VISIBLE
                        rvListNote!!.visibility = View.GONE
                    }
                }
            }

            override fun doInBackground(vararg params: Void?): List<ModelNote>? {
                return NoteDatabase.getInstance(context!!)!!.noteDao()!!.allNote as List<ModelNote>?
            }
        }
        GetNoteAsyncTask().execute()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD && resultCode == RESULT_OK) {
            getNote(REQUEST_ADD, false)
        } else if (requestCode == REQUEST_UPDATE && resultCode == RESULT_OK) {
            if (data != null) {
                getNote(REQUEST_UPDATE, data.getBooleanExtra("NoteDelete", false))
            }
        }
    }

    override fun onClick(modelNote: ModelNote, position: Int) {
        onClickPosition = position
        val intent = Intent(context, CreateNoteActivity::class.java)
        intent.putExtra("EXTRA", true)
        intent.putExtra("EXTRA_NOTE", modelNote)
        startActivityForResult(intent, REQUEST_UPDATE)
    }
}
