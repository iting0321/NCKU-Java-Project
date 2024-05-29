package com.example.project_ui.ui.home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.DisplayMetrics;
import com.example.project_ui.R;
import com.example.project_ui.databinding.FragmentDashboardBinding;
import com.example.project_ui.databinding.FragmentHomeBinding;
import com.example.project_ui.ui.dashboard.DashboardViewModel;
import com.rmondjone.locktableview.LockTableView;
import com.rmondjone.locktableview.DisplayUtil;
import com.rmondjone.xrecyclerview.ProgressStyle;
import com.rmondjone.xrecyclerview.XRecyclerView;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private LinearLayout mContentView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        mContentView = root.findViewById(R.id.contentView_home);
        run();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void run(){
        initDisplayOpinion();

        ArrayList<ArrayList<String>> form_data = new ArrayList<ArrayList<String>>();
        ArrayList<String> first_row = new ArrayList<String>();
        first_row.add("時段");
        first_row.add("星期日");
        first_row.add("星期一");
        first_row.add("星期二");
        first_row.add("星期三");
        first_row.add("星期四");
        first_row.add("星期五");
        first_row.add("星期六");
        form_data.add(first_row);
        ArrayList<String> date_row = new ArrayList<String>();
        for(int i = 0; i <= 7; i++){
            date_row.add("");///////////////input_date
        }
        form_data.add(date_row);
        for(int i = 0; i <= 24; i++){
            ArrayList<String> row_data = new ArrayList<String>();
            if(i < 10){
                row_data.add("0" + i + "  ：00 ~ 0" + i + "  ：59");
            }
            else if(i == 24) row_data.add("");
            else{
                row_data.add(i + "  ：00 ~ " + i + "  ：59");
            }

            for(int j = 0; j < 7; j++){
                row_data.add("");///////////////////////////////data_input
            }
            form_data.add(row_data);
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)requireContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidthDp = (int) (displayMetrics.widthPixels /  displayMetrics.density);

        // 计算一半的宽度
        final LockTableView mLockTableView = new LockTableView(requireContext(), mContentView, form_data);
        mLockTableView.setLockFristRow(true);
        mLockTableView.setTextViewSize(17);
        mLockTableView.setMinRowHeight(45);
        mLockTableView.setMaxRowHeight(45);
        mLockTableView.setColumnWidth(0, screenWidthDp * 7 / 16);
        for(int i = 1; i <= 7; i++){
            mLockTableView.setColumnWidth(i,screenWidthDp * 7 / 16);
        }

        mLockTableView.setCellPadding(0);
        mLockTableView.setNullableString("");
        mLockTableView.setOnLoadingListener(new LockTableView.OnLoadingListener() {
            //下拉刷新、上拉加载监听
            @Override
            public void onRefresh(final XRecyclerView mXRecyclerView, final ArrayList<ArrayList<String>> mTableDatas) {
                //如需更新表格数据调用,部分刷新不会全部重绘
                mLockTableView.setTableDatas(mTableDatas);
                //停止刷新
                mXRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore(final XRecyclerView mXRecyclerView, final ArrayList<ArrayList<String>> mTableDatas) {
                //如需更新表格数据调用,部分刷新不会全部重绘
                mLockTableView.setTableDatas(mTableDatas);
                //停止刷新
                mXRecyclerView.loadMoreComplete();
                //如果没有更多数据调用
                mXRecyclerView.setNoMore(true);
            }
        });
        mLockTableView.show();
        mLockTableView.getTableScrollView().setPullRefreshEnabled(true);
        mLockTableView.getTableScrollView().setLoadingMoreEnabled(true);
        mLockTableView.getTableScrollView().setRefreshProgressStyle(ProgressStyle.SquareSpin);
    }

    private void initDisplayOpinion() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        DisplayUtil.density = dm.density;
        DisplayUtil.densityDPI = dm.densityDpi;
        DisplayUtil.screenWidthPx = dm.widthPixels;
        DisplayUtil.screenhightPx = dm.heightPixels;
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(requireContext(), dm.widthPixels);
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(requireContext(), dm.heightPixels);
    }
}