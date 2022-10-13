package com.example.vamosrachar

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var mtts: TextToSpeech
    private val myLocale: Locale = Locale("pt","BR")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Elementos da tela
        val quantidade_pessoas: TextView = findViewById(R.id.editTextNumberSigned)
        val valor_total: TextView = findViewById(R.id.editTextNumberDecimal)
        val valor_exibido: TextView = findViewById(R.id.valorExibido)
        val btn_tts: FloatingActionButton = findViewById(R.id.floatingActionButton2)
        val btn_share: FloatingActionButton = findViewById(R.id.floatingActionButton)

        //Text-To-Speech
        mtts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR){
                mtts.setLanguage(this.myLocale)
            }
        })

        //Conversão para Float
        var valor_total_float = valor_total.text.toString().toFloatOrNull()
        var quantidade_pessoas_float = quantidade_pessoas.text.toString().toFloatOrNull()

        //Observador de mudanças no texto
        valor_total.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                valor_total_float = valor_total.text.toString().toFloatOrNull()

                if (valor_total_float == null || quantidade_pessoas_float == null || quantidade_pessoas_float == 0f){
                    valor_exibido.text = "Insira os valores"
                }else{
                    val calculo = valor_total_float!! / quantidade_pessoas_float!!
                    val formatter: NumberFormat = DecimalFormat("0.##")
                    Log.v("PDM", formatter.format(calculo))
                    valor_exibido.text = "R$ " + formatter.format(calculo)
                }
            }
        });
        quantidade_pessoas.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                quantidade_pessoas_float = quantidade_pessoas.text.toString().toFloatOrNull()

                if (valor_total_float == null || quantidade_pessoas_float == null || quantidade_pessoas_float == 0f){
                    valor_exibido.text = "Insira os valores"
                }else{
                    val calculo = valor_total_float!! / quantidade_pessoas_float!!
                    val formatter: NumberFormat = DecimalFormat("0.##")
                    valor_exibido.text = "R$ " + formatter.format(calculo)
                }

            }
        });

        //Botão Text-To-Speech
        btn_tts.setOnClickListener{
            val toSpeak = valor_exibido.text.toString()
            if (toSpeak == "Calculando..." || toSpeak == "Insira os valores"){
                mtts.speak("Preencha todos os campos", TextToSpeech.QUEUE_FLUSH, null)
            }else{
                mtts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null)
            }
        }

        //Botão de compartilhar
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