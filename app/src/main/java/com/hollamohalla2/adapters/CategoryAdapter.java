package com.hollamohalla2.adapters;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.hollamohalla2.R;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.orhanobut.hawk.Hawk;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends AbstractItem<CategoryAdapter, CategoryAdapter.ViewHolder>  {

    private Integer id;
    private String name;
    private String namePunjabi;
    private Integer count;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    //The unique ID for this type of item
    @Override
    public int getType() {
        return R.id.cast_button_type_closed_caption;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.row_main_category;
    }

    @Override
    public ViewHolder getViewHolder(@NonNull View v) {
        return new ViewHolder(v);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNamePunjabi() {
        return namePunjabi;
    }

    public void setNamePunjabi(String namePunjabi) {
        this.namePunjabi = namePunjabi;
    }

    /**
     * our ViewHolder
     */
    public static class ViewHolder extends FastAdapter.ViewHolder<CategoryAdapter> {


        @BindView(R.id.catergoryname)
        TextView tvname;
        @BindView(R.id.tvcount)
        TextView tvCount;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void bindView(CategoryAdapter item, List<Object> payloads) {

            if((Integer)Hawk.get("lang")==1)
            {

                tvname.setText(item.name);
            }
            else
            {
                tvname.setText(item.namePunjabi);

            }







            tvCount.setText(item.count+"");

           /* StringHolder.applyTo(item.name, name);
            StringHolder.applyToOrHide(item.description, description);*/
        }

        @Override
        public void unbindView(CategoryAdapter item) {
            tvname.setText(null);
            tvCount.setText(null);
        }
    }
}