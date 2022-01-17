package com.sm.spagent.ui.fragment

import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
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
import androidx.lifecycle.lifecycleScope
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.sm.spagent.R
import com.sm.spagent.databinding.FragmentAccountInfoBinding
import com.sm.spagent.model.AccountCategory
import com.sm.spagent.model.AccountInfo
import com.sm.spagent.model.ImageType
import com.sm.spagent.model.NomineeInfo
import com.sm.spagent.ui.activity.NewMerchantActivity
import com.sm.spagent.ui.viewmodel.AccountInfoViewModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class AccountInfoFragment : BaseFragment() {

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
  private val bankNames = mutableMapOf<String, Int>()
  private val mfsNames = mutableMapOf<String, Int>()
  private val relations = mutableMapOf<String, Int>()
  private val occupations = mutableMapOf<String, Int>()
  private val divisions = mutableMapOf<String, Int>()
  private val districts = mutableMapOf<String, Int>()
  private val policeStations = mutableMapOf<String, Int>()

  private val cropImage = registerForActivityResult(CropImageContract()) { result ->
    showProgress()
    if (result.isSuccessful) {
      // use the returned uri
      val uriContent = result.uriContent
      Log.d(TAG, "uriContent: $uriContent")
      val uriFilePath = result.getUriFilePath(requireContext()) // optional usage
      Log.d(TAG, "uriFilePath: $uriFilePath")
      lifecycleScope.launch {
        showProgress()
        val file = File(uriFilePath)
        Log.d(TAG, "file size (KB): ${file.length() / 1024}")
        val compressedImageFile = Compressor.compress(requireContext(), file) { quality(50) }
        Log.d(TAG, "compressedImageFile size (KB): ${compressedImageFile.length() / 1024}")
        val bitmap = BitmapFactory.decodeFile(compressedImageFile.absolutePath)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
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
        hideProgress()
      }
    } else {
      // an error occurred
      val exception = result.error
      Log.e(TAG, "exception: ${exception?.message}", exception)
    }
    hideProgress()
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

    viewModel.getBankNames()
    viewModel.getMfsNames()
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
        R.id.mfsAccountRadioButton -> {
          accountCategory = AccountCategory.MFS
          binding.accountInfoLayout.visibility = View.GONE
          binding.nomineeInfoLayout.visibility = View.GONE
          binding.mfsInfoLayout.visibility = View.VISIBLE
        }
        R.id.newAccountRadioButton -> {
          accountCategory = AccountCategory.NEW
          binding.accountInfoLayout.visibility = View.GONE
          binding.nomineeInfoLayout.visibility = View.VISIBLE
          binding.mfsInfoLayout.visibility = View.GONE
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

    viewModel.bank.observe(viewLifecycleOwner, { bank ->
      bankNames.clear()
      for (data in bank.bank_names!!) {
        bankNames[data.bank_name.toString()] = data.id!!
      }

      context?.let {
        ArrayAdapter(
          it, android.R.layout.simple_list_item_1,
          bankNames.keys.toList()
        ).also { adapter ->
          binding.bankNameTextView.setAdapter(adapter)
        }
      }
    })

    viewModel.mfs.observe(viewLifecycleOwner, { mfs ->
      mfsNames.clear()
      for (data in mfs.mfs_names!!) {
        mfsNames[data.bank_name.toString()] = data.id!!
      }

      context?.let {
        ArrayAdapter(
          it, android.R.layout.simple_list_item_1,
          mfsNames.keys.toList()
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

    viewModel.accountInfo.observe(viewLifecycleOwner, { accountInfo ->
      Log.d(TAG, "accountInfo: $accountInfo")
      when (accountInfo.sp_code) {
        "1" -> {
          shortToast(accountInfo.message.toString())
        }
        "2" -> {
          shortToast(accountInfo.message.toString())
        }
        else -> {
          shortToast(R.string.something_went_wrong)
        }
      }
    })

    viewModel.nomineeInfo.observe(viewLifecycleOwner, { nomineeInfo ->
      Log.d(TAG, "nomineeInfo: $nomineeInfo")
      when (nomineeInfo.sp_code) {
        "1" -> {
          shortToast(nomineeInfo.message.toString())
        }
        "2" -> {
          shortToast(nomineeInfo.message.toString())
        }
        else -> {
          shortToast(R.string.something_went_wrong)
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
      val moy = if (monthOfYear>8) (monthOfYear+1) else "0${monthOfYear+1}"
      val dom = if (dayOfMonth>9) dayOfMonth else "0$dayOfMonth"
      val date = "$year-$moy-$dom"
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
          ImageType.NOMINEE_NID_FRONT -> setAspectRatio(8, 5)
          ImageType.NOMINEE_NID_BACK -> setAspectRatio(8, 5)
          else -> setAspectRatio(3, 4)
        }
        setFixAspectRatio(true)
      }
    )
  }

  private fun validateExistingBankInputs() {
    val accountCategory = binding.existingAccountRadioButton.text.toString()
    val accountType = binding.accountTypeTextView.text.toString()
    val accountName = binding.accountNameLayout.editText?.text.toString()
    val accountNumber = binding.accountNumberLayout.editText?.text.toString()
    val bankName = binding.bankNameTextView.text.toString()
    val branchName = binding.branchNameLayout.editText?.text.toString()
    val routingNumber = binding.routingNumberLayout.editText?.text.toString()

    if (accountType.isEmpty()) {
      binding.accountTypeLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.accountTypeLayout.y.toInt())
      return
    } else {
      binding.accountTypeLayout.error = null
    }
    if (accountName.isEmpty()) {
      binding.accountNameLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.accountNameLayout.y.toInt())
      return
    } else {
      binding.accountNameLayout.error = null
    }
    if (accountNumber.isEmpty()) {
      binding.accountNumberLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.accountNumberLayout.y.toInt())
      return
    } else {
      binding.accountNumberLayout.error = null
    }
    if (bankName.isEmpty()) {
      binding.bankNameLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.bankNameLayout.y.toInt())
      return
    } else {
      binding.bankNameLayout.error = null
    }

    val accountInfo = AccountInfo(
      accountCategory,
      accountType,
      accountName,
      accountNumber,
      bankNames[bankName]!!,
      branchName,
      routingNumber,
      null,
      (activity as NewMerchantActivity).getShopOwnerId(),
      null,
      null,
      null,
      null
    )

    submitExistingBankInfo(accountInfo)
  }

  private fun submitExistingBankInfo(accountInfo: AccountInfo) {
    viewModel.submitAccountInfo(accountInfo)
  }

  private fun validateMfsInputs() {
    val accountCategory = binding.mfsAccountRadioButton.text.toString()
    val accountType = binding.mfsAccountTypeTextView.text.toString()
    val accountName = binding.mfsAccountNameLayout.editText?.text.toString()
    val accountNumber = binding.mfsAccountNumberLayout.editText?.text.toString()
    val mfsName = binding.mfsNameTextView.text.toString()

    if (accountType.isEmpty()) {
      binding.mfsAccountTypeLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.mfsAccountTypeLayout.y.toInt())
      return
    } else {
      binding.mfsAccountTypeLayout.error = null
    }
    if (accountName.isEmpty()) {
      binding.mfsAccountNameLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.mfsAccountNameLayout.y.toInt())
      return
    } else {
      binding.mfsAccountNameLayout.error = null
    }
    if (accountNumber.isEmpty()) {
      binding.mfsAccountNumberLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.mfsAccountNumberLayout.y.toInt())
      return
    } else {
      binding.mfsAccountNumberLayout.error = null
    }
    if (mfsName.isEmpty()) {
      binding.mfsNameLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.mfsNameLayout.y.toInt())
      return
    } else {
      binding.mfsNameLayout.error = null
    }

    val accountInfo = AccountInfo(
      accountCategory,
      accountType,
      accountName,
      accountNumber,
      mfsNames[mfsName]!!,
      null,
      null,
      1,
      (activity as NewMerchantActivity).getShopOwnerId(),
      null,
      null,
      null,
      null
    )

    submitMfsInfo(accountInfo)
  }

  private fun submitMfsInfo(accountInfo: AccountInfo) {
    viewModel.submitAccountInfo(accountInfo)
  }

  private fun validateNomineeInputs() {
    val accountCategory = binding.newAccountRadioButton.text.toString()
    val nomineeName = binding.nomineeNameLayout.editText?.text.toString()
    val fatherHusbandsName = binding.fathersNameLayout.editText?.text.toString()
    val mothersName = binding.mothersNameLayout.editText?.text.toString()
    val relation = binding.relationTextView.text.toString()
    val contactNo = binding.contactLayout.editText?.text.toString()
    val email = binding.emailLayout.editText?.text.toString()
    val dob = binding.dobLayout.editText?.text.toString()
    val nidNo = binding.nidLayout.editText?.text.toString()
    val occupation = binding.occupationTextView.text.toString()
    val address = binding.addressLayout.editText?.text.toString()
    val division = binding.divisionLayout.editText?.text.toString()
    val district = binding.districtLayout.editText?.text.toString()
    val policeStation = binding.policeStationLayout.editText?.text.toString()

    if (nomineeName.isEmpty()) {
      binding.nomineeNameLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.nomineeNameLayout.y.toInt())
      return
    } else {
      binding.nomineeNameLayout.error = null
    }
    if (fatherHusbandsName.isEmpty()) {
      binding.fathersNameLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.fathersNameLayout.y.toInt())
      return
    } else {
      binding.fathersNameLayout.error = null
    }
    if (mothersName.isEmpty()) {
      binding.mothersNameLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.mothersNameLayout.y.toInt())
      return
    } else {
      binding.mothersNameLayout.error = null
    }
    if (relation.isEmpty()) {
      binding.relationLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.relationLayout.y.toInt())
      return
    } else {
      binding.relationLayout.error = null
    }
    if (contactNo.isEmpty()) {
      binding.contactLayout.error = getString(R.string.this_field_is_required)
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
    if (dob.isEmpty()) {
      binding.dobLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.dobLayout.y.toInt())
      return
    } else {
      binding.dobLayout.error = null
    }
    if (nidNo.isEmpty()) {
      binding.nidLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.nidLayout.y.toInt())
      return
    } else {
      binding.nidLayout.error = null
    }
    if (nidNo.length != 10 && nidNo.length != 13 && nidNo.length != 17) {
      binding.nidLayout.error = getString(R.string.nid_is_invalid)
      binding.scrollView.smoothScrollTo(0, binding.nidLayout.y.toInt())
      return
    } else {
      binding.nidLayout.error = null
    }
    if (occupation.isEmpty()) {
      binding.occupationLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.occupationLayout.y.toInt())
      return
    } else {
      binding.occupationLayout.error = null
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
    if (nomineeImage == null) {
      shortSnack(binding.nomineeImageLayout, R.string.capture_nominee_image)
      binding.scrollView.smoothScrollTo(0, binding.nomineeImageLayout.y.toInt())
      return
    }
    if (nomineeNIDFrontImage == null) {
      shortSnack(binding.nomineeNIDFrontLayout, R.string.capture_front_side_of_nid)
      binding.scrollView.smoothScrollTo(0, binding.nomineeNIDFrontLayout.y.toInt())
      return
    }
    if (nomineeNIDBackImage == null) {
      shortSnack(binding.nomineeNIDBackLayout, R.string.capture_back_side_of_nid)
      binding.scrollView.smoothScrollTo(0, binding.nomineeNIDBackLayout.y.toInt())
      return
    }

    val nomineeInfo = NomineeInfo(
      nomineeName,
      fatherHusbandsName,
      mothersName,
      relations[relation]!!,
      contactNo,
      email,
      dob,
      nidNo,
      occupations[occupation]!!,
      address,
      divisions[division]!!,
      districts[district]!!,
      policeStations[policeStation]!!,
      (activity as NewMerchantActivity).getShopOwnerId(),
      nomineeImage!!,
      nomineeNIDFrontImage!!,
      nomineeNIDBackImage!!,
      null,
      null,
      null,
      null
    )

    submitNomineeInfo(nomineeInfo)
  }

  private fun submitNomineeInfo(nomineeInfo: NomineeInfo) {
    viewModel.submitNomineeInfo(nomineeInfo)
  }

  companion object {
    private const val TAG = "AccountInfoFragment"
  }
}
