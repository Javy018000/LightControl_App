package com.example.lightcontrol_app.menuPrincipal.verOrdenesServicio;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lightcontrol_app.Adapter_RecycleView.InsumosCerrarOrdenAdapter;
import com.example.lightcontrol_app.controllerDB.BaseDeDatosAux;
import com.example.lightcontrol_app.Modelo_RecycleView.Insumos;
import com.example.lightcontrol_app.R;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InsumosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsumosFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btnVolverInfo;
    private Button btnConfirmarCierre;
    private RecyclerView recyclerView;
    private int cantidadEditada;
    private InsumosCerrarOrdenAdapter adapter;
    private ExecutorService executorService;
    private Handler mainHandler;
    private OnFragmentInteractionListener mListener;

    public InsumosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InsumosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InsumosFragment newInstance(String param1, String param2) {
        InsumosFragment fragment = new InsumosFragment();
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
        View view = inflater.inflate(R.layout.fragment_insumos, container, false);

        btnConfirmarCierre = view.findViewById(R.id.buttonConfirmarInsumosCerrarOrden);
        btnVolverInfo = view.findViewById(R.id.buttonVolverInsumosCerrarOrden);

        recyclerView = view.findViewById(R.id.recycleViewInsumosCerrarOrden);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnVolverInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onButtonToInfoClicked();
                }
            }
        });
        btnConfirmarCierre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onButtonConfirmarClicked(adapter.getInsumosModificados());
                }
            }
        });

        cargarCampos();

        return view;
    }

    private void cargarCampos() {
        executorService.execute(() -> {
            BaseDeDatosAux baseDeDatosAux = new BaseDeDatosAux();
            List<Insumos> resultadoInsumos = baseDeDatosAux.cerrarOrden();

            mainHandler.post(() -> {
                if (isAdded() && getActivity() != null) {
                    adapter = new InsumosCerrarOrdenAdapter(resultadoInsumos);
                    recyclerView.setAdapter(adapter);
                }
            });
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onButtonToInfoClicked();
        void onButtonConfirmarClicked(List<Insumos> insumosModificados);
    }
}