package com.example.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agenda.dao.ContatoDao;
import com.example.agenda.model.Contato;

public class ContatoActivity extends AppCompatActivity {
    EditText edtNome;
    EditText edtEmail;
    Button btnSalvar;

    Contato contato;
    ContatoDao contatoDao;

    int modo;
    long retorno;
    private Object ContatoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);

        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        btnSalvar = findViewById(R.id.btnSalvar);

        ContatoDao = new ContatoDao(ContatoActivity.this);

        contato = (Contato) getIntent().getSerializableExtra("contato");
        modo = (int) getIntent().getSerializableExtra("modo");

        edtNome.setText(contato.getNome());
        edtEmail.setText(contato.getEmail());

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // contato = new Contato();
                contato.setNome(edtNome.getText().toString());
                contato.setEmail(edtEmail.getText().toString());

                if (modo == 1) {
                    contatoDao.incluirContato(contato);
                } else {
                    contatoDao.alterarContato(contato);
                }


                if (retorno == -1){
                    Toast.makeText(getApplicationContext(), "Erro ao salvar Contato",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Contato Salvo", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}