package com.stripe.aod.sampleapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.stripe.aod.sampleapp.utils.formatCentsToString
import com.stripe.aod.sampleapp.R
import com.stripe.aod.sampleapp.databinding.FragmentInputBinding
import com.stripe.aod.sampleapp.databinding.FragmentPriceBinding
import com.stripe.aod.sampleapp.utils.backToHome
import com.stripe.aod.sampleapp.utils.navOptions
import com.stripe.aod.sampleapp.utils.setThrottleClickListener

/**
 * A simple [Fragment] subclass.
 * Use the [PriceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PriceFragment : Fragment(R.layout.fragment_price) {
    private lateinit var viewBinding: FragmentInputBinding

    private var selectedPrice: Int? = null
    private var customPrice: Int? = null
    private var selectedTextView: TextView? = null
    private var isCustomPrice: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_price, container, false)
        val constraintLayoutContainer = view.findViewById<ConstraintLayout>(R.id.priceConstraintLayout)
        val customAmountField = view.findViewById<TextInputLayout>(R.id.price_custom_amount_parent)

        // TODO: get price list
        val prices = arrayOf(1000, 2000, 7000) // Example array of integers
        var previousViewId = View.generateViewId()

        for (integer in prices) {
            val textView = TextView(context).apply {
                id = View.generateViewId()
                text = formatCentsToString(integer)
                textSize = 32f
                setPadding(32, 32, 32, 32)
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                background = ContextCompat.getDrawable(context, R.drawable.price_selector_btn)
                setTextColor(ContextCompat.getColorStateList(context, R.color.text_amount))
                setOnClickListener {
                    selectedPrice = integer
                    selectedTextView?.isSelected = false
                    it.isSelected = true
                    selectedTextView = it as TextView
                    isCustomPrice = false
                    customAmountField.visibility = View.INVISIBLE
//                    Toast.makeText(context, "Selected: $integer", Toast.LENGTH_SHORT).show()
                }
            }
            constraintLayoutContainer.addView(textView)

            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayoutContainer)

            constraintSet.connect(textView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            constraintSet.connect(textView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)

            if (integer == 0) {
                constraintSet.connect(textView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            } else {
                constraintSet.connect(textView.id, ConstraintSet.TOP, previousViewId, ConstraintSet.BOTTOM)
            }

            constraintSet.applyTo(constraintLayoutContainer)
            previousViewId = textView.id
        }
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewBinding = FragmentPriceBinding.bind(view)

        // Custom button
        viewBinding.priceCustom.setOnClickListener {
            isCustomPrice = true
            selectedTextView?.isSelected = false
            it.isSelected = true
            selectedTextView = it as TextView
            viewBinding.priceCustomAmountParent.visibility = View.VISIBLE
        }

        viewBinding.priceCustomAmount.doAfterTextChanged { value ->
            val inputValue = value.toString()
            val inputInt = inputValue.toInt()
            if (inputInt > 0)  {
                selectedPrice = (inputInt * 100)
            } else {
                selectedPrice = 0
            }

//            Toast.makeText(context, "Custom amount: $selectedPrice", Toast.LENGTH_SHORT).show()
        }

        viewBinding.backButton.setOnClickListener {
            backToHome()
        }

        viewBinding.confirmAmount.setThrottleClickListener {
            findNavController().navigate(
                PriceFragmentDirections.actionPriceFragmentToDetailFragment(
                    price = selectedPrice ?: 0
                ),
                navOptions()
            )
        }
    }

}