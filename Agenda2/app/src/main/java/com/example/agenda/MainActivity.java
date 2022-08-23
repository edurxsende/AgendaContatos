package com.example.agenda;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Parcelable;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.agenda.dao.ContatoDao;
import com.example.agenda.databinding.ActivityMainBinding;
import com.example.agenda.model.Contato;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    Contato contato;
    ContatoDao contatoDao;
    ArrayList<Contato> arrayListContato;
    ArrayAdapter<Contato> arrayAdapterContato;

    private final int INCLUSAO = 1;
    private final int ALTERACAO = 2;

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.listviewContatos);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contato contato = new Contato();
                Intent intent = new Intent(MainActivity.this, ContatoActivity.class);
                intent.putExtra("contato", contato);
                intent.putExtra("modo", INCLUSAO);
                startActivity(intent);
            }
        });

//_________________________________________CORRIGINDO MINHA BURRADA_________________________________________________________________________

        listView.setLongClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contato contato = arrayListContato.get(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Deseja excluir contato? "+contato.getNome());

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        contatoDao.deletarContato(contato);
                        Toast.makeText(getApplicationContext(),"Contato excluído com sucesso!", Toast.LENGTH_LONG).show();
                    }
                });

                //++++++++++++++++++
                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Exclusão cancelada", Toast.LENGTH_LONG).show();
                    }
                });

                builder.setTitle("Confirmação de exclusão");
                builder.setIcon(R.mipmap.ic_launcher);
                builder.show();
            }
        });
        listView = findViewById(R.id.listviewContatos);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contato contato = arrayListContato.get(i);
                Intent intent = new Intent(MainActivity.this, ContatoActivity.class);

                startActivity(intent);

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.listviewContatos);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
//===================================================================================


    public void atualizarLista(){
        contatoDao = new ContatoDao(MainActivity.this);
        arrayListContato = contatoDao.listarContato();
        contatoDao.close();

        if(listView != null) {
            arrayAdapterContato = new ArrayAdapter<Contato>(
                    MainActivity.this,
                    android.R.layout.simple_list_item_1,
                    arrayListContato
            );

            listView.setAdapter(arrayAdapterContato);
        }
    }

    //----------------------------------------------------------------------------


    @Override
    protected void onResume() {
        super.onResume();
        atualizarLista();
    }
}