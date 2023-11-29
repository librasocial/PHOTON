package com.ssf.homevisit.views

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import androidx.core.view.isVisible
import com.ssf.homevisit.R
import com.ssf.homevisit.controller.UIController
import com.ssf.homevisit.databinding.ImageUploadBinding
import com.ssf.homevisit.models.PrefetchURLResponse
import com.ssf.homevisit.models.PutImageResponse
import com.ssf.homevisit.requestmanager.ApiClient
import com.ssf.homevisit.requestmanager.ApiInterface
import com.ssf.homevisit.requestmanager.AppDefines
import com.ssf.homevisit.utils.Util
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream


class ImageUpload : FrameLayout {

    private lateinit var binding: ImageUploadBinding
    private var isUploading = false
    private var uploadedFileName = ""
    var onClickImage: () -> Unit = {}
    var onCloseImage: () -> Unit = {}
    var onUploadedImage: (fileName: String, uri: Uri) -> Unit = { _,_->}
    var onUploadFailed: () -> Unit = {}

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        binding = ImageUploadBinding.inflate(LayoutInflater.from(context), this, true)
        binding.root.setOnClickListener {
            if (!isUploading) {
                onClickImage()
            }
        }
        binding.imgCrossAttOne.setOnClickListener {
            if (!isUploading && uploadedFileName.isNotEmpty()) {
                setImageState(0, null)
                onCloseImage()
            }
        }
    }

     fun setImageState(state: Int, uri: Uri?, uploadedName: String = "") {
        when (state) {
            0 -> {
                isUploading = false
                uploadedFileName = uploadedName
                binding.progress.isVisible = false
                binding.imgAttOne.scaleType = ImageView.ScaleType.CENTER
                binding.imgAttOne.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_add_image
                    )
                )
                binding.imgCrossAttOne.isVisible = false
            }
            1 -> {
                isUploading = true
                uploadedFileName = uploadedName
                binding.progress.isVisible = true
                binding.imgAttOne.scaleType = ImageView.ScaleType.FIT_CENTER
                binding.imgAttOne.setImageURI(uri!!)
                binding.imgCrossAttOne.isVisible = false
            }
            2 -> {
                isUploading = false
                binding.progress.isVisible = false
                uploadedFileName = uploadedName
                binding.imgAttOne.scaleType = ImageView.ScaleType.FIT_CENTER
                binding.imgAttOne.setImageURI(uri!!)
                binding.imgCrossAttOne.isVisible = true
            }
        }
    }

    public fun uploadImage(uri: Uri, fileName: String, bitmap: Bitmap) {
        setImageState(1, uri)
        val apiInterface = ApiClient.getClient(AppDefines.BASE_URL).create(
            ApiInterface::class.java
        )
        apiInterface.getOrganizationPresignedURL(fileName, Util.getHeader())
            .enqueue(object : Callback<PrefetchURLResponse> {
                override fun onResponse(
                    call: Call<PrefetchURLResponse>,
                    response: Response<PrefetchURLResponse>
                ) {
                    val apiInterface1 = ApiClient.getClient("").create(
                        ApiInterface::class.java
                    )
                    Log.d("buckeykey", fileName)
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    val imageInByte = stream.toByteArray()
                    val body = imageInByte
                        .toRequestBody(
                            "image/*".toMediaTypeOrNull(),
                            0, imageInByte.size
                        )
                    if (response.body()!!.preSignedUrl == null) {
                        setImageState(0, null)
                        onUploadFailed()
                        UIController.getInstance()
                            .displayToastMessage(context, "Cannot get prefetchURl")
                        return
                    }
                    apiInterface1.putImageToS3(response.body()!!.preSignedUrl, "image/*", body)
                        .enqueue(object : Callback<PutImageResponse?> {
                            override fun onResponse(
                                call: Call<PutImageResponse?>,
                                response: Response<PutImageResponse?>
                            ) {
                                setImageState(2, uri, fileName)
                                onUploadedImage(fileName, uri)
                                UIController.getInstance()
                                    .displayToastMessage(context, "Image Uploaded")
                            }

                            override fun onFailure(call: Call<PutImageResponse?>, t: Throwable) {
                                setImageState(0, null)
//                                setImageState(2, uri, fileName)
                                onUploadFailed()
                            }
                        })
                }

                override fun onFailure(call: Call<PrefetchURLResponse>, t: Throwable) {
                    t.printStackTrace()
                    setImageState(0, null)
                    onUploadFailed()
                    UIController.getInstance()
                        .displayToastMessage(context, "Cannot get prefetchURl")
                }
            })
    }

}