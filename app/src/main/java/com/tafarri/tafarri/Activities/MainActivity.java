package com.tafarri.tafarri.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.tafarri.tafarri.Fragments.ContactFragment;
import com.tafarri.tafarri.Fragments.MyBooksFragment;
import com.tafarri.tafarri.Fragments.SummariesFragment;
import com.tafarri.tafarri.Fragments.WelcomeFragment;
import com.tafarri.tafarri.Fragments.ReadFragment;
import com.tafarri.tafarri.R;
import com.tafarri.tafarri.Util.Config;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener{

    private MyPageAdapter pageAdapter;
    private ViewPager mFragmentsHolderViewPager;
    private ConstraintLayout mInfoViewHolderConstraintLayout;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.bottomNavigationView);
        mFragmentsHolderViewPager = findViewById(R.id.activity_mainactivity_fragments_holder_viewpager);


        mBottomNavigationView.setOnItemSelectedListener(this);

        mFragmentsHolderViewPager.addOnPageChangeListener(viewListener);

        List<Fragment> fragmentsList = getFragments();

        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragmentsList);
        mFragmentsHolderViewPager.setAdapter(pageAdapter);


        if(!Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_BOOK_NAME).trim().equalsIgnoreCase("") && !Config.getSharedPreferenceString(getApplicationContext(), Config.SHARED_PREF_KEY_LAST_READING_PDF_URL).trim().equalsIgnoreCase("")){
            mFragmentsHolderViewPager.setCurrentItem(0);
            mBottomNavigationView.setSelectedItemId(R.id.read);
        } else {
            mFragmentsHolderViewPager.setCurrentItem(1);
            mBottomNavigationView.setSelectedItemId(R.id.summaries);
        }


    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mInfoViewHolderConstraintLayout.getId()){
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

        fList.add(ReadFragment.newInstance());
        fList.add(SummariesFragment.newInstance());
        fList.add(MyBooksFragment.newInstance());
        fList.add(ContactFragment.newInstance());
        return fList;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.read:
                mFragmentsHolderViewPager.setCurrentItem(0);
                return true;

            case R.id.summaries:
                mFragmentsHolderViewPager.setCurrentItem(1);
                return true;

            case R.id.mybooks:
                mFragmentsHolderViewPager.setCurrentItem(2);
                return true;

            case R.id.contact:
                mFragmentsHolderViewPager.setCurrentItem(3);
                return true;
        }
        return false;
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