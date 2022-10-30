package com.stenobano.admin.adapter
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stenobano.admin.R
import com.stenobano.admin.databinding.ListSubscriptionBinding
import com.stenobano.admin.model.ModelSubscription

class SubscriptionAdapter(
    val context: Context?, var itemList: ArrayList<ModelSubscription.Result>) :
    RecyclerView.Adapter<SubscriptionAdapter.ItemView>() {
     var onClickItem: OnClickItem?=null
    var index=0
    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ItemView {
        val v= ListSubscriptionBinding.inflate(LayoutInflater.from(p0.context),p0, false)
        return ItemView(v)
    }

    override fun onBindViewHolder(holder: ItemView, position: Int) {

        val model =itemList.get(position)
        holder.binding.validOffer.text=model.offerValidMesg
        holder.binding.desc.text=model.des
        holder.binding.discPrice.text="Rs."+(model.disPrice.toInt()+model.amount.toInt())
        holder.binding.discDesc.text="Rs."+model.amount
        holder.binding.discPrice.setPaintFlags( holder.binding.discPrice.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
        holder.binding.disPerc.text="DISCOUNT Rs."+model.disPrice

        if(model.offerValidMesg.isEmpty())
        {
            holder.binding.validOffer.visibility= View.GONE
        }




        holder.binding.rl.setOnClickListener {
            onClickItem?.clickItem(model)
            index=position
            notifyDataSetChanged()
        }

        if(index==position)
        {
            holder.binding.rl.setBackgroundResource(R.drawable.round_payment_border)
            onClickItem?.selected(model)
        }

        else{
            holder.binding.rl.setBackgroundResource(R.drawable.  round_payment_border_white)
        }

       }


    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ItemView(itemView: ListSubscriptionBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding = itemView

    }

    fun setOnClickListener(onClickItem: OnClickItem)
    {
        this.onClickItem=onClickItem
    }

    interface OnClickItem
    {
        fun clickItem(model: ModelSubscription.Result)
        fun selected(model: ModelSubscription.Result)

    }


}