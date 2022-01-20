package com.example.mvvmstructure.utils.rx

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject


fun rxEditTextSearchObservable(editText: EditText): Observable<String> {
    val subject = PublishSubject.create<String>()
    editText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (editText.hasFocus()) {
                subject.onNext(s!!.toString())
            }
        }

        override fun afterTextChanged(s: Editable?) {}

    })
    return subject
}