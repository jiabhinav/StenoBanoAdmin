package com.stenobano.admin.ui

import com.stenobano.admin.other_class.ProcessingDialog
import com.stenobano.admin.model.ModelSubscription
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import com.stenobano.admin.R
import android.text.TextWatcher
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.stenobano.admin.ui.home.HomeFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.stenobano.admin.retrofit.APIClient
import com.google.gson.Gson
import com.stenobano.admin.databinding.FragmentPurchaseAmountBinding
import com.stenobano.admin.session.SesssionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PurchaseAmountFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentPurchaseAmountBinding
    private val ll: LinearLayout? = null
    private var processingDialog: ProcessingDialog? = null
    var model = ModelSubscription.Result()
    var sesssionManager: SesssionManager? = null
    var status=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sesssionManager = SesssionManager(requireContext())
        model= requireArguments().getParcelable("list")!!
        Log.d("TAG", "onCreate44432: "+Gson().toJson(model))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPurchaseAmountBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }


    fun init()
    {
        processingDialog = ProcessingDialog(requireContext())
        binding.save.setOnClickListener(this)

        binding.plan.setText(model.plan)
        binding.offervaliddesc.setText(model.offerValidMesg)
        binding.offervaliddesc.setText(model.offerValidMesg)
        binding.plandesc.setText(model.des)
        binding.valid.setText(model.valid)
        val plan=model.amount.toInt()+model.disPrice.toInt()
        binding.amount.setText(plan.toString())
        binding.discountamount.setText(model.disPrice)
        binding.finalprice.setText(model.amount)

        val some_array = resources.getStringArray(R.array.plan_status)
        val adapter: ArrayAdapter<*> = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, some_array)
        binding.status.setAdapter(adapter)
        if (model.status.equals("1"))
        {
            binding.status.setSelection(0)
        }
        else
        {
            binding.status.setSelection(1)
        }

        binding.status.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position==0)
                status = "1"
                else
                 status = "0"
            }

        }

        binding.discountamount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {

                if (!binding.discountamount.text.toString().equals(""))
                {
                    val amount = binding.discountamount.text.toString().toLowerCase(Locale.getDefault()).toInt()
                    val planapunt=binding.amount.text.toString().toInt()

                    if (planapunt > amount)
                    {
                        val finalprice=planapunt-amount
                        binding.finalprice.setText(finalprice.toString())
                    }
                    else{
                        Toast.makeText(requireContext(), "Discount amount should be less than subcription plan", Toast.LENGTH_SHORT).show()
                    }
                }
                else
                {
                    binding.finalprice.setText(binding.amount.text.toString())
                }




            }
        })

        binding.amount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
               binding.discountamount.setText("")

            }
        })


    }




    override fun onClick(v: View) {
        if (v.id == R.id.save) {
            UpdatePlan()
        }
    }



    private fun UpdatePlan() {
        if (binding.plan.text.toString() == "Select Plan") {
            Toast.makeText(getContext(), "Choose plan first", Toast.LENGTH_SHORT).show()
        } else if (binding.valid.text.toString() == "") {
            Toast.makeText(getContext(), "Enter days", Toast.LENGTH_SHORT).show()
        } else if (binding.amount.text.toString() == "") {
            Toast.makeText(getContext(), "Enter Amount", Toast.LENGTH_SHORT).show()
        }

        else if (binding.plandesc.text.toString() == "") {
            Toast.makeText(getContext(), "Enter plan description", Toast.LENGTH_SHORT).show()
        }

        else {

            val map = HashMap<String, String>()
            map["id"] = model.id
            map["valid"] = binding.valid.text.toString()
            map["amount"] = binding.finalprice.text.toString()
            if ( binding.discountamount.text.toString().equals(""))
            {
                map["discount"] = "0"
            }
            else
            {
                map["discount"] = binding.discountamount.text.toString()
            }
            map["offer_valid_mesg"] = binding.offervaliddesc.text.toString()
            map["des"] = binding.plandesc.text.toString()
            map["status"] = status

            processingDialog!!.show("wait...")
            val call = APIClient.getInstance().UpdatePurchase(map)
            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    processingDialog!!.dismiss()
                    Log.d("type122sdddd", "msg" + Gson().toJson(response.body()))

                    if (response.body().equals("Updated"))
                    {
                        findNavController().navigateUp()
                    }
                    Toast.makeText(requireContext(), "" + response.body(), Toast.LENGTH_SHORT).show()

                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("erere", t.toString())
                    processingDialog!!.dismiss()
                }
            })
        }
    }
}