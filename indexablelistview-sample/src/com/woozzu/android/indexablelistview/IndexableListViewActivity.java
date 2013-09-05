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
import com.woozzu.android.indexablelistview.ContentAdapter;

public class IndexableListViewActivity extends Activity {
	private ArrayList<String> mItems;
	private IndexableListView mListView;

    public static final String[] POPULAR_CITIES = {"安徽省", "地级市", "合肥", "宿州", "淮北", "阜阳", "蚌埠", "淮南", "滁州", "马鞍",};

    public static final String[] CITIES = {"安徽省", "地级市", "合肥", "宿州", "淮北", "阜阳", "蚌埠", "淮南", "滁州", "马鞍",
        "福州", "厦门", "泉州", "龙岩", "宁德", "三明", "南平", "漳州", 
        "杭州", "宁波", "温州", "嘉兴", "湖州", "绍兴", };
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        List<String> popItems = new ArrayList<String>();
        for (String s : POPULAR_CITIES) {
            popItems.add(s);
        }

        mItems = new ArrayList<String>();
        for (String s : CITIES) {
            mItems.add(s);
        }

        ContentAdapter adapter = new ContentAdapter(this, popItems, mItems);
        mListView = (IndexableListView) findViewById(R.id.listview);
        mListView.setAdapter(adapter);
        mListView.setFastScrollEnabled(true);
    }
}
