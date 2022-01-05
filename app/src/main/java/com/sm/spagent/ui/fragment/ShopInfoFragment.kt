package com.sm.spagent.ui.fragment

import android.R
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.sm.spagent.databinding.FragmentShopInfoBinding
import com.sm.spagent.model.ImageType
import com.sm.spagent.ui.viewmodel.ShopInfoViewModel
import java.io.ByteArrayOutputStream

class ShopInfoFragment : Fragment() {

  private lateinit var viewModel: ShopInfoViewModel
  private var _binding: FragmentShopInfoBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  private var imageType = ImageType.TRADE_LICENSE
  private var tradeLicenseImage: String? = null
  private var shopFrontImage: String? = null

  private val shopSizes = listOf("Big", "Medium", "Small")
  private val businessTypes = mutableMapOf<String, Int>()
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
        ImageType.TRADE_LICENSE -> {
          binding.tradeLicenseImageView.setImageBitmap(bitmap)
          tradeLicenseImage = Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
        ImageType.SHOP_FRONT -> {
          binding.shopFrontImageView.setImageBitmap(bitmap)
          shopFrontImage = Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
        else -> {
          binding.tradeLicenseImageView.setImageBitmap(bitmap)
          tradeLicenseImage = Base64.encodeToString(byteArray, Base64.DEFAULT)
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
    viewModel = ViewModelProvider(this)[ShopInfoViewModel::class.java]

    _binding = FragmentShopInfoBinding.inflate(inflater, container, false)
    val root: View = binding.root

    setupViews()
    observeData()

    viewModel.getDivisions()

    return root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  private fun setupViews() {
    binding.businessTypeTextView.inputType = InputType.TYPE_NULL
    binding.shopSizeTextView.inputType = InputType.TYPE_NULL
    binding.divisionTextView.inputType = InputType.TYPE_NULL
    binding.divisionTextView.inputType = InputType.TYPE_NULL
    binding.districtTextView.inputType = InputType.TYPE_NULL
    binding.policeStationTextView.inputType = InputType.TYPE_NULL

    binding.tradeLicensePickerLayout.setOnClickListener { startImageCrop(ImageType.TRADE_LICENSE) }
    binding.shopFrontPickerLayout.setOnClickListener { startImageCrop(ImageType.SHOP_FRONT) }

    binding.divisionTextView.doAfterTextChanged { text ->
      binding.districtTextView.text = null
      binding.districtTextView.setAdapter(null)
      if (text?.isNotEmpty() == true) {
        val divisionId = divisions[text.toString()]
        viewModel.getDistricts(divisionId!!)
      }
    }

    binding.districtTextView.doAfterTextChanged { text ->
      binding.policeStationTextView.text = null
      binding.policeStationTextView.setAdapter(null)
      if (text?.isNotEmpty() == true) {
        val districtId = districts[text.toString()]
        viewModel.getPoliceStations(districtId!!)
      }
    }

    binding.saveNextButton.setOnClickListener {
      //validateInputs()
    }
  }

  private fun observeData() {
    viewModel.division.observe(viewLifecycleOwner, { division ->
      divisions.clear()
      for(data in division.divisions!!) {
        divisions[data.division_name.toString()] = data.id!!
      }

      context?.let {
        ArrayAdapter(
          it, R.layout.simple_list_item_1,
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
          it, R.layout.simple_list_item_1,
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
          it, R.layout.simple_list_item_1,
          policeStations.keys.toList()
        ).also { adapter ->
          binding.policeStationTextView.setAdapter(adapter)
        }
      }
    })
  }

  private fun startImageCrop(type: ImageType) {
    imageType = type
    cropImage.launch(
      options {
        setImageSource(
          includeGallery = false,
          includeCamera = true
        )
        setGuidelines(CropImageView.Guidelines.ON_TOUCH)
        when(imageType) {
          ImageType.TRADE_LICENSE -> setAspectRatio(3, 4)
          ImageType.SHOP_FRONT -> setAspectRatio(4, 3)
          else -> setAspectRatio(3, 4)
        }
        setFixAspectRatio(true)
      }
    )
  }

  companion object {
    private const val TAG = "ShopInfoFragment"
  }
}
