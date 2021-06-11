package it.uninsubria.takepizza

class SaveReviews (var name: String, var tRecensione: String, var valutazione: String){
    var id: Int = -1
    constructor(): this("", "","")
    constructor(name: String, tRecensione: String, id: Int,valutazione: String): this(name, tRecensione,valutazione){
        this.id = id
    }

    fun set(newReviews: SaveReviews) {
        id = newReviews.id
        name = newReviews.name
        tRecensione = newReviews.name
        valutazione = newReviews.name
    }

}