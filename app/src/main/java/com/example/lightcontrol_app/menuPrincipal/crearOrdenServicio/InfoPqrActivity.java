package com.example.lightcontrol_app.menuPrincipal.crearOrdenServicio;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.lightcontrol_app.controllerDB.BaseDeDatosAux;
import com.example.lightcontrol_app.Modelo_RecycleView.VerPqrs;
import com.example.lightcontrol_app.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InfoPqrActivity extends AppCompatActivity {
    private EditText textConsecutivoInfoPqr;
    private EditText textTipoPqrsInfoPqr;
    private EditText textFechaRegistroInfoPqr;
    private EditText textCanalInfoPqr;
    private EditText textNombreInfoPqr;
    private EditText textDocumentoInfoPqr;
    private EditText textApellidoInfoPqr;
    private EditText textTelefonoInfoPqr;
    private EditText textTipoDocumentoInfoPqr;
    private EditText textCorreoInfoPqr;
    private EditText textDireccionAfectacionInfoPqr;
    private EditText textDescripcionAfectacionInfoPqr;
    private EditText textBarrioAfectacionInfoPqr;
    private EditText textEstadoInfoPqr;
    private EditText textTipoAlumbradoInfoPqr;
    ImageView imageView;
    Button btnCrearOrden;
    Button btnVolver;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_pqr);

        VerPqrs pqrs = getIntent().getParcelableExtra("VerInfoPqrs");

        textConsecutivoInfoPqr = findViewById(R.id.editTextConsecutivoInfoPqr);
        textTipoPqrsInfoPqr = findViewById(R.id.editTextTipoPqrsInfoPqr);
        textFechaRegistroInfoPqr = findViewById(R.id.editTextTFechaRegistroInfoPqr);
        textCanalInfoPqr = findViewById(R.id.editTextTCanalInfoPqr);
        textNombreInfoPqr = findViewById(R.id.editTextTNombreInfoPqr);
        textDocumentoInfoPqr = findViewById(R.id.editTextDocumentoInfoPqr);
        textApellidoInfoPqr = findViewById(R.id.editTextApellidoInfoPqr);
        textTelefonoInfoPqr = findViewById(R.id.editTextTelefonoInfoPqr);
        textTipoDocumentoInfoPqr = findViewById(R.id.editTextTipoDocumentoInfoPqr);
        textCorreoInfoPqr = findViewById(R.id.editTextCorreoInfoPqr);
        textDireccionAfectacionInfoPqr = findViewById(R.id.editTextDireccionAfectacionInfoPqr);
        textDescripcionAfectacionInfoPqr = findViewById(R.id.editTextDescripcionAfectacionInfoPqr);
        textBarrioAfectacionInfoPqr = findViewById(R.id.editTextBarrioAfectacionInfoPqr);
        textEstadoInfoPqr = findViewById(R.id.editTextEstadoInfoPqr);
        textTipoAlumbradoInfoPqr = findViewById(R.id.editTextTipoAlumbradoInfoPqr);
        imageView = findViewById(R.id.imageViewInfoPqr);

        btnVolver = findViewById(R.id.buttonCrearOrdenVolver);
        btnCrearOrden = findViewById(R.id.buttonCrearOrden);

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCrearOrden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CrearOrdenActivity.class);
                intent.putExtra("CrearOrdenServicioId", pqrs.getIdpqrs());
                intent.putExtra("CrearOrdenServicioDescription", pqrs.getDescripcionAfectacion());
                startActivity(intent);
            }
        });

        assert pqrs != null;
        textConsecutivoInfoPqr.setText(String.valueOf(pqrs.getIdpqrs()));
        textTipoPqrsInfoPqr.setText(pqrs.getTipopqrs());
        if (pqrs.getFechaParcelable() != null) {
            textFechaRegistroInfoPqr.setText(pqrs.getFechaParcelable());
        }
        textCanalInfoPqr.setText(pqrs.getCanal());
        textNombreInfoPqr.setText(pqrs.getNombre());
        textDocumentoInfoPqr.setText(pqrs.getDocumento());
        textApellidoInfoPqr.setText(pqrs.getApellido());
        textTelefonoInfoPqr.setText(pqrs.getTelefono());
        textTipoDocumentoInfoPqr.setText(pqrs.getTipoDoc());
        textCorreoInfoPqr.setText(pqrs.getCorreo());
        textDireccionAfectacionInfoPqr.setText(pqrs.getDireccionAfectacion());
        textDescripcionAfectacionInfoPqr.setText(pqrs.getDescripcionAfectacion());
        textBarrioAfectacionInfoPqr.setText(pqrs.getBarrioAfectacion());
        textTipoAlumbradoInfoPqr.setText(pqrs.getTipoAlumbrado());
        if (pqrs.getEstado() == 1) textEstadoInfoPqr.setText("Sin asignar");

        textConsecutivoInfoPqr.setEnabled(false);
        textTipoPqrsInfoPqr.setEnabled(false);
        textFechaRegistroInfoPqr.setEnabled(false);
        textCanalInfoPqr.setEnabled(false);
        textNombreInfoPqr.setEnabled(false);
        textDocumentoInfoPqr.setEnabled(false);
        textApellidoInfoPqr.setEnabled(false);
        textTelefonoInfoPqr.setEnabled(false);
        textTipoDocumentoInfoPqr.setEnabled(false);
        textCorreoInfoPqr.setEnabled(false);
        textDireccionAfectacionInfoPqr.setEnabled(false);
        textDescripcionAfectacionInfoPqr.setEnabled(false);
        textBarrioAfectacionInfoPqr.setEnabled(false);
        textTipoAlumbradoInfoPqr.setEnabled(false);
        textEstadoInfoPqr.setEnabled(false);



        // Ejecutar la operación de red en un hilo secundario
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            BaseDeDatosAux baseDeDatosAux = new BaseDeDatosAux();
            pqrs.setImagen(baseDeDatosAux.obtenerFoto("Imagen", "pqrs", pqrs.getIdpqrs()));
            runOnUiThread(() -> {
                byte[] imagen = pqrs.getImagen();

                if (imagen != null) {
                    // Convertir el arreglo de bytes a Bitmap
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imagen, 0, imagen.length);



                    // Verificar si el Bitmap se decodificó correctamente
                    if (bitmap != null) {
                        // Definir el tamaño deseado
                        int desiredWidth = 800;  // Ancho deseado en píxeles
                        int desiredHeight = 800; // Alto deseado en píxeles

                        // Redimensionar el Bitmap al tamaño deseado
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, desiredWidth, desiredHeight, true);

                        imageView.setImageBitmap(scaledBitmap);
                    }
                }
            });
        });
    }
}