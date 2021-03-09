package fpt.nsr.stub.device.presentation.navigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import fpt.nsr.stub.device.R
import fpt.nsr.stub.device.databinding.BottomNavigationiTemBinding
import org.jetbrains.anko.backgroundResource
import java.util.*

class BottomNavigationAdapter(val mListener: IBottomNavigationAdapterListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data: MutableList<BottomNavigationModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holderBottomNavigationModelBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context), R.layout.bottom_navigationi_tem, parent, false
        )
        return BottomNavigationModelViewHolder(holderBottomNavigationModelBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BottomNavigationModelViewHolder).onBind(getItem(position))
    }

    private fun getItem(position: Int): BottomNavigationModel {
        return data[position]
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun addData(list: List<BottomNavigationModel>) {
        this.data.clear()
        this.data.addAll(list)
        notifyDataSetChanged()
    }

    inner class BottomNavigationModelViewHolder(private val dataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {

        fun onBind(bottomNavigationModel: BottomNavigationModel) {
            val holderBottomNavigation =
                dataBinding as BottomNavigationiTemBinding
            val navigationViewModel = BottomNavigationViewModel(bottomNavigationModel)
            holderBottomNavigation.dataViewModel = navigationViewModel

            when (bottomNavigationModel.type) {
                DATA_TYPE.MENU, DATA_TYPE.VIDEO, DATA_TYPE.CAR -> {
                    holderBottomNavigation.icon.backgroundResource =
                        bottomNavigationModel.resID
                    holderBottomNavigation.icon.visibility = View.VISIBLE
                    holderBottomNavigation.text.visibility = View.GONE
                }
                DATA_TYPE.OK -> {
                    holderBottomNavigation.icon.visibility = View.GONE
                    holderBottomNavigation.text.visibility = View.VISIBLE
                    holderBottomNavigation.text.text = bottomNavigationModel.text
                }
                DATA_TYPE.EMPTY -> {
                    holderBottomNavigation.icon.visibility = View.GONE
                    holderBottomNavigation.text.visibility = View.GONE
                }
            }
            itemView.setOnClickListener {
                mListener.onBottomNavigationItemClick(bottomNavigationModel)
            }
        }
    }

    data class BottomNavigationModel(val type: DATA_TYPE, val text: String, val resID: Int)

    enum class DATA_TYPE {
        MENU, VIDEO, CAR, OK, EMPTY
    }
}
