package e01

/* Features:
 * - no static members, but singleton object
 * - methods defined using def
 * - public visibility is default
 * - types are written after arguments
 * - semicolon inference
 * - block inference via indenting - braces optional, optional end
 */
// 'object' - creates a class HelloWorld and a single instance
// It becomes a singleton
// Therefore all the functions are esentially "static" methods
// because with singleton the difference between a class and an instance blurs
object HelloWorld:
	def main(args: Array[String]): Unit =
		println("Hello world")

	// Alternative syntax for blocks (Scala version 2)
	def printHelloAll(): Unit = {
		println("Hello all")
	}

	// Scala version 3 with explicit block end
	def printHelloUniverse(): Unit =
		println("Hello universe")
	end printHelloUniverse





