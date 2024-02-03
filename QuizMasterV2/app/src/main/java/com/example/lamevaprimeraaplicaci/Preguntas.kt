package com.example.lamevaprimeraaplicaci

data class Question(
    val questionText: String,
    val options: List<String>,
    val correctAnswer: String
)
object QuestionRepository {
    val allQuestions = arrayOf(
        Question("¿Cuál es la capital de Francia?", listOf("Berlín", "París", "Londres", "Madrid"), "París"),
        Question("¿En qué año ocurrió la Revolución Rusa?", listOf("1917", "1789", "1945", "1899"), "1917"),
        // Agrega más preguntas según sea necesario
    )
}