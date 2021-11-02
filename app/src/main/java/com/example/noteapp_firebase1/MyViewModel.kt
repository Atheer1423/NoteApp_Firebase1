package com.example.noteapp_firebase1

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyViewModel(activity: Application):AndroidViewModel(activity) {
    var livedataNote = MutableLiveData<ArrayList<Note>>()
    val db = FirebaseFirestore.getInstance()

    init {
        livedataNote = MutableLiveData()
        getNote()
    }

    fun getNote() {
        db.collection("Notes")
            .get()
            .addOnSuccessListener { result ->
                var details = arrayListOf<Note>()
                for (document in result) {
                    Log.d("s", "{${document.id}, ${document.data}")
                    document.data.map { (key, value) ->
                        if (key == "note") {
                            var note = "$value"
                            details.add(Note(document.id, note))
                        }
                    }
                }
                livedataNote.postValue(details)

            }
            .addOnFailureListener {
                Log.d("f", "failure")
            }
    }

    fun getList(): LiveData<ArrayList<Note>> {
        return livedataNote
    }

    fun addNote(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            db.collection("Notes")
                .add(note)
                .addOnSuccessListener { res ->
                    Log.d("save",
                        "DocumentSnapshot added with ID: " + res.id)
                    getNote()
                }
        }
    }

    fun update(note: Note) {
        db.collection("Notes").document(note.id).update(mapOf(
            "note" to (note.note)
        ))
        getNote()
    }

    fun RemoveNote(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            db.collection("Notes").document(note.id).delete()
            getNote()
        }

    }
}
