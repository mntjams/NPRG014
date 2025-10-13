@groovy.transform.TailRecursive
def fact(BigDecimal n, BigDecimal acc) {
    if (n < 2) acc
    else fact(n - 1, acc*n)
}

println fact(5000, 1)

//TASK Make the function tail recursive so that it can pass the following line
//println fact(10000)