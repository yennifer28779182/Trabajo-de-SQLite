package com.yennifer.gestiondenotas.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yennifer.gestiondenotas.R;
import com.yennifer.gestiondenotas.controller.Notascontroller;
import com.yennifer.gestiondenotas.model.Estudiante;
import com.yennifer.gestiondenotas.model.Nota;

import java.util.List;

public class EstudianteAdapter extends RecyclerView.Adapter<EstudianteAdapter.ViewHolder> {

    private Context context;
    private List<Estudiante> listaEstudiantes;
    private Notascontroller notaController;
    public EstudianteAdapter(Context context, List<Estudiante> listaEstudiantes) {
        this.context = context;
        this.listaEstudiantes = listaEstudiantes;
        this.notaController = new Notascontroller(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.estudiante_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Estudiante estudiante = listaEstudiantes.get(position);
        holder.tvNombre.setText(estudiante.getNombre());
        holder.tvCodigo.setText("Código: " + estudiante.getCodigo());

        // Mostrar promedio
        double promedio = notaController.calcularPromedioNotas(estudiante.getId());
        holder.tvPromedio.setText("Promedio: " + promedio);

        holder.ivOpciones.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.ivOpciones);
            popupMenu.getMenu().add("Ver notas");
            popupMenu.getMenu().add("Agregar nota");
            popupMenu.getMenu().add("Editar nota");
            popupMenu.getMenu().add("Eliminar nota");

            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getTitle().toString()) {
                    case "Ver notas":
                        List<Nota> notas = notaController.obtenerNotasPorEstudiante(estudiante.getId());
                        StringBuilder sb = new StringBuilder();
                        for (Nota n : notas) {
                            sb.append("- ").append(n.getValor()).append("\n");
                        }
                        if (notas.isEmpty()) sb.append("No hay notas registradas.");
                        new AlertDialog.Builder(context)
                                .setTitle("Notas de " + estudiante.getNombre())
                                .setMessage(sb.toString())
                                .setPositiveButton("OK", null)
                                .show();
                        break;
                    case "Agregar nota":
                        mostrarDialogoNota(estudiante.getId(), false);
                        break;
                    case "Editar nota":
                        mostrarDialogoNota(estudiante.getId(), true);
                        break;
                    case "Eliminar nota":
                        mostrarDialogoEliminar(estudiante.getId());
                        break;
                }
                return true;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return listaEstudiantes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCodigo, tvPromedio;
        ImageView ivOpciones;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCodigo = itemView.findViewById(R.id.tvCodigo);
            tvPromedio = itemView.findViewById(R.id.tvPromedio);
            ivOpciones = itemView.findViewById(R.id.ivOpciones);
        }
    }

    private void mostrarDialogoNota(int estudianteId, boolean esEditar) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(esEditar ? "Editar Nota" : "Agregar Nota");

        final EditText inputNota = new EditText(context);
        inputNota.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        inputNota.setHint("Valor de nota");
        builder.setView(inputNota);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String valorStr = inputNota.getText().toString().trim();
            if (!valorStr.isEmpty()) {
                try {
                    double valor = Double.parseDouble(valorStr);

                    if (esEditar) {
                        List<Nota> notas = notaController.obtenerNotasPorEstudiante(estudianteId);
                        if (!notas.isEmpty()) {
                            notaController.wait(notas.get(0).getId(), valor);
                            Toast.makeText(context, "Nota editada", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "No hay notas para editar", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        notaController.agregarNota(estudianteId, valor);
                        Toast.makeText(context, "Nota agregada", Toast.LENGTH_SHORT).show();
                    }

                    // Refrescar lista
                    notifyDataSetChanged();
                } catch (NumberFormatException e) {
                    Toast.makeText(context, "Valor inválido", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "El campo no puede estar vacío", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoEliminar(int estudianteId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Eliminar Nota");
        builder.setMessage("¿Estás seguro de eliminar la nota?");
        builder.setPositiveButton("Sí", (dialog, which) -> {
            List<Nota> notas = notaController.obtenerNotasPorEstudiante(estudianteId);
            if (!notas.isEmpty()) {
                notaController.eliminarNota(notas.get(0).getId());
                Toast.makeText(context, "Nota eliminada", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            } else {
                Toast.makeText(context, "No hay notas para eliminar", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}
