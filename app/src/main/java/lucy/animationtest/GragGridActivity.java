package lucy.animationtest;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import lucy.animationtest.draggridview.DragAdapter;
import lucy.animationtest.draggridview.DragMessage;
import lucy.animationtest.draggridview.GragGridView;
import lucy.animationtest.draggridview.MyMessage;


public class GragGridActivity extends Activity {
	private MyAdapter myAdapter;
	private ArrayList<DragMessage> mlist = new ArrayList<DragMessage>();
	private GragGridView mGridView;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grag_grid);
        initDate();
        mGridView = (GragGridView)findViewById(R.id.drag_grid);
        myAdapter = new MyAdapter(this, mlist);
        mGridView.setAdapter(myAdapter);
        //设置触发拖动的区域，用一个ImageView来设置,这个必须设置
        mGridView.setDragImageId(R.id.grag_grid_item_view);
        //以下这些都做了相应的处理，不设置没关系，而且效果也不错
        //设置拖动浮项的背景色
//      mListView.setDragColor(Color.RED);
        //设置滚动的最大像素
//      mListView.setMaxSH(sh);
        //设置滚动的最小像素
//      mListView.setMinSH(sh);
        //设置滚动区的高度（2*height应该小于ListView自己的高度）
//      mListView.setMoveHeight(height);
    }
	
	private void initDate(){
    	for (int i = 1; i <= 100; i++) {
			MyMessage msg = new MyMessage();
			String str = "DM_" + i;
			msg.msg = str;
			mlist.add(msg);
		}
    }
	
    class MyAdapter extends DragAdapter {
    	
    	Drawable background;

		public MyAdapter(Context mContext, ArrayList<DragMessage> mlist) {
			super(mContext, mlist);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder holder = null;
			if (view == null) {
				view = LayoutInflater.from(mContext).inflate(
						R.layout.drag_grid_item, null);
				holder = new ViewHolder();
				holder.tv = (TextView) view
						.findViewById(R.id.drag_grid_item_text);
				holder.iv = (ImageView) view
				        .findViewById(R.id.drag_grid_item_image);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			holder.tv.setText(((MyMessage)getItem(position)).msg);
			holder.iv.setBackgroundResource(R.drawable.title2);
			if(background == null){
				background = view.getBackground();}
			if(getItem(position).flag == DragMessage.MOVE_FLAG){
				view.setBackgroundColor(Color.GRAY); 
			}
			else{
				view.setBackgroundDrawable(background);
			}

			return view;
		}
    	
    }
    private class ViewHolder {
		TextView tv;
		ImageView iv;
	}

}
