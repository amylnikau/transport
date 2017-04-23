package by.mylnikov.transport.repository

import by.mylnikov.transport.model.Favorite
import io.reactivex.Observable


interface FavoritesRepository {
    fun getFavorites(): Observable<Favorite>
}
