package com.example.project_ui.ui.todo;

import static com.example.project_ui.RoomDataBase.Todo.DataBase.DB_NAME;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.project_ui.R;
import com.example.project_ui.RoomDataBase.Todo.DataBase;
import com.example.project_ui.RoomDataBase.Todo.TodoDao;
import com.example.project_ui.RoomDataBase.Todo.TodoEvents;

import com.example.project_ui.databinding.FragmentTodoBinding;
import com.example.project_ui.ui.todo.Todo.LanguageRVAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.lang.String;

public class TodoFragment extends Fragment {
    private RecyclerView recyclerView;
    private LanguageRVAdapter lngRVAdapter;
    private EditText addEdt;
    private Button addBtn;
    private ArrayList<String> lngList;

    private TodoDao todoDao;



    private FragmentTodoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        DataBase db = Room.databaseBuilder(getContext(), DataBase.class, DB_NAME).allowMainThreadQueries().build();
        todoDao = db.todoDao();

        recyclerView = view.findViewById(R.id.recycler_view);
        addEdt = view.findViewById(R.id.idEdtAdd);
        addBtn = view.findViewById(R.id.idBtnAdd);
        lngList = new ArrayList<>();



        lngRVAdapter = new LanguageRVAdapter(lngList);
        recyclerView.setAdapter(lngRVAdapter);
        recover();

        // on below line we are adding click listener for our add button.
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are calling
                // add item method.
                String addStr = addEdt.getText().toString();
                addItem(addStr);
                // add to database
                TodoEvents todoEvents = new TodoEvents(addStr);
                todoDao.insertData(todoEvents);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // this method is called
                // when the item is moved.
                return false;
            }

            //@Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.
                String deletedCourse = lngList.get(viewHolder.getAdapterPosition());

                // below line is to get the position
                // of the item at that position.
                int position = viewHolder.getAdapterPosition();

                // delete from database
                todoDao.deleteData(lngList.get(position));

                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                lngList.remove(viewHolder.getAdapterPosition());

                // below line is to notify our item is removed from adapter.
                lngRVAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                // below line is to display our snackbar with action.
                Snackbar.make(recyclerView, deletedCourse, Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // adding on click listener to our action of snack bar.
                        // below line is to add our item to array list with a position.
                        lngList.add(position, deletedCourse);

                        // add to database
                        TodoEvents todoEvents = new TodoEvents(lngList.get(position));
                        todoDao.insertData(todoEvents);

                        // below line is to notify item is
                        // added to our adapter class.
                        lngRVAdapter.notifyItemInserted(position);
                    }
                }).show();
            }
            // at last we are adding this
            // to our recycler view.
        }).attachToRecyclerView(recyclerView);
        return view;

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void addItem(String item) {
        // on below line we are checking
        // if item is empty or not.
        if (!item.isEmpty()) {
            // on below line we are adding
            // item to our list
            lngList.add(item);

            // on below line we are notifying
            // adapter that data has updated.
            lngRVAdapter.notifyDataSetChanged();
            addEdt.setText("");
        }
    }


    private void recover(){
        ArrayList<TodoEvents> allEvents = new ArrayList<>(todoDao.getAll());
        for (TodoEvents event:allEvents) {
            addItem(event.getEvent());
        }
    }
}


