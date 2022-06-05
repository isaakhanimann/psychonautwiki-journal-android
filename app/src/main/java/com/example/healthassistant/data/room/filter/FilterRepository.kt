package com.example.healthassistant.data.room.filter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilterRepository @Inject constructor(private val filterDao: FilterDao) {
    suspend fun insert(substanceFilter: SubstanceFilter) = filterDao.insert(substanceFilter)
    suspend fun deleteAll() = filterDao.deleteAll()
    suspend fun deleteFilter(substanceFilter: SubstanceFilter) = filterDao.delete(substanceFilter)

    fun getFilters(): Flow<List<SubstanceFilter>> =
        filterDao.getFilters().flowOn(Dispatchers.IO).conflate()
}