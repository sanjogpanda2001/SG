package com.studgenie.app.data.local.userDetailsDatabase

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert
    suspend fun addUserData(userData: UserData)

    @Update
    suspend fun updateUserdata(userData: UserData)

    @Query("UPDATE UserData SET number=:number, userName = :username,email = :email WHERE id=:pid")
    suspend fun update(number:String,username:String,email:String,pid:Int)

    @Query("SELECT * FROM UserData ORDER BY id DESC")
    fun getUserData(): LiveData<List<UserData>>


    @Query("DELETE FROM UserData")
    suspend fun deleteUserData()
}