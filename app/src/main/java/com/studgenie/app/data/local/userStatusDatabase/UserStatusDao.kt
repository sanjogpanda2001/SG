package com.studgenie.app.data.local.userStatusDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.studgenie.app.data.local.tokenDatabase.AuthToken

@Dao
interface UserStatusDao {
    @Insert
    suspend fun addStatus(userStatus: UserStatus)

    @Query("UPDATE UserStatus SET status=:status WHERE id=:pid")
    suspend fun update(status: String, pid: Int)

    @Query("SELECT * FROM UserStatus ORDER BY id DESC")
    fun getStatus(): LiveData<List<UserStatus>>

    @Query("DELETE FROM UserStatus")
    suspend fun deleteStatusData()
}