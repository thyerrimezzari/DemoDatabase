package com.example.demodatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.demodatabase.adapters.UsuarioListRoomAdapter
import com.example.demodatabase.dataroom.*

class RoomActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase;

    private lateinit var recyclerView:RecyclerView;
    private lateinit var adapter:UsuarioListRoomAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        db = Room.databaseBuilder(this, AppDatabase::class.java, "demo-db-room")
                .allowMainThreadQueries() // isso habilita o uso do ROOM em Activitys normais
                .build()

        recyclerView = findViewById(R.id.recyclerview)

        adapter = UsuarioListRoomAdapter()
        adapter.setUsuarioDelete {
            db.usuarioDao().delete(it)
            atualizaTela()
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // primeiro carregamento de tela e montagem
        atualizaTela()
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }

    // o método atualiza tela é um método que iremos chamar em diversos momentos
    // que precisamos estar sempre recuperando dados do banco e atualizando o que é
    // exibido em tela para o usuário
    // essa função lista apenas os usuários já cadastrados
    fun atualizaTela() {
        val encontrados:List<Usuario> = db.usuarioDao().getAll();
        adapter.submitList(encontrados)
    }

    fun clickBtnSave(view: View) {
        val textName = findViewById<EditText>(R.id.textName)
        val textEmail = findViewById<EditText>(R.id.textEmail)

        val name = textName.text.toString()
        val email = textEmail.text.toString()
        if(!name.equals("") && !email.equals("")) {
            guardaUsuario(name, email)

            textName.setText("")
            textEmail.setText("")

            atualizaTela()
        }
    }

    fun guardaUsuario(name:String, email:String) {
        val novoUsuario:Usuario = Usuario(null, name, email)
        db.usuarioDao().insertAll(novoUsuario)
    }

}