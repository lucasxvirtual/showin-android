package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.repository.ConstantsRepository
import javax.inject.Inject

class SetPopupDateUseCase @Inject constructor(private val constantsRepository: ConstantsRepository){

    fun execute(date: String) {
        constantsRepository.setPopupDate(date)
    }
}