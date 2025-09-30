def fib = null
fib = {
    it <= 1 ? it : (fib(it - 2) + fib(it - 1))
}.memoize() //Can take parameter of how many slots it needs
// But this is broken in the current version... i think

long b = System.currentTimeMillis()
fib(90G)
long a = System.currentTimeMillis()
println((a - b) / 1000 + ' seconds') // about 0.002 seconds on my machine
