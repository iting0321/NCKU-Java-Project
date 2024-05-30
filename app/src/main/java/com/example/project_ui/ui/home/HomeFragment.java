package com.example.project_ui.ui.home;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.params.BlackLevelPattern;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


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

        /*
          初始化資料。(82 ~ 106行)
          可能使用if判斷式，若此時資料為空則以底下方式載入並匯出至資料庫，有資料(ex: date存入)後改以資料庫匯入。
             0  1
          0 Time   Event
          1  NULL  date
          2  0~1   NULL
          3  1~2   NULL
          .
          .
          .
          25 23~24 NULL
          26 NULL  NULL
          因為最底下按鈕會擋到，所以新增index 26確保可檢視完整時段
        */
        ArrayList<ArrayList<String>> form_data = new ArrayList<ArrayList<String>>();
        ArrayList<String> first_row = new ArrayList<String>();
        first_row.add("Time");
        first_row.add("Event");
        form_data.add(first_row);
        ArrayList<String> date_row = new ArrayList<String>();
        date_row.add("");//Next_is_date
        String format = "yyyy / MM / dd";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat date_format = new SimpleDateFormat(format);
        String date = date_format.format(c.getTime());
        date_row.add(date);
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
            row_data.add("");///////////////////////////////data_input
            form_data.add(row_data);
        }

        //配置格子大小
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)requireContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidthDp = (int) (displayMetrics.widthPixels /  displayMetrics.density);


        final LockTableView mLockTableView = new LockTableView(requireContext(), mContentView, form_data);
        mLockTableView.setLockFristRow(true);
        mLockTableView.setTextViewSize(17);
        mLockTableView.setMinRowHeight(45);
        mLockTableView.setMaxRowHeight(45);
        mLockTableView.setColumnWidth(0, screenWidthDp / 2);
        mLockTableView.setColumnWidth(1, screenWidthDp  * 57 / 128);
        mLockTableView.setTableContentTextColor(R.color.black);
        mLockTableView.setCellPadding(0);
        mLockTableView.setNullableString("");

        mLockTableView.setOnLoadingListener(new LockTableView.OnLoadingListener() {
            //重整
            @Override
            public void onRefresh(final XRecyclerView mXRecyclerView, final ArrayList<ArrayList<String>> mTableDatas) {
                //如需更新表格数据调用,部分刷新不会全部重绘
                mLockTableView.setTableDatas(mTableDatas);
                //停止刷新
                mXRecyclerView.refreshComplete();
            }
            //載入更多資料
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
        mLockTableView.setOnItemClickListenter(new LockTableView.OnItemClickListenter() {
            @Override
            public void onItemClick(View view, int i) {
                if(i == 1){
                    //調用日期選擇功能
                    callCalendar(view, form_data, mLockTableView);
                }
                else if(i != 26 && i != 0){
                    LayoutInflater inf = LayoutInflater.from(requireContext());
                    final View dialog = inf.inflate(R.layout.pop_up_window, null);
                    if(form_data.get(i).get(1).isEmpty()){//無事件則出現填寫視窗

                        callDialog(form_data, dialog, i, mLockTableView);
                    }
                    else{
                        check_event(form_data, dialog, i, mLockTableView);
                    }
                }
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
    private void check_event(ArrayList<ArrayList<String>> data, View view, int row, LockTableView ltb){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(data.get(row).get(0));
        builder.setMessage(data.get(row).get(1));
        builder.setIcon(R.drawable.messagebox_schedule);
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callDialog(data, view, row, ltb);
            }
        });
        builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder delete_check = new AlertDialog.Builder(requireContext());
                delete_check.setIcon(R.drawable.warning_icon);
                delete_check.setTitle("Warning：");
                delete_check.setMessage("Data cannot be recovered after deletion.");
                delete_check.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        data.get(row).set(1, "");
                        ltb.setTableDatas(data);
                    }
                });

                delete_check.setNeutralButton("Cancel", null);
                delete_check.show();
            }
        });
        builder.show();
    }
    /* 修改資料視窗 */
    private void callDialog(ArrayList<ArrayList<String>> data, View view, int row, LockTableView ltb){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setCancelable(true);
        builder.setIcon(R.drawable.edit_icon);
        builder.setTitle(data.get(row).get(0));
        builder.setView(view);
        EditText editText = (EditText) view.findViewById(R.id.event_text);
        if(!data.get(row).get(1).isEmpty()){
            editText.setText(data.get(row).get(1));
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                data.get(row).set(1, editText.getText().toString());
                ltb.setTableDatas(data);
            }
        });
        builder.setNeutralButton("Cancel", null);
        builder.show();
    }
    private  void callCalendar(View view, ArrayList<ArrayList<String>> data, LockTableView ltb){
        Calendar calendar = Calendar.getInstance();

        new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                ////////////////取得該日期之數據
                String m, d;
                if((month + 1) < 10){
                    m = "0" + String.valueOf(month + 1);
                }
                else{
                    m = String.valueOf(month + 1);
                }
                if(dayOfMonth < 10){
                    d = "0" + String.valueOf(dayOfMonth);
                }
                else{
                    d = String.valueOf(dayOfMonth);
                }
                data.get(1).set(1, year + " / " + m + " / " + d); //若無該日期則為空資料串，新增此數據
                ltb.setTableDatas(data);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
    }
}