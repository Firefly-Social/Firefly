package org.mozilla.social.feature.account.edit

import android.net.Uri
import java.io.File

interface EditAccountInteractions {
    fun onDisplayNameTextChanged(text: String) = Unit
    fun onBioTextChanged(text: String) = Unit
    fun onSaveClicked() = Unit
    fun onNewAvatarSelected(uri: Uri, file: File) = Unit
    fun onNewHeaderSelected(uri: Uri, file: File) = Unit
    fun onLockClicked() = Unit
    fun onBotClicked() = Unit
}