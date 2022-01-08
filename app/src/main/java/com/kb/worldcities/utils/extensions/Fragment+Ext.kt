package com.kb.worldcities.utils.extensions

import androidx.annotation.MainThread
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.kb.worldcities.ui.base.BaseFragment
import com.kb.worldcities.ui.base.BaseViewModel

@Suppress("UNCHECKED_CAST")
@MainThread
inline fun <reified VM : BaseViewModel> BaseFragment.bindViewModel(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline viewModelProducer: (() -> VM)
) = createViewModelLazy(
  viewModelClass = VM::class,
  storeProducer = { ownerProducer().viewModelStore },
  factoryProducer = {
    return@createViewModelLazy object : ViewModelProvider.Factory {
      override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass != VM::class.java) {
          throw IllegalArgumentException("Unexpected argument $modelClass")
        }

        return (viewModelProducer.invoke() as T).also { vm ->
          (vm as? BaseViewModel)?.bindToUiController(viewLifecycleOwner, this@bindViewModel)
          this@bindViewModel.onViewCreatedListeners.add { _, _ ->
            (vm as? BaseViewModel)?.bindToUiController(viewLifecycleOwner, this@bindViewModel)
          }
        }
      }
    }
  }
)