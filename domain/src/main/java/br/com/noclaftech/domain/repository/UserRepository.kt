package br.com.noclaftech.domain.repository

import br.com.noclaftech.domain.model.User
import io.reactivex.Single

interface UserRepository{
    fun getUser() : Single<User>
}