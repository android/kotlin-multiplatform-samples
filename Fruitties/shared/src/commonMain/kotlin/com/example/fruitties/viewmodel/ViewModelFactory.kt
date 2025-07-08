package com.example.fruitties.viewmodel

import androidx.lifecycle.viewmodel.MutableCreationExtras

fun creationExtras(block: MutableCreationExtras.() -> Unit) = MutableCreationExtras().apply(block)
