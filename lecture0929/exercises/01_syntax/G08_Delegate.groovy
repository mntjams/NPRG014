def joe = [name : 'Joe', age : 83]
def jeff = [name : 'Jeff', age : 38]
def jess = [name : 'Jess', age : 33]

def process(person, code) {
//    We can change the delegate property (the target), it will search all the delegates
//    !!! The delegate is modified from now on !!!
//    code.delegate = person    
//    code.resolveStrategy = Closure.DELEGATE_FIRST
    code.call()
//    This will change the delegate only temporarily
//    person.with(code)
}

name = "Noname"
process(joe, {println name})
//process(jeff, {println age})


//TASK Experiment with owner, delegate as well as with different resolution strategies
