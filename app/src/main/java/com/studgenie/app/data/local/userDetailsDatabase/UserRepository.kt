package com.studgenie.app.data.local.userDetailsDatabase

import androidx.lifecycle.LiveData


class UserRepository(private val userDao: UserDao) {
    val readAllData: LiveData<List<UserData>> = userDao.getUserData()

    suspend fun addUserData(userData: UserData){
        userDao.addUserData(userData)
    }

    suspend fun updateUserdata(userData: UserData){
        userDao.updateUserdata(userData)
    }
    suspend fun update(number:String,username:String,email:String,pid:Int){
        userDao.update(number,username,email,pid)
    }

    suspend fun deleteUserData(){
        userDao.deleteUserData()
    }

}