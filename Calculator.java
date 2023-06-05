import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    private static  Pattern NUMBER_PATTERN = Pattern.compile("[-+]?\\d*\\.?\\d+");
    private static  Pattern VARIABLE_PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");
    private int line=1;
    private String input;
    private int pos;
    private Map<String, Double> variables;

    public Calculator() {
        variables = new HashMap<>();
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            try {
                double result = calculator.evaluate(input);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public double evaluate(String input) {
        this.input = input;
        this.pos = 0;
        double result = calculation();
        if (pos < input.length()) {
            throw new RuntimeException("Unexpected character at position " + pos);
        }
        return result;
    }

    private double calculation() {
        skipSpaces();
        if (peek("define")) {
            return processDefine();
        } else {
            return expression();
        }
    }
    
    private double processDefine() {
        consume("define");
        skipSpaces();
        String identifier = identifier();
        skipSpaces();
        consume("=");
        skipSpaces();
        double value = expression();
        variables.put(identifier, value);
        return value;
    }
    

    private String identifier() {
        return alphanumericString();
    }

    private String alphanumericString() {
        Matcher matcher = match(VARIABLE_PATTERN);
        return matcher.group();
    }


    private String alphaString() {
        if (peek(VARIABLE_PATTERN)) {
            Matcher matcher = match(VARIABLE_PATTERN);
            String identifier = matcher.group();
            return identifier.substring(0, 1);
        }
        return "";
    }

    private String digitString() {
        Matcher matcher = match(Pattern.compile("\\d*"));
        return matcher.group();
    }

    private double expression() {
        double result = term();
        skipSpaces();
        while (peek("+") || peek("-")) {
            if (consume("+")) {
                result += term();
            } else {
                consume("-");
                result -= term();
            }
            skipSpaces();
        }
        return result;
    }

    private double term() {
        double result = factor();
        skipSpaces();
        while (peek("*") || peek("/")) {
            if (consume("*")) {
                result *= factor();
            } else {
                consume("/");
                result /= factor();
            }
            skipSpaces();
        }
        return result;
    }

    private double factor() {
        skipSpaces();
        if (peek("(")) {
            consume("(");
            double result = expression();
            skipSpaces();
            consume(")");
            if (peek("^")) {
                consume("^");
                result = Math.pow(result, factor());
            }
            return result;
        } else if (peek(VARIABLE_PATTERN)) {
            String identifier = identifier();
            if (!variables.containsKey(identifier)) {
                throw new RuntimeException( identifier + " not defined" );
            }
            double value = variables.get(identifier);
            if (peek("^")) {
                consume("^");
                value = Math.pow(value, factor());
            }
            return value;
        } else {
            double value = number();
            if (peek("^")) {
                consume("^");
                value = Math.pow(value, factor());
            }
            return value;
        }
    }
    
    private double number() {
        Matcher numberMatcher = match(NUMBER_PATTERN);
        return Double.parseDouble(numberMatcher.group());
    }
    
    private boolean peek(String s) {
        return input.regionMatches(true, pos, s, 0, s.length());
    }
    
    private boolean peek(Pattern pattern) {
        Matcher matcher = pattern.matcher(input.substring(pos));
        return matcher.lookingAt();
    }
    
    private boolean consume(String s) {
        if (peek(s)) {
            pos += s.length();
            skipSpaces();
            return true;
        }
        return false;
    }
    
    private Matcher match(Pattern pattern) {
        skipSpaces();
        Matcher matcher = pattern.matcher(input.substring(pos));
        if (!matcher.lookingAt()) {
            throw new RuntimeException("Expected pattern " + pattern + " at position " + pos);
        }
        pos += matcher.end();
        skipSpaces();
        return matcher;
    }
    
    private void skipSpaces() {
        while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) {
            pos++;
        }
    }
}    