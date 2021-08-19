package com.mstc.mstcapp.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.room.withTransaction
import com.mstc.mstcapp.MainActivity
import com.mstc.mstcapp.model.Result
import com.mstc.mstcapp.util.Constants
import com.mstc.mstcapp.util.RetrofitService
import java.io.IOException
import java.util.*

private const val TAG = "ResourceRepository"

class ResourceRepository(
    private val context: Context,
    private val stcDatabase: STCDatabase,
) {
    private var service: RetrofitService = RetrofitService.create()
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(Constants.STC_SHARED_PREFERENCES, MODE_PRIVATE)

    fun getBoardMembers() = liveData {
        val disposable = emitSource(
            stcDatabase.databaseDao().getBoardMembers().map {
                Result.Loading(it)
            }
        )
        val lastChecked: Long = sharedPreferences.getLong("lastChecked", -1)
        var nextCheck = System.currentTimeMillis()
        if (lastChecked != -1L) {
            val cal = Calendar.getInstance()
            cal.time = Date(lastChecked)
            cal.add(Calendar.DAY_OF_MONTH, 180)
            nextCheck = cal.time.time
        }
        try {
            if (lastChecked == -1L || nextCheck <= Date().time)
                if (isNetworkAvailable(context)) {
                    fetchBoardMembers()
                    disposable.dispose()
                }
            emitSource(stcDatabase.databaseDao().getBoardMembers().map {
                if (it != null && it.isNotEmpty()) Result.Success(it)
                else Result.Error(NullPointerException("Null"))
            })
        } catch (exception: IOException) {
            emitSource(
                stcDatabase.databaseDao().getBoardMembers().map {
                    Result.Error(exception)
                }
            )
        }
    }

    private suspend fun fetchBoardMembers() {
        try {
            val response = service.getBoard()
            if (response.code() == 200 && response.isSuccessful) {
                Log.d(TAG, "getBoardMembers() returned: ${response.body()}")
                response.body()?.let {
                    stcDatabase.withTransaction {
                        stcDatabase.databaseDao().clearBoardMembers()
                        stcDatabase.databaseDao().insertBoardMembers(it)
                        sharedPreferences.edit()
                            .putLong("lastChecked", Date().time)
                            .apply()
                    }
                }
            } else Log.e(TAG, "getBoardMembers: " + response.code())
        } catch (e: Exception) {
            Log.e(TAG, "fetchBoardMembers: ", e)
        }
    }


    fun getDomainDetails(domain: String) = liveData {
        val disposable = emitSource(
            stcDatabase.databaseDao().getDetails(domain).map {
                Result.Loading(it)
            }
        )
        try {
            if (isNetworkAvailable(context) && !MainActivity.isFetchedData(domain + "_details")) {
                fetchDetails(domain)
                disposable.dispose()
            }
            emitSource(stcDatabase.databaseDao().getDetails(domain).map {
                if (it != null) Result.Success(it)
                else Result.Error(NullPointerException("Null"))
            })
        } catch (exception: IOException) {
            emitSource(
                stcDatabase.databaseDao().getDetails(domain).map {
                    Result.Error(exception)
                }
            )
        }

    }

    fun getDomainRoadmap(domain: String) = liveData {
        val disposable = emitSource(
            stcDatabase.databaseDao().getRoadmap(domain).map {
                Result.Loading(it)
            }
        )
        try {
            if (isNetworkAvailable(context) && !MainActivity.isFetchedData(domain + "_roadmap")) {
                fetchRoadmap(domain)
                disposable.dispose()
            }
            emitSource(stcDatabase.databaseDao().getRoadmap(domain).map {
                if (it != null) Result.Success(it)
                else Result.Error(NullPointerException("Null"))
            })
        } catch (exception: IOException) {
            emitSource(
                stcDatabase.databaseDao().getRoadmap(domain).map {
                    Result.Error(exception)
                }
            )
        }
    }

    fun getDomainResources(domain: String) = liveData {
        val disposable = emitSource(
            stcDatabase.databaseDao().getResources(domain).map {
                Result.Loading(it)
            }
        )
        try {
            if (isNetworkAvailable(context) && !MainActivity.isFetchedData(domain + "_resources")) {
                fetchResources(domain)
                disposable.dispose()
            }
            emitSource(stcDatabase.databaseDao().getResources(domain).map {
                if (it != null && it.isNotEmpty()) Result.Success(it)
                else Result.Error(NullPointerException("Null"))
            })
        } catch (exception: IOException) {
            emitSource(
                stcDatabase.databaseDao().getResources(domain).map {
                    Result.Error(exception)
                }
            )
        }
    }

    fun getProjects() = liveData {
        val disposable = emitSource(
            stcDatabase.databaseDao().getProjects().map {
                Result.Loading(it)
            }
        )
        try {
            if (isNetworkAvailable(context) && !MainActivity.isFetchedData("projects")) {
                fetchProjects()
                disposable.dispose()
            }
            emitSource(stcDatabase.databaseDao().getProjects().map {
                if (it != null && it.isNotEmpty()) Result.Success(it)
                else Result.Error(NullPointerException("Null"))
            })
        } catch (exception: IOException) {
            emitSource(
                stcDatabase.databaseDao().getProjects().map {
                    Result.Error(exception)
                }
            )
        }
    }

    fun getEvents() = liveData {
        val disposable = emitSource(
            stcDatabase.databaseDao().getEvents().map {
                Result.Loading(it)
            }
        )
        try {
            if (isNetworkAvailable(context) && !MainActivity.isFetchedData("events")) {
                fetchEvents()
                disposable.dispose()
            }
            emitSource(stcDatabase.databaseDao().getEvents().map {
                if (it != null && it.isNotEmpty()) Result.Success(it)
                else Result.Error(NullPointerException("Null"))
            })
        } catch (exception: IOException) {
            emitSource(
                stcDatabase.databaseDao().getEvents().map {
                    Result.Error(exception)
                }
            )
        }
    }


    private suspend fun fetchDetails(domain: String) {
        val response = service.getDetails(domain)
        if (response.isSuccessful) {
            Log.d(TAG, "fetchDetails() returned: ${response.body()}")
            stcDatabase.withTransaction {
                response.body()?.let {
                    stcDatabase.databaseDao().deleteDetails(domain)
                    stcDatabase.databaseDao().insertDetails(it)
                    MainActivity.setFetchedData(domain + "_details")
                }
            }
        } else Log.e(TAG, "fetchDetails: " + response.code())

    }


    private suspend fun fetchRoadmap(domain: String) {
        try {
            val response = service.getRoadmap(domain)
            if (response.code() == 200 && response.isSuccessful) {
                Log.d(TAG, "fetchRoadmap() returned: ${response.body()}")
                stcDatabase.withTransaction {
                    response.body()?.let {
                        stcDatabase.databaseDao().deleteRoadmap(domain)
                        stcDatabase.databaseDao().insertRoadmap(it)
                        MainActivity.setFetchedData(domain + "_roadmap")
                    }
                }
            } else Log.e(TAG, "fetchRoadmap: " + response.code())
        } catch (e: Exception) {
            Log.e(TAG, "fetchRoadmap: ", e)
        }
    }

    private suspend fun fetchResources(domain: String) {
        try {
            val response = service.getResources(domain)
            if (response.code() == 200 && response.isSuccessful) {
                Log.d(TAG, "fetchResources() returned: ${response.body()}")
                stcDatabase.withTransaction {
                    response.body()?.let {
                        stcDatabase.databaseDao().deleteResources(domain)
                        stcDatabase.databaseDao().insertResources(it)
                        MainActivity.setFetchedData(domain + "_resources")
                    }
                }
            } else Log.e(TAG, "fetchResources: " + response.code())
        } catch (e: Exception) {
            Log.e(TAG, "fetchResources: ", e)
        }
    }

    private suspend fun fetchProjects() {
        try {
            val response = service.getProjects()
            if (response.code() == 200 && response.isSuccessful) {
                Log.d(TAG, "fetchProjects() returned: ${response.body()}")
                stcDatabase.withTransaction {
                    response.body()?.let {
                        stcDatabase.databaseDao().clearAllProjects()
                        stcDatabase.databaseDao().insertProjects(it)
                        MainActivity.setFetchedData("projects")
                    }
                }
            } else Log.e(TAG, "fetchProjects: " + response.code())
        } catch (e: Exception) {
            Log.e(TAG, "fetchResources: ", e)
        }
    }

    private suspend fun fetchEvents() {
        try {
            val response = service.getEvents()
            if (response.code() == 200 && response.isSuccessful) {
                Log.d(TAG, "fetchEvents() returned: ${response.body()}")
                stcDatabase.withTransaction {
                    response.body()?.let {
                        stcDatabase.databaseDao().clearAllEvents()
                        stcDatabase.databaseDao().insertEvents(it)
                        MainActivity.setFetchedData("events")
                    }
                }
            } else Log.e(TAG, "fetchEvents: " + response.code())
        } catch (e: Exception) {
            Log.e(TAG, "fetchEvents: ", e)
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    suspend fun refreshDetails(domain: String) {
        try {
            if (isNetworkAvailable(context)) fetchDetails(domain)
        } catch (e: Exception) {
            Log.e(TAG, "refreshDetails: ", e)
        }
    }

    suspend fun refreshRoadmap(domain: String) {
        if (isNetworkAvailable(context)) fetchRoadmap(domain)
    }

    suspend fun refreshResources(domain: String) {
        if (isNetworkAvailable(context)) fetchResources(domain)
    }

    suspend fun refreshBoard() {
        if (isNetworkAvailable(context)) fetchBoardMembers()
    }

    suspend fun refreshProjects() {
        if (isNetworkAvailable(context)) fetchProjects()
    }

    suspend fun refreshEvents() {
        if (isNetworkAvailable(context)) fetchEvents()
    }
}