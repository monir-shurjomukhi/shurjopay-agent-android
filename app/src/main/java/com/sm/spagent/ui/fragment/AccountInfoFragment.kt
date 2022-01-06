package com.sm.spagent.ui.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.canhub.cropper.CropImageContract
import com.sm.spagent.R
import com.sm.spagent.databinding.FragmentAccountInfoBinding
import com.sm.spagent.model.AccountType
import com.sm.spagent.model.ImageType
import com.sm.spagent.ui.activity.NewMerchantActivity
import com.sm.spagent.ui.viewmodel.AccountInfoViewModel
import java.io.ByteArrayOutputStream

class AccountInfoFragment : Fragment() {

  private lateinit var viewModel: AccountInfoViewModel
  private var _binding: FragmentAccountInfoBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  private var imageType = ImageType.NOMINEE
  private var accountType = AccountType.EXISTING_BANK
  private var nomineeImage: String? = null
  private var nomineeNIDFrontImage: String? = null
  private var nomineeNIDBackImage: String? = null

  private val divisions = mutableMapOf<String, Int>()
  private val districts = mutableMapOf<String, Int>()
  private val policeStations = mutableMapOf<String, Int>()

  private val cropImage = registerForActivityResult(CropImageContract()) { result ->
    if (result.isSuccessful) {
      // use the returned uri
      val uriContent = result.uriContent
      val uriFilePath = result.getUriFilePath(requireContext()) // optional usage
      val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uriContent)
      val outputStream = ByteArrayOutputStream()
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
      val byteArray: ByteArray = outputStream.toByteArray()
      when (imageType) {
        ImageType.NOMINEE -> {
          binding.nomineeImageView.setImageBitmap(bitmap)
          nomineeImage = Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
        ImageType.NOMINEE_NID_FRONT -> {
          binding.nomineeNIDFrontImageView.setImageBitmap(bitmap)
          nomineeNIDFrontImage = Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
        ImageType.NOMINEE_NID_BACK -> {
          binding.nomineeNIDBackImageView.setImageBitmap(bitmap)
          nomineeNIDBackImage = Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
        else -> {
          binding.nomineeImageView.setImageBitmap(bitmap)
          nomineeImage = Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
      }
    } else {
      // an error occurred
      val exception = result.error
      Log.e(TAG, "exception: ${exception?.message}", exception)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    viewModel = ViewModelProvider(this)[AccountInfoViewModel::class.java]

    _binding = FragmentAccountInfoBinding.inflate(inflater, container, false)
    val root: View = binding.root

    binding.accountRadioGroup.setOnCheckedChangeListener { _, checkedId ->
      when (checkedId) {
        R.id.existingAccountRadioButton -> {
          binding.accountInfoLayout.visibility = View.VISIBLE
          binding.nomineeInfoLayout.visibility = View.GONE
          binding.mfsInfoLayout.visibility = View.GONE
        }
        R.id.newAccountRadioButton -> {
          binding.accountInfoLayout.visibility = View.GONE
          binding.nomineeInfoLayout.visibility = View.VISIBLE
          binding.mfsInfoLayout.visibility = View.GONE
        }
        R.id.mfsAccountRadioButton -> {
          binding.accountInfoLayout.visibility = View.GONE
          binding.nomineeInfoLayout.visibility = View.GONE
          binding.mfsInfoLayout.visibility = View.VISIBLE
        }
      }
    }

    binding.saveNextButton.setOnClickListener {
      (activity as NewMerchantActivity).goToNextStep()
    }

    return root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  private fun observeData() {
    viewModel.division.observe(viewLifecycleOwner, { division ->
      divisions.clear()
      for(data in division.divisions!!) {
        divisions[data.division_name.toString()] = data.id!!
      }

      context?.let {
        ArrayAdapter(
          it, android.R.layout.simple_list_item_1,
          divisions.keys.toList()
        ).also { adapter ->
          binding.divisionTextView.setAdapter(adapter)
        }
      }
    })

    viewModel.district.observe(viewLifecycleOwner, { district ->
      districts.clear()
      for(data in district.districts!!) {
        districts[data.district_name.toString()] = data.id!!
      }

      context?.let {
        ArrayAdapter(
          it, android.R.layout.simple_list_item_1,
          districts.keys.toList()
        ).also { adapter ->
          binding.districtTextView.setAdapter(adapter)
        }
      }
    })

    viewModel.policeStation.observe(viewLifecycleOwner, { policeStation ->
      policeStations.clear()
      for(data in policeStation.police_stations!!) {
        policeStations[data.police_station_name.toString()] = data.id!!
      }

      context?.let {
        ArrayAdapter(
          it, android.R.layout.simple_list_item_1,
          policeStations.keys.toList()
        ).also { adapter ->
          binding.policeStationTextView.setAdapter(adapter)
        }
      }
    })
  }

  companion object {
    private const val TAG = "AccountInfoFragment"
  }
}
