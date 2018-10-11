package net.ivanvega.sqliteenandroid;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import net.ivanvega.sqliteenandroid.db.DAOUsuarios;
import net.ivanvega.sqliteenandroid.db.Usuario;

import java.sql.Date;

public class UsuarioActivity extends AppCompatActivity {

    Button btnGuardar;
    EditText txtNombre,txtEmail, txtTelefono ,txtTwitter, txtFechaNac;


    int id = -1;
    boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        final Context self = this;

        // Linkea views
        txtNombre   = findViewById(R.id.txtNombre);
        txtEmail    = findViewById(R.id.txtEmail);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtTwitter  = findViewById(R.id.txtTwitter);
        txtFechaNac = findViewById(R.id.txtFechaNac);
        btnGuardar  = findViewById(R.id.btnGuardar);

        // Detecta modo
        id = getIntent().getIntExtra("_id", -1);
        if (id != -1) {
            editMode = true;

            DAOUsuarios ado = new DAOUsuarios(this);
            Usuario u = ado.get(id);

            txtNombre.setText(u.getNombre());
            txtTelefono.setText(u.getTelefono());
            txtEmail.setText(u.getEmail());
            txtTwitter.setText(u.getRed_social());
            txtFechaNac.setText(u.getFecha_nac());
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DAOUsuarios ado =
                        new DAOUsuarios(getApplicationContext());

                if (id == -1) {
                    long result = ado.add(
                        new Usuario(
                            0,
                            txtNombre.getText().toString(),
                            txtTelefono.getText().toString(),
                            txtEmail.getText().toString(),
                            txtTwitter.getText().toString(),
                            txtFechaNac.getText().toString()
                        )
                    );

                    if (result > 0){
                        Toast.makeText(getBaseContext(), "Adición exitosa",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    ado.update(
                            new Usuario(
                                    id,
                                    txtNombre.getText().toString(),
                                    txtTelefono.getText().toString(),
                                    txtEmail.getText().toString(),
                                    txtTwitter.getText().toString(),
                                    txtFechaNac.getText().toString()
                            )
                    );
                }

                finish();
            }
        });
    }

}
