/*
 * Copyright 2019 Carlos René Ramos López. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.crrl.beatplayer.ui.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crrl.beatplayer.models.Folder
import com.crrl.beatplayer.models.Song
import com.crrl.beatplayer.repository.FoldersRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
class FolderViewModel(private val context: Context) : ViewModel() {

    private val albums: MutableLiveData<List<Folder>> = MutableLiveData()
    private val songByFolder: MutableLiveData<List<Song>> = MutableLiveData()

    fun getFolders(): LiveData<List<Folder>> {
        Observable.fromCallable { FoldersRepository(context).getFolders() }
            .observeOn(Schedulers.newThread())
            .subscribeOn(Schedulers.newThread())
            .doOnError { Log.println(Log.ERROR, "Error", it.message!!) }
            .subscribe { albums.postValue(it) }
        return albums
    }

    fun getSongsByFolder(ids: List<Long>): LiveData<List<Song>> {
        Observable.fromCallable { FoldersRepository(context).getSongsForIds(ids.toLongArray()) }
            .observeOn(Schedulers.newThread())
            .subscribeOn(Schedulers.newThread())
            .doOnError { Log.println(Log.ERROR, "Error", it.message!!) }
            .subscribe { songByFolder.postValue(it) }
        return songByFolder
    }
}