package com.myproject.recyclerviewwithviewmodel

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.myproject.recyclerviewwithviewmodel.databinding.StoreMenuItemBinding

class MenuRvAdapter(
    private val menuList: ArrayList<StoreMenuRvModel>,
    private val imgClickListener: (Int) -> Unit,
    private val delButtonClickListener: (Int) -> Unit
) :
    RecyclerView.Adapter<MenuRvAdapter.MenuRvViewHolder>() {

    var isDuplicatedName = false

    inner class MenuRvViewHolder(val binding: StoreMenuItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuRvViewHolder {
        return MenuRvViewHolder(
            StoreMenuItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        menuList.map { Log.d("CurrentMenRvAdapter", "MenRvAdapter List Size ${menuList.size}") }
        return menuList.size
    }

    override fun onBindViewHolder(holder: MenuRvViewHolder, position: Int) {
        val binding = holder.binding

        binding.apply {
            menuImgView.apply {
                setOnClickListener {
                    imgClickListener.invoke(position)
                }
                if (menuList[position].imgBitmap != null) {
                    setImageBitmap(menuList[position].imgBitmap)
                }
            }

            delButton.setOnClickListener {
                delButtonClickListener.invoke(position)
            }

            productNameEdit.addTextChangedListener {
                val input = it.toString()

                if (menuList.count { menu -> menu.productName == input } > 0){
                    productNameLayout.helperText = "이미 존재 하는 상품이에요"
                    isDuplicatedName = true
                } else if (input.isEmpty()) {
                    productNameLayout.helperText = "상품명을 입력해 주세요"
                } else {
                    productNameLayout.helperText = ""
                    menuList[position].productName = input
                    isDuplicatedName = false
                }
            }

            productQuantityEdit.addTextChangedListener {
                val input = it.toString()

                if (input.isEmpty()) {
                    productQuantityLayout.helperText = "수량을 입력해 주세요"
                } else {
                    productQuantityLayout.helperText = ""
                    menuList[position].productQuantity = it.toString().toInt()
                }
            }

            productIntroEdit.addTextChangedListener {
                menuList[position].productIntro = it.toString()
            }
        }
    }

}



