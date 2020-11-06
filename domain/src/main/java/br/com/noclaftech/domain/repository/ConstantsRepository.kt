package br.com.noclaftech.domain.repository

import br.com.noclaftech.domain.model.Constants
import io.reactivex.Single

interface ConstantsRepository{
    fun getConstants() : Single<Constants>

    fun saveConstants(constants: Constants)

    fun setPopupDate(date: String)

    fun getPopupDate(): Single<String?>
}