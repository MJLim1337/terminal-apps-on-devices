package com.stripe.aod.sampleapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.stripe.aod.sampleapp.R
import com.stripe.aod.sampleapp.databinding.FragmentAuthBinding
import com.stripe.aod.sampleapp.databinding.FragmentDetailBinding
import com.stripe.aod.sampleapp.databinding.FragmentInputBinding
import com.stripe.aod.sampleapp.databinding.FragmentPriceBinding
import com.stripe.aod.sampleapp.utils.backToHome
import com.stripe.aod.sampleapp.utils.navOptions
import com.stripe.aod.sampleapp.utils.setThrottleClickListener

/**
 * A simple [Fragment] subclass.
 * Use the [AuthFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AuthFragment : Fragment(R.layout.fragment_auth) {
    private val args: AuthFragmentArgs by navArgs()
    private lateinit var viewBinding: FragmentAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentAuthBinding.bind(view)

        viewBinding.authBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        viewBinding.authManualButton.setThrottleClickListener {
            findNavController().navigate(
                AuthFragmentDirections.actionAuthFragmentToDetailFragment(
                    price = args.price
                ),
                navOptions()
            )
        }
    }
}