package edu.vt.ece.hw4.backoff;

/**
 * @author Balaji Arun
 */
public class BackoffFactory {

    public static Backoff getBackoff(String name) {
        switch(name) {
            case "Exponential":
                return new ExponentialBackoff();
            case "Fibonacci":
                return new FibonacciBackoff();
            case "Fixed":
                return new FixedBackoff();
            case "Linear":
                return new LinearBackoff();
            case "Poly":
                return new PolynomialBackoff();
        }
        return null;
    }
}
