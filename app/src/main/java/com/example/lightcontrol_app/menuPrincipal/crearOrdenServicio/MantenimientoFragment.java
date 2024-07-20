package com.example.lightcontrol_app.menuPrincipal.crearOrdenServicio;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.lightcontrol_app.R;

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
    private ExecutorService executorService;
    private Handler mainHandler;

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
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
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
        tipoSolucionSpinner = view.findViewById(R.id.crearOrden_tipoSolucion);
        cuadrillaSpinner = view.findViewById(R.id.crearOrden_cuadrilla);

        generarNuevaOrdenButton = view.findViewById(R.id.crearOrden_generarNuevaOrden);
        atrasButton = view.findViewById(R.id.crearOrden_atras);

        elementoRelacionadoEditText.setText(String.valueOf(idPqr));
        problemaRelacionadoEditText.setText(descripcionPqr);

        elementoRelacionadoEditText.setEnabled(false);
        problemaRelacionadoEditText.setEnabled(false);

        generarNuevaOrdenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //crearOrdenDeServicio(idPqr, fechaRealizarEditText.getText(), );

        return view;
    }
}