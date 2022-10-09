package com.example.vamosrachar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var mtts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val quantidade_pessoas: TextView = findViewById(R.id.editTextNumberSigned)
        val valor_total: TextView = findViewById(R.id.editTextNumberDecimal)
        val valor_exibido: TextView = findViewById(R.id.valorExibido)
        val btn_tts: FloatingActionButton = findViewById(R.id.floatingActionButton2)
        val btn_share: FloatingActionButton = findViewById(R.id.floatingActionButton)

        mtts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR){
                mtts.language = Locale.UK
            }
        })

        var valor_total_float = valor_total.text.toString().toFloatOrNull()
        var quantidade_pessoas_float = quantidade_pessoas.text.toString().toFloatOrNull()

        //Observador de mudanças no texto
        quantidade_pessoas.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                quantidade_pessoas_float = quantidade_pessoas.text.toString().toFloatOrNull()
                Log.v("PDM", "Quantidade: " + quantidade_pessoas_float + " Valor: " + valor_total_float)

                if (valor_total_float == null|| quantidade_pessoas_float == null){
                    valor_exibido.text = "Calculando..."
                }else{
                    val calculo = valor_total_float!! / quantidade_pessoas_float!!
                    valor_exibido.text = "R$ " + calculo
                }
            }
        });
        valor_total.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                valor_total_float = valor_total.text.toString().toFloatOrNull()
                Log.v("PDM", "Quantidade: " + quantidade_pessoas_float + " Valor: " + valor_total_float)

                if (valor_total_float == null|| quantidade_pessoas_float == null){
                    valor_exibido.text = "Calculando..."
                }else{
                    val calculo = valor_total_float!! / quantidade_pessoas_float!!
                    valor_exibido.text = "R$ " + calculo
                }
            }
        });

        //Observador de botão
        btn_tts.setOnClickListener{
            val toSpeak = valor_exibido.text.toString()
            if (toSpeak == "Calculando..." || toSpeak == "Insira os valores"){
                mtts.speak("Preencha todos os campos", TextToSpeech.QUEUE_FLUSH, null)
            }else{
                mtts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null)
            }
        }
        btn_share.setOnClickListener{
            val texto_comp = "O valor para cada amigo ficou: "+ valor_exibido.text.toString() +" - Vamos Rachar!"
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type="text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, texto_comp)
            startActivity(Intent.createChooser(shareIntent,"Compartilhar"))
        }
    }
}