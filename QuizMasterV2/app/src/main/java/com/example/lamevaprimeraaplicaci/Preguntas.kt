package com.example.lamevaprimeraaplicaci

data class Question(
    val questionText: String,
    val options: List<String>,
    val correctAnswer: String,
    val imageResourceId: Int  // Identificador de recurso de imagen
)
object QuestionRepository {
    val allQuestions = arrayOf(
        Question("¿Cuál es la capital de Japón?", listOf("Seúl", "Pekín", "Tokio", "Bangkok"), "Tokio",R.drawable.japon),
        Question("¿Cuál es el río más largo del mundo?", listOf("Amazonas", "Nilo", "Yangtsé", "Misisipi"), "Amazonas",R.drawable.amazonas),
        Question("¿Cuál es el animal más grande del mundo?", listOf("Elefante", "Ballena azul", "Jirafa", "Tiburón ballena"), "Ballena azul",R.drawable.animales),
        Question("¿En qué país se encuentra la Gran Muralla China?", listOf("China", "Japón", "India", "Corea del Sur"), "China",R.drawable.murallachina),
    )

}