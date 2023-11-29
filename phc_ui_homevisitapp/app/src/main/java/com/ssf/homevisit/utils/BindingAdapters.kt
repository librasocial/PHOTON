package com.ssf.homevisit.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.ssf.homevisit.R
import com.ssf.homevisit.models.GetImageUrlResponse
import com.ssf.homevisit.requestmanager.ApiClient
import com.ssf.homevisit.requestmanager.ApiInterface
import com.ssf.homevisit.requestmanager.AppDefines
import com.ssf.homevisit.rmncha.childCare.immunization.VaccineItemData
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("selectText", "trimChar", requireAll = false)
    fun TextView.setSelectText(str: String?, trimChar: String?) {
        if (str.isNullOrEmpty()) {
            text = context.getString(R.string.select)
            setTextColor(context.getColor(R.color.selected_grey))
        } else {
            text = if (trimChar.isNullOrEmpty()) str else str.substringBefore(trimChar)
            setTextColor(context.getColor(R.color.black))
        }
    }

    @JvmStatic
    @BindingAdapter("arrayText")
    fun TextView.setArrayText(array: List<String>?) {
        if (array.isNullOrEmpty()) {
            text = context.getString(R.string.select)
            setTextColor(context.getColor(R.color.selected_grey))
        } else {
            text = array.joinToString(", ") { it }
            setTextColor(context.getColor(R.color.black))
        }
    }

    @JvmStatic
    @BindingAdapter("s3ImageUrlFromArray")
    fun ImageView.loadImageUrl(urls: List<String>?) {
        if (urls.isNullOrEmpty()) {
            this.scaleType = ImageView.ScaleType.CENTER_CROP
            this.setImageResource(R.drawable.ic_image_place_holder)
        } else {
            this.loadImageUrl(urls.first())
        }
    }

    @JvmStatic
    @BindingAdapter("s3OrgImageUrlFromArray")
    fun ImageView.loadOrgImageUrl(urls: List<String>?) {
        if (urls.isNullOrEmpty()) {
            this.scaleType = ImageView.ScaleType.CENTER_CROP
            this.setImageResource(R.drawable.ic_image_place_holder)
        } else {
            this.loadOrgImageUrl(urls.first())
        }
    }

    @JvmStatic
    @BindingAdapter("s3ImageUrl")
    fun ImageView.loadImageUrl(bucket: String?) {
        bucket?.let {
            if (it.isEmpty()) {
                this.scaleType = ImageView.ScaleType.CENTER_CROP
                this.setImageResource(R.drawable.ic_image_place_holder)
                return
            }
            val client = ApiClient.getClient(AppDefines.BASE_URL).create(ApiInterface::class.java)
            client.getMembershipImageUrl(it, Util.getHeader())
                .enqueue(object : retrofit2.Callback<GetImageUrlResponse?> {
                    override fun onResponse(
                        call: Call<GetImageUrlResponse?>,
                        response: Response<GetImageUrlResponse?>
                    ) {
                        response.body()?.preSignedUrl?.let { imageUrl ->
                            Picasso
                                .get()
                                .load(imageUrl)
                                .placeholder(R.drawable.progress_animation)
                                .into(this@loadImageUrl)
                            this@loadImageUrl.scaleType = ImageView.ScaleType.CENTER_CROP
                            } ?: kotlin.run {
                            this@loadImageUrl.scaleType = ImageView.ScaleType.CENTER_CROP
                            Picasso.get().load(R.drawable.ic_image_place_holder).into(this@loadImageUrl)
                        }
                    }

                    override fun onFailure(call: Call<GetImageUrlResponse?>, t: Throwable) {
                        this@loadImageUrl.scaleType = ImageView.ScaleType.CENTER_CROP
                        Picasso.get().load(R.drawable.ic_image_place_holder).into(this@loadImageUrl)
                    }
                })
        } ?: kotlin.run {
            this@loadImageUrl.scaleType = ImageView.ScaleType.CENTER_CROP
            this@loadImageUrl.setImageResource(R.drawable.ic_image_place_holder)
        }
    }

    @JvmStatic
    @BindingAdapter("s3OrgImageUrl")
    fun ImageView.loadOrgImageUrl(bucket: String?) {
        bucket?.let {
            if (it.isEmpty()) {
                this.scaleType = ImageView.ScaleType.CENTER_CROP
                this.setImageResource(R.drawable.ic_image_place_holder)
                return
            }
            val client = ApiClient.getClient(AppDefines.BASE_URL).create(ApiInterface::class.java)
            client.getOrgImageUrl(it, Util.getHeader())
                .enqueue(object : retrofit2.Callback<GetImageUrlResponse?> {
                    override fun onResponse(
                        call: Call<GetImageUrlResponse?>,
                        response: Response<GetImageUrlResponse?>
                    ) {
                        response.body()?.preSignedUrl?.let { imageUrl ->
                            Picasso
                                .get()
                                .load(imageUrl)
                                .placeholder(R.drawable.progress_animation)
                                .into(this@loadOrgImageUrl)
                            this@loadOrgImageUrl.scaleType = ImageView.ScaleType.CENTER_CROP
                        } ?: kotlin.run {
                            this@loadOrgImageUrl.scaleType = ImageView.ScaleType.CENTER_CROP
                            Picasso.get().load(R.drawable.ic_image_place_holder).into(this@loadOrgImageUrl)
                        }
                    }

                    override fun onFailure(call: Call<GetImageUrlResponse?>, t: Throwable) {
                        this@loadOrgImageUrl.scaleType = ImageView.ScaleType.CENTER_CROP
                        Picasso.get().load(R.drawable.ic_image_place_holder).into(this@loadOrgImageUrl)
                    }
                })
        } ?: kotlin.run {
            this@loadOrgImageUrl.scaleType = ImageView.ScaleType.CENTER_CROP
            this@loadOrgImageUrl.setImageResource(R.drawable.ic_image_place_holder)
        }
    }

    @JvmStatic
    @BindingAdapter("textArray")
    fun TextView.setTextArray(urls: List<String>?) {
        text = if (!urls.isNullOrEmpty()) {
            urls.joinToString(", ") { it }
        } else {
            "NA"
        }
    }

    @JvmStatic
    @BindingAdapter("monthFromDate")
    fun TextView.monthFromDate(date: String?) {
        text = if (!date.isNullOrEmpty()) {
            try {
                val format1 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dt1 = format1.parse(date)
                val format2 = SimpleDateFormat("MMMM", Locale.getDefault())
                format2.format(dt1!!)
            } catch (e: Exception) {
                "NA"
            }
        } else {
            "NA"
        }
    }

    @JvmStatic
    @BindingAdapter("bindStartDrawable")
    fun TextView.bindDrawableStart(resource: Int?) {
        resource?.let {
            setCompoundDrawablesWithIntrinsicBounds(resource, 0, 0, 0);
        } ?: setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
    }

    @JvmStatic
    @BindingAdapter("bindDoubleToInt")
    fun TextView.bindDoubleToInt(double: Double?) {
        double?.let {
            text = it.toInt().toString()
        } ?: kotlin.run { text = "-" }
    }

    @JvmStatic
    @BindingAdapter("bindVaccineArray")
    fun TextView.bindVaccineArray(list: List<VaccineItemData?>?) {
        text = if (list.isNullOrEmpty()) "Select" else list.joinToString(", ") {
            it?.name?.value ?: ""
        }
    }
}