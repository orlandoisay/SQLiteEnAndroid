package net.ivanvega.sqliteenandroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import net.ivanvega.sqliteenandroid.db.DAOUsuarios;
import net.ivanvega.sqliteenandroid.db.MiAdaptadorUsuariosConexion;
import net.ivanvega.sqliteenandroid.db.Usuario;

import java.sql.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final int INSERT_USER = 1;

    EditText txtBusqueda;
    Button btnInsertar;
    ListView lsv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context self = this;

        txtBusqueda = findViewById(R.id.txtBusqueda);

        txtBusqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                cargarDatos(txtBusqueda.getText().toString());
            }
        });

        btnInsertar = findViewById(R.id.btnInsertar);
        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentInsertar = new Intent(self, UsuarioActivity.class);
                startActivityForResult(intentInsertar, INSERT_USER);
            }
        });

        lsv = findViewById(R.id.lsv);
        lsv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(self);

                builder.setItems(
                        new CharSequence[]{"Modificar", "Eliminar"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Cursor c = (Cursor) parent.getItemAtPosition(position);
                                int id = c.getInt(0);

                                switch (which) {
                                    case 0:
                                        Intent intentActualizar = new Intent(self, UsuarioActivity.class);
                                        intentActualizar.putExtra("_id", id);

                                        startActivityForResult(intentActualizar, INSERT_USER);
                                        break;
                                    case 1:
                                        DAOUsuarios ado = new DAOUsuarios(getBaseContext());

                                        if (ado.delete(id) > 0)
                                            Toast.makeText(self, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });

                builder.show();
                return false;
            }
        });
    }

    public void cargarDatos(String filtro) {
        DAOUsuarios dao = new DAOUsuarios(this);
        Cursor c =  dao.getFiltered(filtro);

        SimpleCursorAdapter adp = new SimpleCursorAdapter(
                this, android.R.layout.simple_list_item_2 ,
                c , MiAdaptadorUsuariosConexion.COLUMNS_USUARIOS,
                new int[]{android.R.id.text1, android.R.id.text2},
                SimpleCursorAdapter.NO_SELECTION
        );

        lsv.setAdapter(adp);
    }

    public void btnCargar_click(View v){
        cargarDatos("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        txtBusqueda.setText("");
        cargarDatos("");
    }
}
