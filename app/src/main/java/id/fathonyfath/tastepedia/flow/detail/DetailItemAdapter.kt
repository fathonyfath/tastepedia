package id.fathonyfath.tastepedia.flow.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.fathonyfath.tastepedia.R

class DetailItemAdapter :
    ListAdapter<DetailItemType, DetailItemAdapter.ParentViewHolder>(DIFF_CALLBACK) {

    var onUriContentClickListener: ((String) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DetailItemType.Header -> DetailItemType.Header.ITEM_TYPE
            is DetailItemType.Content -> DetailItemType.Content.ITEM_TYPE
            is DetailItemType.UriContent -> DetailItemType.UriContent.ITEM_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            DetailItemType.Header.ITEM_TYPE -> {
                val view = inflater.inflate(R.layout.item_header, parent, false)
                ParentViewHolder.Header(view)
            }
            DetailItemType.Content.ITEM_TYPE -> {
                val view = inflater.inflate(R.layout.item_content, parent, false)
                ParentViewHolder.Content(view)
            }
            DetailItemType.UriContent.ITEM_TYPE -> {
                val view = inflater.inflate(R.layout.item_uri_content, parent, false)
                ParentViewHolder.UriContent(view, onUriContentClickListener)
            }
            else -> throw RuntimeException("Invalid ViewType")
        }
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        val item = getItem(position)
        if (holder is ParentViewHolder.Header && item is DetailItemType.Header) {
            holder.bind(item)
        } else if (holder is ParentViewHolder.Content && item is DetailItemType.Content) {
            holder.bind(item)
        } else if (holder is ParentViewHolder.UriContent && item is DetailItemType.UriContent) {
            holder.bind(item)
        }
    }

    sealed class ParentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        class Header(itemView: View) : ParentViewHolder(itemView) {

            private val headerTextView: TextView = itemView.findViewById(R.id.header_text_view)

            fun bind(data: DetailItemType.Header) {
                headerTextView.text = data.title
            }
        }

        class Content(itemView: View) : ParentViewHolder(itemView) {

            private val contentTextView: TextView = itemView.findViewById(R.id.content_text_view)

            fun bind(data: DetailItemType.Content) {
                contentTextView.text = data.content
            }
        }

        class UriContent(
            itemView: View,
            private val onUriContentClickListener: ((String) -> Unit)?
        ) : ParentViewHolder(itemView) {

            private val uriContentTextView: TextView =
                itemView.findViewById(R.id.uri_content_text_view)

            fun bind(data: DetailItemType.UriContent) {
                uriContentTextView.text = data.uri

                itemView.setOnClickListener {
                    onUriContentClickListener?.invoke(data.uri)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DetailItemType>() {
            override fun areItemsTheSame(
                oldItem: DetailItemType,
                newItem: DetailItemType
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DetailItemType,
                newItem: DetailItemType
            ): Boolean {
                return false
            }
        }
    }
}