package e02

/* Features:
 * - var & val
 * - primitive types are objects
 * - statically typed language
 * - type inference
 */
// Mutable variables - var
// Immutable - val
object StaticTypingMain:
	
	def printValue(value: Int): Unit =
		println("Integer: " + value)

	def printValue(value: Any): Unit =
		println("Any: " + value)

	def main(args: Array[String]): Unit =
		var value: Any = "Hello world"
        // Any specialization
		printValue(value)

        // Type inference
		val valueI = 5

        // Int specialization print
		printValue(valueI)

		value = 3
        // The type of value is any but the value is Int
        // Scala (statically typed) - Any specialization is chosen (Determined at compile time)
        // Groovy (dynamically typed) - Int specialization is chosen (Determined at run time)
		printValue(value)

