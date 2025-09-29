//TASK Define the unless (aka if not) method

def unless(boolean b, Closure code) {
        if (!b){
                code()
        }
}

// This is a method call!
unless(1 > 5) {
    println "Condition not satisfied!"
    def value = 10
    println "Value is $value"
}

println 'done'
