package com.huika.cloud.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 包含两个ScrollView的容器
 * 更多详解见博客http://blog.csdn.net/zhongkejingwang/article/details/38656929
 *
 * @author chenjing
 */
public class ScrollViewContainer extends RelativeLayout {
  /**
   * 自动上滑
   */
  public static final int AUTO_UP = 0;
  /**
   * 自动下滑
   */
  public static final int AUTO_DOWN = 1;
  /**
   * 动画完成
   */
  public static final int DONE = 2;
  /**
   * 动画速度
   */
  public static final float SPEED = 6.5f;
  private boolean isMeasured = false;
  /**
   * 用于计算手滑动的速度
   */
  private VelocityTracker vt;
  private int mViewHeight;
  private int mViewWidth;
  private View topView;
  private View bottomView;
  private ScrollView mBottomScrollView;
  private boolean canPullDown;
  private boolean canPullUp;
  private int state = DONE;
  /**
   * 记录当前展示的是哪个view，0是topView，1是bottomView
   */
  private int mCurrentViewIndex = 0;
  /**
   * 手滑动距离，这个是控制布局的主要变量
   */
  private float mMoveLen;
  private MyTimer mTimer;
  private float mLastY;
  /**
   * 用于控制是否变动布局的另一个条件，mEvents==0时布局可以拖拽了，mEvents==-1时可以舍弃将要到来的第一个move事件，
   * 这点是去除多点拖动剧变的关键
   */
  private int mEvents;
  private OnScrollPageChangeListener onScrollPageChangeListener;
  private Handler handler = new Handler() {

    @Override public void handleMessage(Message msg) {
      if (mMoveLen != 0) {
        if (state == AUTO_UP) {
          mMoveLen -= SPEED;
          if (mMoveLen <= -mViewHeight) {
            mMoveLen = -mViewHeight;
            state = DONE;
            mCurrentViewIndex = 1;
            //						DebugTool.print("===handleMessage===state = AUTO_UP");
          }
        } else if (state == AUTO_DOWN) {
          mMoveLen += SPEED;
          if (mMoveLen >= 0) {
            mMoveLen = 0;
            state = DONE;
            mCurrentViewIndex = 0;
            //						DebugTool.print("===handleMessage====state = AUTO_DOWN");
          }
        } else { // state = DONE
          mTimer.cancel();
          //					DebugTool.print("==handleMessage====mTimer.cancel()");
        }
      }
      invokePageChangeListener();
      requestLayout();
    }
  };
  private OnTouchListener topViewTouchListener = new OnTouchListener() {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
      ScrollView sv = (ScrollView) v;
      canPullUp = sv.getScrollY() == (sv.getChildAt(0).getMeasuredHeight() - sv.getMeasuredHeight())
              && mCurrentViewIndex == 0;
      return false;
    }
  };
  private OnTouchListener bottomViewTouchListener = new OnTouchListener() {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
      if (v instanceof ScrollView) {
        ScrollView sv = (ScrollView) v;
        canPullDown = sv.getScrollY() == 0 && mCurrentViewIndex == 1;
      } else if (v instanceof ListView) {
        ListView listView = (ListView) v;
        canPullDown = listView.getFirstVisiblePosition() == 0 && mCurrentViewIndex == 1;
      }
      return false;
    }
  };

  public ScrollViewContainer(Context context) {
    super(context);
    init();
  }

  public ScrollViewContainer(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public ScrollViewContainer(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private void invokePageChangeListener() {
    if (onScrollPageChangeListener != null) {
      if (Math.abs(mMoveLen) >= topView.getMeasuredHeight()) {
        setOnScrollPageChangeListenerToTop(1);
      } else if (((int) mMoveLen) == 0) {
        setOnScrollPageChangeListenerToTop(0);
      }
    }
  }

  public void setOnScrollPageChangeListenerToTop(int num) {
    if (onScrollPageChangeListener != null) {
      onScrollPageChangeListener.onScrollPageChangeListener(num);
    }
  }

  /** 回到顶部功能实现 */
  public void setScollPageToTop() {
    state = AUTO_DOWN;
    mTimer.schedule(2);
  }

  private void init() {
    mTimer = new MyTimer(handler);
  }

  @Override public boolean dispatchTouchEvent(MotionEvent ev) {
    switch (ev.getActionMasked()) {
      case MotionEvent.ACTION_DOWN:
        if (vt == null) {
          vt = VelocityTracker.obtain();
        } else {
          vt.clear();
        }
        mLastY = ev.getY();
        vt.addMovement(ev);
        mEvents = 0;
        break;
      case MotionEvent.ACTION_POINTER_DOWN:
      case MotionEvent.ACTION_POINTER_UP:
        // 多一只手指按下或抬起时舍弃将要到来的第一个事件move，防止多点拖拽的bug
        mEvents = -1;
        break;
      case MotionEvent.ACTION_MOVE:
        vt.addMovement(ev);
        if (canPullUp && mCurrentViewIndex == 0 && mEvents == 0) {
          mMoveLen += (ev.getY() - mLastY);
          // 防止上下越界
          if (mMoveLen > 0) {
            mMoveLen = 0;
            mCurrentViewIndex = 0;
          } else if (mMoveLen < -mViewHeight) {
            mMoveLen = -mViewHeight;
            mCurrentViewIndex = 1;
          }
          if (mMoveLen < -8) {
            // 防止事件冲突
            ev.setAction(MotionEvent.ACTION_CANCEL);
          }
        } else if (canPullDown && mCurrentViewIndex == 1 && mEvents == 0) {
          mMoveLen += (ev.getY() - mLastY);
          // 防止上下越界
          if (mMoveLen < -mViewHeight) {
            mMoveLen = -mViewHeight;
            mCurrentViewIndex = 1;
          } else if (mMoveLen > 0) {
            mMoveLen = 0;
            mCurrentViewIndex = 0;
          }
          if (mMoveLen > 8 - mViewHeight) {
            // 防止事件冲突
            ev.setAction(MotionEvent.ACTION_CANCEL);
          }
        } else {
          mEvents++;
        }
        mLastY = ev.getY();
        requestLayout();

        if (onScrollPageChangeListener != null) {
          int absMove = (int) Math.abs(mMoveLen);
          if (absMove >= (topView.getMeasuredHeight() - topView.getHeight())
              && absMove < topView.getMeasuredHeight()) {
            //						 if (absMove >= (topView.getMeasuredHeight() - topView.getHeight()) && absMove < topView.getMeasuredHeight()&&state == AUTO_DOWN) {
            //						DebugTool.print("===onScrollPageChangeListener.onScrollPage()");
            onScrollPageChangeListener.onScrollPage();
          }
        }
        break;
      case MotionEvent.ACTION_UP:// 判断是否切换翻页
        mLastY = ev.getY();
        vt.addMovement(ev);
        vt.computeCurrentVelocity(700);
        // 获取Y方向的速度
        float mYV = vt.getYVelocity();
        if (mMoveLen == 0 || mMoveLen == -mViewHeight) break;
        if (Math.abs(mYV) < 1000) {
          // 速度小于一定值的时候当作静止释放，这时候两个View往哪移动取决于滑动的距离
          if (mMoveLen <= -mViewHeight / 2) {
            state = AUTO_UP;
            System.out.println("===state = AUTO_UP");
          } else if (mMoveLen > -mViewHeight / 2) {
            state = AUTO_DOWN;
            System.out.println("===state = AUTO_DOWN");
          }
        } else {
          // 抬起手指时速度方向决定两个View往哪移动
          if (mYV < 0) {
            state = AUTO_UP;
          } else {
            state = AUTO_DOWN;
          }
        }
        mTimer.schedule(2);
        try {
          vt.recycle();
          vt = null;
        } catch (Exception e) {
          e.printStackTrace();
        }
        break;
    }
    // 未解决，先扑捉异常
    try {
      super.dispatchTouchEvent(ev);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }

  // 每次requestLayout会触发这里；重新计算下控件的布局
  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    topView.layout(0, (int) mMoveLen, mViewWidth, topView.getMeasuredHeight() + (int) mMoveLen);
    bottomView.layout(0, topView.getMeasuredHeight() + (int) mMoveLen, mViewWidth,
        topView.getMeasuredHeight() + (int) mMoveLen + bottomView.getMeasuredHeight());
  }

  public void setOnScrollPageChangeListener(OnScrollPageChangeListener onScrollPageChangeListener) {
    this.onScrollPageChangeListener = onScrollPageChangeListener;
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    if (!isMeasured) {
      isMeasured = true;

      mViewHeight = getMeasuredHeight();
      mViewWidth = getMeasuredWidth();

      topView = getChildAt(0);
      bottomView = getChildAt(1);

      mBottomScrollView = getScrollView(bottomView);

      // 这两个监听主要是处理是否可以canPullUp
      topView.setOnTouchListener(topViewTouchListener);
      mBottomScrollView.setOnTouchListener(bottomViewTouchListener);

      // PullToRefreshListView pullToRefreshListView = (PullToRefreshListView) bottomView.findViewById(R.id.mLvPartakeRecord);
      // if (pullToRefreshListView != null) {
      // ListView listView = pullToRefreshListView.getRefreshableView();
      // listView.setOnTouchListener(bottomViewTouchListener);
      // }
      // else {
      // bottomView.setOnTouchListener(bottomViewTouchListener);
      // }
    }
  }

  private ScrollView getScrollView(View view) {
    if (view instanceof ScrollView) {
      return (ScrollView) view;
    }

    if (view instanceof ViewGroup) {
      ViewGroup vp = (ViewGroup) view;
      for (int i = 0; i < vp.getChildCount(); i++) {
        ScrollView child = getScrollView(vp.getChildAt(i));
        if (child != null) return child;
      }
    }
    return null;
  }

  public interface OnScrollPageChangeListener {
    /**
     * 当前显示页变动监听
     *
     * @author FAN 创建于Jan 8, 2015
     */
    void onScrollPageChangeListener(int pageNum);

    /**
     * 翻页动作监听
     *
     * @author FAN 创建于Jan 8, 2015
     */
    void onScrollPage();
  }

  class MyTimer {
    private Handler handler;
    private Timer timer;
    private MyTask mTask;

    public MyTimer(Handler handler) {
      this.handler = handler;
      timer = new Timer();
    }

    public void schedule(long period) {
      if (mTask != null) {
        mTask.cancel();
        mTask = null;
      }
      mTask = new MyTask(handler);
      timer.schedule(mTask, 0, period);
    }

    public void cancel() {
      if (mTask != null) {
        mTask.cancel();
        mTask = null;
      }
    }

    class MyTask extends TimerTask {
      private Handler handler;

      public MyTask(Handler handler) {
        this.handler = handler;
      }

      @Override public void run() {
        handler.obtainMessage().sendToTarget();
      }
    }
  }
}
