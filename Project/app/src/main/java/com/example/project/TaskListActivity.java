package com.example.project;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Collections;
import java.util.Comparator;
import android.widget.AdapterView;

public class TaskListActivity extends AppCompatActivity {

    ListView listViewTasks;
    Spinner spinnerSort;
    ArrayAdapter<Task> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        listViewTasks = findViewById(R.id.listViewTasks);
        spinnerSort = findViewById(R.id.spinnerSort);

        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.sort_array,
                android.R.layout.simple_spinner_item
        );
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(sortAdapter);

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {

                switch (position) {

                    case 0: // Sort by Title
                        Collections.sort(TaskRepository.taskList, Comparator.comparing(Task::getTitle));
                        break;

                    case 1: // Sort by Priority
                        Collections.sort(TaskRepository.taskList, (t1, t2) -> {
                            return getPriorityValue(t1.getPriority()) - getPriorityValue(t2.getPriority());
                        });
                        break;

                    case 2: // Sort by Status
                        Collections.sort(TaskRepository.taskList, Comparator.comparing(Task::getStatus));
                        break;
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, TaskRepository.taskList);
        listViewTasks.setAdapter(adapter);

        listViewTasks.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(TaskListActivity.this, AddEditTaskActivity.class);
            intent.putExtra("taskIndex", position);
            startActivity(intent);
        });

        listViewTasks.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete Task");
            builder.setMessage("Delete task: " + TaskRepository.taskList.get(position).getTitle() + "?");

            builder.setPositiveButton("Yes", (dialog, which) -> {
                TaskRepository.taskList.remove(position);
                adapter.notifyDataSetChanged();
            });

            builder.setNegativeButton("No", null);
            builder.show();

            return true;
        });
    }

    private int getPriorityValue(String priority) {

        if (priority.startsWith("High"))
            return 1;

        if (priority.startsWith("Medium"))
            return 2;

        if (priority.startsWith("Low"))
            return 3;

        return 4;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
