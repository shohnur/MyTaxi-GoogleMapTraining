package uz.mytaxi.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_data_divider.view.*
import kotlinx.android.synthetic.main.item_trip.view.*
import uz.mytaxi.R
import uz.mytaxi.ui.adapters.models.CommonModel
import uz.mytaxi.ui.adapters.models.TimeDividerModel
import uz.mytaxi.ui.adapters.models.TripModel

class TripsAdapter(var listener: (TripModel) -> Unit) :
    RecyclerView.Adapter<TripsAdapter.ViewHolder>() {

    private var data = arrayListOf<CommonModel>()
    fun setData(data: ArrayList<CommonModel>) {
        autoNotify(this.data, data) { old, new ->
            old.getType() == new.getType()
                    && old.getTime() == new.getTime()
                    && old.getCostAmount() == new.getCostAmount()
                    && old.getFromPlace() == new.getFromPlace()
                    && old.getWherePlace() == new.getWherePlace()
                    && old.getPriorityImg() == new.getPriorityImg()
        }
        this.data = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripsAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                if (viewType == 1) R.layout.item_trip else R.layout.item_data_divider, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: TripsAdapter.ViewHolder, position: Int) =
        holder.bindData(data[position])

    override fun getItemViewType(position: Int): Int =
        data[position].getType()

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindData(data: CommonModel) {
            if (data.getType() == 1) {
                data.apply {
                    itemView.apply {
                        fromTv.text = getFromPlace()
                        whereTv.text = getWherePlace()
                        timeNpayment.text = "${getTime()} - ${getCostAmount()}"
                        priorityImg.setImageResource(getPriorityImg())

                        setOnClickListener { listener.invoke(data as TripModel) }
                    }
                }
            } else {
                itemView.date.text = (data as TimeDividerModel).getDateString()
            }
        }
    }

}

fun <T> RecyclerView.Adapter<*>.autoNotify(
    oldList: List<T>,
    newList: List<T>,
    compare: (T, T) -> Boolean
) {
    val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return compare(oldList[oldItemPosition], newList[newItemPosition])
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return compare(oldList[oldItemPosition], newList[newItemPosition])
        }

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size
    })

    diff.dispatchUpdatesTo(this)
}