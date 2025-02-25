/*
    Privacy Friendly QR Scanner
    Copyright (C) 2022-2025 Privacy Friendly QR Scanner authors and SECUSO

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.secuso.privacyfriendlycodescanner.qrscanner.util

import android.content.ContentValues
import android.provider.ContactsContract
import java.util.*

object ContactUtil {

    @JvmStatic
    fun buildPhoneValues(
        phoneNumbers: Array<String?>?,
        phoneNumberTypes: Array<String?>?
    ): List<ContentValues> {
        if (phoneNumbers == null) {
            return listOf()
        }
        return phoneNumbers.mapIndexed { i, phoneNumber ->
            ContentValues().apply {
                put(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                )
                put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)

                if (phoneNumberTypes != null && i in phoneNumberTypes.indices) {
                    // parameters according to https://www.iana.org/assignments/vcard-elements/vcard-elements.xhtml
                    val type: Int = when (phoneNumberTypes[i]?.lowercase(Locale.ENGLISH)) {
                        "text", "textphone", "video", "voice" -> ContactsContract.CommonDataKinds.Phone.TYPE_OTHER
                        "work" -> ContactsContract.CommonDataKinds.Phone.TYPE_WORK
                        "home" -> ContactsContract.CommonDataKinds.Phone.TYPE_HOME
                        "fax" -> ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK
                        "cell" -> ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                        "pager" -> ContactsContract.CommonDataKinds.Phone.TYPE_PAGER
                        "main-number" -> ContactsContract.CommonDataKinds.Phone.TYPE_MAIN
                        else -> {
                            put(ContactsContract.CommonDataKinds.Phone.LABEL, phoneNumberTypes[i])
                            ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM
                        }
                    }
                    put(ContactsContract.CommonDataKinds.Phone.TYPE, type)
                }
            }
        }
    }

    @JvmStatic
    fun buildEmailValues(
        emails: Array<String?>?,
        emailTypes: Array<String?>?
    ): List<ContentValues> {
        if (emails == null) {
            return listOf()
        }
        return emails.mapIndexed { i, email ->
            ContentValues().apply {
                put(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
                )
                put(ContactsContract.CommonDataKinds.Email.ADDRESS, email)
                if (emailTypes != null && i in emailTypes.indices) {
                    // parameters according to https://www.iana.org/assignments/vcard-elements/vcard-elements.xhtml
                    val type: Int = when (emailTypes[i]?.lowercase(Locale.ENGLISH)) {
                        "work" -> ContactsContract.CommonDataKinds.Email.TYPE_WORK
                        "home" -> ContactsContract.CommonDataKinds.Email.TYPE_HOME
                        else -> ContactsContract.CommonDataKinds.Email.TYPE_OTHER
                    }
                    put(ContactsContract.CommonDataKinds.Email.TYPE, type)
                }
            }
        }
    }

    @JvmStatic
    fun buildAddressValues(
        addresses: Array<String?>?,
        addressTypes: Array<String?>?
    ): List<ContentValues> {
        if (addresses == null) {
            return listOf()
        }
        return addresses.mapIndexed { i, address ->
            ContentValues().apply {
                put(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE
                )
                put(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, address)

                if (addressTypes != null && i in addressTypes.indices) {
                    // parameters according to https://www.iana.org/assignments/vcard-elements/vcard-elements.xhtml
                    val type: Int = when (addressTypes[i]?.lowercase(Locale.ENGLISH)) {
                        "work" -> ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK
                        "home" -> ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME
                        else -> ContactsContract.CommonDataKinds.StructuredPostal.TYPE_OTHER
                    }
                    put(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, type)
                }
            }
        }
    }

    @JvmStatic
    fun buildWebsiteValues(urls: Array<String?>?): List<ContentValues> {
        if (urls == null) {
            return listOf()
        }
        return urls.map {
            ContentValues().apply {
                put(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE
                )
                put(ContactsContract.CommonDataKinds.Website.URL, it)
            }
        }
    }
}