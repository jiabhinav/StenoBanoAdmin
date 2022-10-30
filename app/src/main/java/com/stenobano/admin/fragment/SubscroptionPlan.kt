package com.stenobano.admin.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.stenobano.admin.R
import com.stenobano.admin.adapter.SubscriptionAdapter
import com.stenobano.admin.databinding.FragmentSubscroptionPlanBinding
import com.stenobano.admin.model.ModelSubscription
import com.stenobano.admin.other_class.ProcessingDialog
import com.stenobano.admin.retrofit.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SubscroptionPlan : Fragment() {

    lateinit var binding: FragmentSubscroptionPlanBinding

    private var list=ArrayList<ModelSubscription.Result>()
    lateinit var adapter: SubscriptionAdapter

    private var processingDialog: ProcessingDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentSubscroptionPlanBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    fun init()
    {
        processingDialog = ProcessingDialog(requireContext())
        adapter = SubscriptionAdapter(requireActivity(),list)
        binding.recyclerlist.setAdapter(adapter)
        adapter.setOnClickListener(object :SubscriptionAdapter.OnClickItem{
            override fun clickItem(model: ModelSubscription.Result) {
                val bundle=Bundle()
                bundle.putParcelable("list",model)
                findNavController().navigate(R.id.purchae_amount,bundle)
            }

            override fun selected(model: ModelSubscription.Result) {

            }

        })


        GetPlan()

    }

    private fun GetPlan() {
        processingDialog?.show("wait...")
        val map: HashMap<String, String> = HashMap()
        map["key"] = "wodwhouwoifwnhfwoff"
        val call = APIClient.getInstance().getPurchasePlan(map)
        call.enqueue(object : Callback<ModelSubscription> {
            override fun onResponse(
                call: Call<ModelSubscription>,
                response: Response<ModelSubscription>
            ) {
                processingDialog?.dismiss()
                Log.d("plan", "msg" + Gson().toJson(response.body()))
                try {
                    if (response.body().status==200)
                    {
                        list.clear()
                        list.addAll(response.body().result)
                        adapter.notifyDataSetChanged()
                    }


                } catch (e: Exception) {
                    Log.d("onResponse", "There is an error")
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ModelSubscription>, t: Throwable) {
                Log.d("erere", t.toString())
                processingDialog?.dismiss()
            }
        })
    }





}