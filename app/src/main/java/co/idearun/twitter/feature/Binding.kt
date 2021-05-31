package co.idearun.twitter.feature

import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import co.idearun.twitter.R
import co.idearun.twitter.common.extension.visible
import co.idearun.twitter.data.model.User
import co.idearun.twitter.data.model.cdp.customer.Customer
import co.idearun.twitter.feature.adapter.FollowingsAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textfield.TextInputLayout
import timber.log.Timber
import java.text.DecimalFormat
import kotlin.math.roundToInt


object Binding {

    @BindingAdapter("app:imageUrlRounded")
    @JvmStatic
    fun loadImageRounded(view: ImageView, url: String?) {
        var link = url
        url?.let {
            if (url.contains("_normal")) {
                link = url.replace("_normal", "")
            }
        }
        Glide.with(view.context).load(link).apply(RequestOptions.circleCropTransform()).into(view)
        view.visible()
    }

    @BindingAdapter("app:setUpProgress")
    @JvmStatic
    fun setUpProgress(view: ProgressBar, score: String?) {
        view.progress = 0
        view.max = 10
        score?.let {
            if (!score.contains("?"))
                view.progress = score.toInt()
        }

        val res = view.resources
        Timber.e("view.progress ${view.progress}")
        view.progressDrawable = when (view.progress) {
            in 0..4 -> {
                view.context.resources.getDrawable(R.drawable.progress_drawable_red)

            }
            in 4..7 -> {

                view.context.resources.getDrawable(R.drawable.progress_drawable_yello)

            }
            in 7..10 -> {

                view.context.resources.getDrawable(R.drawable.progress_drawable)

            }
            else -> {
                view.context.resources.getDrawable(R.drawable.progress_drawable_red)

            }
        }
    }

    @BindingAdapter("app:setPriceTxt")
    @JvmStatic
    fun setpricetxt(view: TextView, price: String?) {
        if (price != null) {
            val roundToInt = price.toDouble().roundToInt()
            val formattedPrice = DecimalFormat("##,###,###").format(roundToInt)


            view.text = "$formattedPrice"

        }
    }

    @BindingAdapter("app:items")
    @JvmStatic
    fun setFieldsItems(recyclerView: RecyclerView, resource: ArrayList<User>?) {
        if (recyclerView.adapter is FollowingsAdapter)
            with(recyclerView.adapter as FollowingsAdapter) {
                resource?.let {
                    collection = it
                }
            }
    }

    @BindingAdapter("app:errorText")
    @JvmStatic
    fun setErrorMessage(view: TextInputLayout, errorMessage: String?) {
        view.error = errorMessage
        view.editText?.addTextChangedListener {
            it?.let {
                view.error = ""
            }
        }
    }

    @BindingAdapter("app:imageUrl")
    @JvmStatic
    fun loadImage(view: ImageView, url: String?) {
        Glide.with(view.context).load(url).into(view)
    }


}