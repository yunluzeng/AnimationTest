package lucy.animationtest.draggridview;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class DragAdapter extends BaseAdapter {
	
	protected Context mContext;
	protected ArrayList<DragMessage> mlist;
	
	public DragAdapter(Context mContext, ArrayList<DragMessage> mlist) {
		this.mContext = mContext;
		this.mlist = mlist;
	}
	
	public int getCount() {
	    if (this.mlist != null) {
	        return this.mlist.size();
	    }
		return 0;
	}
	
	public DragMessage getItem(int position) {
		return (DragMessage)this.mlist.get(position);
	}
	
	public long getItemId(int position) {
		return 0;
	}
	
	 public void addMsg(DragMessage msg){
	    this.mlist.add(msg);
	 }
	
	 final void reFlag(){
	    for (DragMessage msg : this.mlist) {
	      msg.flag = 0;
	    }
	    notifyDataSetChanged();
	 }

	 final void swap(int srcPosition, int dragPosition){
	    Collections.swap(this.mlist, srcPosition, dragPosition);
	    notifyDataSetChanged();
	 }

	 final void setFlag(int position, int flag){
	    getItem(position).flag = flag;
	    notifyDataSetChanged();
	 }
	 
	public abstract View getView(int position, View convertView, ViewGroup parent);

}
