package by.mylnikov.transport.repository

import by.mylnikov.transport.model.Favorite
import rx.Observable


interface FavoritesRepository {
    fun getFavorites(): Observable<Favorite>
}
