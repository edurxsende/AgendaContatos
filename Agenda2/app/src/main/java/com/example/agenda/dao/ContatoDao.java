package com.example.agenda.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.agenda.model.Contato;

import java.util.ArrayList;

public class ContatoDao extends SQLiteOpenHelper {
    private static final String NOME_BANCO = "AGENDA.db";
    private static final int VERSAO = 1;
    private static final String TABELA = "tbContato";

    public ContatoDao(@Nullable Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABELA +" ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT, " +
                "email TEXT " +
                ");";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS "+TABELA;
        db.execSQL(sql);
        onCreate(db);

    }
    public long incluirContato(Contato contato) {
        ContentValues values = new ContentValues();
        values.put("nome", contato.getNome());
        values.put("email", contato.getEmail());
        return getWritableDatabase().insert(TABELA, null, values);
    }

    public int alterarContato(Contato contato) {
        ContentValues values = new ContentValues();
        values.put("nome", contato.getNome());
        values.put("email", contato.getEmail());
        String idParaSerAlterado = (String.valueOf(contato.getId()));

        return getWritableDatabase().update(TABELA, values, "id=?", new String[]{idParaSerAlterado});
    }

    public void deletarContato(Contato contato){
        SQLiteDatabase db = getWritableDatabase();
        String idSelecionado = (String.valueOf(contato.getId()));
        db.delete(TABELA,"id=?", new String[]{idSelecionado});
    }
    public ArrayList<Contato> listarContato(){
        String[] colunas = {"id", "nome", "email"};
        Cursor cursor = getWritableDatabase().query(TABELA, colunas, null, null, null, null, "nome", null);
        ArrayList<Contato> listContato = new ArrayList<>();

        while (cursor.moveToNext()) {
            Contato c = new Contato();
            c.setId(cursor.getLong(0));
            c.setNome(cursor.getString(1));
            c.setEmail(cursor.getString(2));
            listContato.add(c);
        }
        return listContato;
    }
}
