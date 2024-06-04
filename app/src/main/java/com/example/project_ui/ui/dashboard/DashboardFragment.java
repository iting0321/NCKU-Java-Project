package com.example.project_ui.ui.dashboard;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

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
import androidx.room.Room;

import android.util.DisplayMetrics;
import com.example.project_ui.R;
import com.example.project_ui.RoomDataBase.Record.RecordDao;
import com.example.project_ui.RoomDataBase.Record.RecordEvents;
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
import java.util.Collection;
import java.util.Collections;

import com.example.project_ui.RoomDataBase.Record.DataBase;

import com.example.project_ui.RoomDataBase.Record.Converters;


public class DashboardFragment extends Fragment {

    private static final int ROWNUM = 33;
    private FragmentDashboardBinding binding;
    private LinearLayout recordLayout;

    private RecordDao recordDao;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        recordLayout = root.findViewById(R.id.contentView_dashboard);

        DataBase db = Room.databaseBuilder(getContext(),DataBase.class, "RecordData.db").build();
        recordDao = db.recordDao();

        run1();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void run1(){

        initDisplayOpinion();

        /*
          初始化資料。(80 ~ 104行)
          可能使用if判斷式，若此時資料為空則以底下方式載入並匯出至資料庫，有資料(ex: date存入)後改以資料庫匯入。
             0  1
          0  Time  Record
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

        ArrayList<ArrayList<String>> record_from = new ArrayList<ArrayList<String>>();
        ArrayList<String> first_row = new ArrayList<String>();
        first_row.add("Time");
        first_row.add("Event");
        record_from.add(first_row);
        ArrayList<String> date_row = new ArrayList<String>();
        date_row.add("");//Next_is_date
        String format = "yyyy / MM / dd";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat date_format = new SimpleDateFormat(format);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        String date;
        if(hour >= 6){

            date = date_format.format(c.getTime());
        }
        else{
            c.add(Calendar.DATE, -1);
            date = date_format.format(c.getTime());
        }
        date_row.add(date);
        record_from.add(date_row);
        for(int i = 0; i <= ROWNUM-3; i++){
            ArrayList<String> row_data = new ArrayList<String>();
            if(i < 4){
                row_data.add("0" + (i + 6) + "：00 ~ 0" + (i + 6) + "：59");
            }
            else if(i < 18){
                row_data.add((i + 6) + "：00 ~ " + (i + 6) + "：59");
            }
            else if(i == 18) row_data.add("00：00 ~ 00：59");
            else if(i == 19) row_data.add("01：00 ~ 01：59");
            else{
                row_data.add("");
            }
            row_data.add("");///////////////////////////////data_input
            record_from.add(row_data);
        }

        // load if data of today exists
        new Thread(() -> {
            if(recordDao.getByDate(date) != null) {
                cloneArrArr(Converters.fromString(recordDao.getByDate(date).event), record_from);
            }
        }).start();
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
        record_table.setColumnWidth(1, screenWidthDp * 57 / 128);
        record_table.setTableContentTextColor(R.color.black);
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
                if(i == 1){
                    callCalendar(view, record_from, record_table);
                }
                else if(i != ROWNUM-1 && i != 0){
                    LayoutInflater inf = LayoutInflater.from(requireContext());
                    final View dialog = inf.inflate(R.layout.pop_up_window, null);
                    if(record_from.get(i).get(1).isEmpty()){//無事件則出現填寫視窗

                        callDialog(record_from, dialog, i, record_table);
                    }
                    else{
                        messageBox(record_from, dialog, i, record_table);
                    }
                }
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
    private void messageBox(ArrayList<ArrayList<String>> data, View v, int row, LockTableView ltb) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(data.get(row).get(0));
        builder.setMessage(data.get(row).get(1));
        builder.setIcon(R.drawable.messagebox_record);
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callDialog(data, v, row, ltb);
            }
        });
        builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder delete_record = new AlertDialog.Builder(requireContext());
                delete_record.setIcon(R.drawable.warning_icon);
                delete_record.setTitle("Warning：");
                delete_record.setMessage("Data cannot be recovered after deletion.");
                delete_record.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        data.get(row).set(1, "");
                        // delete from database
                        new Thread(() -> {
                            recordDao.updateByDate(data.get(1).get(1), Converters.fromArrayList(data));
                        }).start();
                        ltb.setTableDatas(data);
                    }
                });
                delete_record.setNeutralButton("Cancel", null);
                delete_record.show();
            }

        });
        builder.show();
    }

    private void callDialog(ArrayList<ArrayList<String>> data, View view, int row, LockTableView ltb){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setCancelable(true);
        builder.setIcon(R.drawable.edit_icon);
        builder.setTitle(data.get(1).get(1));
        builder.setView(view);
        EditText editTime = (EditText) view.findViewById(R.id.time_text);
        EditText editText = (EditText) view.findViewById(R.id.event_text);
        if(!data.get(row).get(0).isEmpty()){
            editTime.setText(data.get(row).get(0));
        }
        if(!data.get(row).get(1).isEmpty()){
            editText.setText(data.get(row).get(1));
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                data.get(row).set(0, editTime.getText().toString());
                data.get(row).set(1, editText.getText().toString());

                // add to database
                new Thread(() -> {
                    if(recordDao.getByDate(data.get(1).get(1)) == null)
                        recordDao.insertData(data.get(1).get(1), com.example.project_ui.RoomDataBase.Plan.Converters.fromArrayList(data));
                    else
                        recordDao.updateByDate(data.get(1).get(1), com.example.project_ui.RoomDataBase.Plan.Converters.fromArrayList(data));
                }).start();
                ltb.setTableDatas(data);
            }
        });
        builder.setNeutralButton("取消", null);
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

                // load data if exists
                new Thread(() -> {
                    if(recordDao.getByDate(data.get(1).get(1)) == null) {
                        timeSet(data);
                    }else {
                        cloneArrArr(Converters.fromString(recordDao.getByDate(data.get(1).get(1)).event), data);
                    }
                }).start();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                ltb.setTableDatas(data);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
    }
    private boolean cloneArrArr(ArrayList<ArrayList<String>> src, ArrayList<ArrayList<String>>des){
        if(src.size() != des.size())  // clone fail
            return false;

        for(int i=0; i<src.size(); i++){
            des.set(i, (ArrayList<String>)src.get(i).clone());
        }
        return true;
    }

    private void timeSet(ArrayList<ArrayList<String>> data){
        for(int i = 2; i < ROWNUM; i++){
            // reset time
            if(i < 6){
                data.get(i).set(0, "0" + (i + 4) + "：00 ~ 0" + (i + 4) + "：59");
            }
            else if(i < 20){
                data.get(i).set(0, (i + 4) + "：00 ~ " + (i + 4) + "：59");
            }
            else if(i == 20) data.get(i).set(0, "00：00 ~ 00：59");
            else if(i == 21) data.get(i).set(0, "01：00 ~ 01：59");
            else{
                data.get(i).set(0, "");
            }

            // clean events
            data.get(i).set(1,"");
        }
    }
}