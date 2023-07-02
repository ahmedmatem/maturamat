package com.ahmedmatem.android.matura.ui.camera

import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navGraphViewModels
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.databinding.FragmentBaseCameraBinding
import com.ahmedmatem.android.matura.ui.test2.NewTest2ViewModel
import com.ahmedmatem.android.matura.utils.clearFullScreen
import com.ahmedmatem.android.matura.utils.saveBitmapInGallery
import com.ahmedmatem.android.matura.utils.setFullScreen
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BaseCameraFragment : BaseFragment() {

    override val viewModel: BaseCameraViewModel by viewModels()
    private val test2ViewModel: NewTest2ViewModel by navGraphViewModels(R.id.nav_graph_test_2)

    private var _binding: FragmentBaseCameraBinding? = null
    private val binding: FragmentBaseCameraBinding get() = _binding!!
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    private var bitmap: Bitmap? = null
    private lateinit var captureSound: MediaPlayer

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private val permissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->
            if(isGranted){
                startCamera()
            } else {
                viewModel.showToast.value = "Camera permission not granted."
                // TODO: Explain why permission is important to be granted
            }
        }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBaseCameraBinding.inflate(inflater, container, false)

        captureSound = MediaPlayer.create(requireContext(), R.raw.camera_shutter_click)

        if(allPermissionsGranted()){
            startCamera()
        } else {
            permissionsLauncher.launch(Manifest.permission.CAMERA)
        }

        binding.apply {
            captureButton.setOnClickListener {
                takePhoto()
            }
            resumeCameraButton.setOnClickListener {
                startCamera()
                updateUI(previewPaused = false)
                unlockScreen()
            }
            photoOkButton.setOnClickListener {
                bitmap?.let { bmp ->
                    saveBitmapInGallery(bmp, FILENAME_FORMAT, RELATIVE_PATH)?.also{ uri ->
                        test2ViewModel.savePhotoInDb(uri)
                    }
                    viewModel.navigationCommand.value = NavigationCommand.Back
                }
            }
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        // Listen to capture effect camera UI update
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isCameraShutterVisible.collect() { isVisible ->
                    if(isVisible)
                        binding.cameraShutter.visibility = View.VISIBLE
                    else
                        binding.cameraShutter.visibility = View.GONE
                }
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFullScreen()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        clearFullScreen()
        unlockScreen()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Set up image capture listener, which is triggered after photo has been taken
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    /** Get bitmap from camera preview holder */
                    bitmap = binding.viewFinder.bitmap

                    /** Steps on capture success */
                    viewModel.runCaptureEffect(CAMERA_SHUTTER_EFFECT_DURATION)
                    stopCamera()
                    captureSound.start()
                    updateUI(previewPaused = true)
                    lockScreen()
                }

                override fun onError(exception: ImageCaptureException) {
                    // TODO: Implement ImageCaptureException
                    // ...
                }
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            // Used to bind lifecycle of camera to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Camera Preview Use Case
            val preview = Preview.Builder().build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            // Camera Use Case for taking a picture
            imageCapture = ImageCapture.Builder().build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun stopCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
        cameraProvider.unbindAll()
    }

    private fun updateUI(previewPaused: Boolean) {
        if(previewPaused) {
            // Hide Capture button
            binding.captureButton.visibility = View.GONE
            // Show Ok and Resume Camera buttons
            binding.photoOkButton.visibility = View.VISIBLE
            binding.resumeCameraButton.visibility = View.VISIBLE
        } else {
            // Show Capture button
            binding.captureButton.visibility = View.VISIBLE
            // Hide Ok and Resume Camera buttons
            binding.photoOkButton.visibility = View.GONE
            binding.resumeCameraButton.visibility = View.GONE
        }
    }

    private fun lockScreen() {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
    }

    private fun unlockScreen() {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission( requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        const val TAG = "BaseCameraFragment"
        const val PHOTO_URI = "photo_uri"
        private const val RELATIVE_PATH = "Pictures/MaturaMat-Images"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val CAMERA_SHUTTER_EFFECT_DURATION : Long = 100L
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                /*Manifest.permission.RECORD_AUDIO*/
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}