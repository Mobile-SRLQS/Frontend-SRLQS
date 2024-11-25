package com.dl2lab.srolqs.ui.home.adapter

import com.dl2lab.srolqs.data.remote.response.DataItem
import com.dl2lab.srolqs.data.remote.response.PeriodDataItem

interface OnClassItemClickListener {
    fun onItemClick(classItem: DataItem)
}