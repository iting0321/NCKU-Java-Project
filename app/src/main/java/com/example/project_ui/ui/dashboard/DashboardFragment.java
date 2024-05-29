package com.example.project_ui.ui.dashboard;

import android.app.AlertDialog;
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


public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private LinearLayout recordLayout;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        recordLayout = root.findViewById(R.id.contentView_dashboard);
        run1();
        
        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void run1(){

        initDisplayOpinion();

        ArrayList<ArrayList<String>> record_from = new ArrayList<ArrayList<String>>();
        ArrayList<String> first_row2 = new ArrayList<String>();
        first_row2.add("時段");
        first_row2.add("紀錄");
        record_from.add(first_row2);
        ArrayList<String> Date_row = new ArrayList<String>();
        Date_row.add("");
        Date_row.add("");
        record_from.add(Date_row);
        for(int i = 0; i <= 24; i++){
            ArrayList<String> row_list = new ArrayList<String>();
            if(i < 10){
                row_list.add("0" + i + "  ：00 ~ 0" + i + "  ：59");
            }
            else if(i == 24) row_list.add("");
            else{
                row_list.add(i + "  ：00 ~ " + i + "  ：59");
            }
            row_list.add("");///////////////////////////////data_input
            record_from.add(row_list);

        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)requireContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidthDp = (int) (displayMetrics.widthPixels /  displayMetrics.density);

        // 计算一半的宽度
        final LockTableView record_table = new LockTableView(requireContext(), recordLayout, record_from);
        record_table.setLockFristRow(true);
        record_table.setTextViewSize(17);
        record_table.setMinRowHeight(45);
        record_table.setMaxRowHeight(45);
        record_table.setColumnWidth(0, screenWidthDp / 2);
        record_table.setColumnWidth(1, screenWidthDp / 2);
        record_table.setCellPadding(0);
        record_table.setNullableString("");
        record_table.setOnLoadingListener(new LockTableView.OnLoadingListener() {
            //下拉刷新、上拉加载监听
            @Override
            public void onRefresh(final XRecyclerView mXRecyclerView, final ArrayList<ArrayList<String>> mTableDatas) {
                //如需更新表格数据调用,部分刷新不会全部重绘
                record_table.setTableDatas(mTableDatas);
                //停止刷新
                mXRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore(final XRecyclerView mXRecyclerView, final ArrayList<ArrayList<String>> mTableDatas) {
                //如需更新表格数据调用,部分刷新不会全部重绘
                record_table.setTableDatas(mTableDatas);
                //停止刷新
                mXRecyclerView.loadMoreComplete();
                //如果没有更多数据调用
                mXRecyclerView.setNoMore(true);
            }
        });
        record_table.setOnItemClickListenter(new LockTableView.OnItemClickListenter() {
            @Override
            public void onItemClick(View view, int i) {
                if(i == 0){
                    String record_time = "日期";
                    String record_Text = record_from.get(i).get(1);
                    messageBox(record_time, record_Text);
                }
                else if(i != 24){
                    String record_time = record_from.get(i).get(0);
                    String record_Text = record_from.get(i).get(1);
                    messageBox(record_time, record_Text);
                }


                // 显示完整文本

            }
        });
        record_table.show();
        record_table.getTableScrollView().setPullRefreshEnabled(true);
        record_table.getTableScrollView().setLoadingMoreEnabled(true);
        record_table.getTableScrollView().setRefreshProgressStyle(ProgressStyle.SquareSpin);
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

    ///////////////
    private void messageBox(String title, String msg) {
        new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("OK", null)
                .show();
    }
}