package com.hiit.timer;


import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PopUp {


    public void showPopupWindow(final View view, DataModel dataModel) {

        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        TextView name = popupView.findViewById(R.id.nameExo);
        name.setText(dataModel.getName());
        TextView description = popupView.findViewById(R.id.descriptionExo);
        description.setText(dataModel.getDescription());

        ImageView imageView = popupView.findViewById(R.id.imgExo);
        String url = dataModel.getImgURL();
        Picasso.get().load(url).resize(90, 90).into(imageView);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                popupWindow.dismiss();
                return true;
            }
        });
    }

}