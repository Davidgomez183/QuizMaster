package com.example.lamevaprimeraaplicaci

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*

data class Question(
    val questionText: String,
    val options: List<String>,
    val correctAnswer: String
)

object QuestionRepository {
    private const val API_URL = "https://opentdb.com/api.php?amount=10&type=multiple"

    private val client = HttpClient(Android) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }

    suspend fun getAllQuestions(): List<Question> {
        return try {
            val response = client.get<TriviaResponse>(API_URL)
            response.results.map { triviaQuestion ->
                Question(
                    questionText = triviaQuestion.question,
                    options = triviaQuestion.incorrect_answers + triviaQuestion.correct_answer,
                    correctAnswer = triviaQuestion.correct_answer

                )

            }

        } catch (e: Exception) {
            // Manejar errores aquí
            emptyList()
        }
    }
}

data class TriviaResponse(
    val results: List<TriviaQuestion>
)

data class TriviaQuestion(
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
)
