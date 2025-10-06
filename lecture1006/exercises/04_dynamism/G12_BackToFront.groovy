// Give each string a 'backToFront' method
String.metaClass.backToFront = {->
    delegate[-1..0]
}

println 'cimanyd si yvoorG'.backToFront()



//TASK define a starTrim() method to surround the original trimmed string with '*'

def s = '   core   '
s.metaClass.starTrim = {-> "*${delegate.trim()}*"}
// Lists if the variable responds to startTrim()
println s.respondsTo('starTrim')
assert '*core*' == s.starTrim()

println 'done'

















