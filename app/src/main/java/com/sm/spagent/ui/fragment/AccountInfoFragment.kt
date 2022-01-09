package com.sm.spagent.ui.fragment

import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.sm.spagent.R
import com.sm.spagent.databinding.FragmentAccountInfoBinding
import com.sm.spagent.model.AccountCategory
import com.sm.spagent.model.ImageType
import com.sm.spagent.ui.viewmodel.AccountInfoViewModel
import java.io.ByteArrayOutputStream
import java.util.*

class AccountInfoFragment : Fragment() {

  private lateinit var viewModel: AccountInfoViewModel
  private var _binding: FragmentAccountInfoBinding? = null

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  private var imageType = ImageType.NOMINEE
  private var accountCategory = AccountCategory.EXISTING_BANK
  private var nomineeImage: String? = null
  private var nomineeNIDFrontImage: String? = null
  private var nomineeNIDBackImage: String? = null

  private val accountTypes = listOf("Current", "Savings")
  private val mfsAccountTypes = listOf("Personal", "Agent", "Merchant")
  private val banks = mutableMapOf<String, Int>()
  private val mfss = mutableMapOf<String, Int>()
  private val relations = mutableMapOf<String, Int>()
  private val occupations = mutableMapOf<String, Int>()
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

    setupViews()
    observeData()

    viewModel.getBanks()
    viewModel.getMfss()
    viewModel.getRelations()
    viewModel.getOccupations()
    viewModel.getDivisions()

    return root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  private fun setupViews() {
    context?.let {
      ArrayAdapter(
        it, android.R.layout.simple_list_item_1,
        accountTypes
      ).also { adapter ->
        binding.accountTypeTextView.setAdapter(adapter)
      }
    }

    context?.let {
      ArrayAdapter(
        it, android.R.layout.simple_list_item_1,
        mfsAccountTypes
      ).also { adapter ->
        binding.mfsAccountTypeTextView.setAdapter(adapter)
      }
    }

    binding.dobLayout.editText?.showSoftInputOnFocus = false
    binding.dobLayout.editText?.setOnTouchListener { _, event ->
      if (event.action == MotionEvent.ACTION_UP) {
        showDatePickerDialog()
        true
      }
      false
    }

    binding.nomineeImagePickerLayout.setOnClickListener { startImageCrop(ImageType.NOMINEE) }
    binding.nomineeNIDFrontPickerLayout.setOnClickListener { startImageCrop(ImageType.NOMINEE_NID_FRONT) }
    binding.nomineeNIDBackPickerLayout.setOnClickListener { startImageCrop(ImageType.NOMINEE_NID_BACK) }

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

    binding.accountRadioGroup.setOnCheckedChangeListener { _, checkedId ->
      when (checkedId) {
        R.id.existingAccountRadioButton -> {
          accountCategory = AccountCategory.EXISTING_BANK
          binding.accountInfoLayout.visibility = View.VISIBLE
          binding.nomineeInfoLayout.visibility = View.GONE
          binding.mfsInfoLayout.visibility = View.GONE
        }
        R.id.newAccountRadioButton -> {
          accountCategory = AccountCategory.NEW
          binding.accountInfoLayout.visibility = View.GONE
          binding.nomineeInfoLayout.visibility = View.VISIBLE
          binding.mfsInfoLayout.visibility = View.GONE
        }
        R.id.mfsAccountRadioButton -> {
          accountCategory = AccountCategory.MFS
          binding.accountInfoLayout.visibility = View.GONE
          binding.nomineeInfoLayout.visibility = View.GONE
          binding.mfsInfoLayout.visibility = View.VISIBLE
        }
      }
    }

    binding.saveNextButton.setOnClickListener {
      when (accountCategory) {
        AccountCategory.EXISTING_BANK -> validateExistingBankInputs()
        AccountCategory.MFS -> validateMfsInputs()
        AccountCategory.NEW -> validateNomineeInputs()
      }
    }
  }

  private fun observeData() {
    viewModel.bank.observe(viewLifecycleOwner, { bank ->
      banks.clear()
      for (data in bank.bank_names!!) {
        banks[data.bank_name.toString()] = data.id!!
      }

      context?.let {
        ArrayAdapter(
          it, android.R.layout.simple_list_item_1,
          banks.keys.toList()
        ).also { adapter ->
          binding.bankNameTextView.setAdapter(adapter)
        }
      }
    })

    viewModel.mfs.observe(viewLifecycleOwner, { mfs ->
      mfss.clear()
      for (data in mfs.mfs_names!!) {
        mfss[data.bank_name.toString()] = data.id!!
      }

      context?.let {
        ArrayAdapter(
          it, android.R.layout.simple_list_item_1,
          mfss.keys.toList()
        ).also { adapter ->
          binding.mfsNameTextView.setAdapter(adapter)
        }
      }
    })

    viewModel.relation.observe(viewLifecycleOwner, { relation ->
      relations.clear()
      for (data in relation.relation_names!!) {
        relations[data.relation_name.toString()] = data.id!!
      }

      context?.let {
        ArrayAdapter(
          it, android.R.layout.simple_list_item_1,
          relations.keys.toList()
        ).also { adapter ->
          binding.relationTextView.setAdapter(adapter)
        }
      }
    })

    viewModel.occupation.observe(viewLifecycleOwner, { occupation ->
      occupations.clear()
      for (data in occupation.occupation_names!!) {
        occupations[data.occupation_name.toString()] = data.id!!
      }

      context?.let {
        ArrayAdapter(
          it, android.R.layout.simple_list_item_1,
          occupations.keys.toList()
        ).also { adapter ->
          binding.occupationTextView.setAdapter(adapter)
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
  }

  private fun showDatePickerDialog() {
    val c = Calendar.getInstance()
    val y = c.get(Calendar.YEAR)
    val m = c.get(Calendar.MONTH)
    val d = c.get(Calendar.DAY_OF_MONTH)

    val dialog = DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
      val date = "$year-${monthOfYear + 1}-$dayOfMonth"
      binding.dobLayout.editText?.setText(date)
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
        when (imageType) {
          ImageType.NOMINEE -> setAspectRatio(3, 4)
          ImageType.NOMINEE_NID_FRONT -> setAspectRatio(4, 3)
          ImageType.NOMINEE_NID_BACK -> setAspectRatio(4, 3)
          else -> setAspectRatio(3, 4)
        }
        setFixAspectRatio(true)
      }
    )
  }

  private fun validateExistingBankInputs() {

  }

  private fun validateMfsInputs() {

  }

  private fun validateNomineeInputs() {

  }

  companion object {
    private const val TAG = "AccountInfoFragment"
  }
}
