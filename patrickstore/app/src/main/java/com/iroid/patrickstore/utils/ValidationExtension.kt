package com.iroid.patrickstore.utils

import java.util.regex.Pattern

val String.isValidEmail: Boolean
    get() {
        val expression = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+"
        return checkExpression(this, expression)
    }

val String.isValidMobile: Boolean
    get() {
        return if (this.length < 6 || this.length > 10) {
            false;
        } else {
            android.util.Patterns.PHONE.matcher(this).matches();
        }
    }

private fun checkExpression(text: String, expression: String): Boolean {
    val pattern = Pattern.compile(expression)
    val matcher = pattern.matcher(text)
    return matcher.matches()

}

val FirstNameError: String
    get() {
        return "First name is required"
    }
val AddressError: String
    get() {
        return "Address name is required"
    }
val LastNameError: String
    get() {
        return "Last name is required"
    }
val EmailError: String
    get() {
        return "Email is required"
    }

val CityError: String
    get() {
        return "City is required"
    }

val LandMarkError: String
    get() {
        return "Landmark  is required"
    }

val AddressTypeError: String
    get() {
        return "Address type is required"
    }
val StateError: String
    get() {
        return "State is required"
    }

val EmailInvalid: String
    get() {
        return "Invalid E-mail id"
    }
val MobileError: String
    get() {
        return "Mobile number  is required"
    }
val MobileInvalid: String
    get() {
        return "Invalid mobile number"
    }
val PinCodeInvalid: String
    get() {
        return "Pin code required"
    }

val PasswordError: String
    get() {
        return "Password is Required"
    }

val ConfirmPasswordError: String
    get() {
        return "Confirm password is required"
    }
val MissMatchPassword: String
    get() {
        return "MissMatch password"
    }
val SOMETHING_WENT_WRONG: String
    get() {
        return "Something went wrong"
    }
val COMING_SOON: String
    get() {
        return "Coming soon..."
    }
val NO_INTERNET_MESSAGE: String
    get() {
        return "No internet connection"
    }
val USERNAME_ERROR: String
    get() {
        return "E-mail or Mobile number is required"
    }
val LANDMARK_ERROR: String
    get() {
        return "Landmark  is required"
    }
val STATE_ERROR: String
    get() {
        return "State  is required"
    }
val LABEL_ERROR: String
    get() {
        return "Address type is required"
    }

val CONTACT_ERROR: String
    get() {
        return "Contact number is required"
    }

val PIN_CODE_ERROR: String
    get() {
        return "Pin  number  is required"
    }
val NAME_ERROR: String
    get() {
        return "Name is required"
    }

val OTP_ERROR: String
    get() {
        return "OTP is required"
    }

val TERMS_AND_CONDITION_ERROR: String
    get() {
        return "Accept terms and condition"
    }

val OLD_PASSWORD_REQUIRED: String
    get() {
        return "Old password required"
    }
val NEW_PASSWORD_REQUIRED: String
    get() {
        return "New password required"
    }
