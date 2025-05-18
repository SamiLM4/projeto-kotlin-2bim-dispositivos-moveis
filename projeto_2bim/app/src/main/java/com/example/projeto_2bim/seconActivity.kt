package com.example.projeto_2bim

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.widget.Button
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.database.database

import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class seconActivity : AppCompatActivity() {

    private val client = OkHttpClient()
    private val apiKey = "AIzaSyCfQVYXUzSEv7N2UZqIYGbWh_2ukNvtaA4"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_layout)


        val buttonGerar = findViewById<Button>(R.id.button)
        val buttonLimpar = findViewById<Button>(R.id.button2)

        val dia1 = findViewById<TextView>(R.id.textView1)
        val dia2 = findViewById<TextView>(R.id.textView2)
        val dia3 = findViewById<TextView>(R.id.textView3)
        val dia4 = findViewById<TextView>(R.id.textView4)
        val dia5 = findViewById<TextView>(R.id.textView5)
        val dia6 = findViewById<TextView>(R.id.textView6)
        val dia7 = findViewById<TextView>(R.id.textView7)

        var diaNum = 1

        val database = Firebase.database
        val myRef = database.getReference(diaNum.toString())


        buttonGerar.setOnClickListener {
            val pergunta = "Um retiro espiritual virtual do amor. Durante 7 dias, o casal recebe desafio " +
                    "como Escreva um haikai sobre os olhos do seu benzinho ou Conte a primeira vez que sentiu borboletas no estômago (sem ser por azia)." +
                    "A IA avalia o nível de romantismo com emojis e declarações automáticas tipo" +
                    "Corações explodindo de ternura detectados ❤️😍. Faça texto de 1, no máximo 2 linhas, não especifique que dia está, apenas a menssagem"

            enviarPerguntaGemini(pergunta) { resposta ->
                runOnUiThread {
                    if (diaNum == 1) {
                        dia1.text = resposta
                        myRef.child(diaNum.toString()).setValue(resposta)
                        diaNum = diaNum + 1
                    } else if (diaNum == 2) {
                        dia2.text = resposta
                        myRef.child(diaNum.toString()).setValue(resposta)
                        diaNum = diaNum + 1
                    } else if (diaNum == 3) {
                        dia3.text = resposta
                        myRef.child(diaNum.toString()).setValue(resposta)
                        diaNum = diaNum + 1
                    } else if (diaNum == 4) {
                        dia4.text = resposta
                        myRef.child(diaNum.toString()).setValue(resposta)
                        diaNum = diaNum + 1
                    } else if (diaNum == 5) {
                        dia5.text = resposta
                        myRef.child(diaNum.toString()).setValue(resposta)
                        diaNum = diaNum + 1
                    } else if (diaNum == 6) {
                        dia6.text = resposta
                        myRef.child(diaNum.toString()).setValue(resposta)
                        diaNum = diaNum + 1
                    } else if (diaNum == 7) {
                        dia7.text = resposta
                        myRef.child(diaNum.toString()).setValue(resposta)
                        diaNum = 1
                    }

                }
            }
        }

        buttonLimpar.setOnClickListener {
            dia1.text = "Dia 1"
            dia2.text = "Dia 2"
            dia3.text = "Dia 3"
            dia4.text = "Dia 4"
            dia5.text = "Dia 5"
            dia6.text = "Dia 6"
            dia7.text = "Dia 7"
            diaNum = 1
        }

    }

    private fun enviarPerguntaGemini(pergunta: String, callback: (String) -> Unit) {
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=$apiKey"

        val json = JSONObject().apply {
            put("contents", JSONArray().put(
                JSONObject().put("parts", JSONArray().put(
                    JSONObject().put("text", pergunta)
                ))
            ))
        }

        val mediaType = "application/json".toMediaType()
        val body = json.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback("Erro: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val resposta = response.body?.string() ?: "Sem resposta"
                try {
                    val jsonResposta = JSONObject(resposta)
                    val texto = jsonResposta
                        .getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")
                    callback(texto)
                } catch (e: Exception) {
                    callback("Erro ao ler resposta: ${e.message}")
                }
            }
        })
    }
}