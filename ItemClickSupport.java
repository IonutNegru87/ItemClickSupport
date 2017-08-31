package com.endava.android.wifiheatmap;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Utility class which adds the ability to add Click Support for RecyclerViews without the need to implement click
 * listeners into the adapter or in the ViewHolder's implementation.
 * <p>
 * Use it by simply binding an click listener to the desired RecyclerView.
 * <pre><code>
 * ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
 *     {@literal @}Override
 *      public void onItemClicked(RecyclerView recyclerView, int position, View v) {
 *          // Handle the clicked item
 *      }
 * });
 * </code></pre>
 * </p>
 * Based on <a href="http://www.littlerobots.nl/blog/Handle-Android-RecyclerView-Clicks/">Handle-Android-RecyclerView
 * -Clicks</a>, <br/><b>Hugo Visser</b>. Which is very similar with the implementation from <a
 * href="https://github.com/lucasr/twoway-view">TwoWay-View</a>.
 * <p/>
 * Created by ionut on 22.03.2016.
 */
@SuppressWarnings("unused")
public class ItemClickSupport {

  private final RecyclerView recyclerView;
  private OnItemClickListener onItemClickListener;
  private final View.OnClickListener onClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      if (onItemClickListener != null) {
        RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(v);
        onItemClickListener.onItemClicked(recyclerView, holder.getAdapterPosition(), v);
      }
    }
  };
  private OnItemLongClickListener onItemLongClickListener;
  private final View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View v) {
      if (onItemLongClickListener != null) {
        RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(v);
        return onItemLongClickListener.onItemLongClicked(recyclerView, holder.getAdapterPosition(), v);
      }
      return false;
    }
  };
  private RecyclerView.OnChildAttachStateChangeListener stateChangeListener =
      new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(View view) {
          if (onItemClickListener != null) {
            view.setOnClickListener(onClickListener);
          }
          if (onItemLongClickListener != null) {
            view.setOnLongClickListener(onLongClickListener);
          }
        }

        @Override
        public void onChildViewDetachedFromWindow(View view) {

        }
      };

  private ItemClickSupport(RecyclerView recyclerView) {
    this.recyclerView = recyclerView;
    this.recyclerView.setTag(R.id.item_click_support, this);
    this.recyclerView.addOnChildAttachStateChangeListener(stateChangeListener);
  }

  /**
   * Add click support to the specified {@link RecyclerView}.
   * <br/>
   * The click support instance will be reused if previously set on the same recycler view.
   *
   * @param view
   *     The recycler view for which to add click support.
   *
   * @return The instance of the click support for the recycler view.
   */
  public static ItemClickSupport addTo(RecyclerView view) {
    ItemClickSupport support = (ItemClickSupport) view.getTag(R.id.item_click_support);
    if (support == null) {
      support = new ItemClickSupport(view);
    }
    return support;
  }

  /**
   * Remove click support from the specified {@link RecyclerView}
   *
   * @param view
   *     The recycler view for which to remove click support.
   *
   * @return The instance of the click support for the recycler view.
   */
  public static ItemClickSupport removeFrom(RecyclerView view) {
    ItemClickSupport support = (ItemClickSupport) view.getTag(R.id.item_click_support);
    if (support != null) {
      support.detach(view);
    }
    return support;
  }

  public ItemClickSupport setOnItemClickListener(OnItemClickListener listener) {
    onItemClickListener = listener;
    return this;
  }

  public ItemClickSupport setOnItemLongClickListener(OnItemLongClickListener listener) {
    onItemLongClickListener = listener;
    return this;
  }

  private void detach(RecyclerView view) {
    view.removeOnChildAttachStateChangeListener(stateChangeListener);
    view.setTag(R.id.item_click_support, null);
  }

  @SuppressWarnings("WeakerAccess")
  public interface OnItemClickListener {

    void onItemClicked(RecyclerView recyclerView, int position, View v);
  }

  @SuppressWarnings("WeakerAccess")
  public interface OnItemLongClickListener {

    boolean onItemLongClicked(RecyclerView recyclerView, int position, View v);
  }

  public static class SimpleOnItemClickListener implements OnItemClickListener {

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
    }
  }
}
