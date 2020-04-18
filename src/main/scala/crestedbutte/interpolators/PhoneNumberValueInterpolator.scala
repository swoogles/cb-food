package crestedbutte.interpolators

import contextual._

object PhoneNumberValueInterpolator extends Interpolator {
//  type Out = PhoneNumberValue
//  type Output = PhoneNumberValue

  def checkValidPhoneNumberValue(str: String): Boolean =
    true

  def contextualize(interpolation: StaticInterpolation) = {
    val lit @ Literal(_, urlString) = interpolation.parts.head
    if (!checkValidPhoneNumberValue(urlString))
      interpolation.abort(lit, 0, "not a valid URL")

    Nil
  }

  def evaluate(
    interpolation: RuntimeInterpolation,
  ): PhoneNumberValue =
    PhoneNumberValue(interpolation.literals.head)
}
