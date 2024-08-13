package com.example.lightcontrol_app.menuPrincipal.crearOrdenServicio;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lightcontrol_app.R;
import com.example.lightcontrol_app.controllerDB.BaseDeDatosAux;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MantenimientoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MantenimientoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Spinner tipoElementoSpinner;
    private EditText codigoElementoEditText;
    private EditText elementoRelacionadoEditText;
    private Spinner problemaValidoSpinner;
    private EditText problemaRelacionadoEditText;
    private EditText prioridadRutaEditText;
    private Spinner ordenPrioridadSpinner;
    private Spinner tipoOrdenSpinner;
    private EditText fechaRealizarEditText;
    private Spinner tipoSolucionSpinner;
    private Spinner cuadrillaSpinner;
    private Button generarNuevaOrdenButton;
    private Button atrasButton;

    public MantenimientoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MantenimientoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MantenimientoFragment newInstance(String param1, String param2) {
        MantenimientoFragment fragment = new MantenimientoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mantenimiento, container, false);
        int idPqr = -1;
        String descripcionPqr = null;
        Bundle bundle = getArguments();
        if (bundle != null){
            idPqr = bundle.getInt("CrearOrdenServicioId");
            descripcionPqr = bundle.getString("CrearOrdenServicioDescription");
        }

        tipoElementoSpinner = view.findViewById(R.id.crearOrden_tipoElemento);
        codigoElementoEditText = view.findViewById(R.id.crearOrden_codigoElemento);
        elementoRelacionadoEditText = view.findViewById(R.id.crearOrden_elementoRelacionado);
        problemaValidoSpinner = view.findViewById(R.id.crearOrden_problemaValido);
        problemaRelacionadoEditText = view.findViewById(R.id.crearOrden_problemaRelacionado);
        prioridadRutaEditText = view.findViewById(R.id.crearOrden_prioridadRuta);
        ordenPrioridadSpinner = view.findViewById(R.id.crearOrden_ordenPrioridad);
        tipoOrdenSpinner = view.findViewById(R.id.crearOrden_tipoOrden);
        fechaRealizarEditText = view.findViewById(R.id.crearOrden_fechaRealizar);
        fechaRealizarEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                if (getActivity() != null){
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                                    fechaRealizarEditText.setText(selectedDate);
                                }
                            }, year, month, day);
                    datePickerDialog.show();
                }

            }
        });
        tipoSolucionSpinner = view.findViewById(R.id.crearOrden_tipoSolucion);
        cuadrillaSpinner = view.findViewById(R.id.crearOrden_cuadrilla);

        generarNuevaOrdenButton = view.findViewById(R.id.crearOrden_generarNuevaOrden);
        atrasButton = view.findViewById(R.id.crearOrden_atras);

        elementoRelacionadoEditText.setText(String.valueOf(idPqr));
        problemaRelacionadoEditText.setText(descripcionPqr);

        elementoRelacionadoEditText.setEnabled(false);
        problemaRelacionadoEditText.setEnabled(false);

        int finalIdPqr = idPqr;
        generarNuevaOrdenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areFieldsValid()) {
                    try {
                        insertarDatosOrdenServicio();
                        actualizarEstadoPQR(finalIdPqr);
                        Toast.makeText(getActivity(), "Se subió la orden de servicio correctamente", Toast.LENGTH_SHORT).show();
                    } catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Ha ocurrido un error inesperado", Toast.LENGTH_SHORT).show();
                    }

                    if (getActivity() != null){
                        getActivity().finish();
                    }
                } else {
                    Toast.makeText(getActivity(), "Por favor completa todos los campos.", Toast.LENGTH_SHORT).show();
                }
            }

            private boolean areFieldsValid() {
                // Verificar EditTexts
                if (codigoElementoEditText.getText().toString().trim().isEmpty() ||
                        prioridadRutaEditText.getText().toString().trim().isEmpty() ||
                        fechaRealizarEditText.getText().toString().trim().isEmpty()) {
                    return false;
                }

                // Verificar Spinners
                if (tipoElementoSpinner.getSelectedItemPosition() == 0 ||
                        problemaValidoSpinner.getSelectedItemPosition() == 0 ||
                        ordenPrioridadSpinner.getSelectedItemPosition() == 0 ||
                        tipoOrdenSpinner.getSelectedItemPosition() == 0 ||
                        tipoSolucionSpinner.getSelectedItemPosition() == 0 ||
                        cuadrillaSpinner.getSelectedItemPosition() == 0) {
                    return false;
                }

                return true;
            }
        });
        atrasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null){
                    getActivity().finish();
                }
            }
        });

        //crearOrdenDeServicio(idPqr, fechaRealizarEditText.getText(), );

        return view;
    }

    private void actualizarEstadoPQR(int idPqr){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(()->{
            try {
                BaseDeDatosAux baseDeDatosAux = new BaseDeDatosAux();
                baseDeDatosAux.actualizarDatos("UPDATE pqrs SET Estado = ? WHERE Idpqrs = ?", 2, idPqr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void insertarDatosOrdenServicio() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(()->{
            try {
                BaseDeDatosAux baseDeDatosAux = new BaseDeDatosAux();
                baseDeDatosAux.actualizarDatos("INSERT INTO ordenes_de_servicio (" +
                                "Tipo_de_elemento, " +
                                "codigo_de_elemento, " +
                                "elemento_relacionado, " +
                                "problema_relacionado, " +
                                "problema_validado, " +
                                "prioridad_de_ruta, " +
                                "fecha_a_realizar, " +
                                "cuadrilla, " +
                                "tipo_de_orden, " +
                                "tipo_de_Solucion, " +
                                "clase_de_orden, " +
                                "Orden_prioridad, " +
                                "IdEstado" +
                                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        tipoElementoSpinner.getSelectedItem().toString(),
                        codigoElementoEditText.getText().toString(),
                        elementoRelacionadoEditText.getText().toString(),
                        problemaRelacionadoEditText.getText().toString(),
                        problemaValidoSpinner.getSelectedItem().toString(),
                        prioridadRutaEditText.getText().toString(),
                        fechaRealizarEditText.getText().toString(),
                        cuadrillaSpinner.getSelectedItem().toString(),
                        tipoOrdenSpinner.getSelectedItem().toString(),
                        tipoSolucionSpinner.getSelectedItem().toString(),
                        "Mantenimiento",
                        ordenPrioridadSpinner.getSelectedItem().toString(),
                        2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}