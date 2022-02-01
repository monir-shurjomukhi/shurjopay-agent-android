package com.sm.spagent.ui.activity

import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.sm.spagent.R
import com.sm.spagent.databinding.ActivityEditPersonalInfoBinding
import com.sm.spagent.model.ImageType
import com.sm.spagent.model.Nid
import com.sm.spagent.model.Ocr
import com.sm.spagent.model.OwnerInfo
import com.sm.spagent.ui.viewmodel.EditPersonalInfoViewModel
import com.sm.spagent.utils.MERCHANT_ID
import com.squareup.picasso.Picasso
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class EditPersonalInfoActivity : BaseActivity() {

  private lateinit var binding: ActivityEditPersonalInfoBinding
  private lateinit var viewModel: EditPersonalInfoViewModel

  private var merchantId = -1
  private var imageType = ImageType.OWNER
  private var ownerImage: String? = null
  private var ownerNIDFront: String? = null
  private var ownerNIDBack: String? = null
  private var ownerSignature: String? = null
  private var currentStep = 1

  private val divisions = mutableMapOf<String, Int>()
  private val divisionsKeyList = mutableListOf<String>()
  private val districts = mutableMapOf<String, Int>()
  private val districtsKeyList = mutableListOf<String>()
  private val policeStations = mutableMapOf<String, Int>()
  private val policeStationsKeyList = mutableListOf<String>()

  private val cropImage = registerForActivityResult(CropImageContract()) { result ->
    if (result.isSuccessful) {
      // use the returned uri
      val uriContent = result.uriContent
      Log.d(TAG, "uriContent: $uriContent")
      val uriFilePath = result.getUriFilePath(this) // optional usage
      Log.d(TAG, "uriFilePath: $uriFilePath")
      lifecycleScope.launch {
        showProgress()
        val file = File(uriFilePath)
        Log.d(TAG, "file size (KB): ${file.length() / 1024}")
        val compressedImageFile =
          Compressor.compress(this@EditPersonalInfoActivity, file) { quality(50) }
        Log.d(TAG, "compressedImageFile size (KB): ${compressedImageFile.length() / 1024}")
        val bitmap = BitmapFactory.decodeFile(compressedImageFile.absolutePath)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val byteArray: ByteArray = outputStream.toByteArray()

        when (imageType) {
          ImageType.OWNER -> {
            binding.ownerImageView.setImageBitmap(bitmap)
            ownerImage = Base64.encodeToString(byteArray, Base64.DEFAULT)
//          Log.d(TAG, "ownerImage: $ownerImage")
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
        hideProgress()
      }
    } else {
      // an error occurred
      val exception = result.error
      Log.e(TAG, "exception: ${exception?.message}", exception)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityEditPersonalInfoBinding.inflate(layoutInflater)
    setContentView(binding.root)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowHomeEnabled(true)
    supportActionBar?.title = getString(R.string.edit_personal_info)

    viewModel = ViewModelProvider(this)[EditPersonalInfoViewModel::class.java]

    merchantId = intent.getIntExtra(MERCHANT_ID, -1)

    setupViews()
    observeData()

    viewModel.getDivisions()
  }

  override fun onDestroy() {
    super.onDestroy()
    hideProgress()
  }

  private fun setupViews() {
    binding.step2DOBLayout.editText?.showSoftInputOnFocus = false
    binding.step2DOBLayout.editText?.setOnTouchListener { _, event ->
      if (event.action == MotionEvent.ACTION_UP) {
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

    this.let {
      ArrayAdapter(
        it, android.R.layout.simple_list_item_1,
        districtsKeyList
      ).also { adapter ->
        binding.districtTextView.setAdapter(adapter)
      }
    }

    this.let {
      ArrayAdapter(
        it, android.R.layout.simple_list_item_1,
        policeStationsKeyList
      ).also { adapter ->
        binding.policeStationTextView.setAdapter(adapter)
      }
    }

    binding.updateButton.setOnClickListener {
      when (currentStep) {
        1 -> validateStep1Inputs() /*goToNextStep()*/
        2 -> validateStep2Inputs()
        3 -> validateInputs()
      }
    }
  }

  private fun observeData() {
    viewModel.progress.observe(this) {
      if (it) {
        showProgress()
      } else {
        hideProgress()
      }
    }

    viewModel.message.observe(this) {
      shortSnack(binding.updateButton, it)
    }

    viewModel.division.observe(this) { division ->
      divisions.clear()
      for (data in division.divisions!!) {
        divisions[data.division_name.toString()] = data.id!!
      }

      divisionsKeyList.clear()
      divisionsKeyList.addAll(divisions.keys.toList())

      this.let {
        ArrayAdapter(
          it, android.R.layout.simple_list_item_1,
          divisionsKeyList
        ).also { adapter ->
          binding.divisionTextView.setAdapter(adapter)
        }
      }

      viewModel.getPersonalInfo(merchantId)
    }

    viewModel.district.observe(this) { district ->
      districts.clear()
      for (data in district.districts!!) {
        districts[data.district_name.toString()] = data.id!!
      }

      districtsKeyList.clear()
      districtsKeyList.addAll(districts.keys.toList())

      this.let {
        ArrayAdapter(
          it, android.R.layout.simple_list_item_1,
          districtsKeyList
        ).also { adapter ->
          binding.districtTextView.setAdapter(adapter)
        }
      }
    }

    viewModel.policeStation.observe(this) { policeStation ->
      policeStations.clear()
      for (data in policeStation.police_stations!!) {
        policeStations[data.police_station_name.toString()] = data.id!!
      }

      policeStationsKeyList.clear()
      policeStationsKeyList.addAll(policeStations.keys.toList())

      this.let {
        ArrayAdapter(
          it, android.R.layout.simple_list_item_1,
          policeStationsKeyList
        ).also { adapter ->
          binding.policeStationTextView.setAdapter(adapter)
        }
      }
    }

    viewModel.ocr.observe(this) { ocr ->
      Log.d(TAG, "ocr: $ocr")
      if (ocr.nid != null && (ocr.nid.toString().length == 10 || ocr.nid.toString().length == 13
            || ocr.nid.toString().length == 17)
      )
        binding.step2NIDLayout.editText?.setText(ocr.nid.toString())
      if (ocr.dob != null && ocr.dob.length == 10) binding.step2DOBLayout.editText?.setText(ocr.dob)
      goToNextStep()
    }

    viewModel.nid.observe(this) { nid ->
      Log.d(TAG, "nid: $nid")
      when (nid.sp_code) {
        "1" -> {
          if (!nid.nid_response?.nameEn.isNullOrEmpty()) {
            binding.nameLayout.editText?.setText(nid.nid_response?.nameEn)
            binding.nameLayout.editText?.isEnabled = false
          }
          if (!nid.nid_response?.fatherEn.isNullOrEmpty()) {
            binding.fathersNameLayout.editText?.setText(nid.nid_response?.fatherEn)
            binding.fathersNameLayout.editText?.isEnabled = false
          } else {
            binding.fathersNameLayout.hint =
              "${getString(R.string.father_husband_s_name)} (${nid.nid_response?.father})"
          }
          if (!nid.nid_response?.motherEn.isNullOrEmpty()) {
            binding.mothersNameLayout.editText?.setText(nid.nid_response?.motherEn)
            binding.mothersNameLayout.editText?.isEnabled = false
          } else {
            binding.fathersNameLayout.hint =
              "${getString(R.string.mother_s_name)} (${nid.nid_response?.mother})"
          }
          if (!nid.nid_response?.nationalId.isNullOrEmpty()) {
            binding.nidLayout.editText?.setText(nid.nid_response?.nationalId)
            binding.nidLayout.editText?.isEnabled = false
          }
          if (!nid.nid_response?.dob.isNullOrEmpty()) {
            val dob = "${nid.nid_response?.dob?.substring(6)}-${
              nid.nid_response?.dob?.substring(0, 2)
            }-${nid.nid_response?.dob?.substring(3, 5)}"
            binding.dobLayout.editText?.setText(dob)
            binding.dobLayout.editText?.isEnabled = false
          } else {
            /*binding.dobLayout.editText?.showSoftInputOnFocus = false
            binding.dobLayout.editText?.setOnTouchListener { _, event ->
              if (event.action == MotionEvent.ACTION_UP) {
                showDatePickerDialog()
                true
              }
              false
            }*/
          }
          goToNextStep()
        }
        "400" -> {
          shortToast(nid.errors?.get(0).toString())
        }
        else -> {
          shortToast(R.string.something_went_wrong)
        }
      }
    }

    viewModel.ownerInfo.observe(this) { ownerInfo ->
      Log.d(TAG, "ownerInfo: $ownerInfo")
      when (ownerInfo.sp_code) {
        "1" -> {
          shortToast(ownerInfo.message.toString())
        }
        "2" -> {
          shortToast(ownerInfo.message.toString())
        }
        else -> {
          shortToast(ownerInfo.message.toString())
        }
      }
    }

    viewModel.personalInfoDetails.observe(this) {
      val shopOwner = it.shop_owner?.get(0)

      Picasso.get()
        .load("https://stagingapp.engine.shurjopayment.com/shop_owner_img/${shopOwner?.owner_img}")
        .placeholder(R.drawable.ic_baseline_person_24)
        .error(R.drawable.ic_baseline_broken_image_24)
        .into(binding.ownerImageView)

      Picasso.get()
        .load("https://stagingapp.engine.shurjopayment.com/nid_img/frontside/${shopOwner?.nid_front}")
        .placeholder(R.drawable.ic_baseline_credit_card_24)
        .error(R.drawable.ic_baseline_broken_image_24)
        .into(binding.ownerNIDFrontImageView)

      Picasso.get()
        .load("https://stagingapp.engine.shurjopayment.com/nid_img/backside/${shopOwner?.nid_back}")
        .placeholder(R.drawable.ic_baseline_credit_card_24)
        .error(R.drawable.ic_baseline_broken_image_24)
        .into(binding.ownerNIDBackImageView)

      if (!shopOwner?.nid_no.isNullOrEmpty()) {
        binding.step2NIDLayout.editText?.setText(shopOwner?.nid_no)
      }
      if (!shopOwner?.owner_dob.isNullOrEmpty()) {
        binding.step2DOBLayout.editText?.setText(shopOwner?.owner_dob)
      }

      if (!shopOwner?.owner_name.isNullOrEmpty()) {
        binding.nameLayout.editText?.setText(shopOwner?.owner_name)
      }
      if (!shopOwner?.father_name.isNullOrEmpty()) {
        binding.fathersNameLayout.editText?.setText(shopOwner?.father_name)
      }
      if (!shopOwner?.mother_name.isNullOrEmpty()) {
        binding.mothersNameLayout.editText?.setText(shopOwner?.mother_name)
      }
      if (!shopOwner?.contact_no.isNullOrEmpty()) {
        binding.contactLayout.editText?.setText(shopOwner?.contact_no)
      }
      if (!shopOwner?.email_address.isNullOrEmpty()) {
        binding.emailLayout.editText?.setText(shopOwner?.email_address)
      }
      if (!shopOwner?.nid_no.isNullOrEmpty()) {
        binding.nidLayout.editText?.setText(shopOwner?.nid_no)
      }
      if (!shopOwner?.owner_dob.isNullOrEmpty()) {
        binding.dobLayout.editText?.setText(shopOwner?.owner_dob)
      }
      if (!shopOwner?.tin_no.isNullOrEmpty()) {
        binding.tinLayout.editText?.setText(shopOwner?.tin_no)
      }
      if (!shopOwner?.perm_addess.isNullOrEmpty()) {
        binding.addressLayout.editText?.setText(shopOwner?.perm_addess)
      }
      if (!shopOwner?.division_name.isNullOrEmpty()) {
        binding.divisionTextView.setText(shopOwner?.division_name, false)
        //binding.divisionTextView.setSelection(divisionsKeyList.indexOf(shopOwner?.division_name))
      }
      if (!shopOwner?.district_name.isNullOrEmpty()) {
        districts[shopOwner?.district_name.toString()] = shopOwner?.perm_district_id!!
        binding.districtTextView.setText(shopOwner.district_name)
      }
      if (!shopOwner?.police_station_name.isNullOrEmpty()) {
        districts[shopOwner?.police_station_name.toString()] = shopOwner?.perm_police_station_id!!
        binding.policeStationTextView.setText(shopOwner.police_station_name)
      }

      Picasso.get()
        .load("https://stagingapp.engine.shurjopayment.com/owner_signature/${shopOwner?.owner_signature}")
        .placeholder(R.drawable.ic_baseline_gesture_24)
        .error(R.drawable.ic_baseline_broken_image_24)
        .into(binding.ownerSignatureImageView)

      ownerImage = shopOwner?.owner_img_base64
      ownerNIDFront = shopOwner?.nid_front_base64
      ownerNIDBack = shopOwner?.nid_back_base64
      ownerSignature = shopOwner?.owner_signature_base64
    }
  }

  private fun showDatePickerDialog() {
    val c = Calendar.getInstance()
    val y = c.get(Calendar.YEAR)
    val m = c.get(Calendar.MONTH)
    val d = c.get(Calendar.DAY_OF_MONTH)

    val dialog = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
      val moy = if (monthOfYear > 8) (monthOfYear + 1) else "0${monthOfYear + 1}"
      val dom = if (dayOfMonth > 9) dayOfMonth else "0$dayOfMonth"
      val date = "$year-$moy-$dom"
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
        when (imageType) {
          ImageType.OWNER -> setAspectRatio(3, 4)
          ImageType.OWNER_NID_FRONT -> setAspectRatio(8, 5)
          ImageType.OWNER_NID_BACK -> setAspectRatio(8, 5)
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
    //goToNextStep()
  }

  private fun submitStep1Data() {
    val ocr = Ocr(ownerNIDFront.toString(), null, null)
    viewModel.ocrNid(ocr)
  }

  private fun validateStep2Inputs() {
    val nidNo = binding.step2NIDLayout.editText?.text.toString()
    val dob = binding.step2DOBLayout.editText?.text.toString()

    if (nidNo.isEmpty()) {
      binding.step2NIDLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.step2NIDLayout.y.toInt())
      return
    } else {
      binding.step2NIDLayout.error = null
    }
    if (nidNo.length != 10 && nidNo.length != 13 && nidNo.length != 17) {
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

    submitStep2Data(nidNo, dob)
  }

  private fun submitStep2Data(nidNo: String, dob: String) {
    val nid = Nid(ownerImage.toString(), nidNo, dob, null, null, null, null, null)
    viewModel.getNidInfo(nid)
  }

  private fun validateInputs() {
    val name = binding.nameLayout.editText?.text.toString()
    val fathersName = binding.fathersNameLayout.editText?.text.toString()
    val mothersName = binding.mothersNameLayout.editText?.text.toString()
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
    if (fathersName.isEmpty()) {
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
    if (dob.isEmpty()) {
      binding.dobLayout.error = getString(R.string.this_field_is_required)
      binding.scrollView.smoothScrollTo(0, binding.dobLayout.y.toInt())
      return
    } else {
      binding.dobLayout.error = null
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
    if (ownerSignature == null) {
      shortSnack(binding.ownerSignatureLayout, R.string.capture_shop_owner_signature)
      binding.scrollView.smoothScrollTo(0, binding.ownerSignatureLayout.y.toInt())
      return
    }

    val ownerInfo = OwnerInfo(
      merchantId,
      name,
      fathersName,
      mothersName,
      contactNo,
      email,
      nid,
      dob,
      tin,
      address,
      divisions[division]!!,
      districts[district]!!,
      policeStations[policeStation]!!,
      ownerImage!!,
      ownerNIDFront!!,
      ownerNIDBack!!,
      ownerSignature!!,
      null,
      null,
      null,
      null,
      null,
    )
    updatePersonalInfo(ownerInfo)
  }

  private fun updatePersonalInfo(ownerInfo: OwnerInfo) {
    viewModel.updatePersonalInfo(ownerInfo)
  }

  private fun goToNextStep() {
    when (currentStep) {
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
        binding.updateButton.text = getString(R.string.update)
      }
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle item selection
    return when (item.itemId) {
      android.R.id.home -> {
        onBackPressed()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  companion object {
    private const val TAG = "EditPersonalInfoActivit"
  }
}
