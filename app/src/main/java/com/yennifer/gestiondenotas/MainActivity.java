package com.yennifer.gestiondenotas;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.yennifer.gestiondenotas.adapter.EstudianteAdapter;
import com.yennifer.gestiondenotas.controller.EstudianteController;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private EstudianteAdapter estudianteAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Inicializar el controlador
        EstudianteController estudianteController = new EstudianteController(this);

        // Agregar solo si no hay datos existentes
        if (estudianteController.obtenerEstudiantes().isEmpty()) {
            estudianteController.agregarEstudiante("Juan", "12345");
            estudianteController.agregarEstudiante("María", "67890");
        }

        // Configurar RecyclerView
        binding.rvListaEstudiantes.setLayoutManager(new LinearLayoutManager(this));
        estudianteAdapter = new EstudianteAdapter(this, estudianteController.obtenerEstudiantes());
        binding.rvListaEstudiantes.setAdapter(estudianteAdapter);
    }
}