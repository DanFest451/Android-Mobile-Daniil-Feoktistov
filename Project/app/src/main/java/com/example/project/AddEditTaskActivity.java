package com.example.project;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class AddEditTaskActivity extends AppCompatActivity {

    EditText etTaskTitle, etTaskDescription;
    Spinner spinnerPriority, spinnerStatus;
    Button btnSaveTask;
    TextView tvFormTitle;

    int taskIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        etTaskTitle = findViewById(R.id.etTaskTitle);
        etTaskDescription = findViewById(R.id.etTaskDescription);
        spinnerPriority = findViewById(R.id.spinnerPriority);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnSaveTask = findViewById(R.id.btnSaveTask);
        tvFormTitle = findViewById(R.id.tvFormTitle);

        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.priority_array,
                android.R.layout.simple_spinner_item
        );
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(priorityAdapter);

        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.status_array,
                android.R.layout.simple_spinner_item
        );
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);

        taskIndex = getIntent().getIntExtra("taskIndex", -1);

        if (taskIndex != -1) {
            Task task = TaskRepository.taskList.get(taskIndex);

            etTaskTitle.setText(task.getTitle());
            etTaskDescription.setText(task.getDescription());

            setSpinnerToValue(spinnerPriority, task.getPriority());
            setSpinnerToValue(spinnerStatus, task.getStatus());

            btnSaveTask.setText("Update Task");
            tvFormTitle.setText("Edit Task");
        }

        btnSaveTask.setOnClickListener(v -> {
            String title = etTaskTitle.getText().toString().trim();
            String description = etTaskDescription.getText().toString().trim();
            String priority = spinnerPriority.getSelectedItem().toString();
            String status = spinnerStatus.getSelectedItem().toString();

            if (title.isEmpty()) {
                etTaskTitle.setError("Enter task title");
                return;
            }

            if (taskIndex == -1) {
                Task task = new Task(title, description, priority, status);
                TaskRepository.taskList.add(task);
                Toast.makeText(this, "Task saved", Toast.LENGTH_SHORT).show();
            } else {
                Task task = TaskRepository.taskList.get(taskIndex);
                task.setTitle(title);
                task.setDescription(description);
                task.setPriority(priority);
                task.setStatus(status);
                Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show();
            }

            finish();
        });
    }

    private void setSpinnerToValue(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}
