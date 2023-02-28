package com.tafarri.tafarri.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.tafarri.tafarri.Fragments.WelcomeFragment;
import com.tafarri.tafarri.Fragments.SettingsFragment;
import com.tafarri.tafarri.R;
import com.tafarri.tafarri.Util.Config;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private MyPageAdapter pageAdapter;
    private ViewPager mFragmentsHolderViewPager;
    private ImageView mSettingsIconImageView, mInfoImageView;
    private ConstraintLayout mInfoViewHolderConstraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentsHolderViewPager = findViewById(R.id.activity_mainactivity_fragments_holder_viewpager);
        mSettingsIconImageView = findViewById(R.id.activity_mainactivity_constraintlayout2_menuicon_imageview);
        //mInfoImageView = findViewById(R.id.activity_mainactivity_constraintlayout2_searchicon_imageview);
        mInfoViewHolderConstraintLayout = findViewById(R.id.activity_mainactivity_constraintlayout2_profileicon_holder_constraintlayout);


        mFragmentsHolderViewPager.addOnPageChangeListener(viewListener);

        List<Fragment> fragmentsList = getFragments();

        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragmentsList);
        mFragmentsHolderViewPager.setAdapter(pageAdapter);
        mFragmentsHolderViewPager.setCurrentItem(0);

        mSettingsIconImageView.setOnClickListener(this);
        //mInfoImageView.setOnClickListener(this);
        mInfoViewHolderConstraintLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mSettingsIconImageView.getId()){
            mFragmentsHolderViewPager.setCurrentItem(0);
        } else if(view.getId() == mInfoViewHolderConstraintLayout.getId()){
            //mFragmentsHolderViewPager.setCurrentItem(1);
            String url = Config.LINK_WEB_HOW_TO_VIEW;
            Config.show_log_in_console("websiteUrl", "LINK_WEB_HOW_TO_VIEW: " + url);
            Config.openActivity(MainActivity.this, WebViewActivity.class, 1, 0, 1, Config.WEBVIEW_KEY_URL, url);
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