package com.example.lightcontrol_app.menuPrincipal.verOrdenesServicio;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lightcontrol_app.Adapter_RecycleView.OrdenServicioTrabajoAdapter;
import com.example.lightcontrol_app.Modelo_RecycleView.Trabajos;
import com.example.lightcontrol_app.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrabajosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrabajosFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recyclerView;
    OrdenServicioTrabajoAdapter adapter;
    OnFragmentInteractionListener mListener;
    Button btnSiguiente;
    Button btnVolver;
    private static final int PICK_IMAGE_REQUEST = 1;
    private TextView textViewFileName;
    private Button buttonCargarImagen;
    private Button buttonVerImagen;
    private EditText editTextObservation;
    private final List<Uri> imageUris = new ArrayList<>();  // Para almacenar las URIs de las imágenes seleccionadas
    private int currentImageIndex = 0;  // Índice de la imagen actualmente mostrada en el dialog

    private byte[] image;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TrabajosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrabajosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrabajosFragment newInstance(String param1, String param2) {
        TrabajosFragment fragment = new TrabajosFragment();
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
        View view = inflater.inflate(R.layout.fragment_trabajos, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewTrabajosOrdenServicio);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnSiguiente = view.findViewById(R.id.buttonSiguienteTrabajosOrdenServicio);
        btnVolver = view.findViewById(R.id.buttonVolverTrabajosOrdenServicio);

        textViewFileName = view.findViewById(R.id.textViewFileName);
        buttonCargarImagen = view.findViewById(R.id.buttonCargarImagen);
        buttonVerImagen = view.findViewById(R.id.buttonVerImagen);

        editTextObservation = view.findViewById(R.id.editTextObservations);

        buttonCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        buttonVerImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog(0);  // Iniciar el dialog mostrando la primera imagen
            }
        });


        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onButtonToInfoVolverClicked();
                }
            }
        });

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    if (image == null){
                        image = getDefaultImageBytes();
                    }
                    mListener.onButtonToInsumosClicked(trabajosSeleccionados(adapter.getTrabajosList()), imageUris, editTextObservation.getText().toString());
                }
            }
        });


        String[] trabajosList = {
                "Mantenimiento preventivo (limpieza)",
                "Cambio de luminaria por luminaria provisional",
                "Ajuste de fotocelda",
                "Ajuste de conectores",
                "Ajuste de acometida luminaria",
                "Ajuste Red de alimentacion",
                "Ajuste de posicion de Luminaria",
                "Cambio de fotocelda",
                "Cambio de conectores",
                "Cambio de acometida Luminaria",
                "Cambio de red Exclusiva",
                "Poda en infraestructura de Alumbrado Publico",
                "Reubicacion de luminaria",
                "Plomado de poste"
        };

        List<Trabajos> trabajos = new ArrayList<>();

        for (String trabajo :
                trabajosList) {
            trabajos.add(new Trabajos(trabajo, false));
        }

        adapter = new OrdenServicioTrabajoAdapter(trabajos);

        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<Trabajos> trabajosSeleccionados(List<Trabajos> trabajosList) {
        List<Trabajos> aux = new ArrayList<>();
        if (!trabajosList.isEmpty()){
            for (Trabajos t :
                    trabajosList) {
                if (t.isEsSeleccionado()){
                    aux.add(t);
                }
            }
        }
        return aux;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.getClipData() != null) {  // Múltiples imágenes seleccionadas
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imageUris.add(imageUri);  // Añadir cada imagen seleccionada a la lista
                    }
                } else if (data.getData() != null) {  // Una sola imagen seleccionada
                    Uri imageUri = data.getData();
                    imageUris.add(imageUri);  // Añadir la imagen seleccionada a la lista
                }
            }

            // Mostrar el nombre de archivo en el TextView y habilitar el botón para ver imágenes si hay imágenes seleccionadas
            if (!imageUris.isEmpty()) {
                textViewFileName.setText(imageUris.size() + " imágenes seleccionadas");
                buttonVerImagen.setVisibility(View.VISIBLE);
            }
        }
    }


    private void showImageDialog(int index) {
        if (getContext() != null && !imageUris.isEmpty()) {
            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.dialog_image);

            ImageView imageView = dialog.findViewById(R.id.dialogImageView);
            ImageButton buttonLeft = dialog.findViewById(R.id.buttonLeft);
            ImageButton buttonRight = dialog.findViewById(R.id.buttonRight);
            Button closeButton = dialog.findViewById(R.id.closeButton);

            // Mostrar la imagen inicial
            currentImageIndex = index;
            imageView.setImageURI(imageUris.get(currentImageIndex));
            updateNavigationButtons(buttonLeft, buttonRight);

            // Manejar la navegación hacia la izquierda
            buttonLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentImageIndex > 0) {
                        currentImageIndex--;
                        imageView.setImageURI(imageUris.get(currentImageIndex));
                        updateNavigationButtons(buttonLeft, buttonRight);
                    }
                }
            });

            // Manejar la navegación hacia la derecha
            buttonRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentImageIndex < imageUris.size() - 1) {
                        currentImageIndex++;
                        imageView.setImageURI(imageUris.get(currentImageIndex));
                        updateNavigationButtons(buttonLeft, buttonRight);
                    }
                }
            });

            // Manejar el cierre del dialog
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }

    private void updateNavigationButtons(ImageButton buttonLeft, ImageButton buttonRight) {
        if (currentImageIndex == 0) {
            buttonLeft.setVisibility(View.GONE);  // Ocultar flecha izquierda si estamos en la primera imagen
        } else {
            buttonLeft.setVisibility(View.VISIBLE);  // Mostrar flecha izquierda si no estamos en la primera imagen
        }

        if (currentImageIndex == imageUris.size() - 1) {
            buttonRight.setVisibility(View.GONE);  // Ocultar flecha derecha si estamos en la última imagen
        } else {
            buttonRight.setVisibility(View.VISIBLE);  // Mostrar flecha derecha si no estamos en la última imagen
        }
    }

    public byte[] getDefaultImageBytes() {
        try {
            // Cambia 'R.drawable.default_image' por el recurso de tu imagen por defecto
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_imagen_por_defecto);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            Log.e("TuActivity", "Error al obtener byte array de la imagen por defecto", e);
            return null;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
        void onButtonToInsumosClicked(List<Trabajos> trabajosSelec, List<Uri> uris, String observacion);
        void onButtonToInfoVolverClicked();
    }
}