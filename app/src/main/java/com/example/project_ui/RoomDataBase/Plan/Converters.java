package com.example.project_ui.RoomDataBase.Plan;

import androidx.room.TypeConverter;
import androidx.room.ProvidedTypeConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@ProvidedTypeConverter
public class Converters {
    @TypeConverter
    static public String fromArrayList(ArrayList<ArrayList<String>> listlist) {
        ArrayList<String> tmp = new ArrayList<>();
        for (ArrayList<String> list : listlist) {
            tmp.add(String.join(",", list));
        }
        return String.join(":", tmp);
    }

    @TypeConverter
    static public ArrayList<ArrayList<String>> fromString(String value) {
        ArrayList<ArrayList<String>> listlist = new ArrayList<ArrayList<String>>();

        ArrayList<String> tmp = new ArrayList<>(Arrays.asList(value.split(":")));
        for (String listStr:tmp) {
            listlist.add(new ArrayList<>(Arrays.asList(listStr.split(","))));
        }
        return listlist;
    }
}
