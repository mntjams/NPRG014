hello ladies, gentlemen
hi guys

// Equally
// hello (ladies, gentlemen)
// hi (guys)









// Used for methods 'hello' and 'hi'
def methodMissing(String name, args) {
    println "$name ${args.join(' and ')}"
    println "${args.join(' and ')} say $name back"
    println ""
}

// Used for arguments 'ladies', 'gentlemen' and 'guys'
def propertyMissing(String name) { name }