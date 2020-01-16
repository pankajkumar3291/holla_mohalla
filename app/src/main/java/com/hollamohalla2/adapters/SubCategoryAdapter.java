package com.hollamohalla2.adapters;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.hollamohalla2.R;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubCategoryAdapter extends AbstractItem<SubCategoryAdapter, SubCategoryAdapter.ViewHolder> implements Serializable {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String name;
    public  String lat;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public  String lng;

    public Double distance;
    public double steps ;







    //The unique ID for this type of item
    @Override
    public int getType() {
        return R.id.cast_button_type_closed_caption;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.row_main_sub_category;
    }

    @Override
    public ViewHolder getViewHolder(@NonNull View v) {
        return new ViewHolder(v);
    }



    /**
     * our ViewHolder
     */
    public static class ViewHolder extends FastAdapter.ViewHolder<SubCategoryAdapter> {
        @BindView(R.id.catergoryname)
        TextView tvname;

        @BindView(R.id.textView3)
        TextView tvSteps;








        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void bindView(SubCategoryAdapter item, final List<Object> payloads) {


            tvname.setText(item.name);

          /*  String steps = item.steps+"";*/


            double v = item.distance*1000;
            tvSteps.setText("Meters :"+Double.valueOf(v).intValue()+" Steps :" +Double.valueOf(item.steps).intValue());


           /* StringHolder.applyTo(item.name, name);
            StringHolder.applyToOrHide(item.description, description);*/
        }

        @Override
        public void unbindView(SubCategoryAdapter item) {
            tvname.setText(null);

        }
    }
}