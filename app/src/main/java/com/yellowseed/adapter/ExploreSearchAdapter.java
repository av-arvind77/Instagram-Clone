package com.yellowseed.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ExploreSearchAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();


    public ExploreSearchAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }



    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();

    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
//    private ArrayList<FollowingRequestModel> arrayList;
//    private Context context;
//
//    public ExploreSearchAdapter(ArrayList<FollowingRequestModel> arrayList, Context context) {
//        this.arrayList = arrayList;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public ExploreSearchAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_exploresearch,parent,false);
//        return new ListViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ExploreSearchAdapter.ListViewHolder holder, int position) {
//        holder.binding.tvExploreSearchName.setText(arrayList.get(position).getUserFollowingName());
//        holder.binding.ivExploreSearchUser.setImageResource(arrayList.get(position).getUserFollowersPicture());
//        holder.binding.tvExploreSearchFollowers.setText(arrayList.get(position).getUserFollowers());
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return  arrayList.size();
//    }
//
//    public class ListViewHolder extends RecyclerView.ViewHolder {
//        private LayoutExploresearchBinding binding;
//        public ListViewHolder(View itemView) {
//            super(itemView);
//            binding = DataBindingUtil.bind(itemView);
//        }
//    }
}
