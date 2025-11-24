package h1
import scala.language.implicitConversions

// Add necessary class and object definitions in order to make the statements in the main work.

class Complex(val real: Int, val img: Int):
    override def toString =
        if (img >= 0)
            s"${real}+${img}i"
        else
            s"${real}${img}i"

    def + (that: Complex) = new Complex(real + that.real, img + that.img)
    def + (that: Int) = new Complex(real + that, img)
    def * (that: Complex) =
        val newReal = real * that.real - img * that.img
        val newImg = real * that.img + img * that.real
        new Complex(newReal, newImg)
    def unary_- = new Complex(-real, -img)

object Complex:
    given Conversion[Int, Complex] = i => new Complex(i, 0)
    val I = Complex(0, 1)

object ComplexNumbers:
	def main(args: Array[String]): Unit =
		import Complex.*

		println(Complex(1,2)) // 1+2i

		println(1 + 2*I + I*3 + 2) // 3+5i

		val c = (2+3*I + 1 + 4*I) * I
		println(-c) // 7-3i
