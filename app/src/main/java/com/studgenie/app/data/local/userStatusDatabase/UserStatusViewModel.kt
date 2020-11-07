package com.studgenie.app.data.local.userStatusDatabase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//class UserStatusViewModel {
//}

class UserStatusViewModel(application: Application): AndroidViewModel(application) {
    var readAllData: LiveData<List<UserStatus>>?=null
    private var repository: UserStatusRepository?=null

    init {
        val userStatusDao = UserStatusDatabase.getUserStatusDatabase(
            application
        ).getUserStatusDao()
        repository = UserStatusRepository(userStatusDao)
        readAllData = repository!!.readAllData
    }

    fun addUserStatus(userStatus:UserStatus){
        viewModelScope.launch(Dispatchers.IO) {
            repository?.addUserStatus(userStatus)
        }
    }

    fun update(status:String,pid:Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository?.update(status, pid)
        }
    }

    fun deleteStatusData(){
        viewModelScope.launch(Dispatchers.IO) {
            repository?.deleteStatusData()
        }
    }
}