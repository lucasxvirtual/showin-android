package br.com.noclaftech.domain.repository

import br.com.noclaftech.domain.model.PaginationFollow
import io.reactivex.Single

interface FollowRepository {
    fun getFollowers(id: Int, page : Int) : Single<PaginationFollow>

    fun getFollowing(id: Int, page : Int) : Single<PaginationFollow>
}