package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.ShowResult
import br.com.noclaftech.domain.model.CreateShow
import br.com.noclaftech.domain.repository.ShowRepository
import io.reactivex.Observable
import javax.inject.Inject

class PutShowUseCase @Inject constructor(private val showRepository: ShowRepository){

    fun execute(id : Int, createShow: CreateShow): Observable<ShowResult> {
        return showRepository.putShow(id, createShow.name!!, createShow.ageRating!!,  createShow.description!!, createShow.sendViewerEmail!!,
            createShow.category!!, createShow.verticalImage!!, createShow.horizontalImage!!, createShow.position!!.toUpperCase(),
            createShow.displayViewers, createShow.setList, createShow.liveTest)
            .toObservable()
            .map { ShowResult.Success(it) as ShowResult }
            .onErrorReturn { ShowResult.Failure(it) }
            .startWith(ShowResult.Loading)
    }
}