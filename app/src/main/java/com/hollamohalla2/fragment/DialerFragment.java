package com.hollamohalla2.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hollamohalla2.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;


public class DialerFragment extends Fragment {


   /* String[] mResources = {
            "01887230300",
            "118"
    };

    String[] mResources2 = {

            getResources().getString(R.string.emergencycontact)+
            "\n"                                  //1
            ,
            getResources().getString(R.string.policecontrolroom)+
            "\n"
    };*/

    String mResources3[] =  new String[2];
    String mResources4[] = new String[2];


    @BindView(R.id.vpagerhome)
    AutoScrollViewPager vPager;


    public DialerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mResources3[0]="01887230300";
        mResources3[1]="118";

        mResources4[0]= getActivity().getResources().getString(R.string.emergencycontact)+"\n";
        mResources4[1]= getActivity().getResources().getString(R.string.policecontrolroom)+"\n";



        vPager.setAdapter(new CustomPagerAdapter(getActivity()));

        vPager.startAutoScroll(5000);
        vPager.setInterval(3000);
    }

    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mResources3.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((CardView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
            TextView textView = (TextView) itemView.findViewById(R.id.tvContactNumber);
            TextView desc = (TextView) itemView.findViewById(R.id.desc);


            desc.setText(mResources4[position]);

            textView.setMovementMethod(LinkMovementMethod.getInstance());

            SpannableString content = new SpannableString(mResources3[position]);
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            content.setSpan(new ForegroundColorSpan(Color.RED), 0,  content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            textView.setText(content);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + mResources3[position]));
                    startActivity(intent);
                }
            });


           /* String number = "12345678";
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" +number));
            startActivity(intent);*/

            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((CardView) object);
        }

    }

}
