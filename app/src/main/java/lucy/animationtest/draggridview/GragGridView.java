package lucy.animationtest.draggridview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

public class GragGridView extends GridView{

	private ImageView dragImageView;//拖动item的preview
	private WindowManager windowManager;
	private WindowManager.LayoutParams windowParams;
	private int dragSrcPosition; //开始拖拽的位置  
	private int dragPosition;    // 结束拖拽的位置 
	private int dragPointX;//相对于item的x坐标 
	private int dragPointY;//相对于item的y坐标  
	private int dragOffsetX;
	private int dragOffsetY;
	private int dragImageId;
	private int itemHeight;
	private int itemWidth;
	private int moveHeight = 0;
	private int upScrollBounce;
	private int downScrollBounce;
	private int dragColor = Color.GRAY;
	
	private int changePosition = -1;
	private long scrollDelayMillis = 10L;
	
	private int middleX;
	private int middleY;
	private boolean isDrag = false;
	
	private RefreshHandler scrollDelayUp = new RefreshHandler(Looper.getMainLooper(), true);
	private RefreshHandler scrollDelayDown = new RefreshHandler(Looper.getMainLooper(), false);
	
	private int scrollHeight = 4;
	private int maxSH = 20;
	private int minSH = 4;
	
    public void setMoveHeight(int height) {
	    this.moveHeight = height;
	}
	
    public void setDragColor(int color) {
      this.dragColor = color;
    }
    
	public void setDragImageId(int id) {
	    this.dragImageId = id;
	}

	public GragGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			int x = (int)ev.getX();
			int y = (int)ev.getY();
			
			this.dragSrcPosition = this.dragPosition = pointToPosition(x, y);
			if (this.dragPosition == -1) {
		        return super.onInterceptTouchEvent(ev);
		    }
			ViewGroup itemView = (ViewGroup)getChildAt(this.dragPosition - 
				        getFirstVisiblePosition());
			//得到当前点在item内部的偏移量 即相对于item左上角的坐标 
			this.itemHeight = itemView.getHeight();
			this.dragPointX = (x - itemView.getLeft());
		    this.dragPointY = (y - itemView.getTop());
		    this.dragOffsetX = (int)(ev.getRawX() - x);
		    this.dragOffsetY = (int)(ev.getRawY() - y);
		    View dragger = itemView.findViewById(this.dragImageId);
		    if ((dragger != null) && (x > dragger.getLeft()&& x < dragger.getRight()) && 
		    		(y > dragger.getTop() && y < dragger.getBottom())) {
		    	if(this.moveHeight <= 0 || (this.moveHeight >= getHeight()/2)) {
		    		this.upScrollBounce = (getHeight() / 3);
		            this.downScrollBounce = (getHeight() * 2 / 3);
		    	} else {
		    		this.upScrollBounce = this.moveHeight;
		            this.downScrollBounce = (getHeight() - this.moveHeight);
		    	}
		    	
		    	//解决问题3     
		    	//每次都销毁一次cache，重新生成一个bitmap   
		    	itemView.destroyDrawingCache(); 
		    	itemView.setDrawingCacheEnabled(true);
		        Drawable background = itemView.getBackground();
		        itemView.setBackgroundColor(this.dragColor);
		        Bitmap bitmap = Bitmap.createBitmap(itemView.getDrawingCache());
		        itemView.setBackgroundDrawable(background);
		        //建立item的缩略图 
		        startDrag(bitmap, x, y);
		    }
		    return false;
		}
		return super.onInterceptTouchEvent(ev);
	}



	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		
		if ((this.dragImageView != null) && (this.dragPosition != -1)) {
			int action = ev.getAction();
			 int moveY = (int)ev.getY();
		     int moveX = (int)ev.getX();
			switch(action) {
			      case MotionEvent.ACTION_UP:
			    	    int upX = (int)ev.getX();                 
			    	    int upY = (int)ev.getY();                 
			    	    stopDrag();                 
			    	    onDrop(upX, upY);
		                break;
		          case MotionEvent.ACTION_MOVE:
				        if (moveX <= 0)
					      this.middleX = 0;
					    else if (moveX >= getWidth())
					      this.middleX = getWidth();
					    else {
					      this.middleX = moveX;
					    }
				        
				        if (moveY <= 0)
				          this.middleY = 0;
				        else if (moveY >= getHeight())
				          this.middleY = getHeight();
				        else {
				          this.middleY = moveY;
				        }
				        dragPositionChanged();
				        onDrag(moveX, moveY);
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}

	private void onDrag(int x, int y) {
		if (this.dragImageView != null) {
			this.windowParams.alpha = 0.8F;
			
			if (this.middleX - this.dragPointX <= 0)
		        this.windowParams.x = this.dragOffsetX;
		    else if (this.middleX - this.dragPointX >= getWidth() - this.itemWidth)
		        this.windowParams.x = (getWidth() - this.itemWidth + this.dragOffsetX);
		    else {
		        this.windowParams.x = (this.middleX - this.dragPointX + this.dragOffsetX);
		    }
			
			if (this.middleY - this.dragPointY <= 0)
		        this.windowParams.y = this.dragOffsetY;
		    else if (this.middleY - this.dragPointY >= getHeight() - this.itemHeight)
		        this.windowParams.y = (getHeight() - this.itemHeight + this.dragOffsetY);
		    else {
		        this.windowParams.y = (this.middleY - this.dragPointY + this.dragOffsetY);
		    }
			this.windowManager.updateViewLayout(this.dragImageView, this.windowParams);
		}
		
		int tempPosition = pointToPosition(this.middleX, this.middleY);
		Log.v("daming", "GragGridView ---> 177 tempPosition == "+tempPosition);
	    if (tempPosition != -1) {
	      this.dragPosition = tempPosition;
	    }
	    
	    if ((y >= this.upScrollBounce) && (y <= this.downScrollBounce)) {
	        this.isDrag = false;
	        return;
	    }
	    
	    if (y < this.upScrollBounce) {
	        float a = this.upScrollBounce - this.middleY;
	        float b = this.upScrollBounce;
	        float c = a / b;
	        this.scrollHeight = (int)(c * (this.maxSH - this.minSH) + this.minSH);
	        this.isDrag = true;
	        this.scrollDelayUp.sleep(0L);
	    } else if (y > this.downScrollBounce) {
	        float a = this.middleY - this.downScrollBounce;
	        float b = this.upScrollBounce;
	        float c = a / b;
	        this.scrollHeight = (int)(c * (this.maxSH - this.minSH) + this.minSH);
	        this.isDrag = true;
	        this.scrollDelayDown.sleep(0L);
	    }
	}
	
	private void startDrag(Bitmap bm, int x, int y) {
		stopDrag();
		
		this.windowParams = new WindowManager.LayoutParams();
		//Gravity.TOP|Gravity.LEFT;这个必须加 
		this.windowParams.gravity = Gravity.TOP|Gravity.LEFT;
		//得到preview左上角相对于屏幕的坐标 
	    this.windowParams.x = (x - this.dragPointX + this.dragOffsetX);
	    this.windowParams.y = (y - this.dragPointY + this.dragOffsetY);

	    //设置拖拽item的宽和高 
	    this.windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;     
	    this.windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
	    this.windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE                         
	                         | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE                         
	                         | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON                         
	                         | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN; 
	    this.windowParams.format = PixelFormat.TRANSLUCENT;
	    this.windowParams.windowAnimations = 0;

	    ImageView imageView = new ImageView(getContext());
	    imageView.setImageBitmap(bm);
	    this.windowManager = ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE));//“window”
	    this.windowManager.addView(imageView, this.windowParams);
	    this.dragImageView = imageView;
	}

	private void stopDrag() {
	   if (this.dragImageView != null) {
	      this.windowManager.removeView(this.dragImageView);
	      this.dragImageView = null;
	   }
	   this.changePosition = -1;
	   this.isDrag = false;
	}
	
	private void dragPositionChanged(){
	    DragAdapter adapter = (DragAdapter)getAdapter();
	    if (this.changePosition != this.dragPosition) {
	      if (this.changePosition == -1)
	      {
	        this.changePosition = this.dragPosition;
	        adapter.setFlag(this.changePosition, 1);
	        return;
	      }

	      adapter.swap(this.changePosition, this.dragPosition);

	      this.changePosition = this.dragPosition;
	    }
	}
	
	public void setMaxSH(int sh){
	    this.maxSH = sh;
	}

	public void setMinSH(int sh){
	    this.minSH = sh;
	}
	
	private void onDrop(int x, int y){
		//为了避免滑动到分割线的时候，返回-1的问题        
		int tempPosition = pointToPosition(x, y);         
		if(tempPosition!=INVALID_POSITION){             
			dragPosition = tempPosition;         
		} 
		//超出边界处理         
		if(y<getChildAt(0).getTop()){             
			//超出上边界            
			dragPosition = 0;         
		}else if(y>getChildAt(getChildCount()-1).getBottom()||
				(y>getChildAt(getChildCount()-1).getTop()&&x>getChildAt(getChildCount()-1).getRight())){
			//超出下边界             
			dragPosition = getAdapter().getCount()-1;         
		} 

		//数据交换         
		if(dragPosition!=dragSrcPosition&&dragPosition>-1&&dragPosition<getAdapter().getCount()){
			DragAdapter adapter = (DragAdapter)getAdapter();
			adapter.reFlag();
		} 
		
//	    DragAdapter adapter = (DragAdapter)getAdapter();
//	    adapter.reFlag();
	}
	
	private void actDown(){
	    int tempPosition = pointToPosition(this.middleX, this.middleY);
	    if (tempPosition != AdapterView.INVALID_POSITION) {
	      this.dragPosition = tempPosition;
	    }
	    dragPositionChanged();
	}
	
	private void actUp(){
	  int tempPosition = pointToPosition(this.middleX, this.middleY);
	  if (tempPosition != AdapterView.INVALID_POSITION) {
	    this.dragPosition = tempPosition;
	  }
	  dragPositionChanged();
	}
	
	class RefreshHandler extends Handler {
		boolean isUp;

		 public RefreshHandler(Looper looper, boolean isUp){
		      super(looper);
		      this.isUp = isUp;
		 }

		 public RefreshHandler(Looper l) {
		      super(l);
		 }
		 
		  public void handleMessage(Message msg){
		      if (GragGridView.this.isDrag) {
		        if (this.isUp)
		        	GragGridView.this.actUp();
		        else {
		        	GragGridView.this.actDown();
		        }
		        sleep(GragGridView.this.scrollDelayMillis);
		      }
		  }
		  
		  public void sleep(long delayMillis) {
		      sendMessageDelayed(obtainMessage(0), delayMillis);
		  }
	}
}
