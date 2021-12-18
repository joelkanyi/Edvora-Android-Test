package com.kanyideveloper.edvoraandroidtest.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kanyideveloper.edvoraandroidtest.databinding.ItemRowBinding
import com.kanyideveloper.edvoraandroidtest.model.Product
import com.kanyideveloper.edvoraandroidtest.util.formatDate

class ProductsAdapter : ListAdapter<Product, ProductsAdapter.MyViewHolder>(
    PRODUCTS_COMPARATOR
) {

    companion object {
        private val PRODUCTS_COMPARATOR = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(old: Product, aNew: Product): Boolean {
                return old.productName == aNew.productName
            }

            override fun areContentsTheSame(old: Product, aNew: Product): Boolean {
                return old == aNew
            }
        }
    }

    inner class MyViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(product: Product?) {
            binding.textViewProductName.text = product?.productName
            binding.textViewBrandName.text = product?.brandName
            binding.textViewPrice.text = product?.price.toString()
            binding.textViewLocation.text = "${product?.address?.state}, ${product?.address?.city}"

            binding.textViewDateValue.text = formatDate(product?.date!!)
            binding.textViewDescription.text = product.description

            Glide.with(binding.imageViewProduct)
                .load(product.image)
                .into(binding.imageViewProduct)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }
}