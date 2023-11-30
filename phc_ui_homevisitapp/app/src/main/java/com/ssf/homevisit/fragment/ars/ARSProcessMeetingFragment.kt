package com.ssf.homevisit.fragment.ars

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.squareup.picasso.Picasso
import com.ssf.homevisit.BuildConfig
import com.ssf.homevisit.R
import com.ssf.homevisit.controller.AppController
import com.ssf.homevisit.controller.UIController
import com.ssf.homevisit.databinding.FragmentArsMeetingBinding
import com.ssf.homevisit.fragment.vhnd.*
import com.ssf.homevisit.fragment.vhsnc.BottomBarMode
import com.ssf.homevisit.fragment.vhsnc.InvitationSentDialog
import com.ssf.homevisit.fragment.vhsnc.PickImageDialog
import com.ssf.homevisit.utils.CameraUtility
import com.ssf.homevisit.utils.resizeBitmap
import com.ssf.homevisit.viewmodel.ARSViewModel
import com.ssf.homevisit.views.ImageUpload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class ARSProcessMeetingFragment : Fragment() {
    private lateinit var binding: FragmentArsMeetingBinding
    private val viewModel: ARSViewModel by activityViewModels()
    private var file: File? = null
    private var imageUri: Uri? = null
    private var isDone = false

    private val inviteSentDialog by lazy {
        val str =
            "ARS meeting documents have been saved and sent successfully!"
        InvitationSentDialog(requireContext(), R.drawable.ic_phone, str) {
            viewModel.refreshMeetings()
            viewModel.setLoading(false)
            requireActivity().onBackPressed()
        }
    }

    private lateinit var lastClickView: ImageUpload

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                CoroutineScope(Dispatchers.IO).launch {
                    val bitmap = Picasso.get().load(it).get()
                    val compressed = resizeBitmap(bitmap, 200, 200)
                    launch(Dispatchers.Main) {
                        lastClickView.uploadImage(it, generateBucketKey() + ".jpeg", compressed)
                    }
                }
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initializerComponents(inflater, container)
        return binding.root
    }

    private fun initializerComponents(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        binding = FragmentArsMeetingBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNewMeetingUI()
        observeData()
    }

    private fun observeData() {
        viewModel.getMeeting(requireArguments().getString("meetingId"))
            .observe(viewLifecycleOwner) {
                print(it?.id ?: "")
            }
        setupBottomBar(false)
    }

    private fun initNewMeetingUI() {
        setImageListener()
        binding.txtMOM.doAfterTextChanged {
            setupBottomBar(viewModel.uploadImageMap.isNotEmpty() && !viewModel.minutesOfMeeting.value.isNullOrEmpty())
        }
    }

    private fun setImageListener() {
        binding.imgAttOne.onClickImage = setCaptureImageListener(binding.imgAttOne)
        binding.imgAttTwo.onClickImage = setCaptureImageListener(binding.imgAttTwo)
        binding.imgMMAttOne.onClickImage = setCaptureImageListener(binding.imgMMAttOne)
        binding.imgMMAttTwo.onClickImage = setCaptureImageListener(binding.imgMMAttTwo)
        binding.imgMpAttOne.onClickImage = setCaptureImageListener(binding.imgMpAttOne)
        binding.imgMpAttTwo.onClickImage = setCaptureImageListener(binding.imgMpAttTwo)
        binding.imgMpAttThree.onClickImage = setCaptureImageListener(binding.imgMpAttThree)

        binding.imgAttOne.onUploadedImage = setUploadSuccessListener(binding.imgAttOne)
        binding.imgAttTwo.onUploadedImage = setUploadSuccessListener(binding.imgAttTwo)
        binding.imgMMAttOne.onUploadedImage = setUploadSuccessListener(binding.imgMMAttOne)
        binding.imgMMAttTwo.onUploadedImage = setUploadSuccessListener(binding.imgMMAttTwo)
        binding.imgMpAttOne.onUploadedImage = setUploadSuccessListener(binding.imgMpAttOne)
        binding.imgMpAttTwo.onUploadedImage = setUploadSuccessListener(binding.imgMpAttTwo)
        binding.imgMpAttThree.onUploadedImage = setUploadSuccessListener(binding.imgMpAttThree)

        binding.imgAttOne.onCloseImage = setCloseImageListener(binding.imgAttOne)
        binding.imgAttTwo.onCloseImage = setCloseImageListener(binding.imgAttTwo)
        binding.imgMMAttOne.onCloseImage = setCloseImageListener(binding.imgMMAttOne)
        binding.imgMMAttTwo.onCloseImage = setCloseImageListener(binding.imgMMAttTwo)
        binding.imgMpAttOne.onCloseImage = setCloseImageListener(binding.imgMpAttOne)
        binding.imgMpAttTwo.onCloseImage = setCloseImageListener(binding.imgMpAttTwo)
        binding.imgMpAttThree.onCloseImage = setCloseImageListener(binding.imgMpAttThree)
    }

    private fun setCloseImageListener(view: ImageUpload): () -> Unit {
        return {
            viewModel.uploadImageMap.remove(view.tag as? String ?: "")
            setupBottomBar(viewModel.uploadImageMap.isNotEmpty() && !viewModel.minutesOfMeeting.value.isNullOrEmpty())
        }
    }

    private fun setUploadSuccessListener(view: ImageUpload): (String, Uri) -> Unit {
        return { name, uri ->
            viewModel.uploadImageMap[view.tag as? String ?: ""] = Pair(name, uri)
            setupBottomBar(!viewModel.minutesOfMeeting.value.isNullOrEmpty())
        }
    }

    private fun setCaptureImageListener(view: ImageUpload): () -> Unit = {
        PickImageDialog(requireContext()) {
            if (it) {
                lastClickView = view
                selectImageFromGallery()
            } else {
                openCamera(view.id)
            }
        }.show()
    }

    @Deprecated("")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            onCameraResult(requestCode)
        }
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private fun generateBucketKey(): String {
        return UUID.randomUUID().toString()
    }

    private fun openCamera(requestCode: Int) {
        if (ActivityCompat.checkSelfPermission(
                AppController.getInstance().applicationContext, Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                AppController.getInstance().currentActivity,
                arrayOf(Manifest.permission.CAMERA),
                2
            )
            return
        }
        if (ActivityCompat.checkSelfPermission(
                AppController.getInstance().applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                AppController.getInstance().currentActivity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                3
            )
            return
        }
        if (ActivityCompat.checkSelfPermission(
                AppController.getInstance().applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                AppController.getInstance().currentActivity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                4
            )
            return
        }
        file = CameraUtility.createImageFile(requireContext())
        if (file == null) {
            UIController.getInstance().displayToastMessage(
                context,
                "Cant write on the external storage, please check permission"
            )
            return
        }
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageUri = FileProvider.getUriForFile(
            requireContext().applicationContext,
            BuildConfig.APPLICATION_ID + ".provider",
            file!!
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, requestCode)
    }

    private fun onCameraResult(requestCode: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val bitmap = Picasso.get().load(imageUri).get()
            val compressed = resizeBitmap(bitmap, 200, 200)
            launch(Dispatchers.Main) {
                binding.root.findViewById<ImageUpload>(requestCode)
                    .uploadImage(imageUri!!, file!!.name, compressed)
            }
        }
    }

    private fun setupBottomBar(enabled: Boolean = false) {
        binding.bottomBar.apply {
            mode = BottomBarMode.SAVE_CONTINUE
            saveButton.isEnabled = enabled
            saveButton.setOnClickListener {
                viewModel.updateOutcome(requireArguments().getString("meetingId")!!)
                    .observe(viewLifecycleOwner) { meet2 ->
                        if (isDone) {
                            return@observe
                        }
                        meet2?.let {
                            isDone = true
                            if (!inviteSentDialog.isShowing) {
                                inviteSentDialog.show()
                            }
                        } ?: run {
                            UIController.getInstance()
                                .displayToastMessage(
                                    requireContext(),
                                    "Something went wrong"
                                )
                        }
                    }
            }
            txtCancel.setOnClickListener { requireActivity().onBackPressed() }
        }
    }
}