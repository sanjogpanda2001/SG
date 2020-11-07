package com.studgenie.app.data.local.userStatusDatabase

import androidx.lifecycle.LiveData


//class UserStatusRepository {
//}

class UserStatusRepository(private val userStatusDao: UserStatusDao) {
    val readAllData: LiveData<List<UserStatus>> = userStatusDao.getStatus()

    suspend fun addUserStatus(userStatus: UserStatus) {
        userStatusDao.addStatus(userStatus)
    }

    suspend fun update(status: String, pid: Int) {
        userStatusDao.update(status, pid)
    }

    suspend fun deleteStatusData() {
        userStatusDao.deleteStatusData()
    }
}