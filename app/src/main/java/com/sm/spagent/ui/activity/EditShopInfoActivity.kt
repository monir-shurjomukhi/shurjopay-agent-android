package com.sm.spagent.ui.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import com.canhub.cropper.CropImageContract
import com.sm.spagent.R
import com.sm.spagent.databinding.ActivityEditShopInfoBinding
import com.sm.spagent.model.ImageType
import com.sm.spagent.ui.viewmodel.EditPersonalInfoViewModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File

class EditShopInfoActivity : BaseActivity() {

  private lateinit var binding: ActivityEditShopInfoBinding
  private lateinit var viewModel: EditPersonalInfoViewModel

  private var shopId = -1
  private var imageType = ImageType.TRADE_LICENSE
  private var tradeLicenseImage: String? = null
  private var shopFrontImage: String? = null

  private val shopSizes = listOf("Big", "Medium", "Small")
  private val businessTypes = mutableMapOf<String, Int>()
  private val divisions = mutableMapOf<String, Int>()
  private val districts = mutableMapOf<String, Int>()
  private val policeStations = mutableMapOf<String, Int>()

  private var shopLocation: Location? = null
  private val locationRequestCode = 1000

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
        val compressedImageFile = Compressor.compress(this@EditShopInfoActivity, file) { quality(50) }
        Log.d(TAG, "compressedImageFile size (KB): ${compressedImageFile.length() / 1024}")
        val bitmap = BitmapFactory.decodeFile(compressedImageFile.absolutePath)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
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
    binding = ActivityEditShopInfoBinding.inflate(layoutInflater)
    setContentView(binding.root)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowHomeEnabled(true)
    supportActionBar?.title = getString(R.string.edit_shop_info)
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
    private const val TAG = "EditShopInfoActivity"
  }
}
