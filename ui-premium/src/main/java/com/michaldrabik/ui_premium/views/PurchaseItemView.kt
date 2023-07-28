package com.michaldrabik.ui_premium.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.michaldrabik.ui_base.utilities.extensions.colorStateListFromAttr
import com.michaldrabik.ui_premium.R
import com.michaldrabik.ui_premium.databinding.ViewPurchaseItemBinding

@SuppressLint("SetTextI18n")
class PurchaseItemView : MaterialCardView {
  private val binding = ViewPurchaseItemBinding.inflate(LayoutInflater.from(context), this)

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

  init {
    layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    strokeWidth = 0
    setCardBackgroundColor(context.colorStateListFromAttr(R.attr.colorAccent))
  }

  fun bindInApp(title: String, price: String) {
    with(binding) {
      viewPurchaseItemTitle.text = title
      viewPurchaseItemDescription.text = "Pay once, unlock forever!"
      viewPurchaseItemDescriptionDetails.text =
        "You will unlock all bonus features with a single payment and enjoy them forever."
      viewPurchaseItemPrice.text = price

      val colorBlack = ContextCompat.getColor(context, R.color.colorBlack)
      val colorWhite = ContextCompat.getColor(context, R.color.colorWhite)

      viewPurchaseItemTitle.setTextColor(colorBlack)
      viewPurchaseItemDescription.setTextColor(colorBlack)
      viewPurchaseItemDescriptionDetails.setTextColor(colorBlack)
      viewPurchaseItemPrice.setTextColor(colorBlack)
      viewPurchaseItemSeparator.setBackgroundColor(colorBlack)
      setCardBackgroundColor(colorWhite)
    }
  }
}
