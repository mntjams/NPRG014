package e30
/*
/* Features:
 * - reference to outer class
 * - path dependent types
 */

abstract class Currency:
  // rename 'this' of the outer class to 'currency'
  currency =>

  val designation: String
  // 'CurrencyAmount' is dependant on the instance of Currency
  // So we can distinguish between USD CurrencyAmount and EUR CurrencyAmount
  abstract class CurrencyAmount:
    // 'this' is CurrencyAmount, 'currency' is Currency
    val amount: Long

    def + (that: CurrencyAmount): CurrencyAmount = make(this.amount + that.amount)

    def * (x: Double): CurrencyAmount = make((this.amount * x).toLong)

    def -(that: CurrencyAmount): CurrencyAmount = make(this.amount - that.amount)

    def /(that: Double) = make((this.amount / that).toLong)

    def /(that: CurrencyAmount) = this.amount.toDouble / that.amount

    private def decimals(n: Long): Int = if n == 1 then 0 else 1 + decimals(n / 10)

    override def toString = ((amount.toDouble / CurrencyUnit.amount.toDouble) formatted ("%." + decimals(CurrencyUnit.amount) + "f")
        + " " + designation)

    def designation = currency.designation


  def make(amnt: Long) = new CurrencyAmount {
    val amount = amnt
  }

  // Not my 'CurrencyAmount' but whichever 'CurrencyAmount'
  // this is done by 'Currency#CurrencyAmount'
  def from(other: Currency#CurrencyAmount): CurrencyAmount =
    make(math.round(other.amount.toDouble * Converter.exchangeRate(other.designation)(designation)))

  /*
  def to(currency: CurrencyAmount) =
    // currency.make(math.round(amount.toDouble) * Converter.exchangeRate(designation)(currency))
  val CurrencyUnit: CurrencyAmount
  */


object USD extends Currency:
  val designation = "USD"

  val Cent = make(1)
  val Dollar = make(100)

  val CurrencyUnit = Dollar


object EUR extends Currency:
  val designation = "EUR"

  val Cent = make(1)
  val Euro = make(100)

  val CurrencyUnit = Euro


object Converter:
  var exchangeRate = Map(
    "USD" -> Map("USD" -> 1.0   , "EUR" -> 0.7596),
    "EUR" -> Map("USD" -> 1.316 , "EUR" -> 1.0   )
  )


object Currencies:
  def main(args: Array[String]): Unit =
    val dollars = USD.Dollar * 3 + USD.Cent * 10 + (USD from EUR.Euro)
    println(dollars)

    // val x = USD.Dollar + EUR.Cent  -- Does not compile

    /* ASSIGNMENT
    Add a new method to class Currency#CurrencyAmount to allow converting an amount to a particular currency
    */
    // println(dollars to EUR)
*/ 
