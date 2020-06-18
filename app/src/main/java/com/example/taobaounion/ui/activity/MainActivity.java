package com.example.taobaounion.ui.activity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseActivity;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.ui.fragment.ChoicenessFragment;
import com.example.taobaounion.ui.fragment.HomeFragment;
import com.example.taobaounion.ui.fragment.OnSellFragment;
import com.example.taobaounion.ui.fragment.SearchFragment;
import com.example.taobaounion.utils.LogUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements IMainActivity{

    @BindView(R.id.main_navigation_bar)
    BottomNavigationView mBar;
    private HomeFragment mHomeFragment;
    private OnSellFragment mRedPacketFragment;
    private SearchFragment mSearchFragment;
    private ChoicenessFragment mChoicenessFragment;
    private FragmentManager mFragmentManager;

    @Override
    protected void initView() {
        mHomeFragment = new HomeFragment();
        mRedPacketFragment = new OnSellFragment();
        mSearchFragment = new SearchFragment();
        mChoicenessFragment = new ChoicenessFragment();
        mFragmentManager = getSupportFragmentManager();
        switchFragment(mHomeFragment);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initPresent() {

    }

    @Override
    protected void initEvent() {
        initListen();
    }

    private void initListen() {
//        设置导航栏item被选中的监听事件
//        根据选中的ID，来切换Fragment
        mBar.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    LogUtils.d(MainActivity.this, (item.getItemId() == R.id.home) + "被选中" + item.getTitle());
                    switchFragment(mHomeFragment);
                    break;
                case R.id.selected:
                    LogUtils.d(MainActivity.this, (item.getItemId() == R.id.selected) + "被选中" + item.getTitle());
                    switchFragment(mChoicenessFragment);
                    break;
                case R.id.red_packet:
                    LogUtils.d(MainActivity.this, (item.getItemId() == R.id.red_packet) + "被选中" + item.getTitle());
                    switchFragment(mRedPacketFragment);
                    break;
                case R.id.search:
                    LogUtils.d(MainActivity.this, (item.getItemId() == R.id.search) + "被选中" + item.getTitle());
                    switchFragment(mSearchFragment);
                    break;
            }
            return true;
        });
    }

    private BaseFragment lastFragment = null;

    /**
     * 使用FragmentTransaction 把targetFragment都添加进去
     * 如果添加有，就show，否则就add
     * 如果lastFragment 上一个fragment存在并且，
     *  targetFragment !=lastFragment;就隐藏上一个
     * @param targetFragment ：添加/显示的Fragment
     */
    private void switchFragment(BaseFragment targetFragment) {
//        使用FragmentTransaction 把Fragment都添加进去
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (!targetFragment.isAdded()) {
            fragmentTransaction.add(R.id.main_page_container, targetFragment);

        } else {
            fragmentTransaction.show(targetFragment);
        }

        if (lastFragment != null && lastFragment != targetFragment) {
            fragmentTransaction.hide(lastFragment);
        }
        lastFragment = targetFragment;
//        fragmentTransaction.replace(R.id.main_page_container,targetFragment);
        fragmentTransaction.commit();
    }


    @Override
    public void switch2Search() {
        mBar.setSelectedItemId(R.id.search);
    }
}
