package com.yellowseed.activity;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.yellowseed.adapter.ChatFragmentAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private RecyclerItemTouchHelperListener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs,RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View forgroundView = ((ChatFragmentAdapter.ListViewHolder) viewHolder).binding.llChatFraUser;

            getDefaultUIUtil().onSelected(forgroundView);

        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final View forgroundView = ((ChatFragmentAdapter.ListViewHolder) viewHolder).binding.llChatFraUser;
        getDefaultUIUtil().clearView(forgroundView);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        final View forgroundView = ((ChatFragmentAdapter.ListViewHolder) viewHolder).binding.llChatFraUser;
        getDefaultUIUtil().onDraw(c, recyclerView, forgroundView, dX/2, dY, actionState, isCurrentlyActive);
    }


    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        final View forgroundView = ((ChatFragmentAdapter.ListViewHolder) viewHolder).binding.llChatFraUser;
        getDefaultUIUtil().onDrawOver(c, recyclerView, forgroundView, dX/2, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}