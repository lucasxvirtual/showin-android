package repository

import api.UserApi
import br.com.noclaftech.domain.model.User
import br.com.noclaftech.domain.repository.UserRepository
import io.reactivex.Single
import mapper.UserMapper
import javax.inject.Inject

class UserImpl @Inject constructor(
    private val userApi: UserApi,
    private val userMapper : UserMapper
): UserRepository {
    override fun getUser(): Single<User> {
        return userApi.getUser().map { userMapper.map(it) }
    }

}