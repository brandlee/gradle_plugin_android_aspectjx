package com.lancewu.aspectj

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Description: 交易密码弹窗
 * Author: lee
 * Date: 2021/9/10
 **/
class TradePasswordDialogFragment : BottomSheetDialogFragment() {
    private lateinit var closeIv: ImageView
    private lateinit var accountIdTv: TextView
    private lateinit var passwordView: PasswordInputView
    private lateinit var errorTv: TextView

    private var loginSuccessAction: (() -> Unit)? = null
    private var closeAction: (() -> Unit)? = null
    private var dismissAction: ((DialogInterface) -> Unit)? = null

    override fun getTheme(): Int = R.style.TradePasswordBottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_trade_password, container, false)
        closeIv = view.findViewById(R.id.iv_close)
        accountIdTv = view.findViewById(R.id.tv_account_number)
        passwordView = view.findViewById(R.id.password_input_view)
        errorTv = view.findViewById(R.id.tv_error)
        passwordView.inputType = InputType.TYPE_NULL
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        //此dialog fragment显示时，设置dim amount为0.3f
        //如果在显示此dialog fragment时，已经显示了分享的 dialog fragment,那么不设置 dimAmount 了
        //分享的 dialog fragment 已经设置了 dim amount 为 0.7f
        if (activity?.supportFragmentManager?.findFragmentByTag("123") == null) {
            dialog?.window?.setDimAmount(0.3f)
        } else {
            //当已经显示了分享的dialog fragment，再显示 此dialog fragment时，如果再次设置dim，在某些机型上会引起屏幕闪一下。
            //这里清除了dim的标记，就不会闪一下了。
            dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        passwordView.postDelayed({
            passwordView.requestFocus()
        }, 200L)


        closeIv.setOnClickListener {
            loginSuccessAction?.invoke()
            dismiss()
        }
    }

    override fun dismiss() {
        // 存在onSavedInstanceState之后调用dismiss方法，导致状态异常crash
        // eg: https://sentry.huaxingsec.com/organizations/sentry/issues/1286/?environment=ONLINE&project=4&query=is%3Aunresolved+error.unhandled%3Atrue&sort=freq&statsPeriod=14d
        if (isStateSaved) {
            dismissAllowingStateLoss()
        } else {
            super.dismiss()
        }
    }

    fun show(activity: FragmentActivity) {
        show(activity.supportFragmentManager, null)
    }

    fun show(fragment: Fragment) {
        show(fragment.parentFragmentManager, null)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissAction?.invoke(dialog)
    }

    class Builder(val context: Context) {
        private val args = Bundle()
        private var cancelable: Boolean = true
        private var mLoginSuccessAction: (() -> Unit)? = null
        private var mCloseAction: (() -> Unit)? = null
        private var mDismissAction: ((DialogInterface) -> Unit)? = null

        fun setLoginSuccessListener(listener: () -> Unit): Builder {
            mLoginSuccessAction = listener
            return this
        }

        fun setCloseListener(listener: () -> Unit): Builder {
            mCloseAction = listener
            return this
        }

        fun setDismissListener(listener: (DialogInterface) -> Unit): Builder {
            mDismissAction = listener
            return this
        }

        fun isCancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        fun build(): TradePasswordDialogFragment {
            return TradePasswordDialogFragment().apply {
                this.isCancelable = cancelable
                this.arguments = args
                this.loginSuccessAction = mLoginSuccessAction
                this.closeAction = mCloseAction
                this.dismissAction = mDismissAction
            }
        }
    }
}
