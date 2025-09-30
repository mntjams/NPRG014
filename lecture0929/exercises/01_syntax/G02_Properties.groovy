class City {
    String name
    int size
    boolean capital = false
    
    static def create(String n, int v, boolean e = false) {
        return new City(name: n, size: v, capital: e)
    }

    @Override
    String toString() {
        return "${capital ? 'Capital c' : 'C'}ity of $name, population: $size"
    }
}

println City.create("Brno", 400000)
def praha = City.create("Praha", 1300000, true)
println praha

// You can use named parameters and not conform to the order of the parameters
// You can actually pass a hashmap to the ctor lol
City pisek = new City(name: 'Písek', size: 25000, capital: false)
City tabor = new City(size: 35000, capital: false, name: 'Tábor')

println pisek
pisek.size = 25001
println pisek

println tabor
//TASK Provide a customized toString() method overriding Object::toString() that prints the name and the population
assert 'City of Písek, population: 25001' == pisek.toString()
assert 'Capital city of Praha, population: 1300000' == praha.toString()
