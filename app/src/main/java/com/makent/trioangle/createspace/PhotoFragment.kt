package com.makent.trioangle.createspace

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.makent.trioangle.BuildConfig
import com.makent.trioangle.R
import com.makent.trioangle.backgroundtask.ImageCompressAsyncTask
import com.makent.trioangle.controller.AppController
import com.makent.trioangle.controller.LocalSharedPreferences
import com.makent.trioangle.createspace.interfaces.ImageDeleteListner
import com.makent.trioangle.helper.Constants
import com.makent.trioangle.helper.CustomDialog
import com.makent.trioangle.helper.RunTimePermission
import com.makent.trioangle.interfaces.ApiService
import com.makent.trioangle.interfaces.ImageListener
import com.makent.trioangle.interfaces.ServiceListener
import com.makent.trioangle.model.JsonResponse
import com.makent.trioangle.util.CommonMethods
import com.makent.trioangle.util.Enums.REQ_SPACEIMAGE_DESCRIPTIOM
import com.makent.trioangle.util.Enums.REQ_SPACEIMAGE_UPLOAD
import com.makent.trioangle.util.RequestCallback
import kotlinx.android.synthetic.main.activity_setup.*
import okhttp3.RequestBody
import java.io.*
import javax.inject.Inject


class PhotoFragment : Fragment(), ImageListener ,ServiceListener , ImageDeleteListner {



    lateinit var res: Resources

    lateinit var listner: SetupActivityInterface

    lateinit var mActivity: SetupActivity

    private var imagePath: String? = ""

    var photoList: ArrayList<SpacePhotoModelClass> = ArrayList<SpacePhotoModelClass>()

    @Inject
    lateinit var runTimePermission: RunTimePermission

    @Inject
    lateinit var customDialog: CustomDialog

    @Inject
    lateinit var commonMethods: CommonMethods

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var gson: Gson

    lateinit var localSharedPreferences: LocalSharedPreferences

    lateinit var imageFile : File


    lateinit var photoListModel : PhotoUpdateModel

    lateinit var imagepath: String
    lateinit var imageUri: Uri

    lateinit var mImageCaptureUri: Uri

    private val CAMERA_REQUEST = 1
    private val RESULT_LOAD_IMAGE = 3

    lateinit var setupStepModel : SetupStepModel
    lateinit var tvAddPhoto: TextView
    lateinit var rvPhoto: RecyclerView
    lateinit var btnContinue: Button

    lateinit var spaceImageAdapter: SpaceImageViewAdapter


    lateinit var mContext: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.photo_layout,
                container, false)

        mContext = container!!.context

        localSharedPreferences = LocalSharedPreferences(mContext)
        btnContinue = view.findViewById<Button>(R.id.btn_continue)

        init()

        rvPhoto = view!!.findViewById<RecyclerView>(R.id.rv_photo)
        tvAddPhoto = view!!.findViewById<TextView>(R.id.tv_add_photo)

        spaceImageAdapter = SpaceImageViewAdapter(photoList, mContext,this)
        rvPhoto.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rvPhoto.adapter = spaceImageAdapter

        val spacingInPixels = 13
        rvPhoto.addItemDecoration(SpacesItemDecoration(spacingInPixels))

        btnContinue.setOnClickListener {



            val gson = GsonBuilder().create()
            val jsArray = gson.toJsonTree(setupStepModel.spacePhotos).asJsonArray

            val setupPhotoDetails = jsArray.toString()

            if(commonMethods.isOnline(mActivity)) {
                commonMethods.showProgressDialog(mActivity, customDialog)
                apiService.updateImageDescription("setup",localSharedPreferences.getSharedPreferences(Constants.AccessToken),localSharedPreferences.getSharedPreferences(Constants.mSpaceId),setupPhotoDetails).enqueue(RequestCallback(REQ_SPACEIMAGE_DESCRIPTIOM,this));
            }else{
                commonMethods.snackBar(mActivity.resources.getString(R.string.network_failure),"",false,2,btnContinue,mActivity.resources,mActivity)
            }
        }

        tvAddPhoto.setOnClickListener {
            val permissions = if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2){
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            }else{
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES,Manifest.permission.CAMERA)
            }
            checkAllPermission(permissions)
        }
        initListner()
        return view
    }


    /**
     * Method to check permissions
     *
     * @param permission
     */

    private fun checkAllPermission(permission: Array<String>) {
        val blockedPermission = runTimePermission.checkHasPermission(mActivity, permission)
        if (blockedPermission != null && !blockedPermission!!.isEmpty()) {
            val isBlocked = runTimePermission.isPermissionBlocked(mActivity, blockedPermission!!.toTypedArray())
            if (isBlocked) {
                Handler(Looper.getMainLooper()).post {
                    showEnablePermissionDailog(0, getString(R.string.enable_permissions)) }
            } else {
                ActivityCompat.requestPermissions(mActivity, permission, 300)
            }
        } else {
            cameraGallery()
        }
    }


    // Show camera or Galley
    fun cameraGallery() {
        val alertdialog1 = android.app.AlertDialog.Builder(mActivity)
        alertdialog1.setMessage(getString(R.string.picture_select))
        alertdialog1.setTitle(getString(R.string.take_picture))
        alertdialog1.setPositiveButton(getString(R.string.camera)) { dialog, which ->
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            imageFile = commonMethods.cameraFilePath()
            imageUri = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + ".provider", imageFile)

            try {
                val resolvedIntentActivities = mActivity.getPackageManager().queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY)
                for (resolvedIntentInfo in resolvedIntentActivities) {
                    val packageName = resolvedIntentInfo.activityInfo.packageName
                    mActivity.grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                cameraIntent.putExtra("return-data", true)
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(cameraIntent, CAMERA_REQUEST)
            commonMethods.refreshGallery(mActivity, imageFile)
        }
        alertdialog1.setNegativeButton(getString(R.string.gallery)) { dialog, which ->
           /* imageFile = commonMethods.getDefaultFileName(mActivity,"Profile_")

            val i = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, RESULT_LOAD_IMAGE)
*/

            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,
                "Complete action using"), RESULT_LOAD_IMAGE)
        }


        alertdialog1.show()
    }


    private fun showEnablePermissionDailog(type: Int, message: String) {
        if (!customDialog.isVisible()) {
            customDialog = CustomDialog("Alert", message, getString(R.string.ok), CustomDialog.btnAllowClick {
                if (type == 0)
                    callPermissionSettings()
                else
                    startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 101)
            })
            customDialog.show(mActivity.supportFragmentManager, "")
        }
    }


    /**
     * To check call permissions
     */

    private fun callPermissionSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", mActivity.getPackageName(), null)
        intent.data = uri
        startActivityForResult(intent, 300)
    }

    override fun onImageCompress(filePath: String?, requestBody: RequestBody?) {
        commonMethods.hideProgressDialog()
        if (!TextUtils.isEmpty(filePath) && requestBody != null) {
            commonMethods.showProgressDialog(mActivity, customDialog)
            apiService.uploadSpaceImage(requestBody).enqueue(RequestCallback(REQ_SPACEIMAGE_UPLOAD,this))
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            imageUri = Uri.fromFile(imageFile)
            imagepath = imageUri.path!!
            ImageCompressAsyncTask(mActivity, imagepath, this, localSharedPreferences.getSharedPreferences(Constants.mSpaceId)).execute()
        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK
                && null != data) {
/*
            mImageCaptureUri = data.data!!
            try {
                val bitmap: Bitmap


                bitmap = this.decodeBitmap(mImageCaptureUri, mActivity)!!
                imagepath = ImageWrite(bitmap)
                ImageCompressAsyncTask(mActivity, imagepath, this, localSharedPreferences.getSharedPreferences(Constants.mSpaceId)).execute()
            } catch (e: IOException) {
                e.printStackTrace()
            }*/
            mImageCaptureUri = data.data!!
            try {
                val bitmap: Bitmap = mImageCaptureUri?.let { decodeBitmap(it, requireContext()) }!!
                imagepath = commonMethods.ImageWrite(bitmap, context, "Listing")
                ImageCompressAsyncTask(activity, imagepath, this, localSharedPreferences.getSharedPreferences(Constants.mSpaceId)).execute()

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

    }

    override fun onSuccess(jsonResp: JsonResponse?, data: String?) {
        commonMethods.hideProgressDialog()
        if (jsonResp!!.isSuccess) {
            when (jsonResp.requestCode) {
                REQ_SPACEIMAGE_UPLOAD -> {
                    onSuccessImageUpload(jsonResp)
                }
                REQ_SPACEIMAGE_DESCRIPTIOM -> {
                    onSuccessSpaceDescription(jsonResp)
                }
            }
        }else if (!TextUtils.isEmpty(jsonResp.statusMsg)) {
            commonMethods.snackBar(jsonResp.statusMsg,"",false,2,btnContinue,mActivity.resources,mActivity)
        }

    }

    private fun onSuccessSpaceDescription(jsonResp: JsonResponse) {

        val navController = Navigation.findNavController(requireActivity(), R.id.setup_nav_host_fragment)
        navController.navigate(R.id.action_photoFragment_to_spaceFragment)
        mActivity.progressBarUpdate(0, 20)
    }

    private fun onSuccessImageUpload(jsonResp: JsonResponse?) {

        photoList.clear()
        photoListModel = gson.fromJson(jsonResp?.strResponse, PhotoUpdateModel::class.java)
        photoList.addAll(photoListModel.photos_list)
        buttonEnableDisable()
        spaceImageAdapter.notifyDataSetChanged()

        setupStepModel.spacePhotos = photoList

    }

    override fun onFailure(jsonResp: JsonResponse?, data: String?) {
        commonMethods.hideProgressDialog()
    }

    fun ImageWrite(bitmap: Bitmap): String {


        val extStorageDirectory = Environment.getExternalStorageDirectory().toString()
        var outStream: OutputStream? = null
        val file = File(extStorageDirectory, "slectimage.png")
        try {
            outStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            outStream.flush()
            outStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return "/sdcard/slectimage.png"

    }


    @Throws(FileNotFoundException::class)
    fun decodeBitmap(selectedImage: Uri, context: Context): Bitmap? {
        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        BitmapFactory.decodeStream(context.contentResolver
                .openInputStream(selectedImage), null, o)

        val REQUIRED_SIZE = 500

        var width_tmp = o.outWidth
        var height_tmp = o.outHeight
        var scale = 1
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break
            }
            width_tmp /= 2
            height_tmp /= 2
            scale *= 2
        }

        val o2 = BitmapFactory.Options()
        o2.inSampleSize = scale
        return BitmapFactory.decodeStream(context.contentResolver
                .openInputStream(selectedImage), null, o2)
    }





    private fun initListner() {

        AppController.getAppComponent().inject(this)

        if (listner == null) return
        res = if (listner.getRes() != null) listner.getRes() else requireActivity().resources
        mActivity = if (listner.getInstance() != null) listner.getInstance() else (activity as SetupActivity?)!!
        mActivity.nsv_setup.scrollTo(0, 0)


    }


    override fun onTextChange(pos: Int, highlights: String) {
        println("Photo change : "+highlights)
        setupStepModel.spacePhotos!!.get(pos).highlights = highlights
    }


    inner class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View,
                                    parent: RecyclerView, state: RecyclerView.State) {
            outRect.right = space
            outRect.bottom = space

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0 || parent.getChildLayoutPosition(view) == 1) {
                outRect.top = space
            } else {
                outRect.top = 0
            }
        }
    }


    private fun init() {
        photoList = ArrayList<SpacePhotoModelClass>()

        var bundle : Bundle

        bundle = this!!.requireArguments()

        if(bundle!=null) {
            setupStepModel = bundle.getSerializable("photos") as SetupStepModel
        }

        photoList.addAll(setupStepModel.spacePhotos!!)

        buttonEnableDisable()


    }

    private fun buttonEnableDisable() {
        btnContinue.isEnabled = photoList.size>0
    }


    override fun onItemDelete(pos: Int) {
        commonMethods.showProgressDialog(mActivity, customDialog)
        println("del position : "+pos)
        apiService.deleteSpaceImage(localSharedPreferences.getSharedPreferences(Constants.AccessToken),localSharedPreferences.getSharedPreferences(Constants.mSpaceId),photoList.get(pos).id).enqueue(RequestCallback(REQ_SPACEIMAGE_UPLOAD,this));
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is SetupActivity) {
            listner = context
        } else {
            throw ClassCastException(
                    context.toString())
        }
    }

}