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
        Question("¿Cuántos continentes hay en el mundo?", listOf("5", "6", "7", "8"), "7", R.drawable.continentes),
        Question("¿Cuál es el planeta más grande del sistema solar?", listOf("Tierra", "Júpiter", "Saturno", "Marte"), "Júpiter", R.drawable.planetas),
        Question("¿Cuál es el país más grande del mundo por área terrestre?", listOf("Estados Unidos", "Rusia", "China", "Canadá"), "Rusia", R.drawable.paises),
        Question("¿Quién pintó la Mona Lisa?", listOf("Leonardo da Vinci", "Vincent van Gogh", "Pablo Picasso", "Claude Monet"), "Leonardo da Vinci", R.drawable.monalisa) ,
        Question("¿Cuál es el elemento más abundante en la corteza terrestre?", listOf("Oxígeno", "Silicio", "Aluminio", "Hierro"), "Oxígeno", R.drawable.elemtnos)
    )

}