package com.sm.spagent.ui.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.sm.spagent.R
import com.sm.spagent.databinding.FragmentShopInfoBinding
import com.sm.spagent.model.ImageType
import com.sm.spagent.model.ShopInfo
import com.sm.spagent.ui.activity.NewMerchantActivity
import com.sm.spagent.ui.viewmodel.ShopInfoViewModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File

class ShopInfoFragment : BaseFragment() {

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
      Log.d(TAG, "uriContent: $uriContent")
      val uriFilePath = result.getUriFilePath(requireContext()) // optional usage
      Log.d(TAG, "uriFilePath: $uriFilePath")
      lifecycleScope.launch {
        val file = File(uriFilePath)
        Log.d(TAG, "file size (KB): ${file.length() / 1024}")
        val compressedImageFile = Compressor.compress(requireContext(), file) { quality(50) }
        Log.d(TAG, "compressedImageFile size (KB): ${compressedImageFile.length() / 1024}")
        val bitmap = BitmapFactory.decodeFile(compressedImageFile.absolutePath)
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

    viewModel.getBusinessTypes()
    viewModel.getDivisions()

    return root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  private fun setupViews() {
    binding.tradeLicensePickerLayout.setOnClickListener { startImageCrop(ImageType.TRADE_LICENSE) }
    binding.shopFrontPickerLayout.setOnClickListener { startImageCrop(ImageType.SHOP_FRONT) }

    context?.let {
      ArrayAdapter(
        it, android.R.layout.simple_list_item_1,
        shopSizes
      ).also { adapter ->
        binding.shopSizeTextView.setAdapter(adapter)
      }
    }

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
      validateInputs()
    }
  }

  private fun observeData() {
    viewModel.progress.observe(viewLifecycleOwner, {
      if (it) {
        showProgress()
      } else {
        hideProgress()
      }
    })

    viewModel.message.observe(viewLifecycleOwner, {
      shortSnack(binding.root, it)
    })

    viewModel.businessType.observe(viewLifecycleOwner, { businessType ->
      businessTypes.clear()
      for (data in businessType.business_type_names!!) {
        businessTypes[data.business_type_name.toString()] = data.id!!
      }

      context?.let {
        ArrayAdapter(
          it, android.R.layout.simple_list_item_1,
          businessTypes.keys.toList()
        ).also { adapter ->
          binding.businessTypeTextView.setAdapter(adapter)
        }
      }
    })

    viewModel.division.observe(viewLifecycleOwner, { division ->
      divisions.clear()
      for (data in division.divisions!!) {
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
      for (data in district.districts!!) {
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
      for (data in policeStation.police_stations!!) {
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

    viewModel.shopInfo.observe(viewLifecycleOwner, { shopInfo ->
      Log.d(TAG, "shopInfo: $shopInfo")
      when (shopInfo.sp_code) {
        "1" -> {
          shortToast(shopInfo.message.toString())
          (activity as NewMerchantActivity).goToNextStep()
        }
        "2" -> {
          shortToast(shopInfo.message.toString())
        }
        else -> {
          shortToast(R.string.something_went_wrong)
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
        when (imageType) {
          ImageType.TRADE_LICENSE -> setAspectRatio(3, 4)
          ImageType.SHOP_FRONT -> setAspectRatio(4, 3)
          else -> setAspectRatio(3, 4)
        }
        setFixAspectRatio(true)
      }
    )
  }

  private fun validateInputs() {
    val shopName = binding.shopNameLayout.editText?.text.toString()
    val tin = binding.tinLayout.editText?.text.toString()
    val tradeLicenseNo = binding.tradeLicenseNoLayout.editText?.text.toString()
    val businessType = binding.businessTypeTextView.text.toString()
    val shopSize = binding.shopSizeTextView.text.toString()
    val shopAddress = binding.shopAddressLayout.editText?.text.toString()
    val division = binding.divisionTextView.text.toString()
    val district = binding.districtTextView.text.toString()
    val policeStation = binding.policeStationTextView.text.toString()
    val shopLocation = binding.locationLayout.editText?.text.toString()

    if (shopName.isEmpty()) {
      binding.shopNameLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.shopNameLayout.y.toInt())
      return
    } else {
      binding.shopNameLayout.error = null
    }
    if (businessType.isEmpty()) {
      binding.businessTypeLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.businessTypeLayout.y.toInt())
      return
    } else {
      binding.businessTypeLayout.error = null
    }
    if (shopSize.isEmpty()) {
      binding.shopSizeLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.shopSizeLayout.y.toInt())
      return
    } else {
      binding.shopSizeLayout.error = null
    }
    if (shopAddress.isEmpty()) {
      binding.shopAddressLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.shopAddressLayout.y.toInt())
      return
    } else {
      binding.shopAddressLayout.error = null
    }
    if (division.isEmpty()) {
      binding.divisionLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.divisionLayout.y.toInt())
      return
    } else {
      binding.divisionLayout.error = null
    }
    if (district.isEmpty()) {
      binding.districtLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.districtLayout.y.toInt())
      return
    } else {
      binding.districtLayout.error = null
    }
    if (policeStation.isEmpty()) {
      binding.policeStationLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.policeStationLayout.y.toInt())
      return
    } else {
      binding.policeStationLayout.error = null
    }
    if (shopLocation.isEmpty()) {
      binding.locationLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.locationLayout.y.toInt())
      return
    } else {
      binding.locationLayout.error = null
    }
    /*if (tradeLicenseImage == null) {
      shortSnack(binding.tradeLicenseLayout, R.string.capture_trade_license_image)
      binding.scrollView.smoothScrollTo(0, binding.tradeLicenseLayout.y.toInt())
      return
    }*/
    if (shopFrontImage == null) {
      shortSnack(binding.shopFrontLayout, R.string.capture_shop_front_image)
      binding.scrollView.smoothScrollTo(0, binding.shopFrontLayout.y.toInt())
      return
    }

    val shopInfo = ShopInfo(
      shopName,
      tin,
      businessTypes[businessType]!!,
      shopSize,
      shopAddress,
      divisions[division]!!,
      districts[district]!!,
      policeStations[policeStation]!!,
      shopLocation,
      tradeLicenseImage,
      shopFrontImage!!,
      (activity as NewMerchantActivity).getShopOwnerId(),
      null,
      null,
      null,
      null
    )
    submitShopInfo(shopInfo)
  }

  private fun submitShopInfo(shopInfo: ShopInfo) {
    viewModel.submitShopInfo(shopInfo)
  }

  companion object {
    private const val TAG = "ShopInfoFragment"
  }
}
