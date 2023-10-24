package com.example.voicehandler2.database

import androidx.lifecycle.LiveData
import com.example.voicehandler2.model.Note

interface DatabaseRepository {

    val readAll: LiveData<List<Note>>

    suspend fun create(note: Note, onSuccess: () -> Unit)

    suspend fun update(note: Note, onSuccess: () -> Unit)

    suspend fun delete(note: Note, onSuccess: () -> Unit)

    fun signOut(){}

    fun connectToDatabase(onSuccess: () -> Unit, onFail: (String) -> Unit){}
    fun registrationInDatabase(onSuccess: () -> Unit, onFail: (String) -> Unit){}

    //fun registrationInDatabaseWithPhone(onSuccess: () -> Unit, onFail: (String) -> Unit){}

}