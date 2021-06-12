package com.tugas.zaky.diaryapps.utils

import com.tugas.zaky.diaryapps.model.ModelNote

/**
 * Created by Azhar Rivaldi on 6/11/2020.
 */

interface onClickItemListener {
    fun onClick(modelNote: ModelNote, position: Int)
}