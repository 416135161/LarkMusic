package internet.com.larkmusic.view;



import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import internet.com.larkmusic.R;

public class MyListView extends ListView implements OnScrollListener{

    private int lastVisibleItem;//最后一个可见的item

    private int totalItemCount;//总的item

    private boolean isLoading = false;//是否正在加载数据

    private ILoadListener mListener;//回调接口，用来加载数据

    private View footer;//底布局


    //注意，三个构造方法都要重写
    public MyListView(Context context) {
        super(context);
        initView(context);

    }
    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }


    //定义一个回调接口，用来获得要加载的数据
    public interface ILoadListener{
        void loadData();
    }

    public void setOnILoadListener(ILoadListener listener){

        this.mListener = listener;
    }



    //初始化view
    private void initView(Context context){

        footer = LayoutInflater.from(context).inflate(R.layout.listview_footer, null);

        //注意，这句代码的意思是给自定义的ListView加上底布局
        this.addFooterView(footer);

        //首先需要隐藏这个底部布局
        footer.findViewById(R.id.progressBar).setVisibility(View.GONE);

        this.setOnScrollListener(this);//千万别忘记设定监听器

    }



    //加载数据完成后，需要执行的操作
    public void loadFinish(){
        flag = true;
        isLoading = false;//不再加载了


    }

    public void loadFinish(boolean flag){
        loadFinish();
        this.flag = flag;
        //底布局也要隐藏
        footer.findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    public void hideProgress(){
        footer.findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    private boolean flag = true;


    //参数scrollState表示滑动的状态
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        //如果最后一个可见item等于总的item，且当前滚动状态为滚动停止，就应该开始加加载数据了
        if(lastVisibleItem == totalItemCount && scrollState==SCROLL_STATE_IDLE){

            if(!isLoading && flag && mListener != null){
                isLoading = true;

                //加载数据
                mListener.loadData();
                //设置底布局可见
                footer.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            }
        }

    }

    /***
     * 该方法用来监听实时滚动中的item
     * firstVisibleItem:当前第一个可见的item
     * visibleItemCount:当前总共有多少个可见的item
     * totalItemCount:总的item
     */
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        this.lastVisibleItem = firstVisibleItem + visibleItemCount;
        this.totalItemCount = totalItemCount;

    }



}
