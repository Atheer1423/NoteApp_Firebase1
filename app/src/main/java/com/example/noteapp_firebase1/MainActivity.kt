package com.example.noteapp_firebase1

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var edNote: EditText
    lateinit var btnsave: Button
    val myViewM by lazy { ViewModelProvider(this).get(MyViewModel::class.java) }
    private lateinit var rvNotes: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myViewM.getList().observe(this) {
            updateRV(it)
        }
        rvNotes = findViewById(R.id.rv)
        edNote = findViewById(R.id.et)
        btnsave = findViewById(R.id.b)

        btnsave.setOnClickListener {
            val s = edNote.text.toString()
            if (!s.isEmpty()) {

                edNote.text.clear()

                CoroutineScope(IO).launch {
                    myViewM.addNote(Note("", s))
                }
                Toast.makeText(applicationContext, "data saved successfully! ", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(applicationContext, "No note entered! ", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun updateRV(data: ArrayList<Note>) {
        CoroutineScope(IO).launch {
            withContext(Main) {
                rvNotes.adapter = adapteritem(this@MainActivity, data)
                rvNotes.layoutManager = LinearLayoutManager(this@MainActivity)
            }
        }
    }

    fun removeNote(note: Note) {
        CoroutineScope(IO).launch {
            myViewM.RemoveNote(note)

        }
    }

    fun updateNote(note: Note) {
        CoroutineScope(IO).launch {
            myViewM.update(note)

        }

    }

    fun Alert(note: Note) {

        val dialogBuilder = AlertDialog.Builder(this)
        val input = EditText(this)
        dialogBuilder.setMessage("Enter note")
            .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, id ->
                updateNote(Note(note.id,input.text.toString()))
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        val alert = dialogBuilder.create()

        alert.setTitle("Update your note")
        alert.setView(input)
        alert.show()

    }
}