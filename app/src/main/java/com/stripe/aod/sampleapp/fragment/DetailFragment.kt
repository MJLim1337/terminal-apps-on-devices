package com.stripe.aod.sampleapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.stripe.aod.sampleapp.R
import com.stripe.aod.sampleapp.data.CreatePaymentParams
import com.stripe.aod.sampleapp.databinding.FragmentDetailBinding
import com.stripe.aod.sampleapp.databinding.FragmentInputBinding
import com.stripe.aod.sampleapp.utils.backToHome
import com.stripe.aod.sampleapp.utils.formatCentsToString
import com.stripe.aod.sampleapp.utils.navOptions
import com.stripe.aod.sampleapp.utils.setThrottleClickListener
import com.stripe.aod.sampleapp.model.CheckoutViewModel
import com.stripe.aod.sampleapp.model.InputViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : Fragment(R.layout.fragment_detail) {
    private val args: DetailFragmentArgs by navArgs()
    private lateinit var viewBinding: FragmentDetailBinding
    private val inputViewModel by viewModels<InputViewModel>()
    private val checkoutViewModel by viewModels<CheckoutViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)

        viewBinding.detailDonationAmount.text = formatCentsToString(args.price)

        viewBinding.detailBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        viewBinding.detailPayBtn.setThrottleClickListener { requestNewPayment() }
    }

    private fun initView(view: View) {
        viewBinding = FragmentDetailBinding.bind(view)
    }

    private fun requestNewPayment() {
        viewBinding.detailPayBtn.isEnabled = false

        checkoutViewModel.createPaymentIntent(
            CreatePaymentParams(
                amount = args.price,
                currency = "sgd",
                description = "Campaign donation",
                metadata = mapOf(
                    "name" to viewBinding.editTextName.toString(),
                    "email" to viewBinding.editTextTextEmailAddress.toString(),
                    "phone" to viewBinding.editTextPhone.toString(),
                    "nric" to viewBinding.editTextIdentity.toString(),
                )
            )
        ) { failureMessage ->
            Snackbar.make(
                viewBinding.root,
                failureMessage.value.ifEmpty {
                    getString(R.string.error_fail_to_create_payment_intent)
                },
                Snackbar.LENGTH_SHORT
            ).show()
            viewBinding.detailPayBtn.isEnabled = true
        }
    }
}