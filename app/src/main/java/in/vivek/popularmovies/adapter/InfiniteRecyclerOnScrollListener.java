package in.vivek.popularmovies.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by vivek on 16/4/16.
 *
 * NOTE : Got Idea from Stackoverflow and copied portion of code from there and changed accordingly.
 */
public abstract class InfiniteRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    @SuppressWarnings("unused")
    public static String TAG = InfiniteRecyclerOnScrollListener.class.getSimpleName();

    private int mPreviousTotal = 0; // The total number of items in the dataset after the last load
    private boolean mLoading = true; // True if we are still waiting for the last set of data to load.
    private int mVisibleThreshold = 15; // The minimum amount of items to have below your current scroll lastClicked before mLoading more.
    private int mcurrentPage = 1;

    int firstVisibleItem, visibleItemCount, totalItemCount;

    private GridLayoutManager mGridLayoutManager;

    public InfiniteRecyclerOnScrollListener(GridLayoutManager mGridLayoutManager) {
        this.mGridLayoutManager = mGridLayoutManager;
        resetScrollSettings();
    }

    public void resetScrollSettings(){
        mPreviousTotal = 0;
        mLoading = true;
        mVisibleThreshold = 10;
        mcurrentPage = 1;

        firstVisibleItem = 0;
        visibleItemCount = 0;
        totalItemCount = 0;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mGridLayoutManager.getItemCount();
        firstVisibleItem = mGridLayoutManager.findFirstVisibleItemPosition();

        if (mLoading) {
            if (totalItemCount > mPreviousTotal) {
                mLoading = false;
                mPreviousTotal = totalItemCount;
            }
        }
        if (!mLoading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + mVisibleThreshold)) {
            // End has been reached
            mcurrentPage++;

            onLoadMore(mcurrentPage);

            mLoading = true;
        }
    }

    public abstract void onLoadMore(int current_page);
}

