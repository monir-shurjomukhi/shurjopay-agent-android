package com.sm.spagent.ui.fragment

import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.sm.spagent.R
import com.sm.spagent.databinding.FragmentPersonalInfoBinding
import com.sm.spagent.model.ImageType
import com.sm.spagent.ui.activity.NewMerchantActivity
import com.sm.spagent.ui.viewmodel.PersonalInfoViewModel
import java.io.ByteArrayOutputStream
import java.util.*


class PersonalInfoFragment : BaseFragment() {

  private lateinit var viewModel: PersonalInfoViewModel
  private var _binding: FragmentPersonalInfoBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  private var imageType = ImageType.OWNER
  private var ownerImage: String? = null
  private var ownerNIDFront: String? = null
  private var ownerNIDBack: String? = null
  private var ownerSignature: String? = null
  private var currentStep = 1

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
        ImageType.OWNER -> {
          binding.ownerImageView.setImageBitmap(bitmap)
          ownerImage = Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
        ImageType.OWNER_NID_FRONT -> {
          binding.ownerNIDFrontImageView.setImageBitmap(bitmap)
          ownerNIDFront = Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
        ImageType.OWNER_NID_BACK -> {
          binding.ownerNIDBackImageView.setImageBitmap(bitmap)
          ownerNIDBack = Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
        ImageType.OWNER_SIGNATURE -> {
          binding.ownerSignatureImageView.setImageBitmap(bitmap)
          ownerSignature = Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
        else -> {
          binding.ownerImageView.setImageBitmap(bitmap)
          ownerImage = Base64.encodeToString(byteArray, Base64.DEFAULT)
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
    viewModel = ViewModelProvider(this)[PersonalInfoViewModel::class.java]
    _binding = FragmentPersonalInfoBinding.inflate(inflater, container, false)
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
    binding.divisionTextView.inputType = InputType.TYPE_NULL
    binding.districtTextView.inputType = InputType.TYPE_NULL
    binding.policeStationTextView.inputType = InputType.TYPE_NULL

    binding.step2DOBLayout.editText?.showSoftInputOnFocus = false
    binding.step2DOBLayout.editText?.setOnTouchListener { _, event ->
      if(event.action == MotionEvent.ACTION_UP) {
        showDatePickerDialog()
        true
      }
      false
    }

    binding.ownerImagePickerLayout.setOnClickListener { startImageCrop(ImageType.OWNER) }
    binding.ownerNIDFrontPickerLayout.setOnClickListener { startImageCrop(ImageType.OWNER_NID_FRONT) }
    binding.ownerNIDBackPickerLayout.setOnClickListener { startImageCrop(ImageType.OWNER_NID_BACK) }
    binding.ownerSignatureLayout.setOnClickListener { startImageCrop(ImageType.OWNER_SIGNATURE) }

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
      when(currentStep) {
        1 -> validateStep1Inputs()
        2 -> validateStep2Inputs()
        3 -> validateInputs()
      }
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

  private fun showDatePickerDialog() {
    val c = Calendar.getInstance()
    val y = c.get(Calendar.YEAR)
    val m = c.get(Calendar.MONTH)
    val d = c.get(Calendar.DAY_OF_MONTH)

    val dialog = DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
      val date = "$year-${monthOfYear+1}-$dayOfMonth"
      binding.step2DOBLayout.editText?.setText(date)
    }, y, m, d)

    dialog.show()
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
          ImageType.OWNER -> setAspectRatio(3, 4)
          ImageType.OWNER_NID_FRONT -> setAspectRatio(4, 3)
          ImageType.OWNER_NID_BACK -> setAspectRatio(4, 3)
          ImageType.OWNER_SIGNATURE -> setAspectRatio(2, 1)
          else -> setAspectRatio(3, 4)
        }
        setFixAspectRatio(true)
      }
    )
  }

  private fun validateStep1Inputs() {
    if (ownerImage == null) {
      shortSnack(binding.ownerImageLayout, R.string.capture_shop_owner_image)
      binding.scrollView.smoothScrollTo(0, binding.ownerImageLayout.y.toInt())
      return
    }
    if (ownerNIDFront == null) {
      shortSnack(binding.ownerNIDFrontLayout, R.string.capture_front_side_of_nid)
      binding.scrollView.smoothScrollTo(0, binding.ownerNIDFrontLayout.y.toInt())
      return
    }
    if (ownerNIDBack == null) {
      shortSnack(binding.ownerNIDBackLayout, R.string.capture_back_side_of_nid)
      binding.scrollView.smoothScrollTo(0, binding.ownerNIDBackLayout.y.toInt())
      return
    }

    submitStep1Data()
  }

  private fun submitStep1Data() {
    goToNextStep()
  }

  private fun validateStep2Inputs() {
    val nid = binding.step2NIDLayout.editText?.text.toString()
    val dob = binding.step2DOBLayout.editText?.text.toString()

    if (nid.isEmpty()) {
      binding.step2NIDLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.step2NIDLayout.y.toInt())
      return
    } else {
      binding.step2NIDLayout.error = null
    }
    if (nid.length != 10 && nid.length != 13 && nid.length != 17) {
      binding.step2NIDLayout.error = getString(R.string.nid_is_invalid)
      binding.scrollView.smoothScrollTo(0, binding.step2NIDLayout.y.toInt())
      return
    } else {
      binding.step2NIDLayout.error = null
    }
    if (dob.isEmpty()) {
      binding.step2DOBLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.step2DOBLayout.y.toInt())
      return
    } else {
      binding.step2DOBLayout.error = null
    }

    submitStep2Data()
  }

  private fun submitStep2Data() {
    goToNextStep()
  }

  private fun validateInputs() {
    val name = binding.nameLayout.editText?.text.toString()
    val contactNo = binding.contactLayout.editText?.text.toString()
    val email = binding.emailLayout.editText?.text.toString()
    val nid = binding.nidLayout.editText?.text.toString()
    val tin = binding.tinLayout.editText?.text.toString()
    val address = binding.addressLayout.editText?.text.toString()
    val division = binding.divisionTextView.text.toString()
    val district = binding.districtTextView.text.toString()
    val policeStation = binding.policeStationTextView.text.toString()
    val dob = binding.dobLayout.editText?.text.toString()

    if (name.isEmpty()) {
      binding.nameLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.nameLayout.y.toInt())
      return
    } else {
      binding.nameLayout.error = null
    }
    if (contactNo.isEmpty()) {
      binding.contactLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.contactLayout.y.toInt())
      return
    } else {
      binding.contactLayout.error = null
    }
    if (contactNo.length != 11 || !contactNo.startsWith("01")) {
      binding.contactLayout.error = getString(R.string.contact_number_is_invalid)
      binding.scrollView.smoothScrollTo(0, binding.contactLayout.y.toInt())
      return
    } else {
      binding.contactLayout.error = null
    }
    if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      binding.emailLayout.error = getString(R.string.email_is_invalid)
      binding.scrollView.smoothScrollTo(0, binding.emailLayout.y.toInt())
      return
    } else {
      binding.emailLayout.error = null
    }
    if (nid.isEmpty()) {
      binding.nidLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.nidLayout.y.toInt())
      return
    } else {
      binding.nidLayout.error = null
    }
    if (nid.length != 10 && nid.length != 13 && nid.length != 17) {
      binding.nidLayout.error = getString(R.string.nid_is_invalid)
      binding.scrollView.smoothScrollTo(0, binding.nidLayout.y.toInt())
      return
    } else {
      binding.nidLayout.error = null
    }
    if (address.isEmpty()) {
      binding.addressLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.addressLayout.y.toInt())
      return
    } else {
      binding.addressLayout.error = null
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
    if (dob.isEmpty()) {
      binding.dobLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.dobLayout.y.toInt())
      return
    } else {
      binding.dobLayout.error = null
    }
    if (ownerSignature == null) {
      shortSnack(binding.ownerSignatureLayout, R.string.capture_shop_owner_signature)
      binding.scrollView.smoothScrollTo(0, binding.ownerSignatureLayout.y.toInt())
      return
    }

    submitPersonalInfo()
  }

  private fun submitPersonalInfo() {
    goToNextStep()
  }

  private fun goToNextStep() {
    when(currentStep) {
      1 -> {
        currentStep = 2
        binding.step1Layout.visibility = View.GONE
        binding.step2Layout.visibility = View.VISIBLE
        binding.step3Layout.visibility = View.GONE
      }
      2 -> {
        currentStep = 3
        binding.step1Layout.visibility = View.GONE
        binding.step2Layout.visibility = View.GONE
        binding.step3Layout.visibility = View.VISIBLE
        binding.saveNextButton.text = getString(R.string.save_next)
      }
      3 -> (activity as NewMerchantActivity).goToNextStep()
    }
  }

  companion object {
    private const val TAG = "PersonalInfoFragment"
  }
}
