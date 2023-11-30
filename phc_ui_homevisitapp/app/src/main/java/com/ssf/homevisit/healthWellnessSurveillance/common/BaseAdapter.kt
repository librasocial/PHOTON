package com.example.application.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * [RecyclerView.Adapter] base class for presenting List data in a
 * [RecyclerView], including computing diffs between Lists on a background thread.
 *
 * This class is a convenience wrapper around {@link AsyncListDiffer} that implements Adapter common
 * default behavior for item access and counting.
 *
 * While using a LiveData&lt;List> is an easy way to provide data to the adapter
 * You can implement it in the [Adapter][RecyclerView.Adapter] class and it provide a [bind] method
 *
 * ### Example of use
 * ```
 *  class TestAdapter : BaseAdapter<Demo>() {
 *      override fun bind(viewBinding: VB, item: Demo) {
 *          itemView.<view_x>.text = item.<data_x>
 *          itemView.<view_y>.text = item.<data_y>
 *      }
 *```
 *      //You can put your logic of ViewHolders here and
 *      // returns BaseTypeFactory.AdapterTypes.YOUR_CUSTOM_ENUM.ordinal
 * ```
 *   override fun getItemViewType(position: Int): Int {
 *          return if (position % 2 == 0) BaseTypesFactory.AdapterTypes.CUSTOM.ordinal else BaseTypesFactory.AdapterTypes.DEFAULT.ordinal
 *      }
 *  }
 * ```
 * @param comparator, which is a default [Comparator], if you want then you can pass your custom [Comparator]
 * @see submitList for submitting list
 * @see getItemViewType for implementing your logic related to [RecyclerView.ViewHolder]
 */
abstract class BaseAdapter<T, VB : ViewBinding>(
    comparator: DiffUtil.ItemCallback<T>,
    @LayoutRes private val layoutId: Int
) : ListAdapter<T, BaseAdapter<T, VB>.VH>(comparator) {

    lateinit var listener: ((view: View, item: T, position: Int) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                layoutId,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(currentList[position], position)
    }

    inner class VH(private val viewBinding: VB) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: T, position: Int) { // name surname city email
            bind(viewBinding, item, position)
        }

        fun getViewBinding() = viewBinding
    }

    /**
     * it binds the [ViewBinding][VB] from the [T] item
     * @param [ViewBinding][VB] is a [ViewBinding][VB], which going to bind with the item [T]
     * @param item is data, which going to bind with the [ViewBinding][VB]
     */
    abstract fun bind(viewBinding: VB, item: T, position: Int)
}
