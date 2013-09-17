#IndexableListView
Implement `TableView` in iphone on android. Add indexer to 'ListView'. When user touch the alphabet on the scroller, it will jump the correspond position of the list view.

#Usage
- install [maven](http://maven.apache.org/).
- run following command on the root directory of this project. `mvn install && cd indexablelistview-sample && mvn install android:redeploy android:run".

#How to use in your project


```java
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
```

#ScreenShot
![screenshot](screenshot.png)
