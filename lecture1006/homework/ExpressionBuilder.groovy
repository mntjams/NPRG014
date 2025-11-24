// 2025/2026
// TASK The MarkupBuilder in Groovy can transform a hierarchy of method calls and nested closures into a valid XML document.
// Create a NumericExpressionBuilder builder that will read a user-specified hierarchy of simple math expressions and build a tree representation of it.
// The basic arithmetics operations as well as the power (aka '^') operation must be supported.
// It will feature a toString() method that will pretty-print the expression tree into a string with the same semantics, as verified by the assert on the last line.
// This means that parentheses must be placed where necessary with respect to the mathematical operator priorities.
// Change or add to the code in the script. Reuse the infrastructure code at the bottom of the script.

abstract class ExpressionItem {
    abstract int getPrecedence()
}

class BinaryOperation extends ExpressionItem {
    String operator
    ExpressionItem left, right

    private final Map PRECEDENCES = [
        '+': 1,
        '-': 1,
        '*': 2,
        '/': 2,
        '^': 3
    ]

    BinaryOperation(String op) {
        this.operator = op
    }

    @Override
    int getPrecedence() {
        PRECEDENCES[operator]
    }

    @Override
    String toString() {
        def leftStr = parenthesize(left)
        def rightStr = parenthesize(right)

        return "$leftStr $operator $rightStr"
    }

    String parenthesize(ExpressionItem child) {
        if(child.getPrecedence() < getPrecedence()) {
            def childStr = child.toString()
            return "($childStr)"
        }

        return child.toString()
    }
}

class NumberItem extends ExpressionItem {
    Number value

    NumberItem(Number val) { this.value = val }

    @Override
    String toString() { value.toString() }

    @Override
    int getPrecedence() { 100 }
}

class VariableItem extends ExpressionItem {
    String value

    VariableItem(String val) { this.value = val }

    @Override
    String toString() { value }

    @Override
    int getPrecedence() { 100 }
}


class NumericExpressionBuilder extends BuilderSupport {

    boolean firstItem = true
    ExpressionItem root = null

    @Override
    protected Object createNode(Object name) {
        def opMap = ['+': '+', '-': '-', '*': '*', '/': '/', 'power': '^']

        if (opMap.containsKey(name.toString())) {
            return new BinaryOperation(opMap[name.toString()])
        }

        return null
    }

    @Override
    protected Object createNode(Object name, Map attributes) {
        switch (name.toString()) {
            case 'number':
                return new NumberItem(attributes.value)
            case 'variable':
                return new VariableItem(attributes.value)
        }

        return null
    }

    @Override
    protected Object createNode(Object name, Map attributes, Object value) {
        // Unsupported by the builder
        return null
    }
    
    @Override
    protected Object createNode(Object name, Object value) {
        // Unsupported by the builder
        return null
    }

    @Override
    protected void setParent(Object parent, Object child) {
        if (firstItem){
            firstItem = false
            root = parent
        }
        
        if (parent instanceof BinaryOperation) {
            if (parent.left == null) {
                parent.left = child
            } else {
                parent.right = child
            }
        }
    }
    
    def rootItem() {
        return root
    }
}

//------------------------- Do not modify beyond this point!

def build(builder, String specification) {
    def binding = new Binding()
    binding['builder'] = builder
    new GroovyShell(binding).evaluate(specification)
}

//Custom expression to display. It should be eventually pretty-printed as 10 + x * (2 - 3) / 8 ^ (9 - 5)
String description = '''
builder.'+' {
    number(value: 10)
    '*' {
        variable(value: 'x')
        '/' {
            '-' {
                number(value: 2)
                number(value: 3)
            }
            power {
                number(value: 8)
                '-' {
                    number(value: 9)
                    number(value: 5)
                }
            }
        }
    }
}
'''

//XML builder building an XML document
build(new groovy.xml.MarkupBuilder(), description)

//NumericExpressionBuilder building a hierarchy of Items to represent the expression
def expressionBuilder = new NumericExpressionBuilder()
build(expressionBuilder, description)
def expression = expressionBuilder.rootItem()
println (expression.toString())
assert '10 + x * (2 - 3) / 8 ^ (9 - 5)' == expression.toString()
