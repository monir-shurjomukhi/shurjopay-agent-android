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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.sm.spagent.databinding.FragmentPersonalInfoBinding
import com.sm.spagent.model.ImageType
import com.sm.spagent.ui.activity.NewMerchantActivity
import com.sm.spagent.ui.viewmodel.DashboardViewModel
import java.io.ByteArrayOutputStream
import java.util.*


class PersonalInfoFragment : Fragment() {

  private lateinit var dashboardViewModel: DashboardViewModel
  private var _binding: FragmentPersonalInfoBinding? = null

  private var imageType = ImageType.OWNER
  private var ownerImage: String? = null
  private var ownerNIDFront: String? = null
  private var ownerNIDBack: String? = null
  private var ownerSignature: String? = null

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

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
    dashboardViewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

    _binding = FragmentPersonalInfoBinding.inflate(inflater, container, false)
    val root: View = binding.root

    binding.divisionLayout.setOnKeyListener(null)
    binding.districtLayout.setOnKeyListener(null)
    binding.policeStationLayout.setOnKeyListener(null)

    binding.dobLayout.editText?.showSoftInputOnFocus = false
    binding.dobLayout.editText?.setOnTouchListener { _, event ->
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

    binding.saveNextButton.setOnClickListener {
      (activity as NewMerchantActivity).goToNextStep()
    }

    return root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  private fun showDatePickerDialog() {
    val c = Calendar.getInstance()
    val y = c.get(Calendar.YEAR)
    val m = c.get(Calendar.MONTH)
    val d = c.get(Calendar.DAY_OF_MONTH)

    val dialog = DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
      val date = "$year-${monthOfYear+1}-$dayOfMonth"
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

  companion object {
    private const val TAG = "PersonalInfoFragment"
  }
}
