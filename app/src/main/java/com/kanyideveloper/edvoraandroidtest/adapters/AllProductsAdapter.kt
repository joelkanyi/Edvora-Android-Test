package com.kanyideveloper.edvoraandroidtest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kanyideveloper.edvoraandroidtest.databinding.AllProductsRecyclerRowBinding
import com.kanyideveloper.edvoraandroidtest.model.CustomProduct

class AllProductsAdapter : ListAdapter<CustomProduct, AllProductsAdapter.MyViewHolder>(
    PRODUCTS_COMPARATOR) {

    companion object {
        private val PRODUCTS_COMPARATOR = object : DiffUtil.ItemCallback<CustomProduct>() {
            override fun areItemsTheSame(old: CustomProduct, aNew: CustomProduct): Boolean {
                return old.productName == aNew.productName
            }

            override fun areContentsTheSame(old: CustomProduct, aNew: CustomProduct): Boolean {
                return old == aNew
            }
        }
    }

    inner class MyViewHolder(private val binding: AllProductsRecyclerRowBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(product: CustomProduct?) {
            binding.textViewProductCategory.text = product?.productName

            // Work on the individual items adapter
            val productsAdapter = ProductsAdapter()
            productsAdapter.submitList(product?.products)

            binding.singleProductsRecycler.run {
                this.adapter = productsAdapter
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(AllProductsRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }
}