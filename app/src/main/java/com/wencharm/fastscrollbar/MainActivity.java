package com.wencharm.fastscrollbar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	private ListView list;
	private FastScroller scroller;
	private TextView section;
	private ArrayList<Data> items;
	private Activity act;
	private Adapter adapter;
	private int prePosition;

	class Data {
		private String index;
		private String content;
		private boolean showIndex;

		public Data(String index, String content, boolean showIndex) {
			this.index = index;
			this.content = content;
			this.showIndex = showIndex;
		}
	}

	private ArrayList<Data> initData() {
		ArrayList<Data> list = new ArrayList<>();
		for (int i = 0; i < FastScroller.b.length; i++) {
			if (i == 0) list.add(new Data(FastScroller.b[0], "section " + FastScroller.b[0], true));
			else {
				for (int j = 0; j < 10; j++) {
					list.add(new Data(FastScroller.b[i], "section " + FastScroller.b[i], j == 0));
				}
			}
		}
		return list;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		act = this;
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		list = (ListView) findViewById(R.id.list);
		scroller = (FastScroller) findViewById(R.id.scroller);
		section = (TextView) findViewById(R.id.section_title);
		scroller.setTextView(section);
		scroller.setOnTouchingLetterChangedListener(s ->
				new Handler(getMainLooper()).post(() -> {
					if (s.equals(FastScroller.HEART)) {
						prePosition = 0;
						list.setSelectionFromTop(0, 0);
					} else {
						int p = adapter.getPositionForSection(s);
						prePosition = p == 0 ? prePosition : p;
						list.setSelectionFromTop(prePosition, 0);
					}
				}));
		items = initData();
		adapter = new Adapter();
		list.setAdapter(adapter);
		setSupportActionBar(toolbar);

	}

	class Adapter extends BaseAdapter {

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Data getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = act.getLayoutInflater().inflate(R.layout.list_item, parent, false);
				viewHolder = new ViewHolder(convertView);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder)convertView.getTag();
			}
			viewHolder.index.setText(getItem(position).index);
			viewHolder.content.setText(getItem(position).content);
			viewHolder.index.setVisibility(getItem(position).showIndex ? View.VISIBLE : View.GONE);
			return convertView;
		}

		public int getPositionForSection(String s) {
			for (int i = 0; i < items.size(); i++) {
				if (items.get(i).index.equals(s)) return i;
			}
			return 0;
		}
	}

	class ViewHolder {
		private TextView index;
		private TextView content;
		public ViewHolder(View view) {
			this.index = (TextView) view.findViewById(R.id.index);
			this.content = (TextView) view.findViewById(R.id.content);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
