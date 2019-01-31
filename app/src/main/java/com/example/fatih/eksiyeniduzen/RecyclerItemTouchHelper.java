package com.example.fatih.eksiyeniduzen;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_IDLE;
import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE;

/**
 * Created by Fatih on 31.10.2017.
 */



 class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private RecyclerItemTouchHelperListener listener;



    float sayı= (float) 250.0;


  //  boolean swipeBack=false;


     RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            if(viewHolder instanceof BadiEngelAdapter.ViewHolder)
            {
                final View foregroundView = ((BadiEngelAdapter.ViewHolder) viewHolder).onplan;

                getDefaultUIUtil().onSelected(foregroundView);
            }
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        if(viewHolder instanceof BadiEngelAdapter.ViewHolder)
        {



            final View foregroundView = ((BadiEngelAdapter.ViewHolder) viewHolder).onplan;
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }

    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        if(viewHolder instanceof BadiEngelAdapter.ViewHolder)
        {

            final View foregroundView = ((BadiEngelAdapter.ViewHolder) viewHolder).onplan;
            getDefaultUIUtil().clearView(foregroundView);
        }


    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {

        if(viewHolder instanceof BadiEngelAdapter.ViewHolder)
        {


          if(dX>0)
          {
              ((BadiEngelAdapter.ViewHolder) viewHolder).arkaplan.setVisibility(View.GONE);
              ((BadiEngelAdapter.ViewHolder) viewHolder).arka2.setVisibility(View.VISIBLE);
          }
           else
          {
              ((BadiEngelAdapter.ViewHolder) viewHolder).arka2.setVisibility(View.GONE);
              ((BadiEngelAdapter.ViewHolder) viewHolder).arkaplan.setVisibility(View.VISIBLE);
          }


            final View foregroundView = ((BadiEngelAdapter.ViewHolder) viewHolder).onplan;

            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }


    }




    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
      /*  if (swipeBack) {
            swipeBack = false;          //BUTON ŞEKLİNDE DURDURMAK İÇİN DENE
            return 0;
        }*/
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }


}