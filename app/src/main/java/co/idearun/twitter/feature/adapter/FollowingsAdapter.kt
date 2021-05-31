package co.idearun.twitter.feature.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import co.idearun.twitter.R
import co.idearun.twitter.data.model.User
import co.idearun.twitter.databinding.LayoutFollowingsItemBinding
import co.idearun.twitter.feature.UserListener
import co.idearun.twitter.feature.viewmodel.TwitterViewModel
import kotlin.properties.Delegates

//twitter://user?user_id=id_num

class FollowingsAdapter(
    private val viewModel: TwitterViewModel,
    private val listener: UserListener
) : RecyclerView.Adapter<FollowingsAdapter.BtnsViewHolder>() {


    internal var collection: List<User> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun getItemCount() = collection.size

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): BtnsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_followings_item, parent, false)
        return BtnsViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: BtnsViewHolder, position: Int) {
        val btnItem = collection[holder.adapterPosition]
        holder.bindItems(btnItem, holder, viewModel, listener)

    }


    class BtnsViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val binding = LayoutFollowingsItemBinding.bind(itemView)

        fun bindItems(
            item: User,
            holder: BtnsViewHolder,
            viewModel: TwitterViewModel,
            listener: UserListener
        ) {

            binding.vm = viewModel
            binding.listener = listener
            binding.holder = holder

            val score=if (item.score_personal?:0<6){
                item.score_personal
            }else{
                5
            }

            binding.rankTxv.text = "$score"

            binding.item = item

            binding.lifecycleOwner = binding.rankTxv.context as LifecycleOwner

        }


    }


}




