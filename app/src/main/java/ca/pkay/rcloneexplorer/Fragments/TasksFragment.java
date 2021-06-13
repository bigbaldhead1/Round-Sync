package ca.pkay.rcloneexplorer.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import ca.pkay.rcloneexplorer.Database.DatabaseHandler;
import ca.pkay.rcloneexplorer.Database.json.Exporter;
import ca.pkay.rcloneexplorer.R;
import ca.pkay.rcloneexplorer.RecyclerViewAdapters.TasksRecyclerViewAdapter;
import ca.pkay.rcloneexplorer.TaskActivity;
import es.dmoral.toasty.Toasty;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

public class TasksFragment extends Fragment {

    private TasksRecyclerViewAdapter recyclerViewAdapter;
    private View fragmentView;


    public TasksFragment() {
        // Required empty public constructor
    }

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((FragmentActivity) getContext()).setTitle(R.string.tasks);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        fragmentView = view;
        populateTaskList(fragmentView);

        view.findViewById(R.id.newTask).setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), TaskActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateTaskList(fragmentView);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.tasks_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_import:
                break;
            case R.id.action_export:
                try {
                    Exporter.create(getActivity());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateTaskList(View v){
        Context c = v.getContext();
        DatabaseHandler dbHandler = new DatabaseHandler(c);

        RecyclerView recyclerView =  v.findViewById(R.id.task_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(c));
        recyclerView.setItemAnimator(new LandingAnimator());

        recyclerViewAdapter = new TasksRecyclerViewAdapter(dbHandler.getAllTasks(), c);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        Activity activity = getActivity();
        Context context = getContext();

        if(activity==null){
            Toasty.error(context, context.getResources().getString(R.string.importer_unknown_error), Toast.LENGTH_SHORT, true).show();
            return;
        }
    }
}