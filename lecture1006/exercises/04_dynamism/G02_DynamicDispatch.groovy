class BusinessPerson {}
class Turist {}
class Visitor {}
class Burglar {}

class Receptionist {
    String welcome(BusinessPerson visitor) {
        'Welcome sir!'
    }

    String welcome(Turist visitor) {
        'Welcome, what can I do for you?'
    }

    String welcome(Burglar visitor) {
        'Stop, or I call the police!'
    }

    // Shorthand for 'Object'
    String welcome(final visitor) {
        'We\'re full, please go away!'
    }
    
    String call(def who) {welcome(who)}
}

final receptionist = new Receptionist()

final visitors = [new BusinessPerson(), new Turist(), new Burglar(), new Visitor()]
final greetings = visitors.collect {
                    receptionist.welcome(it)
                  }
greetings.each {println it}

// Make object behave like a method - override method 'call' !!!
//TASK Make the following code pass
println receptionist(new BusinessPerson())
println receptionist(new Turist())
println receptionist(new Burglar())
println receptionist(new Burglar())