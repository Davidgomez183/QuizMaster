package com.example.lamevaprimeraaplicaci

data class Question(
    val questionText: String,
    val options: List<String>,
    val correctAnswer: String
)
object QuestionRepository {
    val allQuestions = arrayOf(
        Question("¿Cuál es la capital de Japón?", listOf("Seúl", "Pekín", "Tokio", "Bangkok"), "Tokio"),
        Question("¿Cuál es el río más largo del mundo?", listOf("Amazonas", "Nilo", "Yangtsé", "Misisipi"), "Amazonas"),
        Question("¿Quién escribió 'Cien años de soledad'?", listOf("Gabriel García Márquez", "Pablo Neruda", "Isabel Allende", "Mario Vargas Llosa"), "Gabriel García Márquez"),
        Question("¿Cuál es el elemento más abundante en la corteza terrestre?", listOf("Hierro", "Oxígeno", "Silicio", "Aluminio"), "Oxígeno"),
        Question("¿En qué año comenzó la Segunda Guerra Mundial?", listOf("1935", "1938", "1939", "1941"), "1939"),
        Question("¿Cuál es el océano más grande del mundo?", listOf("Océano Atlántico", "Océano Índico", "Océano Pacífico", "Océano Antártico"), "Océano Pacífico"),
        Question("¿Quién pintó la Mona Lisa?", listOf("Leonardo da Vinci", "Vincent van Gogh", "Pablo Picasso", "Claude Monet"), "Leonardo da Vinci"),
        Question("¿Cuál es la montaña más alta del mundo?", listOf("Monte Everest", "K2", "Kangchenjunga", "Lhotse"), "Monte Everest"),
        Question("¿Quién fue el primer presidente de Estados Unidos?", listOf("Thomas Jefferson", "George Washington", "John Adams", "Benjamin Franklin"), "George Washington"),
        Question("¿Cuál es la capital de Australia?", listOf("Melbourne", "Sídney", "Canberra", "Brisbane"), "Canberra")
    )

}