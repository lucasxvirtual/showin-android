package br.com.noclaftech.domain.model

data class Pagination <T> (val count : Int?,
                           val next : String?,
                           val previous : String?,
                           var results : List<T>)