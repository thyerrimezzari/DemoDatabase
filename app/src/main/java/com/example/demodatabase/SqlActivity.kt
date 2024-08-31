package com.example.demodatabase

import android.annotation.SuppressLint
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demodatabase.adapters.UsuarioListAdapter
import com.example.demodatabase.datasql.AppDbHelper
import com.example.demodatabase.datasql.Usuario

class SqlActivity : AppCompatActivity() {

    private lateinit var dbHelper: AppDbHelper;

    private lateinit var recyclerView: RecyclerView;
    private lateinit var adapter: UsuarioListAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sql)

        dbHelper = AppDbHelper(this)

        recyclerView = findViewById(R.id.recyclerview)

        adapter = UsuarioListAdapter()
        adapter.setUsuarioDelete {
            removeUsuario(it)
            atualizaTela()
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // primeiro carregamento de tela e montagem
        atualizaTela()
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }

    fun removeUsuario(usuario: Usuario) {
        val db = dbHelper.writableDatabase
        db?.delete("usuarios", "id = ?", arrayOf(usuario.id.toString()))
    }

    fun guardaUsuario(name:String, email:String):Long? {
        val db = dbHelper.writableDatabase

        val dadosUsuario = ContentValues().apply {
            put("name", name)
            put("email", email)
        }

        return db?.insert("usuarios", null, dadosUsuario)
    }

    fun clickBtnSave(view:View) {
        val textName = findViewById<EditText>(R.id.textName)
        val textEmail = findViewById<EditText>(R.id.textEmail)

        val name = textName.text.toString()
        val email = textEmail.text.toString()
        if(!name.equals("") && !email.equals("")) {
            // logo que guardamos o novo usuário podemos recuperar o mesmo pelo ID
            val novoId = guardaUsuario(name, email)

            textName.setText("")
            textEmail.setText("")

            atualizaTela()
        }
    }

    // o método atualiza tela é um método que iremos chamar em diversos momentos
    // que precisamos estar sempre recuperando dados do banco e atualizando o que é
    // exibido em tela para o usuário
    fun atualizaTela() {
        val encontrados:List<Usuario> = recuperaUsuarios()
        adapter.submitList(encontrados)
    }

    @SuppressLint("Range")
    fun recuperaUsuarios():List<Usuario> {
        val db = dbHelper.readableDatabase

        val campos = arrayOf("id", "name", "email")
        val criterio = null; // "name LIKE ?"
        val criterioValores = null; // arrayOf("%Lucas%")
        val ordem = "email ASC"

        // montando a query e criando um Cursor para acesso dos registros
        val cursor = db.query(
            "usuarios",       // nome da tabela a ser consultada
            campos,           // Campos a serem selecionados, passar null para pegar todos
            criterio,         // Parte "WHERE" desssa query de SELECT
            criterioValores,  // Argumentos dinâmicos para a montagem do WHERE
            null,             // GROUP BY - se precisar
            null,             // HAVING - se precisar
            ordem             // Ordenação dos resultados (ORDER BY)
        )

        var usuarios:ArrayList<Usuario> = ArrayList()

        with(cursor) {
            // cada vez que esse while "girar" teremos os dados
            // de um usuário de cada vez de todos encontrados na query
            while (moveToNext()) {
                val id = getInt(getColumnIndex("id"));
                val name = getString(getColumnIndex("name"));
                val email = getString(getColumnIndex("email"));

                usuarios.add(Usuario(id, name, email))
            }
        }
        cursor.close()

        return usuarios
    }

}