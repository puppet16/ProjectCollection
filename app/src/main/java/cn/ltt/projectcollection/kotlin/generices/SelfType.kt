typealias OnConfirm = () -> Unit
typealias OnCancel = () -> Unit

private val EmptyFunction = {}

open class Dialog(val title: String, val content: String)

class ConfirmDialog(title: String, content: String,
                    val onConfirm: OnConfirm,
                    val onCancel: OnCancel) : Dialog(title, content)

interface SelfType<Self> {
    val self: Self
        get() = this as Self
}


open class DialogBuilder<Self : DialogBuilder<Self>> : SelfType<Self> {
    protected var title = ""
    protected var content = ""

    fun title(title: String): Self {
        this.title = title
        return self
    }

    fun content(content: String): Self {
        this.content = content
        return self
    }

    open fun build() = Dialog(this.title, this.content)
}

class ConfirmDialogBuilder : DialogBuilder<ConfirmDialogBuilder>() {
    private var onConfirm: OnConfirm = EmptyFunction
    private var onCancel: OnCancel = EmptyFunction

    fun onConfirm(onConfirm: OnConfirm): ConfirmDialogBuilder {
        this.onConfirm = onConfirm
        return this
    }

    fun onCancel(onCancel: OnCancel): ConfirmDialogBuilder {
        this.onCancel = onCancel
        return this
    }

    override fun build() = ConfirmDialog(title, content, onConfirm, onCancel)
}

fun main() {
    val confirmDialog = ConfirmDialogBuilder()
            .title("提交弹窗")
            .onCancel {
                println("点击取消")
            }
            .content("确定提交吗？")
            .onConfirm {
                println("点击确定")
            }
            .build()

    confirmDialog.onConfirm()
}