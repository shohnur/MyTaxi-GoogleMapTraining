package uz.mytaxi.base

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.*
import uz.mytaxi.R
import uz.mytaxi.ui.activities.MainActivity

abstract class BaseFragment(@LayoutRes val layoutId: Int) : Fragment(layoutId) {

    protected lateinit var mainActivity: MainActivity
    protected var enableCustomBackPress = false

    override fun onAttach(context: Context) {
        mainActivity = requireActivity() as MainActivity
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialize()
        setFocus(view)
        observe()
    }

    fun addFragment(
        fragment: Fragment,
        addBackStack: Boolean = true, @IdRes id: Int = parentLayoutId(),
        tag: String = fragment.hashCode().toString(),
        setAnim: Int = 1,
    ) {
        hideKeyboard()
        activity?.supportFragmentManager?.commit(allowStateLoss = true) {
            if (!fragment.isAdded) {
                setReorderingAllowed(true)
                if (addBackStack && !fragment.isAdded) addToBackStack(tag)
                when (setAnim) {
                    0 -> setRightLeftAnimation()
                    1 -> setTopBottomAnimation()
                    2 -> setLeftRightAnimation()
                }
                add(id, fragment)
            }
        }
    }

    private fun FragmentTransaction.setLeftRightAnimation() {
        setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
    }

    private fun FragmentTransaction.setRightLeftAnimation() {
        setCustomAnimations(
            R.anim.enter_from_left,
            R.anim.exit_to_right,
            R.anim.enter_from_right,
            R.anim.exit_to_left
        )
    }

    private fun FragmentTransaction.setTopBottomAnimation() {
        setCustomAnimations(
            R.anim.enter_from_bottom,
            R.anim.exit_to_top,
            R.anim.enter_from_top,
            R.anim.exit_to_bottom
        )
    }

    fun finishFragment() {
        hideKeyboard()
        activity?.supportFragmentManager?.popBackStackImmediate()
    }

    fun popInclusive(name: String? = null, flags: Int = FragmentManager.POP_BACK_STACK_INCLUSIVE) {
        hideKeyboard()
        activity?.supportFragmentManager?.popBackStackImmediate(name, flags)
    }

    protected fun hideKeyboard() {
        view?.let {
            val imm =
                it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    protected open fun onFragmentBackButtonPressed() {
    }

    protected open fun observe() {
    }

    private fun setFocus(view: View) {
        view.apply {
            isFocusableInTouchMode = true
            requestFocus()
            setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (enableCustomBackPress) onFragmentBackButtonPressed()
                    else activity?.onBackPressed()
                }
                enableCustomBackPress = false
                true
            }
        }
    }

    abstract fun initialize()

}

fun FragmentActivity.initialFragment(
    fragment: BaseFragment,
    showAnim: Boolean = false
) {
    supportFragmentManager.commit(allowStateLoss = true) {
        if (showAnim)
            setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
        replace(MainActivity.parentLayoutId, fragment)
    }
}


fun FragmentActivity.finishFragment() {
    supportFragmentManager.popBackStack()
}

fun parentLayoutId() = MainActivity.parentLayoutId