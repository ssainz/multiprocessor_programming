package edu.vt.ece.searchtree.redblacktree.flatcombine.v3;

/**
 * @author Balaji Arun
 */
public class BackoffFactory {

    public static Backoff getBackoff(String name) {
        switch(name) {
            case "Fixed":
                return new FixedBackoff();
        }
        return null;
    }
}

