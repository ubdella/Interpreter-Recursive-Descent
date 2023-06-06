# Interpreter-Recursive-Descent

This interpreter recognizes assignments and performs calculations on arbitrary mathematical expressions. It supports the use of rational numbers, parentheses, and various operators, including exponentiation.

## Features

1. **Rational Numbers**: The calculator supports rational numbers, allowing you to work with decimal values such as 4.5669 or -3.5.

2. **Parentheses**: You can use parentheses in your expressions to control the order of operations and group sub-expressions.

3. **Operators**: The following operators are supported:
   - Addition: '+'
   - Subtraction: '-'
   - Multiplication: '*'
   - Division: '/'
   - Exponentiation: '^'
   The exponentiation operator '^' has the highest precedence.

4. **Variable Assignments**: You can define variables using the 'define' keyword followed by an identifier and the assignment operator '='. For example, `define x = 10` will assign the value 10 to the variable 'x'. All defined variables are stored in a hash table for efficient retrieval.

5. **Recursive-Descent Technique**: The interpreter utilizes a recursive-descent technique to parse and evaluate expressions based on a set of production rules. This approach ensures correct handling of complex expressions and supports a wide range of calculations.

Production Rules:

- `<calculation> → <expression> | "define" <identifier> "=" <expression>`
- `<identifier> → <alphanumericstring>`
- `<alphanumericstring> → [<alphastring>{<digitstring>}])`
- `<alphastring> → [<letter>]`
- `<letter> → 'a'|...|'z'| 'A'|...|'Z'`
- `<digitstring> → [<digit>]`
- `<digit> → '0'|'1'|'2'|'3'|'4'|'5'|'6'|'7'|'8'|'9'`
- `<number> → {['+'|'-']<digitstring>['.'<digitsrting>]}`

- `<expression> → <term>`
- `<expression> → <term> {['+'|'-'] <term>}`
- `<term> → <factor>`
- `<term> → <factor> {['*'|'/'] <factor>}`
- `<factor> → <identifier>`
- `<factor> → <number>`
- `<factor> → <identifier>|<number> '^' <identifier>|<number>`
- `<factor> → '(' <expression>')' {['^' <identifier>|<number>] ')'`
- `<factor> → '(' <expression>')' '^' '(' <expression>')'`

