package comin.chaten.statens.caugfitten.straighttoend.its;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import comin.chaten.statens.caugfitten.R;
import comin.chaten.statens.caugfitten.straighttoend.BaseActivity;
import comin.chaten.statens.caugfitten.straighttoend.its.fragments.account.FragmentAddAcoount;
import comin.chaten.statens.caugfitten.straighttoend.its.fragments.contacts.ContactsFragment;
import comin.chaten.statens.caugfitten.straighttoend.its.fragments.contacts.SwitchUser;
import comin.chaten.statens.caugfitten.straighttoend.its.fragments.messenger.MessengerFragment;
import comin.chaten.statens.caugfitten.straighttoend.its.fragments.migration.MigrationFragment;

public class MainActivity extends BaseActivity {

    private ViewPager pager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = (ViewPager)findViewById(R.id.activity_main_viewpager);
        if (pager != null) setupViewPager();

        tabLayout = (TabLayout) findViewById(R.id.activity_main_tablayout);
        if (tabLayout != null) setupTabLayout();
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    private void setupViewPager() {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new SwitchUser() , "Switch User");
        adapter.addFragment(new ContactsFragment(), "View Details");
        adapter.addFragment(new MessengerFragment(), "Message");
        adapter.addFragment(new MigrationFragment(), "Server Details");
        adapter.addFragment(new FragmentAddAcoount(), "Credential");
        pager.setAdapter(adapter);
    }

    private void setupTabLayout() {
        if (pager != null) tabLayout.setupWithViewPager(pager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    private class Adapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        private Adapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
}
