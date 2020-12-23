package com.tapmaxdev.translation.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tapmaxdev.translation.R
import com.tapmaxdev.translation.model.LanguageModel

class LanguageAdapter2(private val itemClickCallback: (LanguageModel) -> Unit, private val alertDialog: AlertDialog)
    : ListAdapter<LanguageModel, LanguageAdapter2.LanguageViewHolder>(LanguageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LanguageViewHolder(inflater, parent, itemClickCallback, alertDialog)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class LanguageViewHolder(inflater: LayoutInflater, parent: ViewGroup,
                             private val itemClickCallback: (LanguageModel) -> Unit,
                             private val alertDialog: AlertDialog):
            RecyclerView.ViewHolder(inflater.inflate(R.layout.item_language, parent, false)) {

        private var languageFlag: ImageView? = null
        private var languageName: TextView? = null
        private var constraintLayout: ConstraintLayout? = null

        init {
            languageFlag = itemView.findViewById(R.id.languageImageView)
            languageName = itemView.findViewById(R.id.languageTextView)
            constraintLayout = itemView.findViewById(R.id.constraintLayout)
        }

        fun bind (item: LanguageModel) {
            languageFlag?.setImageResource(item.flag)
            languageName?.text = item.language
            constraintLayout?.setOnClickListener {
                itemClickCallback(item)
                alertDialog.dismiss()
            }
        }
    }

    class LanguageDiffCallback : DiffUtil.ItemCallback<LanguageModel>() {
        override fun areItemsTheSame(oldItem: LanguageModel, newItem: LanguageModel): Boolean {
            return oldItem.language == newItem.language
        }

        override fun areContentsTheSame(oldItem: LanguageModel, newItem: LanguageModel): Boolean {
            return oldItem == newItem
        }
    }
}