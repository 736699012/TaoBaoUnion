package com.example.taobaounion.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.taobaounion.model.bean.Categories;
import com.example.taobaounion.ui.fragment.HomePagerFragment;

import java.util.ArrayList;
import java.util.List;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private List<Categories.DataBean> mCategoryList = new ArrayList<>();

    public HomePagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
//        LogUtils.d(this,"title " + position +mCategoryList.get(position).getTitle());
        return mCategoryList.get(position).getTitle();
    }

    //    pager绑定对应Fragment
    @NonNull
    @Override
    public Fragment getItem(int position) {
//        LogUtils.d(this,"position ==" + position);
        Categories.DataBean dataBean = mCategoryList.get(position);
        HomePagerFragment homePagerFragment =HomePagerFragment.newInstance(dataBean);
        return homePagerFragment;
    }

    @Override
    public int getCount() {
        return mCategoryList.size();
    }

    public void setCategories(Categories categories) {
        List<Categories.DataBean> data = categories.getData();
        mCategoryList.clear();
        mCategoryList.addAll(data);
//        LogUtils.d(this,"size ==" +mCategoryList.size());
        notifyDataSetChanged();

    }
}
