package com.example.fruitties.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.immediateTransaction
import androidx.room.useWriterConnection
import com.example.fruitties.database.AppDatabase
import com.example.fruitties.model.Fruittie
import com.example.fruitties.model.RemoteKeys
import com.example.fruitties.network.FruittieApi

@OptIn(ExperimentalPagingApi::class)
class FruittieRemoteMediator(
    private val api: FruittieApi,
    private val database: AppDatabase,
) : RemoteMediator<Int, Fruittie>() {
    private val fruittieDao = database.fruittieDao()
    private val remoteKeysDao = database.remoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Fruittie>,
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 0
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevKey
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextKey
                }
            }

            val response = api.getData(page)
            val fruitties = response.feed
            val endOfPaginationReached = fruitties.isEmpty() || page >= response.totalPages - 1

            val prevKey = if (page == 0) null else page - 1
            val nextKey = if (endOfPaginationReached) null else page + 1
            val keys = fruitties.map {
                RemoteKeys(
                    fruittieId = it.id,
                    prevKey = prevKey,
                    nextKey = nextKey,
                )
            }

            database.useWriterConnection {
                it.immediateTransaction {
                    if (loadType == LoadType.REFRESH && fruitties.isNotEmpty()) {
                        remoteKeysDao.clearRemoteKeys()
                        fruittieDao.clearAll()
                    }
                    remoteKeysDao.insertAll(keys)
                    fruittieDao.insert(fruitties)
                }
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Fruittie>): RemoteKeys? =
        state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeysDao.getRemoteKeyByFruittieId(id)
            }
        }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Fruittie>): RemoteKeys? =
        state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { fruittie ->
            remoteKeysDao.getRemoteKeyByFruittieId(fruittie.id)
        }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Fruittie>): RemoteKeys? =
        state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { fruittie ->
            remoteKeysDao.getRemoteKeyByFruittieId(fruittie.id)
        }
}
