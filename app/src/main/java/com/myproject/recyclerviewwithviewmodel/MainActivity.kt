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
import androidx.recyclerview.widget.LinearLayoutManager
import com.myproject.recyclerviewwithviewmodel.Utils.REQUEST_IMAGE_PERMISSIONS
import com.myproject.recyclerviewwithviewmodel.Utils.accessGallery
import com.myproject.recyclerviewwithviewmodel.databinding.ActivityMainBinding
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var launcherForPermission: ActivityResultLauncher<Array<String>>
    private lateinit var launcherForActivity: ActivityResultLauncher<Intent>
    private val menuList = arrayListOf<StoreMenuRvModel>()
    private var selectedItemPosition: Int? = null
    private lateinit var menuAdapter: MenuRvAdapter


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
                addMenuItem()
            }

            upBtn.setOnClickListener{
                rv.scrollToPosition(0)
            }

            toolbar.apply {
                inflateMenu(R.menu.add_menu)

                setOnMenuItemClickListener {
                    when(it.itemId){

                        R.id.add_menu_item -> {

                            menuList.forEachIndexed { index, menu ->
                                if (menu.productName.isEmpty() || menu.productQuantity == 0){
                                    // 입력되지 않은 값이 있으면 해당 항목에 포커스
                                    binding.rv.scrollToPosition(index)

                                    // 특정 위치에 대한 ViewHolder를 검색
                                    binding.rv.findViewHolderForAdapterPosition(index)?.itemView?.requestFocus()
                                    return@forEachIndexed
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
        val newItem = StoreMenuRvModel(null, "", 0, "")

        // 데이터 리스트에 항목 추가
        menuList.add(newItem)

        menuAdapter = MenuRvAdapter(
            menuList,
            { position ->
                if (hasImagePermission()){ accessGallery(launcherForActivity) }
                else launcherForPermission.launch(REQUEST_IMAGE_PERMISSIONS)
                selectedItemPosition = position
            }
        ) { position ->
            Log.d("CurrentMenRvAdapter", "delete Pos : $position")
            Log.d("CurrentMenRvAdapter", "delete target : ${menuList[position].productName}")
            menuList.removeAt(position)

            menuAdapter.notifyItemRemoved(position)
            menuAdapter.notifyItemRangeChanged(position, menuList.size)
            Log.d("CurrentMenRvAdapter", "activity ${menuList.size}")
        }

        with(binding.rv){
            adapter = menuAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun addMenuItem() {
        // 새로운 항목 생성
        val newItem = StoreMenuRvModel(null, "", 0, "")

        // 데이터 리스트에 항목 추가
        menuList.add(newItem)

        // 새로운 항목이 추가되었음을 어댑터에 알림
        menuAdapter.notifyItemInserted(menuList.size - 1)
        // 스크롤을 추가된 항목으로 이동시킴
        binding.rv.scrollToPosition(menuList.size - 1)
    }

    private fun initActivityProcess(){
        val contracts = ActivityResultContracts.RequestMultiplePermissions()
        launcherForPermission = registerForActivityResult(contracts){ permissions ->
            if (permissions.all { it.value }) {
                accessGallery(launcherForActivity)
            } else {
                // 하나 이상의 권한이 거부된 경우 처리할 작업
                permissions.entries.forEach { (permission, isGranted) ->

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

                    //val context: Context = context ?: return@registerForActivityResult
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

                if (selectedItemPosition != null){
                    menuList[selectedItemPosition!!].imgBitmap = bitmap
                    menuAdapter.notifyItemChanged(selectedItemPosition!!)
                }

            }
        }
    }
}