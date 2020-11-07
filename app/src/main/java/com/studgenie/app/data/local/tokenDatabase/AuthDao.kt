package com.studgenie.app.data.local.tokenDatabase

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AuthDao{
    @Insert
    suspend fun addToken(authToken: AuthToken)

    @Update
    suspend fun updateToken(authToken: AuthToken)

    @Query("UPDATE AuthToken SET authToken=:token WHERE id=:pid")
    suspend fun update(token:String,pid:Int)

    @Query("SELECT * FROM AuthToken ORDER BY id DESC")
    fun getAuthToken(): LiveData<List<AuthToken>>

    @Query("DELETE FROM AuthToken")
    suspend fun deleteAuthToken()
}