package repository

import api.FollowApi
import br.com.noclaftech.domain.model.Follow
import br.com.noclaftech.domain.model.PaginationFollow
import br.com.noclaftech.domain.repository.FollowRepository
import io.reactivex.Single
import mapper.FollowMapper
import mapper.PaginationFollowMapper
import javax.inject.Inject

class FollowRepositoryImpl @Inject constructor(
    private val followApi: FollowApi,
    private val paginationFollowMapper: PaginationFollowMapper) : FollowRepository{

    override fun getFollowers(id: Int, page : Int): Single<PaginationFollow> {
        return followApi.getFollowers(id, page).map { paginationFollowMapper.map(it) }
    }

    override fun getFollowing(id: Int, page : Int): Single<PaginationFollow> {
        return followApi.getFollowing(id, page).map { paginationFollowMapper.map(it) }
    }
}