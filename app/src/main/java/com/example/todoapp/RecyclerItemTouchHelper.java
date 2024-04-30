package com.example.todoapp;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Adapter.TodoAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private TodoAdapter adapter;

    public RecyclerItemTouchHelper(TodoAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT) {
            AlertDialog.Builder builder = getBuilder(position);
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            adapter.editItem(position);
        }
    }

    @NonNull
    private AlertDialog.Builder getBuilder(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
        builder.setTitle("DELETE TASK");
        builder.setMessage("Are you sure you want to delete this task");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.deleteItem(position);
                    }
                });
        return builder;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Drawable icon = null;
        ColorDrawable background = null;
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        if (dX > 0) { // Swiping to the right
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_edit); // Check your drawable names
            background = new ColorDrawable(ContextCompat.getColor(adapter.getContext(), R.color.dark_green));

            int iconMargin = (itemView.getHeight() - (icon != null ? icon.getIntrinsicHeight() : 0)) / 2;
            int iconTop = itemView.getTop() + iconMargin;
            int iconBottom = iconTop + (icon != null ? icon.getIntrinsicHeight() : 0);
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = iconLeft + (icon != null ? icon.getIntrinsicWidth() : 0);

            if (icon != null) {
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            }
            background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
        } else if (dX < 0) { // Swiping to the left
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.baseline_delete); // Check your drawable names
            background = new ColorDrawable(Color.RED);

            int iconMargin = (itemView.getHeight() - (icon != null ? icon.getIntrinsicHeight() : 0)) / 2;
            int iconTop = itemView.getTop() + iconMargin;
            int iconBottom = iconTop + (icon != null ? icon.getIntrinsicHeight() : 0);
            int iconLeft = itemView.getRight() - iconMargin - (icon != null ? icon.getIntrinsicWidth() : 0);
            int iconRight = itemView.getRight() - iconMargin;

            if (icon != null) {
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            }
            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        }

        if (background != null) {
            background.draw(c);
        }
        if (icon != null) {
            icon.draw(c);
        }
    }

}

