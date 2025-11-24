package e13

/* Features:
 * - for comprehensions with yield
 * - nested functions
 */
object ForComprehensions:

	def main(args: Array[String]): Unit =

        // This range is created lazily
		for i <- 1 to 10 do
			println(i)

        // Create a collection that has all the files from .../e13
		val filesHere = (new java.io.File("src/main/scala/e13")).listFiles

        // Create a collection of lines in a file
		def fileLines(file: java.io.File) = scala.io.Source.fromFile(file).getLines.toList

        // You CAN do this, but its ugly, don't do it
        // (this is just to showcase Scalas abilities)
		val forLineLengths =
			for
				file <- filesHere
				if file.getName.endsWith(".scala")
				line <- fileLines(file)
				trimmed = line.trim
				if trimmed.matches(".*for.*")  
            // This accumulates a collection
            // which is later returned from this for
			yield file.toString() + ": " + trimmed.length
		
		for (line <- forLineLengths)
			println(line)

		println("----------------------------")

        // The data type of this is determined by the first operand
        // ('filesHere' in this case)
		val forLineLengths2 = filesHere.			
			withFilter { file => file.getName.endsWith(".scala") }.
			flatMap { file => fileLines(file).map(line => (file, line))	}.
			map { case (file, line) => (file, line, line.trim) }.
			withFilter { case (file, line, trimmed) => trimmed.matches(".*for.*") }.
			map { case (file, line, trimmed) => file.toString() + ": " + trimmed.length	}
		
		forLineLengths2.foreach {
			line => println(line)
		}
