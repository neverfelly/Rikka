package top.rechinx.rikka.mvp

import android.os.Bundle
import icepick.Icepick
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import top.rechinx.rikka.mvp.presenter.RxPresenter
import top.rechinx.rikka.mvp.presenter.delivery.Delivery
import top.rechinx.rikka.mvp.presenter.delivery.Delivery.isValid
import top.rechinx.rikka.mvp.view.OptionalView

open class BasePresenter<V> : RxPresenter<V>() {

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        Icepick.restoreInstanceState(this, savedState)
    }

    override fun onSave(state: Bundle?) {
        super.onSave(state)
        Icepick.saveInstanceState(this, state)
    }

    /**
     * Subscribes an observable with [deliverFirst] and adds it to the presenter's lifecycle
     * subscription list.
     *
     * @param onNext function to execute when the observable emits an item.
     * @param onError function to execute when the observable throws an error.
     */
    fun <T> Observable<T>.subscribeFirst(onNext: (V, T) -> Unit, onError: ((V, Throwable) -> Unit)? = null)
            = compose(deliverFirst<T>()).subscribe(split(onNext, onError)).apply { add(this) }

    /**
     * Subscribes an observable with [deliverLatestCache] and adds it to the presenter's lifecycle
     * subscription list.
     *
     * @param onNext function to execute when the observable emits an item.
     * @param onError function to execute when the observable throws an error.
     */
    fun <T> Observable<T>.subscribeLatestCache(onNext: (V, T) -> Unit, onError: ((V, Throwable) -> Unit)? = null)
            = compose(deliverLatestCache<T>()).subscribe(split(onNext, onError)).apply { add(this) }

    /**
     * Subscribes an observable with [deliverReplay] and adds it to the presenter's lifecycle
     * subscription list.
     *
     * @param onNext function to execute when the observable emits an item.
     * @param onError function to execute when the observable throws an error.
     */
    fun <T> Observable<T>.subscribeReplay(onNext: (V, T) -> Unit, onError: ((V, Throwable) -> Unit)? = null)
            = compose(deliverReplay<T>()).subscribe(split(onNext, onError)).apply { add(this) }

    /**
     * Subscribes an observable with [DeliverWithView] and adds it to the presenter's lifecycle
     * subscription list.
     *
     * @param onNext function to execute when the observable emits an item.
     * @param onError function to execute when the observable throws an error.
     */
    fun <T> Observable<T>.subscribeWithView(onNext: (V, T) -> Unit, onError: ((V, Throwable) -> Unit)? = null)
            = compose(deliverWithView<T>()).subscribe(split(onNext, onError)).apply { add(this) }

}