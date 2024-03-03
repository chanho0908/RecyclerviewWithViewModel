package com.myproject.recyclerviewwithviewmodel

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.myproject.recyclerviewwithviewmodel.Utils.REQUEST_IMAGE_PERMISSIONS
import com.myproject.recyclerviewwithviewmodel.Utils.accessGallery
import com.myproject.recyclerviewwithviewmodel.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: StoreMenuManagementViewModel by viewModels()
    private lateinit var launcherForPermission: ActivityResultLauncher<Array<String>>
    private lateinit var launcherForActivity: ActivityResultLauncher<Intent>
    private lateinit var menuAdapter: MenuRvAdapter
    private var selectedItemPosition: Int? = null
    private var updateSize: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initActivityProcess()
        initRv()
    }

    private fun initView(){
        binding.apply {

            addBtn.setOnClickListener {
                viewModel.addMenuItem()
                updateSize?.let { updateSize -> menuAdapter.notifyItemInserted(updateSize) }

            }

            upBtn.setOnClickListener{
                rv.scrollToPosition(0)
            }

            toolbar.apply {
                inflateMenu(R.menu.add_menu)

                setOnMenuItemClickListener {
                    when(it.itemId){

                        R.id.add_menu_item -> {

                            lifecycleScope.launch {
                                repeatOnLifecycle(Lifecycle.State.STARTED){
                                    viewModel.menuList.collect{ menuData ->
                                        menuData.forEachIndexed { index, menu ->
                                            if (menu.productName == null || menu.productQuantity == 0){
                                                // 입력되지 않은 값이 있으면 해당 항목에 포커스
                                                binding.rv.scrollToPosition(index)

                                                // 특정 위치에 대한 ViewHolder를 검색
                                                binding.rv.findViewHolderForAdapterPosition(index)?.itemView?.requestFocus()
                                                return@forEachIndexed
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    true
                }
            }
        }
    }

    private fun initRv(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.menuList.collect { menuData ->
                    updateSize = menuData.size

                    menuAdapter = MenuRvAdapter(
                        menuData,
                        { position ->
                            if (hasImagePermission()) {
                                accessGallery(launcherForActivity)
                            } else{
                                launcherForPermission.launch(REQUEST_IMAGE_PERMISSIONS)
                            }
                            selectedItemPosition = position
                        }
                    ) { position ->
                        Log.d("CurrentMenRvAdapter", "delete Pos : $position")
                        Log.d(
                            "CurrentMenRvAdapter",
                            "delete target : ${menuData[position].productName}"
                        )
                        viewModel.deleteMenuItem(position)
                    }

                    with(binding.rv) {
                        adapter = menuAdapter
                        layoutManager = LinearLayoutManager(this@MainActivity)
                    }
                }
            }
        }
    }

    private fun initActivityProcess(){
        val contracts = ActivityResultContracts.RequestMultiplePermissions()
        launcherForPermission = registerForActivityResult(contracts){ permissions ->
            if (permissions.all { it.value }) {
                accessGallery(launcherForActivity)
            } else {
                // 하나 이상의 권한이 거부된 경우 처리할 작업
                permissions.entries.forEach { (_, isGranted) ->

                    when {
                        isGranted -> {
                            // 권한이 승인된 경우 처리할 작업
                            accessGallery(launcherForActivity)
                        }
                        !isGranted -> {
                            // 권한이 거부된 경우 처리할 작업
                            // 사용자에게 왜 권한이 필요한지 설명하는 다이얼로그 또는 메시지를 표시
                            showPermissionSnackBar(binding.root)
                        }
                        else -> {
                            // 사용자가 "다시 묻지 않음"을 선택한 경우 처리할 작업
                            // 사용자에게 왜 권한이 필요한지 설명하는 다이얼로그 또는 메시지를 표시
                            showPermissionSnackBar(binding.root)
                        }
                    }
                }
            }
        }

        val contracts2 = ActivityResultContracts.StartActivityForResult()
        launcherForActivity = registerForActivityResult(contracts2) { result ->
            val callback = result.data
            if (callback != null){
                val selectedImageUri = callback.data

                val imageUri = Uri.parse(selectedImageUri.toString())
                val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
                val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)

//                if (selectedItemPosition != null){
//                    menuList[selectedItemPosition!!].imgBitmap = bitmap
//                    menuAdapter.notifyItemChanged(selectedItemPosition!!)
//                }

            }
        }
    }
}