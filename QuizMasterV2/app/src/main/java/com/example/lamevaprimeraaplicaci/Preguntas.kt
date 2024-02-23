package com.example.lamevaprimeraaplicaci

import androidx.core.text.HtmlCompat
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
    private const val API_URL = "https://opentdb.com/api.php?amount=15&type=multiple"

    private val client = HttpClient(Android) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }

    suspend fun getAllQuestions(): List<Question> {
        return try {
            val response = client.get<TriviaResponse>(API_URL)
            response.results.map { triviaQuestion ->
                val decodedQuestion = decodeHtmlEntities(triviaQuestion.question)
                val decodedOptions = triviaQuestion.incorrect_answers.map { decodeHtmlEntities(it) } +
                        listOf(decodeHtmlEntities(triviaQuestion.correct_answer))
                Question(
                    questionText = decodedQuestion,
                    options = decodedOptions,
                    correctAnswer = decodedOptions.last()
                )
            }
        } catch (e: Exception) {
            // Manejar errores aquí
            emptyList()
        }
    }
}
private fun decodeHtmlEntities(encodedText: String): String {
    return HtmlCompat.fromHtml(encodedText, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
}
data class TriviaResponse(
    val results: List<TriviaQuestion>
)

data class TriviaQuestion(
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
)
