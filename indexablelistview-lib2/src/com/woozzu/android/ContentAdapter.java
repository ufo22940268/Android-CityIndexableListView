package com.woozzu.android.indexablelistview;

import java.util.*;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.*;
import android.graphics.Color;

import com.woozzu.android.util.StringMatcher;
import com.woozzu.android.widget.IndexableListView;
import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;
import com.woozzu.android.util.HanziToPingyin;

public class ContentAdapter extends BaseAdapter implements SectionIndexer, PinnedSectionListAdapter {

    private int TYPE_ITEM = 0;
    private int TYPE_SECTION = 1;

    private final int SECTION_POPULAR_CITY = 0;

    private List<String> mSections = new ArrayList<String>();
    private List<Item> mItems;
    private Context mContext;
    private Map<String, Character> mCityMap = new HashMap<String, Character>();
    private int mSkipItemCount;
    
    public ContentAdapter(Context context, List<String> popCities, List<String> cities) {
        this(context, popCities, cities, null);
    }

    public ContentAdapter(Context context, List<String> popCities, List<String> cities, Map<String, Character> cityMap) {
        mCityMap = cityMap;
        mContext = context;
        mItems = new ArrayList<Item>();
        if (mCityMap == null) {
            mCityMap = buildCityMap(cities);
        }
        Collections.sort(cities, new CityComparator());

        //Popular cities.
        Item item = new Item();
        item.type = TYPE_SECTION;
        item.text = "热门城市";
        mItems.add(item);
        mSections.add(0, "热");
        for (String city : popCities) {
            item = new Item();
            item.type = TYPE_ITEM;
            item.text = city;
            mItems.add(item);
        }
        mSkipItemCount = 1 + popCities.size();

        //Common cities.
        char prevChar = '.';
        for (String city : cities) {
            char fc = lookupFirstChar(city);
            if (fc != prevChar) {
                mSections.add(String.valueOf(fc));
                prevChar = fc;

                item = new Item();
                item.type = TYPE_SECTION;
                item.text = String.valueOf(fc);
                mItems.add(item);
            }

            item = new Item();
            item.type = TYPE_ITEM;
            item.text = city;
            mItems.add(item);
        }

    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        if (mItems == null) {
            return 0;
        } else {
            return mItems.size();
        }
    }

    @Override
    public int getPositionForSection(int section) {

        if (section == SECTION_POPULAR_CITY) {
            return 0;
        }

        // If there is no item for current section, previous section will be selected
        for (int i = section; i >= 1; i--) {
            for (int j = mSkipItemCount; j < getCount(); j++) {
                if (i == 0) {
                    // For numeric section
                    for (int k = 0; k <= 9; k++) {
                        if (StringMatcher.match(mItems.get(j).text, String.valueOf(k))) {
                            return j - 1;
                        }
                    }
                } else {
                    char fc = lookupFirstChar(((Item)getItem(j)).text);
                    if (StringMatcher.match(String.valueOf(fc), mSections.get(i))) {
                        return j - 1;
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        if (position < mSkipItemCount) {
            return 0;
        }

        for (int i = 0; i < mSections.size(); i ++) {
            char fc = lookupFirstChar(mItems.get(position).text);
            if (mSections.get(i).equals(String.valueOf(fc))) {
                return i;
            }
        }

        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(
                    android.R.layout.simple_list_item_1, parent, false);
        }
        int type = getItemViewType(position);
        ((TextView)view).setText(mItems.get(position).text); 
        if (isItemViewTypePinned(type)) {
            view.setBackgroundColor(0xff4dbdff);
            ((TextView)view).setTextColor(0xffffffff);
        } else {
            view.setBackgroundColor(Color.WHITE);
            ((TextView)view).setTextColor(0xff000000);
        }
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).type; 
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override 
    public boolean isItemViewTypePinned(int type) {
        return type == TYPE_SECTION;
    }

    public boolean isPinned(int pos) {
        return getItemViewType(pos) == TYPE_SECTION;
    }

    @Override
    public Object[] getSections() {
        String[] strs = new String[mSections.size()];
        for (int i = 0; i < mSections.size(); i ++) {
            strs[i] = mSections.get(i);
        }
        return strs;
    }

    private char lookupFirstChar(String str) {
        if (mCityMap != null) {
            Character co = mCityMap.get(str);
            if (co == null) {
                return '.';
            } else {
                return co;
            }
        } else {
            return '.';
        }
    }

    private char parseFirstChar(String str) {
        char fc = str.charAt(0);
        if (fc <= 128 && fc >= 0) {
            return fc;
        } else {
            return HanziToPingyin.getFirstPinYinChar(str);
        }
    }

    private Map<String, Character> buildCityMap(List<String> strs) {
        Map<String, Character> map = new HashMap<String, Character>();
        for (String str : strs) {
            map.put(str, parseFirstChar(str));
            map.put(str, 'a');
        }
        return map;
    }


    public class CityComparator<T> implements Comparator<T> {
        @Override
        public int compare(T lhs, T rhs) {
            return lookupFirstChar((String)lhs) - lookupFirstChar((String)rhs);
        }

        @Override
        public boolean equals(Object object) {
            return true;
        }
    }


    public static class Item {
        public int type;
        public String text;
    }
}
