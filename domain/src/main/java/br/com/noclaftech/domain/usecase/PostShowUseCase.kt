package br.com.noclaftech.domain.usecase

import br.com.noclaftech.domain.ShowResult
import br.com.noclaftech.domain.model.CreateShow
import br.com.noclaftech.domain.repository.ShowRepository
import io.reactivex.Observable
import javax.inject.Inject

class PostShowUseCase @Inject constructor(private val showRepository: ShowRepository){

    fun execute(createShow: CreateShow): Observable<ShowResult> {

        return showRepository.postShow(createShow.name!!, createShow.date!!, createShow.description!!, createShow.duration!!, createShow.ageRating!!, createShow.sendViewerEmail!!,
            createShow.category!!, createShow.ticketLimit, createShow.ticketValue!!, createShow.payWhatYouCan, createShow.verticalImage!!, createShow.horizontalImage!!,
            createShow.position!!.toUpperCase(), createShow.displayViewers, createShow.liveTest)
            .toObservable()
            .map { ShowResult.Success(it) as ShowResult }
            .onErrorReturn { ShowResult.Failure(it) }
            .startWith(ShowResult.Loading)
    }
}