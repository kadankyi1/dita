package com.fishpott.dita.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.fishpott.dita.Fragments.WelcomeFragment;
import com.fishpott.dita.Fragments.SettingsFragment;
import com.fishpott.dita.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private MyPageAdapter pageAdapter;
    private ViewPager mFragmentsHolderViewPager;
    private ImageView mSettingsIconImageView, mInfoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentsHolderViewPager = findViewById(R.id.activity_mainactivity_fragments_holder_viewpager);
        mSettingsIconImageView = findViewById(R.id.activity_mainactivity_constraintlayout2_menuicon_imageview);
        mInfoImageView = findViewById(R.id.activity_mainactivity_constraintlayout2_searchicon_imageview);


        mFragmentsHolderViewPager.addOnPageChangeListener(viewListener);

        List<Fragment> fragmentsList = getFragments();

        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragmentsList);
        mFragmentsHolderViewPager.setAdapter(pageAdapter);
        mFragmentsHolderViewPager.setCurrentItem(1);

        mSettingsIconImageView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mSettingsIconImageView.getId()){
            mFragmentsHolderViewPager.setCurrentItem(0);
        }
    }


    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private List<Fragment> getFragments(){
        List<Fragment> fList = new ArrayList<Fragment>();

        fList.add(SettingsFragment.newInstance());
        fList.add(WelcomeFragment.newInstance());
        return fList;
    }


    // THE FRAGMENT ADAPTER
    private class MyPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;

        public MyPageAdapter(FragmentManager fragmentManager, List<Fragment> fragmentsList ) {
            super(fragmentManager);
            this.fragmentList = fragmentsList;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return this.fragmentList.size();
        }
    }

}