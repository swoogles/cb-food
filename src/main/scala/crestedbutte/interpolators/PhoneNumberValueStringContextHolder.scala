package crestedbutte.interpolators

import contextual.Prefix

object PhoneNumberValueStringContextHolder {

  implicit class PhoneNumberValueStringContext(sc: StringContext) {
    val phoneNumberValue = Prefix(PhoneNumberValueInterpolator, sc)
  }

}
