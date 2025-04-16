package com.pwstud.jbash.math;

/**
 * A more precise version for storing and manipulating floating point values on
 * an extremely basic leve.
 * 
 * @author Cirnicianu Ciprian Nicoale
 * @version 1.0
 */
public class PFloat extends Number {
  private static final String decimalDigitsDefault = "100000"; // represents 5 decimal digits: .00_000 to .99_999; Minimum 10; unsinged.
  private static int decimalDigits = Integer.parseUnsignedInt(decimalDigitsDefault);
  private int exponent; // the numberse beore the comma [164].12345
  private int mantissa; // the numbers after the comma 164.[12345]

  /**
   * Initializes the number with 0.0
   */
  public PFloat() {
    this.exponent = 0;
    this.mantissa = 0;
  }

  public PFloat(PFloat original) {
    this.exponent = original.exponent;
    this.mantissa = original.mantissa;
  }

  /**
   * Parses the string argument as an precise floating point decimal number. The
   * characters in the string must all be decimal digits,
   * except that the first character may be an ASCII plus sign <code>'+'</code>
   * <code>'\{@literal u002B'}</code>, and the separator <code>'.'</code>
   * <code>'\{@literal u002E'}</code> that splits the signed integer exponent,
   * from the unsigned integer mantissa.
   * 
   * @param preciseFloat a <code>String</code> representing the precise floating
   *                     point number to be stored.
   * @throws NumberFormatExcepcion if the number is null, contains unsuported
   *                               character, or if the separator {@code '.'} does
   *                               not exist. The only supported characters
   *                               are {@code '+' '-' '0 >> 9'}
   */
  public PFloat(String preciseFloat) throws NumberFormatException {
    // if the string is null return exception
    if (preciseFloat.equals(null))
      throw new NumberFormatException("Cannot convert null to a precise float.");

    // if the first character in the string is not +, -, or a decimal number, return
    // excepcion.
    char first = preciseFloat.charAt(0);
    if (!Character.isDigit(first) && first != '+' && first != '-')
      throw new NumberFormatException("The first character is not a valid simbol of '+', '-', or decimal number.");

    // if the string does not contain the separator it returns excepcion.
    String[] splits = preciseFloat.split("\\.");
    if (splits.length == 1)
      throw new NumberFormatException("the '.' separator not found.");

    // assign the values accordingly.
    this.exponent = Integer.valueOf(splits[0]);
    this.mantissa = normalizeMantissa(splits[1]);
  }

  /**
   * Parses the values of the exponent and mantissa directly to their values in
   * the variable.
   * 
   * @param signedExponent   signed integer between -2^31 and 2^31 - 1.
   *                         {@code 5}.0001
   * @param unsignedDecimals unsigned integer between 0 and 2^32 representing the
   *                         mantissa. 5.{@code 0001}
   */
  public PFloat(int signedExponent, int unsignedMantissa) {
    this(Integer.toString(signedExponent) + "." + Integer.toUnsignedString(unsignedMantissa));
  }

  /**
   * Adds together the values of {@code this} with the values of {@code other}.
   * 
   * @param other the precise float to be added.
   */
  public static PFloat add(PFloat first, PFloat other) {
    PFloat completed = new PFloat(first);
    int sumMantissa = completed.mantissa + other.mantissa;
    int carry = 0;

    if (sumMantissa >= decimalDigits) {
      carry = 1;
      sumMantissa -= decimalDigits;
    }

    completed.mantissa = sumMantissa;
    completed.exponent += other.exponent + carry;
    return completed;
  }

  /**
   * Adds together the values of {@code this} with the values of {@code other}.
   * 
   * @param other the precise float to be added.
   */
  public PFloat add(PFloat other) {
    PFloat result = PFloat.add(this, other);
    this.exponent = result.exponent;
    this.mantissa = result.mantissa;
    return result;
  }

  /**
   * Rests the values of {@code other} from the values of {@code this}.
   * 
   * @param other the precise float to be added.
   */
  public static PFloat rest(PFloat first, PFloat other) {
    PFloat completed = new PFloat(first);
    int newExponent = completed.exponent - other.exponent;
    int newMantissa = completed.mantissa - other.mantissa;

    if (newMantissa < 0) {
      newExponent -= 1;
      newMantissa += decimalDigits;
    }

    completed.exponent = newExponent;
    completed.mantissa = newMantissa;
    return completed;
  }

  /**
   * Rests the values of {@code other} from the values of {@code this}.
   * 
   * @param other the precise float to be added.
   */
  public PFloat rest(PFloat other) {
    PFloat result = PFloat.rest(this, other);
    this.exponent = result.exponent;
    this.mantissa = result.mantissa;
    return result;
  }

  /**
   * Multiply the values of {@code this} with the values of {@code other}.
   * 
   * @param other the precise float to be added.
   */
  public static PFloat multiply(PFloat first, PFloat other) {
    PFloat completed = new PFloat(first);
    long a = (long) completed.exponent * decimalDigits + completed.mantissa;
    long b = (long) other.exponent * decimalDigits + other.mantissa;

    long result = a * b;

    // Now divide back to get exponent and mantissa
    long finalExponent = result / decimalDigits / decimalDigits;
    long finalMantissa = (result / decimalDigits) % decimalDigits;

    completed.exponent = (int) finalExponent;
    completed.mantissa = (int) finalMantissa;
    return completed;
  }

  /**
   * Multiply the values of {@code this} with the values of {@code other}.
   * 
   * @param other the precise float to be added.
   */
  public PFloat multiply(PFloat other) {
    PFloat result = PFloat.multiply(this, other);
    this.exponent = result.exponent;
    this.mantissa = result.mantissa;
    return result;
  }

  /**
   * Divide the values of {@code this} with the values of {@code other}.
   * 
   * @param other the precise float to be added.
   */
  public static PFloat divide(PFloat first, PFloat other) {
    PFloat completed = new PFloat(first);
    long a = (long) completed.exponent * decimalDigits + completed.mantissa;
    long b = (long) other.exponent * decimalDigits + other.mantissa;

    if (b == 0)
      throw new ArithmeticException("Divide by zero");

    // Multiply numerator to preserve mantissa digits after division
    long result = (a * decimalDigits) / b;

    completed.exponent = (int) (result / decimalDigits);
    completed.mantissa = (int) (result % decimalDigits);
    return completed;
  }

  /**
   * Divide the values of {@code this} with the values of {@code other}.
   * 
   * @param other the precise float to be added.
   */
  public PFloat divide(PFloat other) {
    PFloat result = PFloat.divide(this, other);
    this.exponent = result.exponent;
    this.mantissa = result.mantissa;
    return result;
  }

  /**
   * Modulo the values of {@code this} with the values of {@code other}.
   * 
   * @param other the precise float to be added.
   */
  public static PFloat mod(PFloat first, PFloat other) {
    PFloat completed = new PFloat(first);
    long a = (long) completed.exponent * decimalDigits + completed.mantissa;
    long b = (long) other.exponent * decimalDigits + other.mantissa;

    if (b == 0)
      throw new ArithmeticException("Modulo by zero");

    long result = a % b;

    completed.exponent = (int) (result / decimalDigits);
    completed.mantissa = (int) (result % decimalDigits);
    return completed;
  }

  /**
   * Modulo the values of {@code this} with the values of {@code other}.
   * 
   * @param other the precise float to be added.
   */
  public PFloat mod(PFloat other) {
    PFloat result = PFloat.mod(this, other);
    this.exponent = result.exponent;
    this.mantissa = result.mantissa;
    return result;
  }

  /**
   * Reformulates the given mantissa into one that has as many 0 as the decimal
   * digits.
   * 
   * @param rawMantissa the raw mantisa value {@code Esample: 0.[91]}
   * @return a mantisa with as many 0 as the 0 in the decimalDigits static
   *         variable {@code Example: 0.[91000]} // if it has 5 digits.
   */
  private int normalizeMantissa(String rawMantissa) {
    // Remove any non-digit characters just in case
    String digitsOnly = rawMantissa.replaceAll("[^0-9]", "");

    // Pad with zeros or truncate to match desired digit count
    int decimalLength = Integer.toString(decimalDigits).length() - 1;
    if (digitsOnly.length() < decimalLength) {
      // right-pad with 0s
      digitsOnly = String.format("%-" + decimalLength + "s", digitsOnly).replace(' ', '0');
    } else if (digitsOnly.length() > decimalLength) {
      // truncate extra digits
      digitsOnly = digitsOnly.substring(0, decimalLength);
    }

    return Integer.parseInt(digitsOnly);
  }

  @Override
  public int intValue() {
    return exponent;
  }

  @Override
  public long longValue() {
    return (long) exponent;
  }

  @Override
  public float floatValue() {
    return Float.parseFloat((byte) exponent + "." + Integer.toUnsignedString(mantissa));
  }

  @Override
  public double doubleValue() {
    return Double.parseDouble((byte) exponent + "." + Integer.toUnsignedString(mantissa));
  }

  @Override
  public String toString() {
    return this.exponent + "." + this.mantissa;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj.getClass().getComponentType().equals(Number.class)) {
      Number otherNum = (Number) obj;
      if (this.exponent == Integer.parseInt(Double.valueOf(otherNum.doubleValue()).toString().split("\\.")[0]) &&
          this.mantissa == Integer.parseUnsignedInt(Double.valueOf(otherNum.doubleValue()).toString().split("\\.")[1]))
        return true;
    }

    return false;
  }

  /**
   * Returns true if the exponent of {@code this} and {@code other} are the same,
   * and if the
   * mantissa of {@code this} and {@code other} are the same.
   * 
   * @param other the PFloat to be compared against.
   * @return true if the exponent of {@code this} and {@code other} are the same,
   *         and if the
   *         mantissa of {@code this} and {@code other} are the same.
   */
  public boolean equals(PFloat other) {
    if (this.exponent == other.exponent && this.mantissa == other.mantissa)
      return true;

    return false;
  }

  public void setExponent(int exponent) {
    this.exponent = exponent;
  }

  public void setMantissa(int mantissa) {
    this.mantissa = mantissa;
  }
  public int getExponent() {
    return exponent;
  }

  public int getMantissa() {
    return mantissa;
  }

  public static int getDecimalDigits() {
    return decimalDigits;
  }

  /**
   * Resets the decimal digits variable to its default value.
   */
  public static void resetDecimalDigits() {
    decimalDigits = Integer.parseUnsignedInt(decimalDigitsDefault);
  }

  /**
   * Sets the floating point precision of the numbers. If you're planning to use
   * the same precision level for thhe entire project, then there is no need to
   * reset it.
   * But if you want to use the default for most other situations, use
   * {@code resetDecimalDigits()}
   * at the end of the section using the custom precision, to set it back to its
   * default value.
   * 
   * @param decimalDigits unsinged integer, where the first number is allways 1,
   *                      and the 0 after it represent the number
   *                      of digits you want the precise floating point number to
   *                      have.
   * @see PFloat#resetDecimalDigits()
   */
  public static void setDecimalDigits(int decimalDigits) {
    PFloat.decimalDigits = decimalDigits;
  }
}
