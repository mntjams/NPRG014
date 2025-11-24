// 2025/2026
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformationClass
import org.codehaus.groovy.ast.stmt.TryCatchStatement
import static org.codehaus.groovy.control.CompilePhase.SEMANTIC_ANALYSIS
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.syntax.SyntaxException
import org.codehaus.groovy.control.messages.SyntaxErrorMessage
import groovyjarjarasm.asm.Opcodes
import org.codehaus.groovy.ast.ClassHelper
import static org.codehaus.groovy.ast.tools.GeneralUtils.*

@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.TYPE])
@GroovyASTTransformationClass("CreatedAtTransformation")
public @interface CreatedAt {
    String name() default "timestamp";
}


@GroovyASTTransformation(phase = SEMANTIC_ANALYSIS)
public class CreatedAtTransformation implements ASTTransformation {

    public void visit(ASTNode[] astNodes, SourceUnit source) {
    
        // Add a timestamp field
        ClassNode clazz = astNodes[1]
        final initializer = new AstBuilder().buildFromString('System.currentTimeMillis()')
        clazz.addField('timestamp', Opcodes.ACC_PRIVATE, ClassHelper.long_TYPE, initializer[0].statements[0].expression)
        
        // All existing methods increase timestamp
        final methods = clazz.getMethods()
        methods.each { method ->
            final newCode = new AstBuilder().buildFromString(SEMANTIC_ANALYSIS, """
            if (System.currentTimeMillis() > timestamp + 1000) 
            { 
                timestamp = System.currentTimeMillis(); 
            }""")[0]
            
            method.code.statements.add(0, newCode)
        }
        
        // Add clearTimestamp() method
        List<ASTNode> code = new AstBuilder().buildFromString(SEMANTIC_ANALYSIS, '''
            timestamp = 0;
        ''')
        clazz.addMethod("clearTimestamp", Opcodes.ACC_PUBLIC, ClassHelper.VOID_TYPE, [] as Parameter[], [] as ClassNode[], code[0])

        // Add a public final method returning the timestamp
        List<ASTNode> res = new AstBuilder().buildFromString('''
            timestamp
        ''')
        String name = astNodes[0].members.name.value
        clazz.addMethod("$name", Opcodes.ACC_PUBLIC & Opcodes.ACC_FINAL, ClassHelper.long_TYPE, [] as Parameter[], [] as ClassNode[], res[0])
    }
}

final calculator = new GroovyShell(this.class.getClassLoader()).evaluate('''
@CreatedAt(name = "timestamp")
class Calculator {
    int sum = 0
    
    def add(int value) {
        int v = sum + value
        sum = v
    }

    def subtract(int value) {
        sum -= value
    }
}

new Calculator()
''')

assert System.currentTimeMillis() >= calculator.timestamp()
assert calculator.timestamp() == calculator.timestamp()
def oldTimeStamp = calculator.timestamp()

sleep(1500)
calculator.add(10)
assert calculator.sum == 10

assert oldTimeStamp < calculator.timestamp()
//The timestamp should have been updated since the pause was longer than 1s
assert calculator.timestamp() == calculator.timestamp()
oldTimeStamp = calculator.timestamp()

sleep(1500)
calculator.subtract(1)
assert calculator.sum == 9
//The timestamp should have been updated since the pause was longer than 1s
assert oldTimeStamp < calculator.timestamp()
assert calculator.timestamp() == calculator.timestamp()

oldTimeStamp = calculator.timestamp()
sleep(100)
calculator.subtract(1)
assert calculator.sum == 8
//The timestamp should not have been updated since the pause was shorter than 1s
assert oldTimeStamp == calculator.timestamp()
assert calculator.timestamp() == calculator.timestamp()

calculator.clearTimestamp()
assert calculator.timestamp() == 0

println 'well done'
