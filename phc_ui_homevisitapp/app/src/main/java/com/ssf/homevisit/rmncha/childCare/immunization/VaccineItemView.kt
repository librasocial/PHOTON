package com.ssf.homevisit.rmncha.childCare.immunization

import android.app.DatePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.MutableLiveData
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.ItemVaccineItemBinding
import java.util.*

class VaccineItemView : FrameLayout {
    lateinit var itemVaccineItemBinding: ItemVaccineItemBinding
    var onExpiryDateSelection: (day: Int, month: Int, year: Int) -> Unit = { d, m, y -> }
    private val vaccineItemData = VaccineItemData(
        MutableLiveData(""),
        MutableLiveData(""),
        MutableLiveData(""),
        MutableLiveData("")
    )

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context) : super(context) {
        initView()
    }

    private fun initView() {
        itemVaccineItemBinding =
            ItemVaccineItemBinding.inflate(LayoutInflater.from(context), this, true)
        itemVaccineItemBinding.vaccineData = vaccineItemData
        itemVaccineItemBinding.etExpiryDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                context,
                R.style.DatePickerDialogTheme,
                { _, year, month, day ->
                    onExpiryDateSelection.invoke(day, month, year)
                },
                Calendar.getInstance()[Calendar.YEAR],
                Calendar.getInstance()[Calendar.MONTH],
                Calendar.getInstance()[Calendar.DAY_OF_MONTH]
            )
            datePickerDialog.show()
        }
    }


    fun setData(newData: VaccineItemData) {
        itemVaccineItemBinding.vaccineData = newData
        itemVaccineItemBinding.invalidateAll()
    }

    fun setName(name: String) {
        vaccineItemData.name.value = name
        invalidate()
    }

    fun setBatch(batch: String) {
        vaccineItemData.batch.value = batch
        invalidate()
    }

    fun setExpiry(expiryDate: String) {
        vaccineItemData.expiryDate.value = expiryDate
        itemVaccineItemBinding.vaccineData = vaccineItemData
        invalidate()
    }

    fun setManufacturer(manufacturer: String) {
        vaccineItemData.manufacturer.value = manufacturer
        invalidate()
    }
}

data class VaccineItemData(
    val name: MutableLiveData<String>,
    val batch: MutableLiveData<String> = MutableLiveData(""),
    val expiryDate: MutableLiveData<String> = MutableLiveData(""),
    val manufacturer: MutableLiveData<String> = MutableLiveData("")
)